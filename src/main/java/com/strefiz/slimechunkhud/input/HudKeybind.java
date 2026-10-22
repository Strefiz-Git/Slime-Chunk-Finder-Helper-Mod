package com.strefiz.slimechunkhud.input;

import com.strefiz.slimechunkhud.config.ModConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * Keybind do HUD, registrada nas configurações de teclas do Minecraft.
 * <p>
 * Pressionar a tecla ciclo entre tręs estados:
 * <ol>
 *   <li>Desligado: nada ė mostrado.</li>
 *   <li>HUD: texto contínuo na posiçāo escolhida pelo utilizador.</li>
 *   <li>Notificaçāo: aviso transitório acima da hotbar ao entrar numa slime chunk.</li>
 * </ol>
 * A tecla pode ser redefinida pelo jogador nas opções de controles do jogo.
 */
public class HudKeybind {

    private static final String CATEGORY = "key.categories.slimechunkhud";
    private static final String KEY_NAME = "key.slimechunkhud.toggle_mode";

    private enum Mode {
        OFF, HUD, NOTIFICATION
    }

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
                Mode current = cfg.notificationMode
                        ? Mode.NOTIFICATION
                        : (cfg.hudEnabled ? Mode.HUD : Mode.OFF);
                Mode next = switch (current) {
                    case OFF -> Mode.HUD;
                    case HUD -> Mode.NOTIFICATION;
                    case NOTIFICATION -> Mode.OFF;
                };

                switch (next) {
                    case OFF -> {
                        cfg.hudEnabled = false;
                        cfg.notificationMode = false;
                    }
                    case HUD -> {
                        cfg.hudEnabled = true;
                        cfg.notificationMode = false;
                    }
                    case NOTIFICATION -> {
                        cfg.hudEnabled = true;
                        cfg.notificationMode = true;
                    }
                }
                ModConfig.save();

                String msg = switch (next) {
                    case OFF -> "Slime Chunk: desativado";
                    case HUD -> "Slime Chunk: modo HUD";
                    case NOTIFICATION -> "Slime Chunk: modo notificaçāo";
                };
                MinecraftClient.getInstance().player.sendMessage(Text.literal(msg), false);
            }
        });
    }

    public KeyBinding getKeyBinding() {
        return keybind;
    }
}
