# REVIEW â€” Slime Chunk Finder Helper Mod

> The agent's self-reviews (and the user's reviews). Run `/review` before closing a milestone, before big commits and after long blocks of autonomous work.
> Skill: `code-review`. For a second independent opinion use the `@reviewer` subagent; to attack a plan use `@critic`.

## Quick checklist (per review)

- [ ] **Correctness:** does it do what the acceptance criteria ask for? Edge cases and errors handled?
- [ ] **Tests:** cover the new behavior and the fixed bugs? Pass deterministically?
- [ ] **Design:** clear responsibilities, small modules, low coupling, no obvious duplication?
- [ ] **Readability:** names, structure, comments only where they explain the *why*?
- [ ] **Security:** input validated, secrets out of the repository, dependencies justified? (`security-review`)
- [ ] **Performance:** nothing clearly quadratic/blocking on hot paths?
- [ ] **UI/UX** (if applicable): states (empty/loading/error), contrast, focus, responsiveness? (`ui-design`)
- [ ] **Docs:** do README/ARCHITECTURE/CHANGELOG/CONVENTIONS reflect reality?
- [ ] **Git:** small commits, clear messages, no files that don't belong?

## Severity of findings

`blocker` (do not close without fixing) Â· `major` Â· `minor` Â· `nit` Â· `idea` (goes to IDEAS.md)

## Reviews

### Template

```
### R-NNN â€” Scope (YYYY-MM-DD) â€” Result: âœ… ok / âš ï¸ with caveats / âŒ fails
- **Scope:** commits/files reviewed.
- **Findings:**
  | # | Severity | Where | Problem | Action | Status |
  |---|------------|------|----------|------|--------|
- **What works well:** â€¦
- **Residual risks:** â€¦
```

_No reviews yet._

