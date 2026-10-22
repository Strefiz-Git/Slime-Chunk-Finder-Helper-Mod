# Slime Chunk Finder Helper Mod

Mod cliente (Fabric) para Minecraft 26.3 que mostra no HUD se a chunk onde o jogador está é uma slime chunk, e opcionalmente pinta as slime chunks a verde quando a visualização de bordas de chunk está ativa (F3 + G).

> **Status:** in progress · **Stack:** Fabric 26.3, client-side only, Java 25 · **Created:** 2026-10-22

## What it does

O mod resolve um problema simples: em Minecraft, apenas 1 em cada 10 chunks é uma "slime chunk" (onde slimes podem spawnar abaixo da camada 40), mas o jogo não indica quais são. Como o cliente não conhece a seed do mundo em multiplayer, o mod permite definir a seed manualmente por servidor e, a partir dela, calcular exatamente quais chunks são slime chunks.

## Features

- [x] HUD mostrando "Slime Chunk: SIM / NÃO / seed desconhecida" com âncora, offsets e escala configuráveis
- [x] Keybind nas opções de controle do Minecraft: ciclo OFF → HUD → Notificação → OFF
- [x] Notificação "SLIME CHUNK!" acima da hotbar ao entrar numa slime chunk (modo notificação)
- [x] Overlay verde nos chunk borders (F3 + G) — estrutura pronta, Mixin F3+G inativo por segurança (documentado em DECISIONS.md D-006)
- [x] Comando client-side `/slimeseed <seed>` para definir a seed do servidor atual
- [x] Configuração persistida em `config/slimechunkhud.json` (Gson): sementes por servidor, cores, raio do overlay, âncora, escala
- [x] Seed automática em singleplayer (servidor integrado); estado "seed desconhecida" em multiplayer sem config
- [x] Escondido fora da Overworld (Nether/End mostram "N/A" ou ficam escondidos)
- [x] Logo do mod: icon.png (128×128), logo_512.png, logo.svg

## Getting started

### Pré-requisitos

- **Gradle 9.2+** (o wrapper já está no repo, mas o Loom requer Gradle 9.2+. O wrapper commitado é Gradle 9.8.0)
- **Java 25** (Minecraft 26.x requer Java 25; o build.gradle configura toolchain para 25)
- Internet para baixar dependências do Fabric (repo.maven.acessível)

### Build

```bash
# Na raiz do repositório
./gradlew build
```

### Executar (cliente de desenvolvimento)

```bash
./gradlew runClient
```

Isso abre o Minecraft com o mod carregado. Crie um mundo singleplayer ou entre num servidor.

### Configurar a seed (multiplayer)

Em servidores multiplayer, o cliente não recebe a seed. Você tem duas opções:

1. **Comando no jogo:** `/slimeseed <seed>` — define a seed do servidor atual imediatamente.
2. **Config manual:** edite `config/slimechunkhud.json` e adicione a seed sob `serverSeeds` (chave = endereço do servidor).

### Testes

```bash
./gradlew test
```

## Repository structure

```
.
├── AGENTS.md          # rules for AI agents
├── ROADMAP.md         # vision and milestones
├── TODO.md            # task queue
├── PROGRESS.md        # current state and session log
├── DECISIONS.md       # technical decisions
├── QUESTIONS.md       # questions for the user
├── BUGS.md            # known bugs
├── REVIEW.md          # self-reviews
├── IDEAS.md           # out-of-scope ideas
├── ARCHITECTURE.md    # architecture
├── CHANGELOG.md       # version history
├── gradle.properties  # Fabric/Loom/Minecraft versions
├── build.gradle       # Gradle build script
├── gradlew            # Gradle wrapper (Unix)
├── gradlew.bat        # Gradle wrapper (Windows)
├── gradle/wrapper/    # Gradle wrapper files
├── docs/              # conventions, testing, design, glossary, module catalog (MODULES.md)
├── scripts/           # utilities (check, commit, autopilot…)
├── .opencode/         # OpenCode skills, commands, agents and plugins
└── src/
    ├── main/
    │   ├── java/com/strefiz/slimechunkhud/  # mod source code
    │   └── resources/
    │       ├── fabric.mod.json
    │       └── assets/slimechunkhud/        # icon.png, logo_512.png, logo.svg
    └── test/
        └── java/com/strefiz/slimechunkhud/seed/  # unit tests
```

## Development with OpenCode

```bash
opencode            # open at the repo root
/bootstrap          # (first time) fill in the docs and the project commands
/autopilot 120      # autonomous work for ~2h
```

See `docs/TEMPLATE_GUIDE.md` for the full workflow guide.

## Contributing

Commits follow [Conventional Commits](https://www.conventionalcommits.org/). Before submitting: `./gradlew build` (or `bash scripts/check.sh`).

## License

MIT License
