# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build debug APK (dev flavor)
./gradlew assembleDevDebug

# Build release APK (prod flavor)
./gradlew assembleProdRelease

# Run unit tests
./gradlew test

# Run lint
./gradlew lint

# Run a single test class
./gradlew test --tests "net.habui.tv.ExampleUnitTest"
```

Product flavors: `dev` (appends `.dev` suffix, uses dev BASE_URL) and `prod`. Always specify flavor when building — e.g., `assembleDevDebug`.

## Architecture

Clean Architecture + MVVM, feature-based, single-activity (`MainActivity`).

**Source root:** `app/src/main/java/net/habui/tv`

**Package layout:**
- `core/` — shared infrastructure: `designsystem`, `network`, `player`, `focus`, `result`, `ui`, `util`
- `feature/` — product features: `home`, `player`, `settings`; each has `presentation/`, `domain/`, `data/`
- `navigation/` — `AppNavHost`, `Routes`

**Layer rules (strictly enforced):**
- `domain/` → pure Kotlin only, no Android/Compose imports
- `data/` → implements domain repository interfaces; DTOs must not leak into presentation
- `presentation/` → Compose UI + ViewModel; no direct repository access

**Data flow:** UI Action → ViewModel → UseCase → Repository → `Resource<T>` → `LiveData<UiState>`

**Result type** (`core/result/Resource.kt`): `Resource.Loading`, `Resource.Success<T>`, `Resource.Error(AppError)` — use this for all async operations.

## Navigation

`AppNavHost` owns the `ModalNavigationDrawer` + `NavHost`. Top-level routes (Home, Settings) show the drawer; Player does not. The drawer is a `ModalNavigationDrawer` (collapsed = icon-only, expanded = label+icon). Focus restoration after drawer close is handled via `homeFocusRestoreRequest` counter passed down to `HomeScreen`. Routes are defined in `navigation/Routes.kt` and use string-based nav arguments.

## State Management

Each screen has three files: `*UiState`, `*UiAction`, `*ViewModel`. State is exposed as `LiveData<UiState>` and observed with `observeAsState()`. Stateless composables — ViewModels are injected at the screen level and callbacks are passed into sub-composables.

`SettingsViewModel` is shared across the nav graph (instantiated once in `AppNavHost`) to persist theme state across navigation.

## TV Focus System

The most non-obvious part of this codebase. Key utilities in `core/focus/`:

- **`PositionFocusedItemInLazyLayout`** — wraps a lazy list and overrides `BringIntoViewSpec` so focused items snap to a consistent position (fraction-based or fixed-offset). Default: `parentFraction=0.3f`, `childFraction=0f`. Use this instead of writing custom scroll logic.
- **`AlwaysAnchorFocusedItemInLazyLayout`** — similar, for always-anchor behavior.

Focus rules to preserve:
- Use stable `key` on every `LazyRow`/`LazyColumn` item
- Use `FocusRequester` only for manual restoration (e.g., returning from drawer)
- Preserve scroll position on back navigation
- Avoid `moveFocus()` except in the nav/drawer restoration pattern already in `AppNavHost`

## Theme System

Configured via `SettingsViewModel` + `core/designsystem/`. Uses Material 3 with `CompositionLocal` for theme, typography, spacing, dimensions. `LocalThemeConfig` carries accent color and dark/light mode. Only use `CompositionLocal` for theme/UI config — never for repositories or business logic.

## DI

Hilt throughout. Feature-level Hilt modules live in `feature/[feature]/di/`. Core modules: `NetworkModule`, `PlayerModule`, `DispatcherModule`. Prefer constructor injection; no manual singletons.

## Compose TV Components

Always use `androidx.tv.material3` and `androidx.tv.foundation` (not standard Material3) for all interactive TV components. All interactive components must handle focused/selected/pressed visual states (scale, border, elevation, color). Avoid `crossfade` in scrolling rows — it causes jank on TV hardware.

## Docs

- `docs/architecture.md` — layer rules and dependency graph
- `docs/tv-guidelines.md` — TV UX and focus rules
- `AGENTS.md` — AI agent guidelines (mirrors this file's rules)
