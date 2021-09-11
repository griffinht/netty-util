package net.stzups.netty.util;

import io.netty.buffer.ByteBuf;

public interface Deserializer<T> {
    T deserialize(ByteBuf byteBuf) throws DeserializationException;
}
