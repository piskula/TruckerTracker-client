---
name: release-app
description: Use when cutting a signed Android release build or explaining how to release the app. Triggered by phrases like "release the app", "cut a release", "release android", "publish a new version", "tag a release", "how do I release".
---

# Skill: Cut a Signed Android Release

> `.github/workflows/release-app.yml` builds a signed release APK + AAB and publishes them as a GitHub Release whenever a `vMAJOR.MINOR.PATCH` tag is pushed. There is no separate version-bump commit or manifest edit — the tag *is* the version, parsed at build time.

## Triggers

- "release the app", "cut a release", "release version X.Y.Z"
- "how do I publish a new Android build"
- Debugging a failed `release-app.yml` run (pair with the `analyze-ci-failure` skill)

## How to cut a release

```bash
git tag v1.2.3
git push origin v1.2.3
```

That's the entire release process. Pushing the tag triggers `release-android`, which:

1. Parses `vMAJOR.MINOR.PATCH` from `GITHUB_REF_NAME` — **fails fast** if the tag doesn't match, or if `MINOR`/`PATCH` are ≥ 100.
2. Computes `versionCode = MAJOR * 10000 + MINOR * 100 + PATCH` (this is why `MINOR`/`PATCH` are capped under 100 — keeps codes from colliding across versions).
3. Decodes the release keystore from the `ANDROID_KEYSTORE_BASE64` secret and runs `:app:android:assembleRelease :app:android:bundleRelease` with `-PappVersionName`/`-PappVersionCode` set from the parsed tag — these flow into `app/android/build.gradle.kts`'s `versionName`/`versionCode` via `stringProperty("appVersionName"/"appVersionCode")`.
4. Renames the outputs to `truck-track-<version>.apk` / `truck-track-<version>.aab` (version-suffixed, not the raw AGP output names).
5. Publishes both as a GitHub Release titled after the tag via `gh release create "$GITHUB_REF_NAME" ...`.

Verified end to end against tag `v0.0.1`: release assets were `truck-track-0.0.1.apk` / `truck-track-0.0.1.aab`, and `aapt dump badging` on the APK confirmed `versionCode='1' versionName='0.0.1'` (`0*10000 + 0*100 + 1 = 1`) — the version flows through correctly from tag → Gradle → manifest → filename.

## Required secrets

Four repo secrets must exist (`gh secret list` to confirm) — release-android fails during "Build Release APK and AAB" if any are missing, since `AndroidSigningPlugin` only wires the `release` signing config when all four are non-blank:

| Secret | Used for |
|---|---|
| `ANDROID_KEYSTORE_BASE64` | base64-encoded `.keystore`/`.jks` file, decoded to `$RUNNER_TEMP/release.keystore` |
| `ANDROID_KEYSTORE_PASSWORD` | keystore password |
| `ANDROID_KEY_ALIAS` | signing key alias |
| `ANDROID_KEY_PASSWORD` | signing key password |

## Tag rules

| Rule | Why |
|---|---|
| Must match `^v([0-9]+)\.([0-9]+)\.([0-9]+)$` exactly | e.g. `v1.2.3` — not `1.2.3` (missing `v`), not `v1.2` (missing patch) |
| `MINOR` and `PATCH` each `< 100` | Guarantees `versionCode = MAJOR*10000 + MINOR*100 + PATCH` never collides between two different `MAJOR.MINOR.PATCH` combinations |

## If you tag the wrong thing

`gh release create` fails outright if a release for that tag already exists — you can't just re-push the same tag to retry. Delete both the tag and release first:

```bash
gh release delete v1.2.3 --yes --cleanup-tag   # deletes the GitHub release + remote tag
git tag -d v1.2.3                              # delete the local tag too (cleanup-tag only removes remote)
```

Then re-tag and push as above.

## If the run fails

Use the `analyze-ci-failure` skill against the `release-app.yml` run. Known-specific failure modes for this workflow:

| Symptom | Cause |
|---|---|
| `Tag 'vX.Y.Z' does not match required vMAJOR.MINOR.PATCH format` | Tag pushed without the `v` prefix, or wrong segment count |
| `MINOR and PATCH must be less than 100 to avoid versionCode collisions` | e.g. `v1.150.0` |
| Build fails at "Build Release APK and AAB" with a signing-related Gradle error | One of the four `ANDROID_KEYSTORE_*`/`ANDROID_KEY_*` secrets is missing, wrong, or the keystore file is corrupt/mismatched with the alias |
| `gh release create` fails with "release already exists" | The tag was already released once — delete the release + tag first (see above), don't just push again |

## Verification

- [ ] Tag matches `vMAJOR.MINOR.PATCH` with `MINOR`/`PATCH` under 100
- [ ] `gh run view <run-id>` shows `release-android` succeeded
- [ ] `gh release view vX.Y.Z` shows both `truck-track-X.Y.Z.apk` and `truck-track-X.Y.Z.aab` assets
- [ ] (optional) `aapt dump badging truck-track-X.Y.Z.apk | head -1` confirms `versionName`/`versionCode` match the tag
