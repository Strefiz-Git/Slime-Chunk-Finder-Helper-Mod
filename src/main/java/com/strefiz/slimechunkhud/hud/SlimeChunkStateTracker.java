package com.strefiz.slimechunkhud.hud;



import com.strefiz.slimechunkhud.seed.SeedProvider;
import net.minecraft.client.Minecraft;
/**
 * Rastreador leve que deteta quando o jogador entra numa slime chunk.
 * <p>
 * Compara o estado da chunk atual com o estado do tick anterior para produzir
 * um evento de "entrada" (transiÃ§Äo de nÄo-slime â†’ slime), usado pela
 * notificaÃ§Äo acima da hotbar.
 */
public class SlimeChunkStateTracker {

    private final SeedProvider seedProvider;
    private boolean wasInSlimeChunk = false;

    public SlimeChunkStateTracker(SeedProvider seedProvider) {
        this.seedProvider = seedProvider;
    }

    /**
     * Chama a cada tick. Retorna {@code true} apenas no tick em que o jogador
     * entra numa slime chunk (transiÃ§Äo de fora para dentro).
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

    /** {@code true} se o jogador estÄ atualmente numa slime chunk. */
    public boolean isInSlimeChunk() {
        return wasInSlimeChunk;
    }

    /** Reinicia o rastreador (Ãºtil ao trocar de mundo). */
    public void reset() {
        wasInSlimeChunk = false;
    }

    private int currentChunkX() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return 0;
        }
        return (int) Math.floor(client.player.getX() / 16.0);
    }

    private int currentChunkZ() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return 0;
        }
        return (int) Math.floor(client.player.getZ() / 16.0);
    }
}

