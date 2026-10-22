package com.strefiz.slimechunkhud.screen;

import net.fabricmc.fabric.api.client.screen.v1.ScreenRegistrationEvents;

/**
 * Integraçāo opcional com Mod Menu e botão de configuraçāo no menu principal.
 * <p>
 * O Mod Menu detecta automaticamente {@code custom.modmenu.screen} no
 * fabric.mod.json. Este registro adicional adiciona um botão "Configuraçāo" ao
 * menu principal quando disponível.
 */
public class ModMenuIntegration {

    public static void init() {
        ModMenuScreenFactory.init();
    }
}
