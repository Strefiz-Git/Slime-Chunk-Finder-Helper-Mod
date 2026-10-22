package com.strefiz.slimechunkhud.seed;

import com.strefiz.slimechunkhud.config.ModConfig;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fornece a seed do mundo atual, respeitando a limitaçāo de que o cliente nāo
 * conhece a seed em multiplayer.
 * <p>
 * Ordem de prioridade:
 * <ol>
 *   <li>Singleplayer: seed do servidor integrado.</li>
 *   <li>Multiplayer: seed inserida manualmente na config, por servidor.</li>
 *   <li>Indisponível: estado "seed desconhecida".</li>
 * </ol>
 * A funcionalidade sō fica ativa na Overworld.
 * <p>
 * A detecçāo de Overworld e a leitura da seed sāo feitas pelo entrypoint
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

    public SeedProvider() {
        // Carrega sementes jā salvas na config (se houver).
        ModConfig.get().serverSeeds.forEach((k, v) -> serverSeeds.put(k, v));
    }

    /** Define a chave do servidor atual (endereço) e carrega a seed guardada. */
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

    /** Retorna a seed, ou {@link #UNKNOWN} se nāo houver. */
    public long getSeed() {
        return seed;
    }

    /** {@code true} se conhecemos a seed do mundo atual. */
    public boolean hasSeed() {
        return seed != UNKNOWN;
    }

    /** {@code true} se a dimensão atual é a Overworld. */
    public boolean isOverworld() {
        return dimensionOverworld;
    }

    public void setDimensionOverworld(boolean overworld) {
        this.dimensionOverworld = overworld;
    }

    /**
     * {@code true} se a chunk é slime chunk (sō em Overworld e com seed conhecida).
     */
    public boolean isSlimeChunk(int chunkX, int chunkZ) {
        if (!dimensionOverworld || !hasSeed()) {
            return false;
        }
        return checker.isSlimeChunk(seed, chunkX, chunkZ);
    }

    /** Limpa estado ao trocar de mundo/dimensão. */
    public void reset() {
        this.currentServerKey = "singleplayer";
        this.seed = UNKNOWN;
        this.dimensionOverworld = true;
        checker.clear();
    }
}
