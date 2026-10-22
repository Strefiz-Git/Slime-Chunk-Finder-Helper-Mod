package com.strefiz.slimechunkhud.seed;

/**
 * Interface para fornecedores de slime chunks.
 * <p>
 * Implementado por {@code SlimeChunkChecker} (cálculo local) e
 * {@code ServerSlimeChunkProvider} (dados do servidor companheiro).
 */
public interface SlimeChunkProvider {
    /** {@code true} se o servidor companheiro está disponível. */
    boolean serverHasMod();

    /** {@code true} se temos dados para o chunk dado. */
    boolean hasData(int chunkX, int chunkZ);

    /** {@code true} se o chunk é slime chunk. */
    boolean isSlimeChunk(int chunkX, int chunkZ);
}
