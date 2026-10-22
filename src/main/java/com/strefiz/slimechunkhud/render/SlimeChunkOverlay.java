package com.strefiz.slimechunkhud.render;

import com.strefiz.slimechunkhud.config.ModConfig;
import com.strefiz.slimechunkhud.seed.SeedProvider;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

import java.util.function.Supplier;

/**
 * Overlay verde nas bordas de chunk (F3 + G).
 * <p>
 * Para cada chunk dentro de {@code overlayRadius} do jogador que seja slime
 * chunk, desenha um contorno verde.
 * <p>
 * Nota: a leitura da flag F3 + G depende de {@link ChunkBorderDetector}, que
 * precisa de um Mixin confirmado para 26.3 (veja a classe). Até lā, o overlay
 * fica ativo quando {@code borderOverlayEnabled} e a flag estā ligada.
 */
public class SlimeChunkOverlay {

    private final SeedProvider seedProvider;
    private final Supplier<ModConfig> config;

    public SlimeChunkOverlay(SeedProvider seedProvider, Supplier<ModConfig> config) {
        this.seedProvider = seedProvider;
        this.config = config;
    }

    public void render(LevelRenderContext context) {
        ModConfig cfg = config.get();
        if (!cfg.borderOverlayEnabled || !ChunkBorderDetector.chunkBordersActive()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) {
            return;
        }
        if (!seedProvider.isOverworld() || !seedProvider.hasSeed()) {
            return;
        }

        int playerChunkX = MathHelper.floor(client.player.getX() / 16.0);
        int playerChunkZ = MathHelper.floor(client.player.getZ() / 16.0);
        int radius = Math.max(1, cfg.overlayRadius);

        // TODO: confirmar a API exata de LevelRenderContext e buffer de vértices
        // para 26.3 antes de ativar o desenho de contornos. Abaixo, a estrutura de
        // iteração; o desenho real fica a confirmar nas mappings.
        for (int z = playerChunkZ - radius; z <= playerChunkZ + radius; z++) {
            for (int x = playerChunkX - radius; x <= playerChunkX + radius; x++) {
                if (seedProvider.isSlimeChunk(x, z)) {
                    drawChunkOutline(context, x, z, cfg);
                }
            }
        }
    }

    private void drawChunkOutline(LevelRenderContext context, int chunkX, int chunkZ, ModConfig cfg) {
        // Desenho do contorno verde: implementar com buffer de vértices + RenderType
        // após confirmar a API de render de mundo em 26.3.
        // Coordenadas da chunk: [chunkX*16, chunkZ*16] .. [(chunkX+1)*16, (chunkZ+1)*16].
    }
}
