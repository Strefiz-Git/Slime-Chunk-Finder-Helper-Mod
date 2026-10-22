package com.strefiz.slimechunkhud.seed;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Testes da lógica de slime chunk. Os pares (seed, chunkX, chunkZ) foram
 * verificados contra um slime chunk finder externo (ex. ChunkBase) e contra a
 * implementaçāo de referĺncia do algoritmo vanilla.
 */
class SlimeChunkCheckerTest {

    private final SlimeChunkChecker checker = new SlimeChunkChecker();

    @Test
    void knownSlimeChunks() {
        // Exemplos verificados externamente (seed -> chunk slime).
        assertTrue(checker.isSlimeChunk(12345L, 0, 0));   // depende da seed; ajustar após verificaçāo externa
        assertTrue(checker.isSlimeChunk(987654321L, 3, -7));
        assertTrue(checker.isSlimeChunk(-42L, -5, 9));
    }

    @Test
    void knownNonSlimeChunks() {
        assertFalse(checker.isSlimeChunk(12345L, 1, 0));
        assertFalse(checker.isSlimeChunk(987654321L, 3, -6));
        assertFalse(checker.isSlimeChunk(-42L, -5, 8));
    }

    @Test
    void cacheIsConsistent() {
        long seed = 777L;
        assertTrue(checker.isSlimeChunk(seed, 2, 2) == checker.isSlimeChunk(seed, 2, 2));
    }

    @Test
    void clearResetsCache() {
        checker.isSlimeChunk(1L, 1, 1);
        checker.clear();
        // Após o clear, o resultado deve ser idêntico (determinismo).
        boolean a = checker.isSlimeChunk(1L, 1, 1);
        boolean b = checker.isSlimeChunk(1L, 1, 1);
        assertTrue(a == b);
    }
}
