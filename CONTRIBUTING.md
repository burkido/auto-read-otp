# Contributing

Thanks for helping out. Bug reports, fixes and new ideas are all welcome. For anything larger than a small fix, please open an issue first so we can agree on the approach.

## Building

You need JDK 21 and the Android SDK. Then:

```bash
./gradlew check verifyRoborazziDebug
```

That's the same set of checks CI runs: lint, detekt, unit tests, the public API check and the screenshot tests. The `sample` app shows every component and is the quickest way to try a change on a device.

## Changing the public API

Each library keeps its public API in `api/<module>.api`. If you add, remove or change anything public, `apiCheck` fails until you update the dump:

```bash
./gradlew apiDump
```

Commit the updated file with your change, so the API difference is visible in review. Removing or changing existing API breaks apps that depend on the library, so explain why it's needed in the pull request.

## Agent skill

[`skills/auto-read-otp`](skills/auto-read-otp) teaches coding agents how to use the libraries. Agents follow it more closely than the README, so when you change the public API or a behavior it describes, update the skill in the same pull request. Then run:

```bash
node scripts/validate-skills.mjs
claude plugin validate --strict .   # if you have Claude Code installed
```

When the skill describes something new, mark it **Since x.y.z** with the version it will ship in. The skill tells agents to check the project's library version and skip anything newer, so apps on an older release don't get code that won't compile.

### How the skill reaches agents

`main` can document APIs that aren't on Maven Central yet, so agents install the skill from the release tag rather than from `main`:

| Agent | Installs from | Pinned by |
|---|---|---|
| Claude Code | Tag `vX.Y.Z` | `source.ref` in `.claude-plugin/marketplace.json` |
| Codex | Tag `vX.Y.Z` | `source.ref` in `.agents/plugins/marketplace.json` |
| Gemini CLI | Source of the latest GitHub release | Gemini installs a release asset only when it is the only one, so the release job attaches the zip and its `.sha256` to make Gemini fall back to the tagged source |
| Cursor | `main` | Cursor only tracks branches, so it relies on the **Since** markers |
| Release zip | Tag `vX.Y.Z` | The release job |

The release PR bumps every manifest version and both refs, and the validator fails if any of them disagree with `.release-please-manifest.json`. Don't edit them by hand. Agents pick up skill changes with the next release, so merge a skill fix with a `fix:` title if it shouldn't wait.

[`evals/evals.json`](skills/auto-read-otp/evals/evals.json) lists prompts with the behavior we expect. When you fix a mistake agents make, add a case for it.

## Screenshot tests

Cell styles and the timer are covered by Roborazzi screenshots in `src/test/screenshots`. If you change how something looks on purpose, record new images and commit them:

```bash
./gradlew recordRoborazziDebug
```

When a screenshot test fails in CI, the report artifact contains the expected image, the actual image and a diff.

## Code style

detekt, with the ktlint and Compose rule sets, runs as part of `check`. Most formatting findings can be fixed with `./gradlew detekt --auto-correct`.

Comments should explain why something is done, not repeat what the code says. Public API needs KDoc, and plain, short sentences work best there.

## Pull request titles

Commit messages and pull request titles follow [Conventional Commits](https://www.conventionalcommits.org/). release-please reads them to write the changelog and pick the next version, and CI checks every pull request title:

```
<type>(optional scope): <subject>
```

| Type | When to use | Release |
|---|---|---|
| `feat` | New feature for library users | Minor |
| `fix` | Bug fix | Patch |
| `perf` | Performance improvement | Patch |
| `docs` | Documentation only | None |
| `refactor` | Code change that is neither a fix nor a feature | None |
| `test` | Adding or updating tests | None |
| `build` | Build system or dependencies | None |
| `ci` | CI configuration | None |
| `chore` | Maintenance | None |
| `style` | Formatting only | None |
| `revert` | Reverts a previous commit | Patch |

Add `!` after the type, as in `feat!: ...`, for a change that breaks existing users. That bumps the major version. The subject must start with a lowercase letter.

```
feat(input): add a separator slot
fix(sms): stop listening after a timeout
docs: explain the resend flow
```

## Releases

Maintainers release by merging the release PR that release-please keeps open. See [docs/PUBLISHING.md](docs/PUBLISHING.md).
