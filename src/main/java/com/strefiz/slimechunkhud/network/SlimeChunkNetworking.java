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
        // Registra payloads no cliente (necessário para enviar C2S e receber S2C).
        // Em singleplayer, o servidor integrado já registrou; ignoramos erro se já registado.
        try {
            PayloadTypeRegistry.serverboundPlay().register(C2SRequestSlimeChunks.TYPE, C2SRequestSlimeChunks.CODEC);
        } catch (IllegalArgumentException ignored) {
            // Já registado (ex: singleplayer, servidor integrado registrou primeiro)
        }
        try {
            PayloadTypeRegistry.clientboundPlay().register(S2CSlimeChunks.TYPE, S2CSlimeChunks.CODEC);
        } catch (IllegalArgumentException ignored) {
            // Já registado
        }

        // Handler do cliente: recebe slime chunks do servidor.
        ClientPlayNetworking.registerGlobalReceiver(
                S2CSlimeChunks.TYPE, (payload, context) -> {
                    provider.onSlimeChunksReceived(payload);
                });
    }
}