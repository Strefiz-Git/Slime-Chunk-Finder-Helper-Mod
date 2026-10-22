package com.strefiz.slimechunkhud.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntryList;

/**
 * Estado da visualização de bordas de chunk (F3 + G).
 * <p>
 * Em Minecraft 26.3, a flag de chunk borders é armazenada em
 * {@code Minecraft.getInstance().debugEntries}, que pode ser consultado com
 * {@link DebugScreenEntries#CHUNK_BORDERS}.
 * <p>
 * Não é necessário Mixin: a flag é acessível diretamente via API pública.
 */
public final class ChunkBorderDetector {

    private ChunkBorderDetector() {}

    /**
     * Retorna {@code true} se a visualização de bordas de chunk (F3 + G) está ativa.
     */
    public static boolean chunkBordersActive() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null || mc.debugEntries == null) {
                return false;
            }
            return mc.debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_BORDERS);
        } catch (Throwable e) {
            // Fallback: se a API mudar, tentar ler manualmente.
            return false;
        }
    }
}
