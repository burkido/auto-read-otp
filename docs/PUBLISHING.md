# Publishing

Releases go to Maven Central under `io.github.burkido`. The release workflow does the upload, so after the one time setup below a release is just merging the release PR that release-please opens.

## One time setup

### 1. Maven Central account and namespace

1. Sign in at [central.sonatype.com](https://central.sonatype.com) with the `burkido` GitHub account.
2. Open **Namespaces**. `io.github.burkido` should already be listed and verified, because you signed in with the matching GitHub account. If it's missing, add it and follow the instructions (you'll be asked to create a temporary public repository with a given name).

### 2. Publishing token

1. In the Central Portal, open your account menu, then **View Account**, then **Generate User Token**.
2. It shows a username and a password. They are not your login, and the portal shows them only once.

| GitHub secret | Value |
|---|---|
| `MAVEN_CENTRAL_USERNAME` | The token username |
| `MAVEN_CENTRAL_PASSWORD` | The token password |

### 3. Signing key

Maven Central rejects unsigned artifacts.

```bash
gpg --full-generate-key                # choose RSA and RSA, 4096 bits, no expiry or a long one
gpg --list-secret-keys --keyid-format SHORT
# sec   rsa4096/ABCD1234 ...           <- ABCD1234 is the key id

gpg --keyserver keyserver.ubuntu.com --send-keys ABCD1234
gpg --export-secret-keys --armor ABCD1234 > signing-key.asc
```

| GitHub secret | Value |
|---|---|
| `SIGNING_KEY` | The whole content of `signing-key.asc`, including the `BEGIN` and `END` lines |
| `SIGNING_KEY_PASSWORD` | The passphrase you chose for the key |

Delete `signing-key.asc` once the secret is saved, and keep a backup of the key somewhere safe. Losing it means future releases are signed with a different key.

### 4. Release PR token (optional, recommended)

Pull requests opened with the default `GITHUB_TOKEN` don't trigger other workflows, so CI won't run on release-please's PRs. A fine-grained personal access token fixes that.

1. GitHub, then **Settings**, **Developer settings**, **Fine-grained tokens**, **Generate new token**.
2. Repository access: only `burkido/auto-read-otp`.
3. Permissions: **Contents** read and write, **Pull requests** read and write.

| GitHub secret | Value |
|---|---|
| `RELEASE_PLEASE_TOKEN` | The token |

Without it, releases still work. You'd just need to run CI on the release PR by hand, for example by pushing an empty commit to it.

### 5. Documentation site

In the repository settings, open **Pages** and set **Source** to **GitHub Actions**.

### Where to add the secrets

Repository **Settings**, then **Secrets and variables**, **Actions**, **New repository secret**. The names must match exactly.

## Releasing

1. Merge pull requests into `main` with Conventional Commit titles. `fix:` bumps the patch version, `feat:` bumps the minor version, and a `!` or a `BREAKING CHANGE:` footer bumps the major version.
2. release-please keeps a release PR open that updates `VERSION_NAME`, the README and `CHANGELOG.md`.
3. Merging that PR tags the release, uploads the artifacts to Maven Central, and publishes the docs. New versions usually show up on Maven Central within 30 minutes.

## Publishing from your machine

This is only needed if the workflow is broken. Put the same values in `~/.gradle/gradle.properties`, never in the repository:

```properties
mavenCentralUsername=<token username>
mavenCentralPassword=<token password>
signingInMemoryKey=<the armored key, with each line break written as \n>
signingInMemoryKeyPassword=<key passphrase>
```

Then run:

```bash
./gradlew publishAndReleaseToMavenCentral
```

To check the artifacts without uploading anything, run `./gradlew publishToMavenLocal` and look in `~/.m2/repository/io/github/burkido/`. That works without any credentials, and CI runs it on every pull request.
