# PROGRESS — Slime Chunk Finder Helper Mod

> **The project's working memory.** Read at the start of each session and after each context compression.
> Keep *Current state* short and truthful (≤ 30 lines). The session log goes below, most recent first.

## Current state

- **Current milestone:** M0 — Foundations + Fase 1 (setup do projeto Fabric 26.3)
- **Last update:** 2026-10-22
- **Build/test health:** ✅ structure initialized; ❓ Gradle build not run (Gradle not installed locally; versions in gradle.properties are assumptions to pin)
- **In progress:** Fase 1 — setup do projeto (build.gradle, settings.gradle, gradle.properties, fabric.mod.json)
- **Recommended next step:** T-002 (concluir Fase 1) → T-003 (Fase 2 slime chunk + testes)
- **Blockers / open questions:** versões exatas de Fabric API/Loom/Minecraft 26.3 ainda não confirmadas (marcadas como suposição em gradle.properties); Gradle não instalado localmente — não foi possível executar `./gradlew build` nem testar no Minecraft.
- **Risks / relevant technical debt:** nomes de classes/métodos da API 26.3 (HudElement, LevelRenderContext, WorldgenRandom, getSingleplayerServer) são suposições a confirmar antes de compilar; Mixin F3+G inativo por segurança.

## Session log

### 2026-10-22 — Repositório criado a partir do Repo-Template e Fase 1 implementada (esqueleto)
- **Done:** copiado Repo-Template para Slime-Chunk-Finder-Helper-Mod (mantido .gitattributes e LICENSE próprios); placeholders substituídos; TEMPLATE_GUIDE.md movido para docs/; src/.gitkeep criado; primeiro commit `chore: initialize project from template` (f94c396).
- **Done (Fase 1/esqueleto):** gradle.properties, settings.gradle, build.gradle (versões como suposição); fabric.mod.json (id slimechunkhud, environment client, entrypoint client, mixins); .gitignore ajustado (Java/Gradle).
- **Done (código):** SlimeChunkHudMod (ClientModInitializer), seed/SlimeChunkChecker (algoritmo vanilla + cache), seed/SeedProvider (singleplayer auto / manual por servidor / desconhecido), config/ModConfig (Gson JSON), hud/SlimeChunkHud (HudElement), input/HudKeybind (alterna HUD), render/SlimeChunkOverlay + ChunkBorderDetector (overlay verde, F3+G flag), screen/ConfigScreen + ModMenuIntegration, mixin/ChunkBorderFlagMixin (inativo por segurança).
- **Done (testes):** src/test/.../SlimeChunkCheckerTest.java (pares conhecidos + cache).
- **Done (docs):** docs/MODULES.md (catálogo + APIs a confirmar), DECISIONS.md (D-003..D-007), TODO.md (fases T-001..T-014), PROGRESS.md.
- **Decisions:** D-003 HudElementRegistry · D-004 algoritmo vanilla + cache · D-005 seed (singleplayer auto / manual MP) · D-006 Mixin F3+G inativo · D-007 versões como suposição.
- **Findings / problemas:** Fabric APIs pesquisadas (HudElementRegistry, ClientCommandRegistrationCallback, LevelRenderEvents, WorldgenRandom.seedSlimeChunk) — nomes a confirmar nas mappings 26.3. Gradle/Maven não instalados; TCP para repo.maven.apache.org OK.
- **State on exit:** build ❓ (não executado) · testes ❓ (não executados) · branch main · último commit f94c396.
- **Next step:** T-002 concluir Fase 1; depois T-003 Fase 2.

## Archive

_Old entries (> 20 sessions) summarized here._
