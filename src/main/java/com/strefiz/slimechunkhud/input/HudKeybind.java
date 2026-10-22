package com.strefiz.slimechunkhud.input;

import com.strefiz.slimechunkhud.config.ModConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/**
 * Keybind do HUD, registrada nas configurações de teclas do Minecraft.
 * <p>
 * Pressionar a tecla alterna a visibilidade do HUD (ativa/desativa
 * {@code ModConfig.hudEnabled}). A tecla pode ser redefinida pelo jogador nas
 * opções de controles do jogo.
 */
public class HudKeybind {

    private static final String CATEGORY = "key.categories.slimechunkhud";
    private static final String KEY_NAME = "key.slimechunkhud.toggle_hud";

    private final KeyBinding keybind;

    public HudKeybind() {
        this.keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_NAME,
                GLFW.GLFW_KEY_K, // tecla padrão (redefinível pelo jogador)
                CATEGORY
        ));
    }

    public void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keybind.wasPressed()) {
                ModConfig cfg = ModConfig.get();
                cfg.hudEnabled = !cfg.hudEnabled;
                ModConfig.save();
                MinecraftClient.getInstance().player.sendMessage(
                        net.minecraft.text.Text.literal(
                                "Slime Chunk HUD: " + (cfg.hudEnabled ? "ativado" : "desativado")),
                                false);
            }
        });
    }

    public KeyBinding getKeyBinding() {
        return keybind;
    }
}
