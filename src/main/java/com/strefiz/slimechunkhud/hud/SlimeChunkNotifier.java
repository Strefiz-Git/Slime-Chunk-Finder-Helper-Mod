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
 * Notificaçāo transitória renderizada acima da hotbar quando o jogador entra
 * numa slime chunk.
 * <p>
 * Só fica visível enquanto {@code ModConfig.notificationMode} estiver ativo e o
 * jogador estiver numa slime chunk (ou nos primeiros ticks após entrar, consoante
 * a implementaçāo). Renderiza acima da hotbar (parte inferior do ecrā).
 */
public class SlimeChunkNotifier implements HudElement {

    private final SeedProvider seedProvider;
    private final ModConfig.Supplier config;
    private final SlimeChunkStateTracker tracker;
    private int notificationTicksLeft = 0;

    public SlimeChunkNotifier(SeedProvider seedProvider,
                              ModConfig.Supplier config,
                              SlimeChunkStateTracker tracker) {
        this.seedProvider = seedProvider;
        this.config = config;
        this.tracker = tracker;
    }

    /** Chama a cada tick para atualizar o estado da notificaçāo. */
    public void tick() {
        ModConfig cfg = config.get();
        if (!cfg.notificationMode || !cfg.notifyOnEnterSlimeChunk) {
            notificationTicksLeft = 0;
            return;
        }

        if (tracker.tick()) {
            // Entrou numa slime chunk: mostra a notificaçāo por alguns ticks.
            notificationTicksLeft = 40; // ~2 segundos a 20 tps
        }

        if (notificationTicksLeft > 0) {
            notificationTicksLeft--;
        }
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        ModConfig cfg = config.get();
        if (!cfg.notificationMode || notificationTicksLeft <= 0) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            return;
        }
        if (!seedProvider.isOverworld() || !seedProvider.hasSeed()) {
            return;
        }

        if (!tracker.isInSlimeChunk()) {
            return;
        }

        TextRenderer textRenderer = client.textRenderer;
        String text = "SLIME CHUNK!";
        int width = textRenderer.getWidth(text);
        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();

        // Acima da hotbar: perto do centro inferior do ecrā.
        int x = (scaledWidth - width) / 2;
        int y = scaledHeight - 40; // acima da hotbar (que fica por volta de scaledHeight - 20)

        MatrixStack matrices = graphics.getMatrices();
        matrices.pushPose();
        matrices.translate(x, y, 0);
        matrices.scale(1.5f, 1.5f, 1.0f);

        int bgWidth = width + 8;
        int bgHeight = textRenderer.fontHeight + 4;
        graphics.fill(0, 0, bgWidth, bgHeight, 0xA0000000);
        graphics.drawText(textRenderer, text, 4, 2, 0xFF00FF00, true);
        matrices.popPose();
    }
}
