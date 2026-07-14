# iOS Readiness — Known Limitations

> Updated: 2026-07-08

Every `core`/`feature` module and `app:shared` compile clean for `iosArm64` and
`iosSimulatorArm64`, and `app/app/ios/iosApp.xcodeproj` builds up to (but not through) a final native
link step on this machine — see below for why. This document tracks what's still genuinely
unresolved, not a history of completed work.

---

## Known Limitations

### 1 · No interactive sign-in has ever been run end-to-end

The full `core:user` OAuth rewrite (AppAuth → [kotlin-multiplatform-oidc](https://github.com/kalinjul/kotlin-multiplatform-oidc))
compiles and links correctly on both platforms, but the actual browser-based sign-in flow against
the real Keycloak realm has never been clicked through on a device or simulator in this
environment — only the code paths were verified by reading them, not by running them.

**How to resolve:** run the app on a real device or a working simulator (see items 2–3) and
manually walk through sign-in, token refresh, and sign-out at least once before relying on it.

### 2 · Building requires a newer Xcode than what's on this machine

`app/app/ios`'s build fails at the final `ld` step with undefined iOS 17+ UIKit symbols
(`UITextLoupeSession`, `UITraitUserInterfaceStyle`, `UIViewLayoutRegion`, etc.) referenced by
Compose Multiplatform 1.11.1's native UIKit-interop layer. This machine's Xcode (14.3.1) only
ships the iOS 16.4 SDK, which predates those symbols. This is not a bug in the repo — the Gradle
config, `project.pbxproj`, and Swift entry point are all confirmed correct; only the SDK is too old.

**How to resolve:** install a current Xcode (15+; Kotlin 2.4.0 itself supports up to Xcode 26.4)
on any machine that needs to build/run `app/app/ios` locally, then rebuild.

### 3 · This machine can't run the iOS Simulator locally (Intel Mac)

This Mac is `x86_64`, but the project only declares `iosArm64` (device) and `iosSimulatorArm64`
(Apple Silicon simulator) Kotlin/Native targets — there's no `iosX64` (Intel simulator) target.
Adding one was attempted and reverted: Compose Multiplatform 1.11.1 and AndroidX Navigation3 no
longer publish `iosX64` artifacts at all (an industry-wide drop of Intel-simulator support).

**How to resolve:** there's no in-repo fix for this — it needs either a physical iOS device
connected via USB, or building/running from an Apple Silicon Mac. CI (`macos-15` runners, see
item 4) is unaffected since GitHub's macOS runners are Apple Silicon.

### 4 · CI is code-complete for signed builds but has no certificate/profile to sign with yet

`build-ios` (`.github/workflows/build-app.yml`) and `release-ios` (`.github/workflows/release-app.yml`)
both know how to import an ad-hoc distribution certificate + provisioning profile into a temporary
CI keychain and run `xcodebuild archive` + `-exportArchive` to produce a real, device-installable
`.ipa`, distributed via Firebase App Distribution (`internal-testers` group on every push,
`release` group on a version tag). But no Apple Developer account, certificate, or provisioning
profile exist yet, so both jobs currently take their fallback path: `build-ios` builds today's
unsigned Simulator app instead (CI stays green), and `release-ios` skips its signing steps
entirely with a warning.

**How to resolve:** once an Apple Developer Program membership, an "Apple Distribution"
certificate (`.p12`), and an ad-hoc provisioning profile for `com.momosi.trucktrack` exist, add
five GitHub Secrets — `IOS_TEAM_ID`, `IOS_DISTRIBUTION_CERTIFICATE_BASE64`,
`IOS_DISTRIBUTION_CERTIFICATE_PASSWORD`, `IOS_PROVISIONING_PROFILE_BASE64`,
`FIREBASE_IOS_APP_ID` (the iOS app's Firebase App ID, registered in the same Firebase project as
Android) — and both workflows switch to the real signed path automatically, no code changes
needed. Ad-hoc distribution caps at 100 registered devices/year, and every new tester's iPhone
UDID has to be registered with Apple and baked into a regenerated provisioning profile.

### 5 · JWT verification uses Keycloak's legacy `{realm}` endpoint instead of standard JWKS

`TokenVerifier` fetches the realm's raw PEM public key from Keycloak's non-standard `{realm}` info
endpoint (as the pre-migration code always did) rather than the standard OIDC discovery document's
`jwks_uri`. This works today but doesn't support key rotation or multiple signing keys the way a
real JWKS-based verifier would.

**How to resolve:** switch `AuthApi`/`RealmDto` to fetch `{realm}/.well-known/openid-configuration`,
read `jwks_uri` from it, fetch the JWK Set, and match by `kid` from the JWT header instead of
caching a single key.

### 6 · Token storage is not encrypted at rest on either platform

`UserStorageImpl` uses plain `SharedPreferences` on Android and plain `NSUserDefaults` on iOS —
neither encrypts the stored access/refresh tokens. This matches the pre-migration Android behavior
(it was never encrypted before either), so it's not a regression, but it's a real gap worth closing.

**How to resolve:** Android — swap to `androidx.security.crypto`'s `EncryptedSharedPreferences` (or
the OIDC library's own `AndroidEncryptedPreferencesSettingsStore`). iOS — move to Keychain via
`Security.framework`'s `SecItemAdd`/`SecItemCopyMatching` (or the library's `IosKeychainTokenStore`).

### 7 · `AuthActionResult.Failed.NoActivity` detection is a heuristic

`AuthManagerImpl` maps a generic `IllegalStateException` from `AndroidCodeAuthFlowFactory` to
`NoActivity`, since the library doesn't expose a typed exception for "activity never registered."
This works for the known failure mode (`registerActivity()` not called before the Activity's
`ON_CREATE`) but could misclassify some other `IllegalStateException` as `NoActivity`.

**How to resolve:** revisit if `kotlin-multiplatform-oidc` ever adds a dedicated exception type for
this case; until then, this is a reasonable best-effort mapping, not a correctness bug.
