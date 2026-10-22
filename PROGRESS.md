# PROGRESS — Slime Chunk Finder Helper Mod

> **The project's working memory.** Read at the start of each session and after each context compression.
> Keep *Current state* short and truthful (≤ 30 lines). The session log goes below, most recent first.

## Current state

- **Current milestone:** M0 — Foundations + Fase 1 (setup do projeto Fabric 26.3)
- **Last update:** 2026-10-22
- **Build/test health:** ❌ Gradle build não executável neste ambiente (Loom requer Gradle 9.2+; apenas Gradle 9.0 disponível para download)
- **In progress:** Fase 1 — setup do projeto (versões reais do Fabric aplicadas; wrapper Gradle 9.0 gerado)
- **Recommended next step:** Resolver bloqueio do Gradle (instalar Gradle 9.2+) → `./gradlew genSources` → confirmar APIs 26.3 → corrigir erros de compilação
- **Blockers / open questions:** Loom 1.14.10 requer Gradle 9.2.0+, mas apenas Gradle 9.0.0 está disponível para download direto (9.2+ retorna 404). Necessário instalar Gradle 9.2+ via outro meio ou encontrar espelho.
- **Risks / relevant technical debt:** nomes de classes/métodos da API 26.3 (HudElement/HudElementRegistry, LevelRenderContext/LevelRenderEvents, WorldgenRandom.seedSlimeChunk, getSingleplayerServer, ClientCommandManager.greedyString) são suposições a confirmar nos sources decompilados via `genSources`; Mixin F3+G inativo por segurança.

## Session log

### 2026-10-22 — Repositório criado a partir do Repo-Template; Fase 1 esqueleto + versões reais do Fabric + wrapper Gradle
- **Done:** copiado Repo-Template para Slime-Chunk-Finder-Helper-Mod; placeholders substituídos; TEMPLATE_GUIDE.md movido para docs/; src/.gitkeep criado; primeiro commit `chore: initialize project from template` (f94c396).
- **Done (Fase 1/esqueleto):** build.gradle, settings.gradle, gradle.properties (versões reais do template oficial Fabric: loom 1.14.10, MC 26.3, loader 0.19.5, fabric-api 0.164.0+26.3, Java 21); fabric.mod.json (id slimechunkhud, environment client, entrypoint client, mixins, icon do logo); .gitignore ajustado (Java/Gradle).
- **Done (código):** SlimeChunkHudMod (ClientModInitializer), seed/SlimeChunkChecker (algoritmo vanilla + cache), seed/SeedProvider (singleplayer auto / manual por servidor / desconhecido), config/ModConfig (Gson JSON), hud/SlimeChunkHud (HudElement), input/HudKeybind (ciclo OFF→HUD→Notificação→OFF), hud/SlimeChunkStateTracker (deteta entrada na slime chunk), hud/SlimeChunkNotifier (notificação acima da hotbar "SLIME CHUNK!"), render/SlimeChunkOverlay + ChunkBorderDetector (overlay verde, F3+G flag), screen/ConfigScreen + ModMenuIntegration + ModMenuScreenFactory, mixin/ChunkBorderFlagMixin (inativo por segurança).
- **Done (logo):** icon.png, logo_512.png, logo.svg copiados para src/main/resources/assets/slimechunkhud/; icon configurado no fabric.mod.json.
- **Done (testes):** src/test/.../SlimeChunkCheckerTest.java (pares conhecidos + cache).
- **Done (gradle):** gradlew, gradlew.bat, gradle/wrapper/ (Gradle 9.0.0, jar do wrapper baixado do fabric-example-mod); settings.gradle simplificado (sem subprojeto client); build.gradle simplificado (projeto único).
- **Done (docs):** docs/MODULES.md, DECISIONS.md (D-003..D-007), TODO.md (fases T-001..T-014), PROGRESS.md.
- **Decisions:** D-003 HudElementRegistry · D-004 algoritmo vanilla + cache · D-005 seed (singleplayer auto / manual MP) · D-006 Mixin F3+G inativo · D-007 versões como suposição (agora com versões reais do template oficial).
- **Findings / problemas:** Fabric APIs pesquisadas (HudElementRegistry, ClientCommandRegistrationCallback, LevelRenderEvents, WorldgenRandom.seedSlimeChunk) — nomes a confirmar nas mappings 26.3. TCP para repo.maven.apache.org OK. Gradle 9.2+ não disponível para download direto (404); Gradle 9.0.0 instalado mas incompatível com Loom 1.14.10 (requer 9.2.0).
- **State on exit:** build ❌ (não executável neste ambiente) · testes ❓ (não executados) · branch main · último commit a3133c5.
- **Next step:** Resolver bloqueio do Gradle → `./gradlew genSources` → confirmar APIs 26.3 → corrigir erros de compilação um a um.

## Archive

_Old entries (> 20 sessions) summarized here._
