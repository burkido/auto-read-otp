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
