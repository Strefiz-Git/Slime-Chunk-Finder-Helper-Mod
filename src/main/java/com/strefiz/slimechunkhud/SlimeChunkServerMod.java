package com.strefiz.slimechunkhud;


import com.strefiz.slimechunkhud.network.SlimeChunkNetworking;
import com.strefiz.slimechunkhud.network.SlimeChunkNetworkingServer;
import net.fabricmc.api.ModInitializer;

/**
 * Entrypoint do servidor (ModInitializer).
 * <p>
 * Registra os handlers de rede para slime chunks e inicializa o servidor.
 * O servidor calcula slime chunks com a seed do mundo e envia apenas o resultado
 * (não a seed) ao cliente, mantendo a seed secreta.
 */
public class SlimeChunkServerMod implements ModInitializer {

    public static final String MOD_ID = "slimechunkhud";

    @Override
    public void onInitialize() {
        // Registra payloads de rede (C2S e S2C) e handlers do servidor.
        SlimeChunkNetworkingServer.register();
    }
}
