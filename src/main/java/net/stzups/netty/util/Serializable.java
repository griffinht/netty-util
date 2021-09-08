package net.stzups.netty.util;

import io.netty.buffer.ByteBuf;

public interface Serializable {
    void serialize(ByteBuf byteBuf);
}
