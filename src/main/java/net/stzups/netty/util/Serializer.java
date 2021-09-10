package net.stzups.netty.util;

import io.netty.buffer.ByteBuf;

public interface Serializer<T> extends Serializable {
    void serialize(ByteBuf byteBuf, T object);
}
