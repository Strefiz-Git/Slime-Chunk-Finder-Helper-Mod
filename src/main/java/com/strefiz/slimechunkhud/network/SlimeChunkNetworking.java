package com.strefiz.slimechunkhud.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Registra os payloads de rede e o handler S2C no cliente.
 * <p>
 * Esta classe é usada pelo cliente para registrar o handler de slime chunks.
 * O handler C2S do servidor está em {@link SlimeChunkNetworkingServer}.
 */
public class SlimeChunkNetworking {

    public static void registerClient(com.strefiz.slimechunkhud.seed.ServerSlimeChunkProvider provider) {
        // Registra payloads (comum a cliente e servidor).
        PayloadTypeRegistry.serverboundPlay().register(C2SRequestSlimeChunks.TYPE, C2SRequestSlimeChunks.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2CSlimeChunks.TYPE, S2CSlimeChunks.CODEC);

        // Handler do cliente: recebe slime chunks do servidor.
        ClientPlayNetworking.registerGlobalReceiver(
                S2CSlimeChunks.TYPE, (payload, context) -> {
                    provider.onSlimeChunksReceived(payload);
                });
    }
}
