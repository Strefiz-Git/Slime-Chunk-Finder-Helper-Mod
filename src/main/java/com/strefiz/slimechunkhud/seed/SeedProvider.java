package com.strefiz.slimechunkhud.seed;

import com.strefiz.slimechunkhud.config.ModConfig;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fornece a seed do mundo atual, respeitando a limitaÃ§Äo de que o cliente nÄo
 * conhece a seed em multiplayer.
 * <p>
 * Ordem de prioridade:
 * <ol>
 *   <li>Singleplayer: seed do servidor integrado.</li>
 *   <li>Multiplayer: seed inserida manualmente na config, por servidor.</li>
 *   <li>IndisponÃ­vel: estado "seed desconhecida".</li>
 * </ol>
 * A funcionalidade sÅ fica ativa na Overworld.
 * <p>
 * A detecÃ§Äo de Overworld e a leitura da seed sÄo feitas pelo entrypoint
 * ({@link com.strefiz.slimechunkhud.SlimeChunkHudMod}) que tem acesso a
 * {@code Minecraft.getInstance().level} e ao servidor integrado.
 */
public class SeedProvider {

    private static final long UNKNOWN = Long.MIN_VALUE;

    private final SlimeChunkChecker checker = new SlimeChunkChecker();
    private final Map<String, Long> serverSeeds = new ConcurrentHashMap<>();
    private String currentServerKey = "singleplayer";
    private long seed = UNKNOWN;
    private boolean dimensionOverworld = true;
    private SlimeChunkProvider serverProvider;

    public SeedProvider() {
        // Carrega sementes jÄ salvas na config (se houver).
        ModConfig.get().serverSeeds.forEach((k, v) -> serverSeeds.put(k, v));
    }

    /** Define o SlimeChunkProvider do servidor (quando disponÃ­vel). */
    public void setServerProvider(SlimeChunkProvider provider) {
        this.serverProvider = provider;
    }

    /** Define a chave do servidor atual (endereÃ§o) e carrega a seed guardada. */
    public void setCurrentServer(String serverKey) {
        this.currentServerKey = Objects.requireNonNullElse(serverKey, "singleplayer");
        this.seed = serverSeeds.getOrDefault(this.currentServerKey, UNKNOWN);
    }

    /** Define manualmente a seed do servidor atual. */
    public void setCurrentServerSeed(long seed) {
        this.seed = seed;
        serverSeeds.put(currentServerKey, seed);
        ModConfig.get().serverSeeds.put(currentServerKey, seed);
        ModConfig.save();
    }

    /** Retorna a seed, ou {@link #UNKNOWN} se nÄo houver. */
    public long getSeed() {
        return seed;
    }

    /** {@code true} se conhecemos a seed do mundo atual. */
    public boolean hasSeed() {
        return seed != UNKNOWN;
    }

    /** {@code true} se a dimensÃ£o atual Ã© a Overworld. */
    public boolean isOverworld() {
        return dimensionOverworld;
    }

    public void setDimensionOverworld(boolean overworld) {
        this.dimensionOverworld = overworld;
    }

    /**
     * {@code true} se a chunk Ã© slime chunk.
     * <p>
     * Se o servidor companheiro estÃ¡ disponÃ­vel, usa o bitset recebido do servidor
     * (sem expor a seed). Caso contrÃ¡rio, usa o cÃ¡lculo local com a seed.
     */
    public boolean isSlimeChunk(int chunkX, int chunkZ) {
        if (!dimensionOverworld) {
            return false;
        }
        // Se o servidor tem o mod companheiro, usar os dados do servidor.
        if (serverProvider != null && serverProvider.serverHasMod() && serverProvider.hasData(chunkX, chunkZ)) {
            return serverProvider.isSlimeChunk(chunkX, chunkZ);
        }
        // Fallback: cÃ¡lculo local (seed manual ou singleplayer).
        if (!hasSeed()) {
            return false;
        }
        return checker.isSlimeChunk(seed, chunkX, chunkZ);
    }

    /** Limpa estado ao trocar de mundo/dimensÃ£o. */
    public void reset() {
        this.currentServerKey = "singleplayer";
        this.seed = UNKNOWN;
        this.dimensionOverworld = true;
        checker.clear();
    }
}
