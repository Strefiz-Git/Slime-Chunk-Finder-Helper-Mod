# TODO — Slime Chunk Finder Helper Mod

> The agent's task queue. Order = priority. A task = something that fits in a short session and has **verifiable acceptance criteria**.
> Format: `- [ ] T-NNN [P0–P3] [size S/M/L] Title — acceptance: …  (ref: ROADMAP M#, BUGS B-NNN)`
> P0 = blocks everything · P1 = current milestone · P2 = important · P3 = when there is time.
> L tasks (> ~2h) must be broken down before starting (skill `planning`).

## Now

- [ ] T-001 [P0] [S] /bootstrap — acceptance: AGENTS.md has real commands; `scripts/check.sh` runs; ARCHITECTURE.md filled in.
- [ ] T-002 [P0] [M] Fase 1 — setup do projeto Fabric 26.3 — acceptance: build.gradle/gradle.properties/fabric.mod.json criados; ./gradlew build passa.
- [ ] T-003 [P1] [M] Fase 2 — lógica de slime chunk + testes — acceptance: SlimeChunkChecker + SlimeChunkCheckerTest; testes de propriedade passam.
- [ ] T-004 [P1] [M] Fase 3 — obtenção da seed (SeedProvider) — acceptance: singleplayer auto, multiplayer manual por servidor, estado desconhecido, comando /slimeseed.
- [ ] T-005 [P1] [M] Fase 4 — configuração (ModConfig JSON) — acceptance: config/slimechunkhud.json persiste; todas as opções lidas/gravadas.
- [ ] T-006 [P1] [M] Fase 5 — HUD + notificação acima da hotbar — acceptance: HUD renderiza sim/não/desconhecida; keybind ciclo OFF→HUD→Notificação; notificação "SLIME CHUNK!" acima da hotbar ao entrar na slime chunk.
- [ ] T-007 [P1] [M] Fase 6 — overlay verde nos chunk borders — acceptance: SlimeChunkOverlay itera chunks dentro do raio; desliga sem flag; sem crash.
- [ ] T-008 [P1] [M] Fase 7 — robustez — acceptance: reset ao trocar mundo/dimensão; null-safety; nada no servidor dedicado.
- [ ] T-009 [P1] [M] Fase 8 — testes e entrega — acceptance: build limpo; testes passam; docs atualizadas; commit.

## Next

- [x] T-010 [P2] [S] ./gradlew runClient — testar in-game: mod carrega no MC 26.3 sem crash.
- [x] T-011 [P2] [S] Preencher README (descrição, features, getting started, seed em multiplayer).
- [x] T-012 [P2] [S] Atualizar CHANGELOG.md com resumo do release.
- [x] T-013 [P2] [S] git push para o repositório remoto. ✅ (push origin main concluído)
- [x] T-014 [P3] [M] Detecção F3 + G via API pública — ChunkBorderDetector usa Minecraft.getInstance().debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_BORDERS). Sem Mixin necessário. Overlay verde só desenha quando chunk borders está ativo.
- [ ] T-015 [P3] [S] Resource pack / shader compatibilidade do overlay — acceptance: overlay não crasha com shaders.

## Blocked (waiting for the user's answer — see QUESTIONS.md)

- Nenhum.

## Done (last 20; the rest goes to PROGRESS.md › Archive)

- T-000 [P0] [S] Repositório inicializado a partir do Repo-Template — acceptance: primeiro commit feito.
- T-000b [P0] [S] Logo do mod adicionado (icon.png, logo_512.png, logo.svg).
- T-000c [P0] [S] Toggle HUD ↔ Notificação acima da hotbar implementado.
- T-000d [P0] [S] Build passa com ./gradlew build e testes com ./gradlew test (API 26.3 confirmada via mods irmãos).
- T-000e [P0] [S] ./gradlew runClient passa — mod carrega no Minecraft 26.3 sem crash.
- T-000f [P0] [S] README preenchido com descrição, features, getting started, seed em multiplayer.
