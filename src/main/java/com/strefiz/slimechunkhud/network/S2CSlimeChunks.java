package com.strefiz.slimechunkhud.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.BitSet;

/**
 * S2C payload: o servidor responde com a lista de slime chunks numa área.
 * <p>
 * Leva o chunk central (x, z), o raio efetivo e um bitset de (2r+1)^2 bits.
 * Cada bit indica se a chunk correspondente é slime chunk.
 * O bitset é serializado como bytes para economizar espaço.
 */
public record S2CSlimeChunks(int centerX, int centerZ, int radius, byte[] bitset) implements CustomPacketPayload {
    public static final Type<S2CSlimeChunks> TYPE = CustomPacketPayload.createType("s2c_slime_chunks");

    public static final StreamCodec<FriendlyByteBuf, S2CSlimeChunks> CODEC = CustomPacketPayload.codec(
            (payload, buf) -> {
                buf.writeInt(payload.centerX());
                buf.writeInt(payload.centerZ());
                buf.writeVarInt(payload.radius());
                buf.writeVarInt(payload.bitset().length);
                buf.writeBytes(payload.bitset());
            },
            buf -> {
                int cx = buf.readInt();
                int cz = buf.readInt();
                int r = buf.readVarInt();
                int len = buf.readVarInt();
                byte[] bs = new byte[len];
                buf.readBytes(bs);
                return new S2CSlimeChunks(cx, cz, r, bs);
            });

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /** Converte o bitset para um array de booleans para facilitar o uso. */
    public boolean isSlimeChunk(int chunkX, int chunkZ) {
        int size = 2 * radius + 1;
        int dx = chunkX - (centerX - radius);
        int dz = chunkZ - (centerZ - radius);
        if (dx < 0 || dx >= size || dz < 0 || dz >= size) {
            return false;
        }
        int idx = dz * size + dx;
        return (bitset[idx / 8] & (1 << (idx % 8))) != 0;
    }
}
