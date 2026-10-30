#!/bin/sh
set -u

root="${1:-.}"

if [ ! -d "$root" ]; then
    echo "Directory not found: $root" >&2
    exit 2
fi

b='(^|[^[:alnum:]_])'
patterns="com\.github\.burkido|com\.burkido\.autoreadotp|com\.burkido\.otpinputkit|${b}SmsUserConsent[[:space:]]*\(|${b}smsCodeLength|${b}onOTPReceived|${b}(remember)?OtpInputFieldState|${b}textFieldState[[:space:]]*=|${b}endDate[[:space:]]*=|CountdownTimerDefaults\.size|${b}backgroundAlpha|${b}remainingTime|${b}getRemainingTimes|${b}startTimer[[:space:]]*\(|${b}cancelTimer[[:space:]]*\(|${b}setOnTimerFinishListener|com\.burkido\.otp\.(input|timer)|burkido:otp-(input|timer)|libs\.otp\.(input|timer)|jitpack\.io"

matches=$(grep -rnE "$patterns" "$root" \
    --include='*.kt' --include='*.kts' --include='*.gradle' --include='*.toml' \
    --exclude-dir=build --exclude-dir=.gradle --exclude-dir=.git --exclude-dir=.idea \
    2>/dev/null)

if [ -z "$matches" ]; then
    echo "No 1.x or 2.0 usages found."
    exit 0
fi

echo "$matches"
echo
echo "$(printf '%s\n' "$matches" | wc -l | tr -d ' ') line(s) still use 1.x or 2.0 names. jitpack.io lines only need removing if nothing else uses JitPack."
exit 1
