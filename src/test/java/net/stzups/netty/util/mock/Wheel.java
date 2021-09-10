package net.stzups.netty.util.mock;

import io.netty.buffer.ByteBuf;
import net.stzups.netty.util.NettyUtils;
import net.stzups.netty.util.RandomUtil;
import net.stzups.netty.util.Serializable;

public class Wheel implements Serializable {
    private final int maxPressure;
    private final int pressure;
    private final float diameter;
    private final String manufacturer;

    public Wheel() {
        maxPressure = RandomUtil.random.nextInt();
        pressure = RandomUtil.random.nextInt();
        diameter = RandomUtil.random.nextFloat();
        manufacturer = RandomUtil.getString();
    }

    public Wheel(ByteBuf byteBuf) {
        maxPressure = byteBuf.readInt();
        pressure = byteBuf.readInt();
        diameter = byteBuf.readFloat();
        manufacturer = NettyUtils.readString8(byteBuf);
    }

    @Override
    public void serialize(ByteBuf byteBuf) {
        byteBuf.writeInt(maxPressure);
        byteBuf.writeInt(pressure);
        byteBuf.writeFloat(diameter);
        NettyUtils.writeString8(byteBuf, manufacturer);
    }
}
