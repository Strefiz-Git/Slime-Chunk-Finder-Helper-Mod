package com.strefiz.slimechunkhud.screen;

import net.fabricmc.fabric.api.client.screen.v1.ScreenRegistrationEvents;

/**
 * Fábrica de ecrãs para integraçāo com Mod Menu.
 * <p>
 * Referenciada em {@code custom.modmenu.screen} no {@code fabric.mod.json}.
 */
public final class ModMenuScreenFactory {

    private ModMenuScreenFactory() {}

    /** Registra o ecrã de configuraçāo no Mod Menu (se disponível). */
    public static void init() {
        ScreenRegistrationEvents.MAIN_MENU.register((screen, context) -> {
            // Botão de configuração no menu principal pode ser adicionado aqui.
        });
    }
}
