package com.strefiz.slimechunkhud.screen;

import com.strefiz.slimechunkhud.config.ModConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

/**
 * Ecrã simples de configuração do mod. Permite ativar/desativar HUD e overlay,
 * ajustar âncora/offset/escala e definir a seed do servidor atual.
 */
public class ConfigScreen extends Screen {

    private final Screen parent;

    protected ConfigScreen(Screen parent) {
        super(Text.literal("Slime Chunk HUD — Configuraçāo"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ModConfig cfg = ModConfig.get();

        int cx = width / 2 - 100;
        int cy = 60;

        // Botão: alternar HUD
        addDrawableChild(ButtonWidget.builder(
                Text.literal("HUD: " + (cfg.hudEnabled ? "ativado" : "desativado")),
                btn -> {
                    cfg.hudEnabled = !cfg.hudEnabled;
                    ModConfig.save();
                    btn.setMessage(Text.literal("HUD: " + (cfg.hudEnabled ? "ativado" : "desativado")));
                })
                .dimensions(cx, cy, 200, 20).build());

        // Botão: alternar overlay de bordas
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Overlay bordas: " + (cfg.borderOverlayEnabled ? "ativado" : "desativado")),
                btn -> {
                    cfg.borderOverlayEnabled = !cfg.borderOverlayEnabled;
                    ModConfig.save();
                    btn.setMessage(Text.literal("Overlay bordas: " + (cfg.borderOverlayEnabled ? "ativado" : "desativado")));
                })
                .dimensions(cx, cy + 26, 200, 20).build());

        // Campo: seed do servidor atual
        TextFieldWidget seedField = new TextFieldWidget(textRenderer, cx, cy + 56, 200, 20,
                Text.literal("Seed do servidor"));
        seedField.setText(String.valueOf(cfg.serverSeeds.getOrDefault("current", 0L)));
        addDrawableChild(seedField);

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Definir seed"),
                btn -> {
                    try {
                        long seed = Long.parseLong(seedField.getText());
                        cfg.serverSeeds.put("current", seed);
                        ModConfig.save();
                    } catch (NumberFormatException ignored) {}
                })
                .dimensions(cx, cy + 82, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Concluído"),
                btn -> client.setScreen(parent))
                .dimensions(cx, cy + 110, 200, 20).build());
    }

    @Override
    public void onClose() {
        ModConfig.save();
        client.setScreen(parent);
    }
}
