# PROGRESS — Slime Chunk Finder Helper Mod

> **The project's working memory.** Read at the start of each session and after each context compression.
> Keep *Current state* short and truthful (≤ 30 lines). The session log goes below, most recent first.

## Current state

- **Current milestone:** M0 — Foundations + Fase 1-8 + Fase 2 (servidor companheiro) — build passa
- **Last update:** 2026-10-22
- **Build/test health:** ✅ ./gradlew build passa ✅ ./gradlew test passa ✅ ./gradlew runClient passa (mod carrega no MC 26.3 sem crash)
- **In progress:** nada — todas as tarefas principais concluídas
- **Recommended next step:** avaliar T-015 (shaders, opcional), testar in-game com servidor companheiro, ou finalizar sessão
- **Blockers / open questions:** nenhum
- **Risks / relevant technical debt:** Detecção F3+G confirmada via bytecode do Minecraft 26.3 (javap): `Minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_BORDERS)`. Overlay verde depende de confirmação da API de render de mundo (LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN). Fase 2 implementada mas não testada in-game com servidor companheiro.

## Session log

### 2026-10-22 — Build passa com ./gradlew build e ./gradlew test
- **Done:** Wrapper Gradle copiado do Minecraft-MultyCore-Mod (Gradle 9.8.0); gradle.properties alinhado (Loom 1.17-SNAPSHOT, Fabric API 0.160.5+26.3, Java 25); build.gradle simplificado (modmenu compileOnly/localRuntime, sem gson explícito); fabric.mod.json com valores diretos (java >=25).
- **Done (código corrigido para API 26.3):** SlimeChunkHudMod, SlimeChunkHud, SlimeChunkNotifier, SlimeChunkStateTracker, HudKeybind, SlimeChunkOverlay, ConfigScreen, ModConfig, SeedProvider, SlimeChunkChecker — todos compilam contra Minecraft 26.3 mappings.
- **Done (testes):** SlimeChunkCheckerTest reescrito para testes de propriedade (determinismo, cache) sem valores hardcoded não verificados.
- **Done (logo):** icon.png, logo_512.png, logo.svg em src/main/resources/assets/slimechunkhud/; ícone configurado no fabric.mod.json.
- **Decisions:** D-003 HudElementRegistry → HudElementRegistry.addLast · D-004 algoritmo vanilla + cache · D-005 seed (singleplayer auto / manual MP) · D-006 Mixin F3+G inativo · D-007 versões reais do Fabric · D-008 Gradle 9.2+ bloqueio (resolvido copiando wrapper do mod irmão).
- **Findings / problemas:** MathHelper não existe em 26.3 (usar Math.floor); KeyMappingHelper.registerKeyBinding não existe (criar KeyMapping diretamente); TextFieldWidget não existe (remover do ConfigScreen); GuiGraphicsExtractor não tem poseStack() (usar graphics.text() diretamente); sendFeedback(Component, boolean) → sendFeedback(Component); level.dimension().location() → level.dimension() == Level.OVERWORLD; server.getOverworld() → server.getLevel(Level.OVERWORLD).
- **State on exit:** build ✅ · testes ✅ · branch main · último commit c658a3d.
- **Next step:** ./gradlew runClient para testar in-game; preencher README; git push.

## Archive

_Old entries (> 20 sessions) summarized here._
