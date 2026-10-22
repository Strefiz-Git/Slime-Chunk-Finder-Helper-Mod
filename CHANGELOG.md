# CHANGELOG — Slime Chunk Finder Helper Mod

## [Unreleased]

### Added
- Mod cliente Fabric 26.3 (`slimechunkhud`, environment client)
- HUD mostrando "Slime Chunk: SIM / NÃO / seed desconhecida" com âncora, offsets e escala configuráveis
- Keybind registrada nas opções de controle do Minecraft: ciclo OFF → HUD → Notificação → OFF
- Notificação "SLIME CHUNK!" acima da hotbar ao entrar numa slime chunk (modo notificação)
- Overlay verde nos chunk borders (F3 + G) para slime chunks (Mixin F3+G inativo por segurança)
- Comando client-side `/slimeseed <seed>` para definir a seed do servidor atual
- Configuração persistida em `config/slimechunkhud.json` (Gson): sementes por servidor, cores, raio do overlay, etc.
- Seed automática em singleplayer (servidor integrado); estado "seed desconhecida" em multiplayer sem config
- Integração opcional com Mod Menu (`custom.modmenu.screen`)
- Logo do mod: icon.png (128×128), logo_512.png, logo.svg

### Changed
- Versões do build atualizadas para as reais do template oficial Fabric (MC 26.3, Loader 0.19.5, Fabric API 0.164.0+26.3, Loom 1.14.10, Java 21)
- Gradle wrapper adicionado (Gradle 9.0.0)

### Blocked
- `./gradlew build` não executável: Loom 1.14.10 requer Gradle 9.2.0+; apenas Gradle 9.0.0 disponível para download
