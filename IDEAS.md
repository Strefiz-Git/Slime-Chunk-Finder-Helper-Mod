# IDEAS — Slime Chunk Finder Helper Mod

> Ideas inbox. **Golden rule: an idea is not a task.** Capture it in 1–2 lines and get on with the work.
> Triage (agent, when the `TODO.md` queue runs out — or the user, at any time):
> `Promote` → `TODO.md`/`ROADMAP.md` · `Defer` · `Reject` (with a reason — avoids discussing it again).
> Criteria for promoting: does it serve the `ROADMAP.md` **vision/milestone**? High value / low effort? Doesn't duplicate anything?

## Inbox

- (2026-10-22) [origin: user] Fase 2 opcional — servidor companheiro que envia slime chunks (não a seed) para automatizar a detecção em servidores sem introdução manual de seed. Padrão: RocketWithSpearMod (ClientPlayNetworking.canSend + payload custom). Sem encriptação própria (a ligação já é cifrada). Ver detalhes abaixo em "Fase 2".

## Under evaluation

_Empty.

## Promoted

_Empty.

## Deferred

_Empty.

## Rejected (with reason)

_Empty.

---

## Fase 2 — Servidor companheiro (opcional, seed nunca sai do servidor)

**Contexto:** Em servidores vanilla, o servidor nunca envia a seed ao cliente (só um hash parcial que não serve para calcular slime chunks). O utilizador quer que o mod funcione automaticamente em servidores, sem introdução manual da seed. A encriptação da seed não protege de quem controla o cliente (código é MIT e público; a seed está na memória). A única forma de manter a seed secreta é o servidor calcular as slime chunks e enviar apenas o resultado.

**Abordagem (Option 2 — servidor envia slime chunks, não a seed):**

1. **Reestruturar o projeto** de "client-only" para "both" (environment: *):
   - build.gradle: `splitEnvironmentSourceSets()` + `sourceSet sourceSets.main` + `sourceSet sourceSets.client`
   - fabric.mod.json: entrypoint `main` (servidor) + `client`
   - Mover código cliente para `src/client/java/` (ou manter em `src/main/java` com `@Environment(EnvType.CLIENT)`)

2. **Servidor (entrypoint main):**
   - Ler a seed do mundo na dimensão do jogador (só Overworld)
   - Receber payload C2SRequestSlimeChunks (centro + raio)
   - Limitar raio a maxRadius (config do servidor, por exemplo 8 a 10 chunks)
   - Aplicar cooldown por jogador (config: cooldownTicks)
   - Calcular slime chunks com a fórmula vanilla (ou `WorldgenRandom.seedSlimeChunk`)
   - Responder com S2CSlimeChunks (centro, raio, bitset de (2r+1)² bits)

3. **Cliente:**
   - Ao entrar no servidor, usar `ClientPlayNetworking.canSend` para detetar se o servidor tem o mod
   - Se tiver: pedir área quando o jogador muda de chunk (não a cada tick), desenhar o bitset recebido
   - Se não tiver: cair para o modo da Fase 1 (seed manual via /slimeseed)

4. **Config do servidor:**
   - enabled, maxRadius, cooldownTicks
   - Permissão por jogador (OP only ou todos)

5. **Aviso honesto sobre segurança:**
   - Esconde a seed, mas não impede por completo que alguém a deduza (com ferramentas de brute-force que usam padrões de slime chunks). O raio pequeno, cooldown e limite à zona perto do jogador aumentam o custo. Pode responder apenas para chunks dentro da view distance do jogador.

6. **Padrão de implementação:** seguir RocketWithSpearMod (ClientPlayNetworking.canSend + payload custom + ModNetworking). O Slime Chunk Finder só precisa de passar a "environment": "*" com entrypoint main.

**Decisão do utilizador (2026-10-22):** Option 2 — servidor envia slime chunks (não a seed), sem encriptação própria (a ligação já é cifrada). A seed nunca sai do servidor.

**Fase 1 (atual, completo):** cliente-only, seed manual via /slimeseed, guardada por servidor em config/slimechunkhud.json. Funciona em servidores vanilla.

---

**Decisão sobre encriptação (2026-10-22):** A encriptação da seed não protege de quem controla o cliente (código é MIT e público; a seed está na memória). O Minecraft já encripta a ligação (handshake RSA + AES em online-mode). A encriptação própria seria complexidade sem ganho de segurança. Apenas a Option 2 (servidor envia slime chunks, não a seed) protege a seed.