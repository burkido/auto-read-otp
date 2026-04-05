---
name: conventional-commits
description: 'Write short, explanatory commit messages and PR titles following Conventional Commits. Use when: writing a git commit message, naming a PR title, choosing a commit prefix, or asking what type/prefix to use.'
argument-hint: 'Describe what you changed, e.g. "added dark mode toggle"'
---

# Conventional Commits

## Allowed Prefixes

| Prefix | When to use |
|--------|-------------|
| `feat` | New feature visible to library consumers |
| `fix` | Bug fix |
| `ci` | CI/CD workflow changes (.github/workflows, build-logic) |
| `build` | Build system, dependency, or Gradle changes |
| `chore` | Maintenance that doesn't affect production code (e.g. version bumps, cleanup) |
| `docs` | Documentation only |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `perf` | Performance improvement |
| `test` | Adding or updating tests |
| `style` | Formatting, whitespace — no logic change |
| `revert` | Reverts a previous commit |

## Rules

1. Format: `<prefix>: <subject>`
2. Subject must **not** start with an uppercase letter — use lowercase.
3. Subject must be short (≤72 chars total line length) yet descriptive enough to understand the change without opening the diff.
4. No trailing period.
5. Scope is optional. If used: `<prefix>(<scope>): <subject>` (e.g. `fix(sms-reader): handle null bundle on resume`)

## Procedure

1. Identify the **primary intent** of the change — pick the single most fitting prefix from the table above.
2. Write a subject that answers *what* changed and *why* in plain language, without restating the prefix word.
3. If the change is breaking, append `!` after the prefix: `feat!: remove deprecated API`.

## Good vs Bad Examples

| Bad | Good |
|-----|------|
| `Updated stuff` | `refactor: simplify OTP input state machine` |
| `Fix bug` | `fix: prevent crash when SMS permission is denied` |
| `CI changes` | `ci: add tag-existence guard to release workflow` |
| `feat: Added new feature` | `feat: add clipboard paste support to OTP input` |
| `Chore: bump version` | `chore: bump otp-input-kit to 1.2.0` |

## PR Titles

PR titles follow the same format. Use the prefix that matches the **overall** purpose of the branch. When a PR touches multiple concerns, pick the dominant one — typically `feat` or `fix` for product changes, `ci` for workflow-only PRs.
