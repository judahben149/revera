# Revera architecture

Revera is a single Compose Multiplatform codebase that ships to **Android** (phones, tablets,
foldables, Googlebook/ChromeOS), **iOS + iPadOS**, **desktop** (macOS, Windows, Linux) and the
**web** (Kotlin/Wasm). Almost everything — UI, navigation, state, data — lives in shared Kotlin;
each platform contributes a thin shell.

## Module map

```
apps/
  android/      Android application shell (MainActivity, manifest, launcher icons)
  ios/          Xcode project; links the :shared framework (ReveraShared)
  desktop/      JVM desktop shell (window, menu bar, native installers)
  web/          Kotlin/Wasm shell (index.html, PWA manifest)
shared/         Root UI (App), navigation graph, app-level actions, Koin graph, iOS framework
core/
  common/       Dispatchers, platform info, logging, generated BuildConfig
  model/        Pure domain models
  data/         Repositories (the only thing features use to read/write data)
  network/      Ktor client with a per-platform engine
  preferences/  Key-value settings (SharedPreferences / NSUserDefaults / java.util.prefs / localStorage)
  designsystem/ Theme, color generation, spacing, icons, base components
  ui/           Adaptive primitives (window classes, posture, tabletop layout), shortcuts, previews
  navigation/   Navigator + per-tab back stacks on Navigation 3
  testing/      Fakes and test helpers (test dependency only)
feature/
  <name>/api    Navigation keys (and future public interfaces) — tiny, stable
  <name>/impl   Screens, ViewModels, navigation entries, Koin module
build-logic/    Convention plugins: every module's build configuration lives here
```

### Dependency rules

```
apps/*  ──►  shared  ──►  feature/*/impl  ──►  feature/*/api  ──►  core/navigation
                             │
                             └──►  core/{ui, designsystem, data, model, common}
core/data ──► core/{preferences, model, common};   core/network ──► core/common
```

- A feature `impl` may depend on **other features' `api`** modules, never on another `impl`.
  That keeps features independently buildable and stops the dependency graph from turning into
  a web as the app grows.
- `core/model` has no dependencies. `core/designsystem` knows nothing about the domain.
- Only `shared` knows about every feature; it assembles navigation and DI.

## Build configuration

Module build files are a few lines long. All of the build configuration lives in `build-logic`:

| Plugin | Use for | Gives you |
|---|---|---|
| `revera.kmp.library` | Non-UI shared code | All targets (Android, iOS arm64 + simulator, desktop JVM, wasmJs), JDK toolchain, Android host tests, kotlin-test |
| `revera.cmp.library` | Shared UI without Material (e.g. navigation) | The above + Compose, Compose resources (`Res` in `<namespace>.resources`), previews, stability config |
| `revera.cmp.material3` | UI built on Material 3 | The above + Material 3 and its experimental-API opt-ins |
| `revera.cmp.adaptive` | UI that adapts to window size / panes / postures | The above + Material 3 adaptive (window size classes, posture, pane scaffolds) |
| `revera.feature.api` | `feature/x/api` | KMP + serialization + `core:navigation` |
| `revera.feature.impl` | `feature/x/impl` | `revera.cmp.adaptive` + every core UI/data module, Koin, lifecycle, Navigation 3 scenes, test fakes |
| `revera.android.application` | `apps/android` | SDK levels, versioning, Compose, lint |
| `revera.build.config` | `core/common` | Generated `BuildConfig` (version, environment, API base URL) |

- **Targets are declared in one place**: `build-logic/.../KotlinMultiplatform.kt`. Adding a target
  (e.g. `js()` for pre-WasmGC browsers, `macosArm64()`) is a one-line change there.
