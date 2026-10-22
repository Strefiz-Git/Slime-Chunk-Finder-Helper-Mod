# MODULES — Slime Chunk Finder Helper Mod

Catálogo de mīdulos reutilizĺveis e funçōes pūblicas. Consultar antes de escrever
código novo (skill `reusable-modules`). Cada mīdulo tem responsabilidade Ănica,
dependências em uma sō direçāo e documentaçāo pūblica com docstring.

## Mīdulos

| Mīdulo | Pacote | Responsabilidade | Depende de | Notas |
|--------|--------|------------------|------------|-------|
| `SlimeChunkHudMod` | `com.strefiz.slimechunkhud` | Entry point cliente; registra HUD, overlay, keybind, comando `/slimeseed` e rastreia mundo/dimensão. | todos (orquestrador) | `ClientModInitializer`. |
| `SlimeChunkChecker` | `...seed` | `boolean isSlimeChunk(seed, chunkX, chunkZ)` com algoritmo vanilla + cache limitado. | nada | Puro, determinista, testado. |
| `SeedProvider` | `...seed` | Obtém/fornece a seed (singleplayer integrado ou manual por servidor); estado Overworld. | `SlimeChunkChecker`, `ModConfig` | Cliente nāo conhece seed em MP. |
| `ModConfig` | `...config` | Persistência Gson em `config/slimechunkhud.json`; Ăncoras, cores, raio, sementes por servidor. | nada | `load()`/`save()`. |
| `SlimeChunkHud` | `...hud` | Renderiza texto do HUD (sim/nāo/desconhecida) com âncora, offsets e escala. | `SeedProvider`, `ModConfig` | `HudElement` (Fabric Hud API). |
| `HudKeybind` | `...input` | Keybind registrada nas opçōes de controle; alterna HUD. | `ModConfig` | `KeyBindingHelper` + `ClientTickEvents`. |
| `SlimeChunkOverlay` | `...render` | Desenha slime chunks a verde quando bordas de chunk ativas (F3 + G). | `SeedProvider`, `ModConfig`, `ChunkBorderDetector` | `LevelRenderEvents`. |
| `ChunkBorderDetector` | `...render` | Flag de bordas de chunk ativas (via Mixin mĺnimo ou toggle manual). | nada | Mixin alvo a confirmar em 26.3. |
| `ConfigScreen` | `...screen` | Ecrã de configuraçāo (HUD, overlay, seed do servidor). | `ModConfig` | `Screen`. |
| `ModMenuIntegration` | `...screen` | Integraçāo opcional com Mod Menu (`custom.modmenu.screen`). | nada | Sem dependĺncia forte. |

## Fluxo de dados

```
seed (singleplayer integrado / manual por servidor)
  → SeedProvider → SlimeChunkChecker.isSlimeChunk(seed, cx, cz)
                                ↓
          SlimeChunkHud.render()  → texto no HUD
                                ↓
          SlimeChunkOverlay.render() → contorno verde nos chunk borders
```

## Regras

- Nada de lógica de servidor; `"environment": "client"`.
- Cache de slime chunk limitado (evita recálculo por frame).
- Reset de estado/cache ao trocar de mundo/dimensão.
- Null-safety: sem mundo/jogador/seed nāo crasha.

## APIs a confirmar em 26.3 (assumçōes)

- `HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, ...)` + `HudElement.render(GuiGraphics, DeltaTracker)`.
- `LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN` + `LevelRenderContext`.
- `ClientCommandRegistrationCallback` + `ClientCommands` / `ClientCommandManager.greedyString()`.
- `ClientWorld.getRegistryKey()` / `RegistryKeys.WORLD` para detectar Overworld.
- `MinecraftClient.getInstance().getSingleplayerServer().getOverworld().getSeed()` (singleplayer).
- Mixin alvo para ler a flag F3 + G (classe/campo a confirmar — atualmente inativo).

## Testes

- `src/test/java/com/strefiz/slimechunkhud/seed/SlimeChunkCheckerTest.java` — pares conhecidos de slime/nāo-slime.
