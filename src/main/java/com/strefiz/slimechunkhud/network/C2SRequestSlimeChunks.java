package com.strefiz.slimechunkhud.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * C2S payload: o cliente pede ao servidor a lista de slime chunks numa área.
 * <p>
 * Leva o chunk central (x, z) e o raio solicitado. O servidor limita o raio
 * a maxRadius (config do servidor) e responde com S2CSlimeChunks.
 */
public record C2SRequestSlimeChunks(int centerX, int centerZ, int radius) implements CustomPacketPayload {
    public static final Type<C2SRequestSlimeChunks> TYPE = CustomPacketPayload.createType("c2s_request_slime_chunks");

    public static final StreamCodec<FriendlyByteBuf, C2SRequestSlimeChunks> CODEC = CustomPacketPayload.codec(
            (payload, buf) -> {
                buf.writeInt(payload.centerX());
                buf.writeInt(payload.centerZ());
                buf.writeVarInt(payload.radius());
            },
            buf -> new C2SRequestSlimeChunks(buf.readInt(), buf.readInt(), buf.readVarInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
