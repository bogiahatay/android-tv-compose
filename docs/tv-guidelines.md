# Android TV Guidelines

This document defines the Android TV UX and Compose TV principles used in this project.

---

# TV UX & Navigation

- D-pad first experience
- Do not assume touch input
- Focus must always remain visible
- Preserve focus after navigation
- Avoid unexpected focus jumps
- Preserve scroll position when returning to screens

Navigation Drawer:
- Use `ModalNavigationDrawer`
- Restore focus correctly when returning from drawer navigation

---

# Focus Management

- Use `FocusRequester` only when manual focus restoration is required
- Preserve previously focused item where appropriate
- Avoid excessive manual focus handling
- Use consistent focus positioning behavior in lazy lists
- Use stable keys for `LazyRow` and `LazyColumn`

---

# Compose TV Components

- Prefer `androidx.tv.material3`
- Prefer `androidx.tv.foundation`
- All interactive components must support focus states
- Focused items should have clear visual feedback:
    - scale
    - border
    - elevation
    - color change

---

# Performance

- TVs often have weaker hardware than phones
- Minimize unnecessary recompositions
- Avoid expensive animations in `LazyRow`
- Optimize image loading sizes
- Use lazy layouts for content lists
- Preserve smooth D-pad navigation

---

# Visual Standards

- Use large readable text for 10-foot experience
- Maintain strong contrast between text and background
- Preserve focus visibility in dark and light themes
- Avoid placing important UI too close to screen edges