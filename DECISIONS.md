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

### D-003 — HUD via Fabric HudElementRegistry (not deprecated HudRenderCallback)  (2026-10-22) — Accepted
- **Context:** Fabric 21.6+ rewrote the HUD API; `HudRenderCallback` is deprecated. Need a modern HUD element.
- **Options:** A) `HudElementRegistry.attachElementBefore(...)` with `HudElement.render(GuiGraphics, DeltaTracker)`. B) deprecated `HudRenderCallback`.
- **Analysis:** precedent: Fabric docs use `HudElementRegistry`. intent: future-proof on 26.x. evidence: Fabric docs `HudElementRegistry`. risk: low (API stable since 1.21.6). simplicity: A is the recommended way.
- **Decision:** A — `HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, ...)`.
- **Assumptions:** `VanillaHudElements.CHAT` and `HudElement.render(GuiGraphics, DeltaTracker)` exist in 26.3.
- **How to revert:** switch to `addLast` or another anchor; low cost.

### D-004 — Slime chunk algorithm + cache  (2026-10-22) — Accepted
- **Context:** need `isSlimeChunk(seed, x, z)` matching vanilla.
- **Options:** A) `WorldgenRandom.seedSlimeChunk(x, z, seed, 987234911L).nextInt(10) == 0`. B) hand-written `Random(...)` formula.
- **Analysis:** precedent: vanilla & wiki algorithm identical. evidence: WorldgenRandom javadoc (salt=987234911L). risk: B must match int-then-long casts exactly. simplicity: prefer A if accessible, else B.
- **Decision:** prefer A (vanilla method); fall back to B with exact casts; cache keyed by record `(seed, chunkX, chunkZ)` in a bounded `ConcurrentHashMap`.
- **Assumptions:** `WorldgenRandom.seedSlimeChunk` is accessible in client mappings; otherwise B is byte-identical.
- **How to revert:** remove cache or switch to B.

### D-005 — Seed acquisition (client does not know seed in multiplayer)  (2026-10-22) — Accepted
- **Context:** client lacks world seed in multiplayer; only singleplayer integrated server exposes it.
- **Options:** A) singleplayer auto + manual per-server config in MP. B) try to crack partial hash (not feasible).
- **Analysis:** precedent: plan's stated limitation. intent: honest "seed desconhecida" state. risk: none. simplicity: A.
- **Decision:** A — singleplayer: `MinecraftClient.getSingleplayerServer().getOverworld().getSeed()`. MP: manual seed per server in config + `/slimeseed` command. Else: "seed desconhecida".
- **Assumptions:** client can access integrated server + overworld seed (API to confirm in 26.3).
- **How to revert:** disable seed-dependent features.

### D-006 — Chunk border F3+G detection via optional Mixin  (2026-10-22) — Accepted
- **Context:** overlay must activate only when F3 + G chunk borders are on; no clean public API to read that flag.
- **Options:** A) Mixin minimal on chunk-border renderer to expose flag. B) user-toggleable flag only.
- **Analysis:** precedent: plan says minimal Mixin to read flag; stop if API missing. risk: unverified mixin target crashes on load. simplicity: B is safest.
- **Decision:** provide `ChunkBorderDetector` flag + inactive Mixin source (not registered by default). Overlay renders when flag is active. Mixin to be enabled only after confirming target/field in 26.3.
- **Assumptions:** overlay works without auto F3+G detection until Mixin confirmed.
- **How to revert:** remove Mixin; keep manual flag toggle.

### D-007 — Project coordinates & versions (assumptions to pin)  (2026-10-22) — Accepted
- **Context:** mod id `slimechunkhud`, environment client, Fabric 26.3, Java 21; Gradle not installed locally.
- **Options:** A) write `build.gradle`/`gradle.properties` with placeholder versions. B) wait for exact Fabric API version.
- **Analysis:** intent: give the executing AI a compilable template. evidence: Fabric Maven exists. risk: versions may be wrong for 26.3. simplicity: A with clear assumption comments.
- **Decision:** A — versions in `gradle.properties` marked as assumptions; user must pin real Fabric/MC 26.x versions and install Gradle before `./gradlew build`.
- **Assumptions:** Fabric API + Loom versions in gradle.properties are placeholders.
- **How to revert:** edit `gradle.properties`/`build.gradle` versions.

### D-008 — Gradle/Loom version compatibility (blocked)  (2026-10-22) — Accepted
- **Context:** Loom 1.14.10 (latest stable compatible with Java 21) requires Gradle 9.2.0+ (plugin.api-version 9.2.0). Only Gradle 9.0.0 is available for direct download; 9.2+ returns 404 from services.gradle.org.
- **Options:** A) install Gradle 9.2+ via alternative source (SDKMAN, mirror, GitHub release). B) use an older Loom version compatible with Gradle 9.0. C) wait for Gradle 9.2+ to be published.
- **Analysis:** precedent: Fabric template uses Loom 1.18-SNAPSHOT (requires Gradle 9.7+). intent: make `./gradlew build` work. risk: Loom versions older than 1.14 may not exist or lack MC 26.3 support. simplicity: A is the only viable path.
- **Decision:** A — user must install Gradle 9.2+ (or newer) before `./gradlew build`/`genSources` can run. Wrapper is set to Gradle 9.0.0 as a placeholder.
- **Assumptions:** Gradle 9.2+ exists but isn't directly downloadable from services.gradle.org.
- **How to revert:** change `gradle/wrapper/gradle-wrapper.properties` distributionUrl to a different Gradle version.


