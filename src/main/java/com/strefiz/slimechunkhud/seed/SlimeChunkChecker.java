package com.strefiz.slimechunkhud.seed;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Decide se uma dada chunk é uma slime chunk, com base na seed do mundo e nas
 * coordenadas da chunk.
 * <p>
 * O algoritmo é o vanilla (equivalente a
 * {@code WorldgenRandom.seedSlimeChunk(x, z, seed, 987234911L).nextInt(10) == 0}):
 * <pre>
 * new Random(seed
 *     + (long)(x * x * 0x4c1906)
 *     + (long)(x * 0x5ac0db)
 *     + (long)(z * z) * 0x4307a7L
 *     + (long)(z * 0x5f24f) ^ 0x3ad8025fL).nextInt(10) == 0
 * </pre>
 * Atenção: as multiplicações com chunkX/chunkZ são feitas em {@code int} (com
 * overflow) e só depois convertidas para {@code long}, exatamente como no
 * algoritmo de referência.
 * <p>
 * Os resultados são cacheados num mapa limitado para não recalcular a cada
 * frame.
 */
public final class SlimeChunkChecker {

    private final Map<Key, Boolean> cache = new ConcurrentHashMap<>();

    /**
     * Verifica se a chunk {@code (chunkX, chunkZ)} é slime chunk para a seed dada.
     *
     * @param seed      seed do mundo
     * @param chunkX    coordenada X da chunk
     * @param chunkZ    coordenada Z da chunk
     * @return {@code true} se for slime chunk
     */
    public boolean isSlimeChunk(long seed, int chunkX, int chunkZ) {
        Key key = new Key(seed, chunkX, chunkZ);
        return cache.computeIfAbsent(key, k -> compute(seed, chunkX, chunkZ));
    }

    private static boolean compute(long seed, int chunkX, int chunkZ) {
        long s = seed
                + (long) (chunkX * chunkX * 0x4c1906)
                + (long) (chunkX * 0x5ac0db)
                + (long) (chunkZ * chunkZ) * 0x4307a7L
                + (long) (chunkZ * 0x5f24f) ^ 0x3ad8025fL;
        return new java.util.Random(s).nextInt(10) == 0;
    }

    /** Limpa o cache (útil ao trocar de mundo/semente). */
    public void clear() {
        cache.clear();
    }

    /** Chave de cache imutável e sem colisões para (seed, chunkX, chunkZ). */
    private record Key(long seed, int chunkX, int chunkZ) {
        @Override
        public int hashCode() {
            return Objects.hash(seed, chunkX, chunkZ);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Key k)) return false;
            return seed == k.seed && chunkX == k.chunkX && chunkZ == k.chunkZ;
        }
    }
}
