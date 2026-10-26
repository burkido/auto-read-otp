# Security policy

`otp-sms` handles one-time codes and launches an intent received from a broadcast, so security reports are taken seriously.

## Reporting a vulnerability

Please don't open a public issue. Report it privately through [GitHub's security advisories](https://github.com/burkido/auto-read-otp/security/advisories/new) instead. Include the affected version, what an attacker can do, and steps or code to reproduce it.

Once a fix is released, the advisory is published with credit to you, unless you'd rather stay anonymous.

## Supported versions

Only the latest release gets security fixes. Version 1.x has a known intent redirection problem in the SMS consent flow that is fixed in 2.0, so please upgrade.
