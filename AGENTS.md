# AI Agent Guidelines

This document provides context and standards for AI agents working on the TV project.

---

# 1. Project Overview

- Platform: Android TV (10-foot UI)
- Language: Kotlin
- UI: Jetpack Compose for TV (`androidx.tv.material3`)
- Architecture: Clean Architecture + MVVM
- DI: Hilt
- Navigation: Navigation Compose
- State Management: LiveData + UiState
- Source Root: `app/src/main/java/net/habui/tv`

Architecture style:
- feature-based structure
- single activity
- unidirectional data flow

---

# 2. Project Structure

```txt
core/
├── designsystem/
├── network/
├── player/
├── focus/
├── result/
├── ui/
└── util/

feature/
├── home/
├── player/
├── settings/
└── detail/

navigation/
```

Feature structure:

```txt
feature/[feature]/
├── presentation/
├── domain/
└── data/
```

---

# 3. Architecture Rules

- Domain layer must remain pure Kotlin.
- Domain layer must not depend on Android or Compose.
- Presentation layer must not access repository implementations directly.
- UseCases contain business logic.
- ViewModels manage UI state and actions.
- Use mappers between Data / Domain / UI models.
- Prefer existing project patterns over introducing new patterns.
- Read relevant docs before modifying a feature.

Data flow:

```txt
UI Action
    ↓
ViewModel
    ↓
UseCase
    ↓
Repository
    ↓
Resource<T>
    ↓
UiState
```

---

# 4. State Management

Each screen should contain:
- UiState
- UiAction
- ViewModel

Rules:
- expose state using LiveData
- observe state with `observeAsState()`
- prefer stateless composables
- use `remember` / `rememberSaveable` only for UI-local state

---

# 5. Android TV Standards

- D-pad first experience
- focused item must always remain visible
- preserve focus after navigation
- avoid touch-only interactions
- optimize for 10-foot readability

Focus rules:
- use `FocusRequester` only when necessary
- preserve focus state when returning from NavigationDrawer
- avoid random focus jumps
- use stable keys for LazyRow/LazyColumn
- avoid excessive focus logic

Lists:
- use LazyRow / LazyColumn
- keep focused item in a consistent visible position
- preserve scroll position when navigating back

---

# 6. Compose Standards

- use `androidx.tv.material3`
- prefer small composables
- avoid business logic inside composables
- use state hoisting
- pass callbacks instead of ViewModels into reusable components
- avoid unnecessary recompositions

Performance:
- optimize image loading for TV devices
- avoid expensive animations in LazyRows
- avoid image crossfade in scrolling rows

---

# 7. Theme & CompositionLocal

Use CompositionLocal only for:
- theme
- typography
- spacing
- dimensions
- global UI configuration

Do NOT use CompositionLocal for:
- repositories
- business logic
- screen state

---

# 8. Dependency Injection

Use Hilt for:
- repositories
- use cases
- Retrofit
- Room
- ExoPlayer dependencies

Rules:
- prefer constructor injection
- avoid manual singletons
- avoid service locator patterns

---

# 9. Development Guidelines

- keep diffs small
- avoid unrelated refactors
- preserve existing architecture
- preserve existing TV focus/navigation behavior
- preserve existing package structure unless requested

Documentation:
- `docs/architecture.md`
- `docs/tv-guidelines.md`

Before finishing:
- run `./gradlew test`
- run `./gradlew lint`