- **Namespaces are derived** from the Gradle path: `:feature:home:impl` → `com.jj.revera.feature.home.impl`.
- **Versions** live only in `gradle/libs.versions.toml`. Compose Multiplatform pins matching
  Jetpack versions (Material 3, adaptive, lifecycle, navigation 3); bump them together using the
  [CMP release notes](https://github.com/JetBrains/compose-multiplatform/releases).
- **App version** is `revera.versionName` / `revera.versionCode` in `gradle.properties` (Android,
  desktop, `BuildConfig`). iOS reads `apps/ios/Configuration/Config.xcconfig`.
- **Environments**: `-Prevera.environment=dev|staging|prod` and `-Prevera.apiBaseUrl=…` flow into
  `BuildConfig` on every platform.

## UI architecture

Unidirectional data flow: repositories expose `Flow`s → ViewModels map them to immutable UI state
(`StateFlow`) → stateless screen composables render it and emit events back as lambdas.

Every screen is split in two:

- `XRoute` (stateful): obtains its ViewModel with `koinViewModel()` and collects state.
- `XScreen` (stateless): takes state + callbacks, so it's previewable and unit-testable without DI.

## Navigation

Navigation 3 (multiplatform), wrapped by `core/navigation`:

- **Routes** are `@Serializable` `NavKey`s declared in a feature's `api` module, plus a
  `NavKeyRegistration` that `shared` adds to `ReveraNavConfiguration`. Non-JVM platforms have no
  reflection, so **every route must be registered** there or saving state fails.
- **Per-tab back stacks**: `NavigationState` keeps one stack per top-level destination, so tabs
  remember their history. Every stack is saved (configuration changes, process death).
- **`Navigator`** is the only navigation API features see: `navigate(route)`,
  `navigateToTopLevel(route)`, `goBack()`.
- **Adaptive scenes**: entries carry `ListDetailSceneStrategy.listPane()/detailPane()` metadata.
  On wide windows the list and detail render side by side, avoiding hinges on foldables; on compact
  windows they stack. Add `extraPane()` for a third pane on extra-wide windows.

## Dependency injection

Koin. Each module exposes a `val xModule = module { … }`; `shared/di/Koin.kt` lists them in
`reveraModules`. Platform shells call `initKoin()` once at startup (Android also passes
`androidContext`). `DependencyGraphTest` (desktopTest) fails the build if a definition can't be
resolved.

## Form factors

| Concern | Where it's handled |
|---|---|
| Window size classes (Compact → Extra-large) | `core/ui/adaptive/ReveraWindowInfo.kt`, read via `LocalReveraWindowInfo` |
| Navigation chrome | `shared/ReveraApp.kt`: bottom bar (compact, tabletop) → rail (medium/expanded) → expanded rail with labels (large/extra-large) |
| Multi-pane layouts, hinge avoidance | Navigation 3 `ListDetailSceneStrategy` + `rememberReveraPaneDirective()` |
| Foldable postures | `FoldPosture` (Flat / Tabletop / Book) + `TabletopLayout` (content above/below the hinge) |
| Keyboard shortcuts | `shared/action/AppAction.kt` — one declaration, used in-app, in the desktop menu bar and in Android's Keyboard Shortcuts Helper. ⌘ on Apple platforms, Ctrl elsewhere |
| Pointer / hover | Material components show hover states; `Modifier.handCursor()` for custom clickables |
| Readable line lengths on big windows | `Modifier.readableWidth()` |

### Android: foldables, tablets, Googlebook, ChromeOS

`apps/android/src/main/AndroidManifest.xml`:

- `resizeableActivity="true"`, no orientation lock (targetSdk 37 ignores locks on large screens anyway).
- `configChanges` covers size, layout, density, keyboard and UI mode, so resizing a free-form
  window, folding/unfolding, moving to another display or attaching a keyboard doesn't recreate
  the Activity.
- `<layout>` gives a sensible default and minimum free-form window size for desktop windowing.
- `android.hardware.touchscreen` is optional, so keyboard/trackpad-only devices aren't filtered
  out on Play.
- `MainActivity.onProvideKeyboardShortcuts` publishes shortcuts to the system helper (Meta + /).
- Edge-to-edge is on; Compose insets also cover the desktop-windowing caption bar.

Test with the Android Studio **Resizable**, **Pixel Fold** and **Desktop** emulators.

### iOS / iPadOS

Universal app (`TARGETED_DEVICE_FAMILY = 1,2`), every iPad orientation, multitasking and multiple
windows enabled (Split View, Slide Over, Stage Manager), pointer support
(`UIApplicationSupportsIndirectInputEvents`), and 120 Hz on ProMotion. Compose handles safe areas.

### Desktop: macOS, Windows, Linux

One JVM build runs on all three. The macOS title bar follows system appearance and the menu bar
uses the macOS screen menu bar. `packageDistributionForCurrentOS` builds DMG/PKG on macOS,
MSI/EXE on Windows and DEB/RPM on Linux. jpackage can't cross-compile, which is why CI has a job
per OS. Signing and notarization are configured through Gradle properties; keep credentials out
of the repo.

### Web

Kotlin/Wasm (WasmGC, supported in all evergreen browsers). Preferences persist in `localStorage`.
For browsers without WasmGC, add a `js()` target in `KotlinMultiplatform.kt` and serve the
compatibility build.

## Testing

| Where | What | Runs on |
|---|---|---|
| `commonTest` | ViewModels, repositories, navigation, pure logic | desktop JVM, Android host JVM, iOS simulator, wasm |
| `desktopTest` | Compose UI tests (`runComposeUiTest`), JVM-only checks (Koin graph) | desktop JVM (headless) |

`core/testing` provides `MainDispatcherTest`, `FakeTopicRepository`, `FakeUserPreferencesRepository`.

## Adding a feature

1. Create `feature/<name>/api/build.gradle.kts` → `plugins { alias(libs.plugins.revera.feature.api) }`
   and declare `@Serializable` routes + a `NavKeyRegistration`.
2. Create `feature/<name>/impl/build.gradle.kts` → `revera.feature.impl` with `api(projects.feature.<name>.api)`.
3. In `impl`, add the screens, ViewModels, `fun EntryProviderScope<NavKey>.<name>Entries(...)` and a Koin module.
4. Register in `settings.gradle.kts`, then in `shared`: add the dependency, the route registration
   in `ReveraNavConfiguration`, the entries in `ReveraApp`, and the Koin module in `reveraModules`.
5. Optional: add it to `TopLevelDestination` to show it in the navigation bar/rail.

## Next decisions (deliberately left open)

- **Database**: Room KMP (Android/iOS/JVM) or SQLDelight (adds web via web workers). Add as
  `core/database`; repositories become offline-first.
- **Auth/session**, **crash reporting** (add a Kermit writer), **analytics**.
- **Android Baseline Profiles** (a `:baselineprofile` module) once real user journeys exist.
- **Localization**: strings are already in Compose resources; add `values-<locale>/strings.xml`.
- **Brand fonts**: add to `core/designsystem/src/commonMain/composeResources/font` and wire into `ReveraTypography`.
