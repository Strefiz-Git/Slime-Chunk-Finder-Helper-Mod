package com.strefiz.slimechunkhud.hud;



import com.strefiz.slimechunkhud.config.ModConfig;
import com.strefiz.slimechunkhud.seed.SeedProvider;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.minecraft.network.chat.Component;
/**
 * Elemento de HUD que mostra se a chunk atual Ã© slime chunk.
 * <p>
 * Renderiza texto colorido (verde = sim, vermelho = nÄo, amarelo = seed
 * desconhecida) posicionado conforme a Ã¢ncora, offsets e escala da config.
 * Escondido fora da Overworld.
 */
public class SlimeChunkHud implements HudElement {

    private final SeedProvider seedProvider;
    private final ModConfig.Supplier config;

    public SlimeChunkHud(SeedProvider seedProvider, ModConfig.Supplier config) {
        this.seedProvider = seedProvider;
        this.config = config;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        render(graphics);
    }

    /** Desenha o texto do HUD. Chama de {@link #extractRenderState}. */
    public void render(GuiGraphicsExtractor graphics) {
        ModConfig cfg = config.get();
        if (!cfg.hudEnabled) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) {
            return;
        }

        // SÃ³ Overworld.
        if (!seedProvider.isOverworld()) {
            return;
        }

        Font font = client.font;
        int chunkX = (int) Math.floor(client.player.getX() / 16.0);
        int chunkZ = (int) Math.floor(client.player.getZ() / 16.0);

        String text;
        int color;
        if (!seedProvider.hasSeed()) {
            text = "Slime Chunk: seed desconhecida";
            color = cfg.textColorUnknown;
        } else if (seedProvider.isSlimeChunk(chunkX, chunkZ)) {
            text = "Slime Chunk: SIM";
            color = cfg.textColorSlime;
        } else {
            text = "Slime Chunk: NÃƒO";
            color = cfg.textColorNotSlime;
        }

        int scaledWidth = graphics.guiWidth();
        int scaledHeight = graphics.guiHeight();
        int[] pos = position(scaledWidth, scaledHeight, font.width(text), font.lineHeight, cfg);

        // Desenha o fundo e o texto na posiÃ§Ã£o calculada.
        if (cfg.showBackground) {
            int bgWidth = font.width(text);
            int bgHeight = font.lineHeight;
            graphics.fill(pos[0], pos[1], pos[0] + bgWidth, pos[1] + bgHeight, 0x80000000);
        }

        graphics.text(font, Component.literal(text), pos[0], pos[1], color, true);
    }

    /**
     * Calcula a posiÃ§Äo (x, y) do HUD com base na Ã¢ncora e offsets.
     * Retorna {@code [x, y]}.
     */
    private static int[] position(int screenW, int screenH, int textW, int textH,
                                  ModConfig cfg) {
        int x = cfg.hudOffsetX;
        int y = cfg.hudOffsetY;

        switch (cfg.hudAnchor) {
            case TOP_CENTER -> x = (screenW - textW) / 2 + cfg.hudOffsetX;
            case TOP_RIGHT -> x = screenW - textW - cfg.hudOffsetX;
            case MIDDLE_LEFT -> y = (screenH - textH) / 2 + cfg.hudOffsetY;
            case MIDDLE_RIGHT -> {
                x = screenW - textW - cfg.hudOffsetX;
                y = (screenH - textH) / 2 + cfg.hudOffsetY;
            }
            case BOTTOM_LEFT -> y = screenH - textH - cfg.hudOffsetY;
            case BOTTOM_CENTER -> {
                x = (screenW - textW) / 2 + cfg.hudOffsetX;
                y = screenH - textH - cfg.hudOffsetY;
            }
            case BOTTOM_RIGHT -> {
                x = screenW - textW - cfg.hudOffsetX;
                y = screenH - textH - cfg.hudOffsetY;
            }
            default -> {} // TOP_LEFT
        }

        return new int[]{x, y};
    }

    /** Fornece a config atual (permite reler a cada frame sem reiniciar). */
    @FunctionalInterface
    public interface Supplier {
        ModConfig get();
    }
}

