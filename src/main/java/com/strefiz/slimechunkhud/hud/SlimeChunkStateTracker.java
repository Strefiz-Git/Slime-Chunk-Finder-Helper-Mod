package com.strefiz.slimechunkhud.hud;

import com.strefiz.slimechunkhud.seed.SeedProvider;

/**
 * Rastreador leve que deteta quando o jogador entra numa slime chunk.
 * <p>
 * Compara o estado da chunk atual com o estado do tick anterior para produzir
 * um evento de "entrada" (transição de nāo-slime → slime), usado pela
 * notificaçāo acima da hotbar.
 */
public class SlimeChunkStateTracker {

    private final SeedProvider seedProvider;
    private boolean wasInSlimeChunk = false;

    public SlimeChunkStateTracker(SeedProvider seedProvider) {
        this.seedProvider = seedProvider;
    }

    /**
     * Chama a cada tick. Retorna {@code true} apenas no tick em que o jogador
     * entra numa slime chunk (transiçāo de fora para dentro).
     *
     * @return {@code true} se entrou numa slime chunk neste tick.
     */
    public boolean tick() {
        boolean inSlimeChunk = seedProvider.isSlimeChunk(
                currentChunkX(), currentChunkZ());
        boolean entered = !wasInSlimeChunk && inSlimeChunk;
        wasInSlimeChunk = inSlimeChunk;
        return entered;
    }

    /** {@code true} se o jogador estā atualmente numa slime chunk. */
    public boolean isInSlimeChunk() {
        return wasInSlimeChunk;
    }

    /** Reinicia o rastreador (útil ao trocar de mundo). */
    public void reset() {
        wasInSlimeChunk = false;
    }

    private int currentChunkX() {
        var client = net.minecraft.client.MinecraftClient.getInstance();
        if (client.player == null) {
            return 0;
        }
        return net.minecraft.util.math.MathHelper.floor(client.player.getX() / 16.0);
    }

    private int currentChunkZ() {
        var client = net.minecraft.client.MinecraftClient.getInstance();
        if (client.player == null) {
            return 0;
        }
        return net.minecraft.util.math.MathHelper.floor(client.player.getZ() / 16.0);
    }
}
