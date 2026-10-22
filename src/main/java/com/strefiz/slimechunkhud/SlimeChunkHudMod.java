package com.strefiz.slimechunkhud;

import com.strefiz.slimechunkhud.config.ModConfig;
import com.strefiz.slimechunkhud.hud.SlimeChunkHud;
import com.strefiz.slimechunkhud.hud.SlimeChunkNotifier;
import com.strefiz.slimechunkhud.hud.SlimeChunkStateTracker;
import com.strefiz.slimechunkhud.input.HudKeybind;
import com.strefiz.slimechunkhud.render.SlimeChunkOverlay;
import com.strefiz.slimechunkhud.seed.SeedProvider;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Entrypoint do mod (apenas cliente).
 * <p>
 * Responsabilidades:
 * <ul>
 *   <li>Inicializar keybind, HUD, notificador, tracker, overlay de bordas e comando {@code /slimeseed}.</li>
 *   <li>Rastrear mundo/dimensão e resetar estado ao trocar de mundo.</li>
 * </ul>
 */
public class SlimeChunkHudMod implements ClientModInitializer {

    public static final String MOD_ID = "slimechunkhud";

    private static final Identifier HUD_ID = Identifier.fromNamespaceAndPath(MOD_ID, "slime_chunk_hud");
    private static final Identifier NOTIFIER_ID = Identifier.fromNamespaceAndPath(MOD_ID, "slime_chunk_notifier");

    private HudKeybind keybind;
    private SlimeChunkHud hud;
    private SlimeChunkNotifier notifier;
    private SlimeChunkStateTracker tracker;
    private SlimeChunkOverlay overlay;
    private SeedProvider seedProvider;

    @Override
    public void onInitializeClient() {
        seedProvider = new SeedProvider();

        keybind = new HudKeybind();
        keybind.register();

        ModConfig.load();

        tracker = new SlimeChunkStateTracker(seedProvider);
        hud = new SlimeChunkHud(seedProvider, ModConfig::get);
        notifier = new SlimeChunkNotifier(seedProvider, ModConfig::get, tracker);
        overlay = new SlimeChunkOverlay(seedProvider, ModConfig::get);

        // HUD (apos os overlays vanilla para nāo ser coberto).
        HudElementRegistry.addLast(HUD_ID, (gfx, delta) -> hud.extractRenderState(gfx, delta));

        // Notificaçāo acima da hotbar ao entrar numa slime chunk.
        HudElementRegistry.addLast(NOTIFIER_ID, (gfx, delta) -> notifier.extractRenderState(gfx, delta));

        // Overlay verde nos chunk borders (F3 + G).
        LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register((context) -> {
            overlay.render(context);
        });

        // Comando client-side: /slimeseed <seed>
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal("slimeseed")
                            .executes(context -> {
                                context.getSource().sendFeedback(
                                        Component.literal("Uso: /slimeseed <seed> (número inteiro)"));
                                return 0;
                            })
            );
        });

        // Rastrear mundo/dimensão, slime chunk state e notificaçāo a cada tick.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientLevel level = client.level;
            boolean overworld = level != null && isOverworld(level);
            seedProvider.setDimensionOverworld(overworld);

            tracker.tick();
            notifier.tick();

            // Reset ao trocar de mundo/dimensão.
            if (!overworld) {
                tracker.reset();
            }

            // Singleplayer: tentar obter a seed do servidor integrado.
            if (overworld && !seedProvider.hasSeed()) {
                try {
                    var server = client.getSingleplayerServer();
                    if (server != null) {
                        var overworldLevel = server.getLevel(net.minecraft.world.level.Level.OVERWORLD);
                        if (overworldLevel != null) {
                            long spSeed = overworldLevel.getSeed();
                            seedProvider.setCurrentServerSeed(spSeed);
                            seedProvider.setCurrentServer("singleplayer");
                        }
                    }
                } catch (Throwable ignored) {
                    // Seed indisponível neste momento; mantém "desconhecida".
                }
            }
        });
    }

    private static boolean isOverworld(ClientLevel level) {
        try {
            return level.dimension() == net.minecraft.world.level.Level.OVERWORLD;
        } catch (Throwable e) {
            return true;
        }
    }

    public HudKeybind getKeybind() {
        return keybind;
    }

    public SeedProvider getSeedProvider() {
        return seedProvider;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
