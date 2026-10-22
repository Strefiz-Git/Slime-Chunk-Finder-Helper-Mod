---
description: Long autonomous work (default 120 min) â€” only stops on real blockers
---
Enable the autopilot and start working.

1. Run: `bash scripts/autopilot.sh start $ARGUMENTS` (no argument = 120 minutes).
2. Follow the skill `autonomous-workflow` and the protocol in `AGENTS.md` Â§4 from beginning to end.
3. Don't ask for confirmations. Doubts â†’ 5 angles (`decide-without-asking`) â†’ decide and register it in `DECISIONS.md`.
4. Commit per logical unit and checkpoint `PROGRESS.md` every ~25 min (use `bash scripts/clock.sh` to see the time).
5. When the useful work runs out, you're blocked on everything, or time runs out: `bash scripts/autopilot.sh stop "reason"` and write the final summary.

