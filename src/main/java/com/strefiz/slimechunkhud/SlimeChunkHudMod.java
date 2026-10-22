package com.strefiz.slimechunkhud;

import com.strefiz.slimechunkhud.config.ModConfig;
import com.strefiz.slimechunkhud.hud.SlimeChunkHud;
import com.strefiz.slimechunkhud.input.HudKeybind;
import com.strefiz.slimechunkhud.render.SlimeChunkOverlay;
import com.strefiz.slimechunkhud.screen.ModMenuIntegration;
import com.strefiz.slimechunkhud.seed.SeedProvider;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Entrypoint do mod (apenas cliente).
 * <p>
 * Responsabilidades:
 * <ul>
 *   <li>Inicializar keybind, HUD, provider de seed, overlay de bordas e comando {@code /slimeseed}.</li>
 *   <li>Ligar/desligar funcionalidades com base na config.</li>
    <li>Rastrear mundo/dimensão e resetar estado ao trocar de mundo.</li>
 * </ul>
 *
 * <b>Notas de API (26.3):</b> chamadas a {@code MinecraftClient}, {@code ClientWorld}
 * e registros da Fabric API sāo suposiçōes a confirmar nas mappings oficiais.
 */
public class SlimeChunkHudMod implements ClientModInitializer {

    public static final String MOD_ID = "slimechunkhud";

    private static final Identifier HUD_ID = Identifier.of(MOD_ID, "slime_chunk_hud");

    private HudKeybind keybind;
    private SlimeChunkHud hud;
    private SlimeChunkOverlay overlay;
    private SeedProvider seedProvider;

    @Override
    public void onInitializeClient() {
        seedProvider = new SeedProvider();

        keybind = new HudKeybind();
        keybind.register();

        ModConfig.load();

        hud = new SlimeChunkHud(seedProvider, ModConfig::get);
        overlay = new SlimeChunkOverlay(seedProvider, ModConfig::get);

        // Integraçāo com Mod Menu (config no menu principal).
        ModMenuIntegration.init();

        // HUD (render condiçāo herdada da camada anexada: respeita F1/Options.hideGui).
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT, HUD_ID, hud::render);

        // Overlay verde nos chunk borders (F3 + G).
        LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register((context) -> {
            overlay.render(context);
        });

        // Comando client-side: /slimeseed <seed>
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommands.literal("slimeseed")
                            .then(ClientCommands.argument("seed", ClientCommandManager.greedyString())
                                    .executes(context -> {
                                        String arg = context.getInput().trim();
                                        int idx = arg.indexOf(' ');
                                        if (idx < 0) {
                                            context.getSource().sendFeedback(
                                                    Text.literal("Uso: /slimeseed <seed> (número inteiro)"), false);
                                            return 0;
                                        }
                                        try {
                                            long seed = Long.parseLong(arg.substring(idx + 1).trim());
                                            seedProvider.setCurrentServerSeed(seed);
                                            context.getSource().sendFeedback(
                                                    Text.literal("Seed do slime chunk definida para: " + seed), false);
                                            return 1;
                                        } catch (NumberFormatException e) {
                                            context.getSource().sendFeedback(
                                                    Text.literal("Uso: /slimeseed <seed> (número inteiro)"), false);
                                            return 0;
                                        }
                                    })
                            )
            );
        });

        // Rastrear mundo/dimensão a cada tick; resetar ao trocar de mundo.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientWorld world = client.world;
            boolean overworld = world != null && isOverworld(world);
            seedProvider.setDimensionOverworld(overworld);

            // Singleplayer: tentar obter a seed do servidor integrado.
            if (overworld && !seedProvider.hasSeed()) {
                try {
                    var server = client.getSingleplayerServer();
                    if (server != null) {
                        long spSeed = server.getOverworld().getSeed();
                        seedProvider.setCurrentServerSeed(spSeed);
                        seedProvider.setCurrentServer("singleplayer");
                    }
                } catch (Throwable ignored) {
                    // Seed indisponível neste momento; mantém "desconhecida".
                }
            }
        });
    }

    private static boolean isOverworld(ClientWorld world) {
        try {
            return world.getRegistryKey()
                    .equals(net.minecraft.registry.RegistryKey.of(
                            net.minecraft.registry.RegistryKeys.WORLD,
                            Identifier.ofVanilla("overworld")));
        } catch (Throwable e) {
            return true; // em dúvida, assume Overworld para nāo bloquear
        }
    }

    public HudKeybind getKeybind() {
        return keybind;
    }

    public SeedProvider getSeedProvider() {
        return seedProvider;
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
