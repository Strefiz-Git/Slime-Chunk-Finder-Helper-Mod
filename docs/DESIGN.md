# Design â€” Slime Chunk Finder Helper Mod

> Source of truth for the interface (web, desktop, mobile or game). Skill: `ui-design`.
> If the project has no UI, delete this file.

## Direction

- **Tone/personality:** _e.g.: welcoming, minimalist, "cozy"_
- **Platform and target sizes:** _e.g.: desktop 1280Ã—720+, mobile 360Ã—640+_
- **Visual references:** _to be filled in_

## Tokens

### Color

| Token | Value | Use |
|-------|-------|-----|
| `--color-bg` | `#â€¦` | Base background |
| `--color-surface` | `#â€¦` | Cards/panels |
| `--color-text` | `#â€¦` | Main text (contrast â‰¥ 4.5:1 with `bg`) |
| `--color-text-muted` | `#â€¦` | Secondary text |
| `--color-primary` | `#â€¦` | Primary action |
| `--color-danger` / `--color-success` / `--color-warning` | `#â€¦` | States |

### Typography

| Role | Family | Size/weight/line-height |
|-------|---------|--------------------------|
| Title | | |
| Body | | |
| Caption | | |

### Spacing (4/8 scale)

`4 Â· 8 Â· 12 Â· 16 Â· 24 Â· 32 Â· 48 Â· 64`

### Radii, shadows, borders

_to be defined_

### Motion

Duration 120â€“240 ms, smooth *easing*; respect "reduce motion".

## Components

| Component | Required states | Notes |
|------------|---------------------|-------|
| Button | normal Â· hover Â· focus Â· pressed Â· disabled Â· loading | |
| Text field | normal Â· focus Â· error Â· disabled | |
| List/table | empty Â· loading Â· error Â· with data | |

## Layout principles

_Hierarchy, grid, alignment, density._

## Accessibility

- Contrast â‰¥ 4.5:1 (text) / 3:1 (UI) Â· touch targets â‰¥ 44 px Â· keyboard/gamepad navigation Â· visible focus Â· don't rely on color alone.

## Design change log

| Date | Change | Reason |
|------|-----------|--------|

