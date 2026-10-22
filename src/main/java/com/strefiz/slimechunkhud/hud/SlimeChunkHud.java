package com.strefiz.slimechunkhud.hud;

import com.strefiz.slimechunkhud.config.ModConfig;
import com.strefiz.slimechunkhud.seed.SeedProvider;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

/**
 * Elemento de HUD que mostra se a chunk atual é slime chunk.
 * <p>
 * Renderiza texto colorido (verde = sim, vermelho = nāo, amarelo = seed
 * desconhecida) posicionado conforme a âncora, offsets e escala da config.
 * Escondido fora da Overworld e quando o HUD estā oculto (F1 — via render
 * condiçāo herdada da camada a que está anexado).
 */
public class SlimeChunkHud implements HudElement {

    private final SeedProvider seedProvider;
    private final ModConfig.Supplier config;

    public SlimeChunkHud(SeedProvider seedProvider, Supplier config) {
        this.seedProvider = seedProvider;
        this.config = config;
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        ModConfig cfg = config.get();
        if (!cfg.hudEnabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            return;
        }

        // Só Overworld.
        if (!seedProvider.isOverworld()) {
            return;
        }

        TextRenderer textRenderer = client.textRenderer;
        int chunkX = MathHelper.floor(client.player.getX() / 16.0);
        int chunkZ = MathHelper.floor(client.player.getZ() / 16.0);

        String text;
        int color;
        if (!seedProvider.hasSeed()) {
            text = "Slime Chunk: seed desconhecida";
            color = cfg.textColorUnknown;
        } else if (seedProvider.isSlimeChunk(chunkX, chunkZ)) {
            text = "Slime Chunk: SIM";
            color = cfg.textColorSlime;
        } else {
            text = "Slime Chunk: NÃO";
            color = cfg.textColorNotSlime;
        }

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        int[] pos = position(width, height, textRenderer.getWidth(text), textRenderer.fontHeight, cfg);

        MatrixStack matrices = graphics.getMatrices();
        matrices.pushPose();
        matrices.translate(pos[0], pos[1], 0);
        matrices.scale(cfg.hudScale, cfg.hudScale, 1.0f);

        if (cfg.showBackground) {
            int bgWidth = textRenderer.getWidth(text);
            int bgHeight = textRenderer.fontHeight;
            graphics.fill(0, 0, bgWidth, bgHeight, 0x80000000);
        }

        graphics.drawText(textRenderer, text, 0, 0, color, true);
        matrices.popPose();
    }

    /**
     * Calcula a posiçāo (x, y) do HUD com base na âncora e offsets.
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
