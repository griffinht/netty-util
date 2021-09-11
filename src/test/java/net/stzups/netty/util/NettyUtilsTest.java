package net.stzups.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

public class NettyUtilsTest {
    @Test
    public void testZeroLength() throws DeserializationException {
        ByteBuf byteBuf = Unpooled.buffer();
        NettyUtils.writeArray8(byteBuf, new String[0], NettyUtils::writeString8);

        NettyUtils.readArray8(String.class, byteBuf, NettyUtils::readString8);
    }
}
