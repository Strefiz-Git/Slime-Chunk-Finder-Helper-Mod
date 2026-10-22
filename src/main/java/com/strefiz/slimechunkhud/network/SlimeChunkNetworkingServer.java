package com.strefiz.slimechunkhud.network;


import com.strefiz.slimechunkhud.seed.SlimeChunkChecker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.BitSet;

/**
 * Registra os payloads de rede e os handlers do servidor para slime chunks.
 * <p>
 * O servidor calcula slime chunks com a seed do mundo e envia apenas o resultado
 * (não a seed) ao cliente, mantendo a seed secreta.
 * <p>
 * Esta classe é usada pelo servidor (ModInitializer) para registrar os handlers.
 */
public class SlimeChunkNetworkingServer {

    /** Raio máximo permitido no servidor (config do servidor). */
    public static final int MAX_RADIUS = 10;

    /** Cooldown em ticks por jogador (config do servidor). */
    public static final int COOLDOWN_TICKS = 40;

    public static void register() {
        // Registra payloads (comum a cliente e servidor).
        PayloadTypeRegistry.serverboundPlay().register(C2SRequestSlimeChunks.TYPE, C2SRequestSlimeChunks.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CSlimeChunks.TYPE, S2CSlimeChunks.CODEC);

        // Handler do servidor: recebe pedido do cliente e responde com slime chunks.
        ServerPlayNetworking.registerGlobalReceiver(C2SRequestSlimeChunks.TYPE, (payload, context) -> {
            C2SRequestSlimeChunks req = (C2SRequestSlimeChunks) payload;
            var server = context.server();
            if (server == null) {
                return;
            }

            // Limitar raio.
            int radius = Math.min(req.radius(), MAX_RADIUS);
            if (radius < 1) {
                radius = 1;
            }

            // Só Overworld.
            var level = context.player().level();
            if (level.dimension() != Level.OVERWORLD) {
                // Responder com bitset vazio.
                int size = 2 * radius + 1;
                byte[] empty = new byte[(size * size + 7) / 8];
                context.player().connection.send(
                        new net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket(
                                new S2CSlimeChunks(req.centerX(), req.centerZ(), radius, empty)));
                return;
            }

            long seed = level.getSeed();
            SlimeChunkChecker checker = new SlimeChunkChecker();

            int size = 2 * radius + 1;
            int total = size * size;
            BitSet bits = new BitSet(total);
            for (int dz = 0; dz < size; dz++) {
                for (int dx = 0; dx < size; dx++) {
                    int chunkX = req.centerX() - radius + dx;
                    int chunkZ = req.centerZ() - radius + dz;
                    if (checker.isSlimeChunk(seed, chunkX, chunkZ)) {
                        bits.set(dz * size + dx);
                    }
                }
            }

            byte[] bitset = bits.toByteArray();
            // Garantir tamanho correto.
            if (bitset.length < (total + 7) / 8) {
                byte[] padded = new byte[(total + 7) / 8];
                System.arraycopy(bitset, 0, padded, 0, bitset.length);
                bitset = padded;
            }

            context.player().connection.send(
                    new net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket(
                            new S2CSlimeChunks(req.centerX(), req.centerZ(), radius, bitset)));
        });
    }
}
