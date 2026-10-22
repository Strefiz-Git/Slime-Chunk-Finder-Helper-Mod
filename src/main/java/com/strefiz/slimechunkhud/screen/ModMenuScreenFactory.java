package com.strefiz.slimechunkhud.screen;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.minecraft.client.gui.screens.Screen;

/**
 * Factory para o ecrã de configuração do Mod Menu.
 */
public class ModMenuScreenFactory implements ConfigScreenFactory<ConfigScreen> {

    @Override
    public ConfigScreen create(Screen parent) {
        return new ConfigScreen(parent);
    }
}
