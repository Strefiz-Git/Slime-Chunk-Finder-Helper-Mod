package com.strefiz.slimechunkhud.seed;

import com.strefiz.slimechunkhud.network.C2SRequestSlimeChunks;
import com.strefiz.slimechunkhud.network.S2CSlimeChunks;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Fornece slime chunks quando o servidor companheiro está disponível.
 * <p>
 * O servidor envia apenas o resultado (bitset de slime chunks numa área),
 * nunca a seed. O cliente pede a área quando o jogador muda de chunk e
 * armazena o bitset recebido para consulta rápida.
 * <p>
 * Se o servidor não tiver o mod, cai para o modo da Fase 1 (seed manual).
 */
public class ServerSlimeChunkProvider implements com.strefiz.slimechunkhud.seed.SlimeChunkProvider {

    private final Map<Long, S2CSlimeChunks> cache = new ConcurrentHashMap<>();
    private int lastChunkX = Integer.MIN_VALUE;
    private int lastChunkZ = Integer.MIN_VALUE;
    private boolean serverHasMod = false;
    private boolean checked = false;

    /** {@code true} se o servidor companheiro está disponível. */
    public boolean serverHasMod() {
        if (!checked) {
            checked = true;
            serverHasMod = ClientPlayNetworking.canSend(C2SRequestSlimeChunks.TYPE);
        }
        return serverHasMod;
    }

    /** Reinicia o estado ao trocar de servidor. */
    public void reset() {
        cache.clear();
        lastChunkX = Integer.MIN_VALUE;
        lastChunkZ = Integer.MIN_VALUE;
        checked = false;
        serverHasMod = false;
    }

    /**
     * Chama a cada tick. Se o servidor tem o mod e o jogador mudou de chunk,
     * pede a área de slime chunks ao servidor.
     */
    public void tick() {
        if (!serverHasMod()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }

        int chunkX = (int) Math.floor(mc.player.getX() / 16.0);
        int chunkZ = (int) Math.floor(mc.player.getZ() / 16.0);

        if (chunkX == lastChunkX && chunkZ == lastChunkZ) {
            return; // Sem mudança de chunk, não pedir novamente.
        }

        lastChunkX = chunkX;
        lastChunkZ = chunkZ;

        // Pedir área ao servidor (raio 6).
        ClientPlayNetworking.send(new C2SRequestSlimeChunks(chunkX, chunkZ, 6));
    }

    /** Armazena o resultado recebido do servidor. */
    public void onSlimeChunksReceived(S2CSlimeChunks response) {
        long key = (long) response.centerX() * 31 + response.centerZ();
        cache.put(key, response);
    }

    /** {@code true} se o chunk é slime chunk (segundo o servidor). */
    public boolean isSlimeChunk(int chunkX, int chunkZ) {
        if (!serverHasMod()) {
            return false; // Fallback para modo manual.
        }

        // Procurar na cache (chunk dentro de alguma área recebida).
        for (S2CSlimeChunks response : cache.values()) {
            if (response.isSlimeChunk(chunkX, chunkZ)) {
                return true;
            }
            // Verificar se o chunk está dentro do raio da resposta.
            int dx = Math.abs(chunkX - response.centerX());
            int dz = Math.abs(chunkZ - response.centerZ());
            if (dx <= response.radius() && dz <= response.radius()) {
                return false; // Dentro do raio mas não é slime chunk.
            }
        }
        return false; // Fora de qualquer área conhecida.
    }

    /** {@code true} se o servidor tem o mod e temos dados para este chunk. */
    public boolean hasData(int chunkX, int chunkZ) {
        if (!serverHasMod()) {
            return false;
        }
        for (S2CSlimeChunks response : cache.values()) {
            int dx = Math.abs(chunkX - response.centerX());
            int dz = Math.abs(chunkZ - response.centerZ());
            if (dx <= response.radius() && dz <= response.radius()) {
                return true;
            }
        }
        return false;
    }
}
