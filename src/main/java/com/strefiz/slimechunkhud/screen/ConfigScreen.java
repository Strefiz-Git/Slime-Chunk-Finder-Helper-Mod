package com.strefiz.slimechunkhud.screen;

import com.strefiz.slimechunkhud.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Ecrã simples de configuraçāo do mod. Permite ativar/desativar HUD, overlay e
 * modo notificaçāo.
 */
public class ConfigScreen extends Screen {

    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.literal("Slime Chunk HUD — Configuraçāo"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ModConfig cfg = ModConfig.get();

        int cx = this.width / 2 - 100;
        int cy = 60;
        int bw = 200;
        int bh = 20;

        // Botão: alternar HUD
        addRenderableWidget(Button.builder(
                Component.literal("HUD: " + (cfg.hudEnabled ? "ativado" : "desativado")),
                btn -> {
                    cfg.hudEnabled = !cfg.hudEnabled;
                    ModConfig.save();
                    btn.setMessage(Component.literal("HUD: " + (cfg.hudEnabled ? "ativado" : "desativado")));
                })
                .bounds(cx, cy, bw, bh).build());
        cy += bh + 6;

        // Botão: alternar overlay de bordas
        addRenderableWidget(Button.builder(
                Component.literal("Overlay bordas: " + (cfg.borderOverlayEnabled ? "ativado" : "desativado")),
                btn -> {
                    cfg.borderOverlayEnabled = !cfg.borderOverlayEnabled;
                    ModConfig.save();
                    btn.setMessage(Component.literal("Overlay bordas: " + (cfg.borderOverlayEnabled ? "ativado" : "desativado")));
                })
                .bounds(cx, cy, bw, bh).build());
        cy += bh + 6;

        // Botão: alternar modo notificaçāo
        addRenderableWidget(Button.builder(
                Component.literal("Notificaçāo: " + (cfg.notificationMode ? "ativada" : "desativada")),
                btn -> {
                    cfg.notificationMode = !cfg.notificationMode;
                    ModConfig.save();
                    btn.setMessage(Component.literal("Notificaçāo: " + (cfg.notificationMode ? "ativada" : "desativada")));
                })
                .bounds(cx, cy, bw, bh).build());
        cy += bh + 6;

        addRenderableWidget(Button.builder(
                Component.literal("Concluído"),
                btn -> Minecraft.getInstance().setScreenAndShow(this.parent))
                .bounds(cx, cy, bw, bh).build());
    }

    @Override
    public void onClose() {
        ModConfig.save();
        Minecraft.getInstance().setScreenAndShow(this.parent);
    }
}
