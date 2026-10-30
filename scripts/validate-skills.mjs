#!/usr/bin/env node
import { readFileSync, readdirSync, existsSync, statSync } from 'node:fs';
import { join, dirname, resolve } from 'node:path';

const ROOT = resolve(dirname(new URL(import.meta.url).pathname), '..');
const SKILLS_DIR = join(ROOT, 'skills');
const NAME_PATTERN = /^[a-z0-9]+(-[a-z0-9]+)*$/;
const MAX_SKILL_LINES = 500;
const MANIFESTS = ['.claude-plugin/plugin.json', '.cursor-plugin/plugin.json', 'plugin.json', 'gemini-extension.json'];
const MARKETPLACES = ['.claude-plugin/marketplace.json', '.cursor-plugin/marketplace.json', '.agents/plugins/marketplace.json'];

const errors = [];
const fail = (msg) => errors.push(msg);
const rel = (p) => p.slice(ROOT.length + 1);

function parseFrontmatter(text, path) {
  const match = text.match(/^---\r?\n([\s\S]*?)\r?\n---\r?\n/);
  if (!match) {
    fail(`${path}: missing YAML frontmatter delimited by ---`);
    return null;
  }
  const fields = {};
  const lines = match[1].split(/\r?\n/);
  for (let i = 0; i < lines.length; i++) {
    const m = lines[i].match(/^([A-Za-z][\w-]*):\s*(.*)$/);
    if (!m) continue;
    const [, key, rawValue] = m;
    const block = [];
    while (i + 1 < lines.length && /^\s+\S|^\s*$/.test(lines[i + 1])) block.push(lines[++i]);
    if (/^[>|][+-]?$/.test(rawValue)) {
      fields[key] = block.map((l) => l.trim()).join(rawValue.startsWith('>') ? ' ' : '\n').trim();
    } else if (rawValue === '') {
      fields[key] = Object.fromEntries(
        block
          .map((l) => l.trim().match(/^([\w-]+):\s*(.*)$/))
          .filter(Boolean)
          .map(([, k, v]) => [k, unquote(v)]),
      );
    } else {
      fields[key] = unquote(rawValue);
    }
  }
  return { fields, body: text.slice(match[0].length) };
}

function unquote(value) {
  const v = value.replace(/\s+#.*$/, '').trim();
  return /^(["']).*\1$/.test(v) ? v.slice(1, -1) : v;
}

function readJson(file) {
  const path = join(ROOT, file);
  if (!existsSync(path)) {
    fail(`${file}: missing`);
    return null;
  }
  try {
    return JSON.parse(readFileSync(path, 'utf8'));
  } catch (e) {
    fail(`${file}: invalid JSON (${e.message})`);
    return null;
  }
}

function checkLinks(file, text) {
  for (const [, target] of text.matchAll(/\]\(([^)#\s]+)(?:#[^)]*)?\)/g)) {
    if (/^[a-z]+:/i.test(target)) continue;
    if (!existsSync(join(dirname(file), target))) fail(`${rel(file)}: broken link to ${target}`);
  }
}

function markdownFiles(dir) {
  return readdirSync(dir).flatMap((entry) => {
    const path = join(dir, entry);
    if (statSync(path).isDirectory()) return markdownFiles(path);
    return entry.endsWith('.md') ? [path] : [];
  });
}

function validateSkill(skillDir) {
  const name = skillDir.split('/').pop();
  const skillFile = join(skillDir, 'SKILL.md');
  if (!existsSync(skillFile)) return fail(`${rel(skillDir)}: missing SKILL.md`);

  const text = readFileSync(skillFile, 'utf8');
  const parsed = parseFrontmatter(text, rel(skillFile));
  if (!parsed) return;
  const { fields } = parsed;

  if (!fields.name) fail(`${rel(skillFile)}: "name" is required`);
  else {
    if (fields.name.length > 64) fail(`${rel(skillFile)}: "name" exceeds 64 characters`);
    if (!NAME_PATTERN.test(fields.name)) fail(`${rel(skillFile)}: "name" must be lowercase letters, numbers and single hyphens`);
    if (fields.name !== name) fail(`${rel(skillFile)}: "name" (${fields.name}) must match its directory (${name})`);
  }

  if (!fields.description) fail(`${rel(skillFile)}: "description" is required`);
  else if (fields.description.length > 1024) fail(`${rel(skillFile)}: "description" is ${fields.description.length} characters (max 1024)`);

  if (fields.compatibility && fields.compatibility.length > 500) fail(`${rel(skillFile)}: "compatibility" exceeds 500 characters`);

  const lineCount = text.split('\n').length;
  if (lineCount > MAX_SKILL_LINES) fail(`${rel(skillFile)}: ${lineCount} lines (keep under ${MAX_SKILL_LINES}; move detail into referenced files)`);

  for (const file of markdownFiles(skillDir)) checkLinks(file, readFileSync(file, 'utf8'));

  const evals = join(skillDir, 'evals', 'evals.json');
  if (existsSync(evals)) readJson(rel(evals));

  return fields;
}

function validateManifests(skillVersions) {
  const release = readJson('.release-please-manifest.json')?.['.'];
  const names = new Set();
  for (const file of MANIFESTS) {
    const json = readJson(file);
    if (!json) continue;
    names.add(json.name);
    if (json.version !== release) fail(`${file}: version ${json.version} does not match .release-please-manifest.json (${release})`);
  }
  for (const [skill, version] of skillVersions) {
    if (version !== release) fail(`skills/${skill}/SKILL.md: metadata.version ${version} does not match ${release}`);
  }
  if (names.size > 1) fail(`manifests disagree on plugin name: ${[...names].join(', ')}`);

  for (const file of MARKETPLACES) {
    const json = readJson(file);
    for (const entry of json?.plugins ?? []) {
      if (!names.has(entry.name)) fail(`${file}: entry "${entry.name}" does not match the plugin name`);
      if ('version' in entry) fail(`${file}: entry "${entry.name}" sets a version; keep versions in plugin manifests only`);
      const ref = entry.source?.ref;
      if (ref !== undefined && ref !== `v${release}`) fail(`${file}: entry "${entry.name}" points at ${ref}, expected the release tag v${release}`);
    }
  }
}

const skillDirs = existsSync(SKILLS_DIR)
  ? readdirSync(SKILLS_DIR).map((d) => join(SKILLS_DIR, d)).filter((d) => statSync(d).isDirectory())
  : [];

if (skillDirs.length === 0) fail('skills/: no skill folders found');

const skillVersions = new Map();
for (const dir of skillDirs) {
  const fields = validateSkill(dir);
  if (fields) skillVersions.set(fields.name, fields.metadata?.version);
}
validateManifests(skillVersions);

if (errors.length) {
  for (const e of errors) console.error(`✖ ${e}`);
  process.exit(1);
}
console.log(`✔ ${skillDirs.length} skill(s) and all manifests valid`);
