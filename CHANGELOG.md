# CHANGELOG — Slime Chunk Finder Helper Mod

## [1.0.0] — 2026-10-22

### Added
- Mod cliente Fabric 26.3 (`slimechunkhud`, environment client, Java 25)
- HUD mostrando "Slime Chunk: SIM / NÃO / seed desconhecida" com âncora, offsets e escala configuráveis
- Keybind registrada nas opções de controle do Minecraft: ciclo OFF → HUD → Notificação → OFF
- Notificação "SLIME CHUNK!" acima da hotbar ao entrar numa slime chunk (modo notificação)
- Overlay verde nos chunk borders (F3 + G) para slime chunks (estrutura pronta; Mixin F3+G inativo por segurança)
- Comando client-side `/slimeseed <seed>` para definir a seed do servidor atual
- Configuração persistida em `config/slimechunkhud.json` (Gson): sementes por servidor, cores, raio do overlay, etc.
- Seed automática em singleplayer (servidor integrado); estado "seed desconhecida" em multiplayer sem config
- Integração opcional com Mod Menu (compileOnly/localRuntime, não é dependência obrigatória)
- Logo do mod: icon.png (128×128), logo_512.png, logo.svg

### Changed
- Versões do build alinhadas com mods irmãos que compilam (Loom 1.17-SNAPSHOT, Fabric API 0.160.5+26.3, Loader 0.19.5, Java 25)
- Nomes de API corrigidos para Minecraft 26.3 (confirmados via ScreenMine, RocketWithSpearMod, ClientHarvestMod)

### Added
- Detecção F3+G via API pública do Minecraft 26.3: `Minecraft.getInstance().debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_BORDERS)` (sem Mixin necessário)
- Overlay verde desenha slime chunks apenas quando chunk borders (F3+G) está ativo
- Mixin ChunkBorderFlagMixin removido (não mais necessário)

### Known limitations
- Overlay verde depende de confirmação da API de render de mundo (LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN) e implementação do desenho de contornos (BufferBuilder/RenderType em 26.3)
- Resource pack / shader compatibilidade do overlay não testada
