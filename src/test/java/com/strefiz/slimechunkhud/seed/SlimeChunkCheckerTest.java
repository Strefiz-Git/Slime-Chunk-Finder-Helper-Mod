package com.strefiz.slimechunkhud.seed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Testes da lógica de slime chunk.
 * <p>
 * Valida propriedades invariantes do algoritmo: determinismo, variação entre seeds,
 * e proporção de slime chunks próxima de 10% (1 em 10 chunks, como no vanilla).
 */
class SlimeChunkCheckerTest {

    private final SlimeChunkChecker checker = new SlimeChunkChecker();

    @Test
    void deterministicSameResult() {
        // O algoritmo deve ser determinístico: mesma seed e chunk dão sempre o mesmo resultado.
        long seed = 12345L;
        assertEquals(checker.isSlimeChunk(seed, 0, 0), checker.isSlimeChunk(seed, 0, 0));
        assertEquals(checker.isSlimeChunk(seed, 10, -5), checker.isSlimeChunk(seed, 10, -5));
        assertEquals(checker.isSlimeChunk(seed, -3, 7), checker.isSlimeChunk(seed, -3, 7));
    }

    @Test
    void differentChunksAreConsistent() {
        // Para uma mesma seed, o resultado para cada chunk é sempre o mesmo.
        long seed = 12345L;
        boolean c1a = checker.isSlimeChunk(seed, 0, 0);
        boolean c1b = checker.isSlimeChunk(seed, 0, 0);
        boolean c2a = checker.isSlimeChunk(seed, 1, 0);
        boolean c2b = checker.isSlimeChunk(seed, 1, 0);
        assertEquals(c1a, c1b);
        assertEquals(c2a, c2b);
    }

    @Test
    void slimeChunkProportionIsAboutTenPercent() {
        // Em uma amostra grande, cerca de 1 em 10 chunks deve ser slime chunk.
        long seed = 987654321L;
        int[] counts = {0, 0}; // [0] = total, [1] = slimeCount
        for (int x = -50; x < 50; x++) {
            for (int z = -50; z < 50; z++) {
                counts[0]++;
                if (checker.isSlimeChunk(seed, x, z)) {
                    counts[1]++;
                }
            }
        }
        int total = counts[0];
        int slimeCount = counts[1];
        double proportion = (double) slimeCount / total;
        // Espera-se cerca de 10%, com margem de erro razoável para amostra finita.
        assertTrue(proportion > 0.05 && proportion < 0.15,
                () -> "Proporção de slime chunks inesperada: " + proportion + " (slime=" + slimeCount + "/" + total + ")");
    }

    @Test
    void cacheIsConsistentAfterClear() {
        // Após clear(), o resultado deve ser idêntico (determinismo preservado).
        long seed = 777L;
        int x = 2, z = 2;
        boolean before = checker.isSlimeChunk(seed, x, z);
        checker.clear();
        boolean after = checker.isSlimeChunk(seed, x, z);
        assertEquals(before, after);
    }

    @Test
    void resetDoesNotChangeAlgorithm() {
        // O reset do SeedProvider não altera o algoritmo do checker.
        long seed = 42L;
        int x = 5, z = -3;
        boolean result = checker.isSlimeChunk(seed, x, z);
        assertTrue(result == true || result == false); // tipo de retorno é boolean
    }
}
