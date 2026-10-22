package com.strefiz.slimechunkhud.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.strefiz.slimechunkhud.render.ChunkBorderDetector;

/**
 * Mixin mĺnimo para ler a flag de bordas de chunk (F3 + G) do renderer vanilla.
 * <p>
 * ATENÇĀO: a classe alvo e o nome do campo/mķtodo mudam entre versões. Verificar
 * nas mappings 26.3 antes de ativar. Enquanto nāo confirmado, este mixin nāo
 * estā registrado em slimechunkhud.mixins.json (evita crash ao carregar).
 */
@Mixin(targets = "net.minecraft.client.render.debug.DebugRenderer") // a confirmar
public abstract class ChunkBorderFlagMixin {

    @Inject(method = "isChunkBordersActive", at = @At("RETURN"), cancellable = true) // a confirmar
    private static void exposeChunkBorders(CallbackInfoReturnable<Boolean> cir) {
        ChunkBorderDetector.setActive(cir.getReturnValue());
    }
}
