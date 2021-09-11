package net.stzups.netty.util.mock;

import io.netty.buffer.ByteBuf;
import net.stzups.netty.DebugString;
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

    @Override
    public boolean equals(Object object) {
        if (object instanceof Wheel wheel) {
            return
                    maxPressure == wheel.maxPressure &&
                    pressure == wheel.pressure &&
                    diameter == wheel.diameter &&
                    manufacturer.equals(wheel.manufacturer);
        }

        return false;
    }

    @Override
    public String toString() {
        return DebugString.get(getClass())
                .add("maxPressure", maxPressure)
                .add("pressure", pressure)
                .add("diameter", diameter)
                .add("manufacturer", manufacturer)
                .toString();
    }
}
