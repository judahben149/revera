# Revera

One Kotlin codebase, every screen: **Android** (phones, tablets, foldables, Googlebook/ChromeOS),
**iOS & iPadOS**, **desktop** (macOS, Windows, Linux) and the **web**, built with
Compose Multiplatform.

`com.jj.revera` · Kotlin 2.4.21 · Compose Multiplatform 1.12.2 · AGP 9.4.1 · Gradle 9.8.2

## Platforms

| Platform | Shell | Run | Ship |
|---|---|---|---|
| Android | `apps/android` | `./gradlew :apps:android:installDebug` | `./gradlew :apps:android:bundleRelease` |
| iOS / iPadOS | `apps/ios` | Open `apps/ios/Revera.xcodeproj` in Xcode → Run | Archive in Xcode |
| macOS / Windows / Linux | `apps/desktop` | `./gradlew :apps:desktop:run` | `./gradlew :apps:desktop:packageDistributionForCurrentOS` |
| Web (Kotlin/Wasm) | `apps/web` | `./gradlew :apps:web:wasmJsBrowserDevelopmentRun` | `./gradlew :apps:web:wasmJsBrowserDistribution` |

Desktop installers are built on the OS they target (DMG/PKG on macOS, MSI/EXE on Windows,
DEB/RPM on Linux); CI has a job for each.

CI (`.github/workflows/ci.yml`) is currently **disabled** on GitHub. Enable it when needed with
`gh workflow enable CI` (or Actions → CI → Enable workflow).

## Requirements

- **JDK 17+** (Gradle will provision a toolchain if needed)
- **Android SDK** with platform 37 (`local.properties` → `sdk.dir=…`, or `ANDROID_HOME`)
- **Xcode 26+** for iOS (macOS only). Signing uses the team in
  `apps/ios/Configuration/Config.xcconfig` (`TEAM_ID`). Simulators are Apple-silicon (`arm64`) only.
- Node.js for the web build is downloaded automatically.

Linux and Windows machines can build everything except iOS. Those targets are skipped automatically.

## Everyday commands

```bash
./gradlew desktopTest testAndroidHostTest   # fast unit + UI tests (JVM)
./gradlew iosSimulatorArm64Test             # shared tests on the iOS simulator
./gradlew allTests                          # everything, every platform
./gradlew spotlessApply                     # format (ktlint + Compose rules)
./gradlew spotlessCheck                     # what CI runs
```

Useful properties: `-Prevera.environment=prod`, `-Prevera.apiBaseUrl=https://…`,
`-Prevera.warningsAsErrors=true`, `-Prevera.composeCompilerReports=true`.

## Project layout

```
apps/          platform shells (android, ios, desktop, web) — thin
shared/        App(), navigation graph, keyboard actions, DI graph, iOS framework
core/          common · model · data · network · preferences · designsystem · ui · navigation · testing
feature/       <feature>/api (routes) + <feature>/impl (screens, ViewModels, DI)
build-logic/   convention plugins: all build configuration lives here
tools/brand/   logo source + icon generator for every platform
docs/          architecture and decisions
```

See **[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)** for module rules, navigation, DI, form-factor
support (foldables, Googlebook, iPad, desktop windows) and a step-by-step guide to adding a feature.

## Branding

`tools/brand/revera-mark.svg` is the logo source. Rebrand every platform's icons with:

```bash
python3 tools/brand/generate_icons.py
```

The theme is generated from one seed color: `ReveraSeedColor` in
`core/designsystem/.../theme/Color.kt`.

## Release signing

- **Android**: put `keystore.properties` (`storeFile`, `storePassword`, `keyAlias`, `keyPassword`)
  at the repo root, or set `REVERA_KEYSTORE_PATH` / `REVERA_KEYSTORE_PASSWORD` / `REVERA_KEY_ALIAS` /
  `REVERA_KEY_PASSWORD`. Both are git-ignored or environment-only.
- **macOS**: set `compose.desktop.mac.sign` and the notarization properties in
  `~/.gradle/gradle.properties` (see `apps/desktop/build.gradle.kts`).
- **iOS**: automatic signing with the `TEAM_ID` in `Config.xcconfig`.

## Authors

See [AUTHORS](AUTHORS).
