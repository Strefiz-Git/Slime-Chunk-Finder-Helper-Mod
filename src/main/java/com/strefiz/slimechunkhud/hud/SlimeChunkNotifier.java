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
 * Notificaçāo transitória renderizada acima da hotbar quando o jogador entra
 * numa slime chunk.
 */
public class SlimeChunkNotifier implements HudElement {

    private final SeedProvider seedProvider;
    private final ModConfig.Supplier config;
    private final SlimeChunkStateTracker tracker;

    public SlimeChunkNotifier(SeedProvider seedProvider,
                              ModConfig.Supplier config,
                              SlimeChunkStateTracker tracker) {
        this.seedProvider = seedProvider;
        this.config = config;
        this.tracker = tracker;
    }

    /** Chama a cada tick para atualizar o estado da notificaçāo. */
    public void tick() {
        // A notificaçāo fica visível enquanto o jogador estiver numa slime chunk.
        // Não desaparece automaticamente; desaparece ao sair da slime chunk.
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        render(graphics);
    }

    /** Desenha a notificaçāo. Chama de {@link #extractRenderState}. */
    public void render(GuiGraphicsExtractor graphics) {
        ModConfig cfg = config.get();
        if (!cfg.notificationMode) {
            return;
        }

        if (!tracker.isInSlimeChunk()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) {
            return;
        }
        if (!seedProvider.isOverworld() || !seedProvider.hasSeed()) {
            return;
        }

        if (!tracker.isInSlimeChunk()) {
            return;
        }

        Font font = client.font;
        String text = "SLIME CHUNK!";
        int width = font.width(text);
        int scaledWidth = graphics.guiWidth();
        int scaledHeight = graphics.guiHeight();

        int x = (scaledWidth - width) / 2;
        int y = scaledHeight - 40;

        int bgWidth = width + 8;
        int bgHeight = font.lineHeight + 4;
        graphics.fill(x, y, x + bgWidth, y + bgHeight, 0xA0000000);
        graphics.text(font, Component.literal(text), x + 4, y + 2, 0xFF00FF00, true);
    }
}
