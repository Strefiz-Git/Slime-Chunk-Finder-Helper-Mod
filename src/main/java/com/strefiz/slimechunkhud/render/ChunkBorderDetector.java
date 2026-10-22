package com.strefiz.slimechunkhud.render;

/**
 * Estado da visualização de bordas de chunk (F3 + G).
 * <p>
 * A detecção automática da flag do vanilla requer um Mixin mĺnimo sobre o
 * renderer de chunk borders de 26.3 (classe/campo a confirmar nas mappings
 * atuais). Até que seja confirmado, o jogador pode alternar esta flag manualmente
 * via {@link #setActive(boolean)} (ex.: ligado a uma keybind).
 */
public final class ChunkBorderDetector {

    private static boolean active = false;

    private ChunkBorderDetector() {}

    public static boolean chunkBordersActive() {
        return active;
    }

    /** Chamado pelo Mixin (quando implementado) ou manualmente para ativar/desativar. */
    public static void setActive(boolean active) {
        ChunkBorderDetector.active = active;
    }
}
