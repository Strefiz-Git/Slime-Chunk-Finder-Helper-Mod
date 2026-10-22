package com.strefiz.slimechunkhud.screen;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Mod Menu integration: exposes the config screen behind the gear button.
 * Follows the same pattern as RocketWithSpearMod (Fabric 26.3, compiles locally).
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreen::new;
    }
}
