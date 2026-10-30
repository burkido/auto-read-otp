#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
out=build/skills
rm -rf "$out"
mkdir -p "$out"
for dir in skills/*/; do
  skill="$(basename "$dir")"
  (cd skills && zip -qr "../${out}/${skill}.zip" "$skill" -x "${skill}/evals/*")
  (cd "$out" && shasum -a 256 "${skill}.zip" > "${skill}.zip.sha256")
done
ls "$out"
