package com.strefiz.slimechunkhud.input;

import com.strefiz.slimechunkhud.config.ModConfig;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

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

    private static final String KEY_NAME = "key.slimechunkhud.toggle_mode";

    private enum Mode {
        OFF, HUD, NOTIFICATION
    }

    private final KeyMapping keybind;

    public HudKeybind() {
        this.keybind = KeyMappingHelper.registerKeyBinding(new KeyMapping(
                KEY_NAME,
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_K, // tecla padrão (redefinível pelo jogador)
                KeyMapping.Category.GAMEPLAY
        ));
    }

    public void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keybind.consumeClick()) {
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
                Minecraft.getInstance().player.sendSystemMessage(Component.literal(msg));
            }
        });
    }

    public KeyMapping getKeyMapping() {
        return keybind;
    }
}
