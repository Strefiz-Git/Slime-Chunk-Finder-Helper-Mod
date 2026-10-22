# Conventions â€” Slime Chunk Finder Helper Mod

> Loaded automatically in every session (via `opencode.json â€º instructions`). **Keep it short and practical.**
> Precedence: what existing code already does > this document > the agent's general preferences.

## Structure

- Code in `src/`, tests in `tests/` (or alongside the code, depending on the stack) â€” _confirm with `/bootstrap`_.
- One module = one responsibility. Files < ~300 lines, functions < ~40 lines (guidelines, not dogma).

## Reuse and modules

- **Search before writing:** `docs/MODULES.md` + `bash scripts/find-reuse.sh <terms>`.
- Pure functions, explicit inputs/outputs, no globals; I/O and state at the edges; dependencies as parameters.
- 1-2-3 rule: 1st consumer â†’ private to the feature Â· 2nd consumer â†’ `shared/<concept>/` Â· 3rd case â†’ generalize.
- Each module: single entry point (minimal public API), `README.md`, tests on the public API. Always import through the entry point.
- No generic `utils`/`helpers`: group by concept.

## Naming

- Files/folders: _e.g.: `kebab-case`_ Â· Types/classes: `PascalCase` Â· functions/variables: _e.g.: `camelCase`_ Â· constants: `UPPER_SNAKE_CASE`.
- Names say **what**, not **how**. Avoid obscure abbreviations. Booleans: `isX`, `hasX`, `canX`.

## Style

- Automatic formatting (the stack's formatter) â€” never debate style by hand.
- Comments explain **why**; code explains **how**.
- No dead code or forgotten *feature flags*.

## Documentation

- Docstrings on **everything** exported: what, parameters, return, errors, side effects, example (the stack's idiomatic format).
- `README.md` in each module (template: `docs/templates/MODULE_README.md`); a line in `docs/MODULES.md` for whatever is reusable.
- Language: _identifiers in English; docstrings/README in English_ (adjust to the project).
- Code and docs change in the same commit.

## Errors and logging

- Errors handled at the boundaries; actionable messages; never swallow exceptions silently.
- Logs with levels (`debug/info/warn/error`); never log secrets or personal data.

## Dependencies

- Prefer the standard library. New dependency: justify it in `DECISIONS.md` (maintenance, size, license).

## Git

- Conventional Commits (in English). Branches: `feat/â€¦`, `fix/â€¦`, `chore/â€¦` (long-running work can live in `ai/<topic>`).

## Stack-specific (Fabric 26.3, client-side only, Java 21)

_To be filled in with `/bootstrap`: formatter, linter, package manager, typical structure._

## Commands

See the table in `AGENTS.md` Â§1.

