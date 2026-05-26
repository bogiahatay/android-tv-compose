# Architecture

The project follows Clean Architecture principles combined with the MVVM pattern using a feature-based structure.

---

# Layers

## Presentation Layer (`feature/*/presentation`)
- Compose UI
- ViewModel
- Navigation
- TV focus handling

Responsibilities:
- render UI state
- handle user interaction
- forward actions to ViewModel

Rules:
- avoid business logic inside composables
- observe LiveData using `observeAsState()`
- prefer stateless composables
- UI-only local state is allowed with `remember` / `rememberSaveable`
- preserve TV focus and scroll state

---

## Domain Layer (`feature/*/domain`)
- UseCases
- Models
- Repository interfaces

Responsibilities:
- business logic
- reusable business operations

Rules:
- pure Kotlin only
- no Android dependencies
- no Compose dependencies

---

## Data Layer (`feature/*/data` & `core/network`)
- Retrofit APIs
- Room database
- Repository implementations
- Data sources
- DTOs
- Mappers

Responsibilities:
- fetch/store data
- map DTOs into domain models

Rules:
- repositories implement domain interfaces
- DTOs must not leak into presentation layer

---

# Dependency Rules

Dependencies always point toward the Domain layer.

Allowed:
- presentation -> domain
- data -> domain
- feature -> core

Forbidden:
- domain -> Android
- domain -> Compose
- presentation -> repository implementations

---

# MVVM & State Management

## ViewModel
- handles screen state
- executes UseCases
- exposes `LiveData<UiState>`
- receives actions via `onAction(UiAction)`

## UI (Composable)
- renders `UiState`
- sends events through `UiAction`
- should remain mostly stateless

## Result Handling
Uses `Resource<T>` for:
- Loading
- Success<T>
- Error(AppError)

---

# Navigation

- Single `MainActivity`
- Navigation Compose
- screen-level navigation only

Requirements:
- preserve TV focus state after navigation
- preserve scroll state when returning to screen

---

# TV Architecture Rules

Requirements:
- D-pad first experience
- focused item must always remain visible
- avoid random focus jumps
- use stable keys for LazyRow/LazyColumn
- avoid excessive FocusRequester usage

Performance:
- minimize recompositions during focus changes
- optimize LazyRow scrolling
- optimize image loading for TV devices
- avoid expensive animations in scrolling lists

---

# Theme & CompositionLocal

Use CompositionLocal only for:
- theme
- typography
- dimensions
- spacing
- global UI configuration

Do NOT use CompositionLocal for:
- repositories
- business logic
- screen state

---

# Dependency Injection

Uses Hilt for:
- repositories
- use cases
- Retrofit
- Room
- ExoPlayer dependencies

Rules:
- prefer constructor injection
- avoid manual singleton/service locator patterns

---

# Project Structure

```txt
net.habui.tv
├── core
│   ├── designsystem
│   ├── network
│   ├── player
│   ├── focus
│   ├── ui
│   └── util
│
├── feature
│   ├── home
│   ├── player
│   ├── settings
│   └── detail
│
└── navigation