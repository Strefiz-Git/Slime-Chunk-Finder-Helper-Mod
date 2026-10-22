# DECISIONS â€” Slime Chunk Finder Helper Mod

> Decision log (short ADR). **Always record** when you choose between real alternatives, especially when *you* decided instead of asking.
> Read before reopening a topic. To change a decision: create a new one that **replaces it** (do not delete the old one).
> Status: `Accepted` Â· `Provisional` (assumed without the user's confirmation â€” revisit if it matters) Â· `Superseded by D-NNN`.

## Template

```
### D-NNN â€” Title  (YYYY-MM-DD) â€” Status
- **Context:** the problem and the constraints.
- **Options:** A) â€¦ B) â€¦ C) â€¦
- **Analysis (5 angles):** precedent Â· intent Â· external evidence Â· risk/reversibility Â· simplicity â€” 1 line each.
- **Decision:** the chosen one and why.
- **Assumptions:** what I assumed without confirming.
- **How to revert:** steps/cost.
- **Consequences:** what changes; what becomes harder.
```

## Decisions

### D-001 â€” Use the repository template with management documents  (2026-10-22) â€” Accepted
- **Context:** a new project, developed with AI agents during long sessions.
- **Decision:** keep the project state in versioned `.md` files (`PROGRESS`, `TODO`, `DECISIONS`â€¦) to survive context compression and session handoffs.
- **How to revert:** delete the management files and simplify `AGENTS.md`.

### D-002 â€” Modular, reusable, documented code by default  (2026-10-22) â€” Accepted
- **Context:** much code is written by agents in long sessions; without rules, duplication, `utils` junk drawers and modules nobody knows how to use appear.
- **Decision:** reuse before writing (`docs/MODULES.md` + `scripts/find-reuse.sh`); pure functions and modules with a minimal public API; structure by feature; everything public documented (docstring + module README). Skills `reusable-modules`, `project-structure`, `code-documentation`.
- **How to revert:** remove the three skills and the corresponding rules in `AGENTS.md` Â§5 and `docs/CONVENTIONS.md`.

