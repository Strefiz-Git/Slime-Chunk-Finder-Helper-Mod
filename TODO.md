# TODO — Slime Chunk Finder Helper Mod

> The agent's task queue. Order = priority. A task = something that fits in a short session and has **verifiable acceptance criteria**.
> Format: `- [ ] T-NNN [P0–P3] [size S/M/L] Title — acceptance: …  (ref: ROADMAP M#, BUGS B-NNN)`
> P0 = blocks everything · P1 = current milestone · P2 = important · P3 = when there is time.
> L tasks (> ~2h) must be broken down before starting (skill `planning`).

## Now

- [ ] T-001 [P0] [S] /bootstrap — acceptance: AGENTS.md has real commands; `scripts/check.sh` runs; ARCHITECTURE.md filled in.
- [ ] T-002 [P1] [M] Fase 1 — setup do projeto Fabric 26.3 — acceptance: build.gradle/gradle.properties/fabric.mod.json criados; compila (quando Gradle disponível); log "mod carregado".
- [ ] T-003 [P1] [M] Fase 2 — lógica de slime chunk + testes — acceptance: SlimeChunkChecker + SlimeChunkCheckerTest; pares conhecidos passam; cache limitado.
- [ ] T-004 [P1] [M] Fase 3 — obtenção da seed (SeedProvider) — acceptance: singleplayer auto, multiplayer manual por servidor, estado desconhecido, comando /slimeseed.
- [ ] T-005 [P1] [M] Fase 4 — configuração (ModConfig JSON) — acceptance: config/slimechunkhud.json persiste; todas as opções lidas/gravadas.
- [ ] T-006 [P1] [M] Fase 5 — HUD (SlimeChunkHud + HudKeybind) — acceptance: HUD renderiza sim/não/desconhecida; keybind alterna; posição/escala da config.
- [ ] T-007 [P1] [M] Fase 6 — overlay verde nos chunk borders — acceptance: SlimeChunkOverlay itera chunks dentro do raio; desliga sem flag; sem crash.
- [ ] T-008 [P1] [M] Fase 7 — robustez — acceptance: reset ao trocar mundo/dimensão; null-safety; nada no servidor dedicado.
- [ ] T-009 [P1] [M] Fase 8 — testes e entrega — acceptance: build limpo; checklist manual documentado; docs atualizadas; commit.

## Next

- [ ] T-010 [P2] [S] ConfigScreen e integração Mod Menu — acceptance: ecrã de configuração abre; salva config.
- [ ] T-011 [P2] [S] Confirmar APIs reais do Minecraft 26.3 (HudElement, LevelRenderContext, WorldgenRandom) e ajustar nomes.
- [ ] T-012 [P3] [M] Modo de arrastar o HUD (preview) — acceptance: posição do HUD ajustável por arraste (se anchor+offset for insuficiente).

## Later

- [ ] T-013 [P3] [S] Resource pack / shader compatibilidade do overlay — acceptance: overlay não crasha com shaders.
- [ ] T-014 [P3] [S] Mixin ativo para detecção F3 + G — acceptance: ChunkBorderFlagMixin aplicado sem crash em 26.3.

## Blocked (waiting for the user's answer — see QUESTIONS.md)

- Nenhum.

## Done (last 20; the rest goes to PROGRESS.md › Archive)

- T-000 [P0] [S] Repositório inicializado a partir do Repo-Template — acceptance: primeiro commit feito.
