package net.stzups.netty.util.mock;

import io.netty.buffer.ByteBuf;
import net.stzups.netty.util.DeserializationException;
import net.stzups.netty.util.NettyUtils;
import net.stzups.netty.util.RandomUtil;
import net.stzups.netty.util.Serializable;

public class Car implements Serializable {
    private final Wheel wheel;
    private final Model model;
    private final String manufacturer;
    private final long vin;

    public Car() {
        this.wheel = new Wheel();
        this.model = Model.random();
        this.manufacturer = RandomUtil.getString();
        this.vin = RandomUtil.random.nextLong();
    }

    public Car(ByteBuf byteBuf) throws DeserializationException {
        wheel = new Wheel(byteBuf);
        model = Model.deserialize(byteBuf);
        manufacturer = NettyUtils.readString8(byteBuf);
        vin = byteBuf.readLong();
    }

    public void serialize(ByteBuf byteBuf) {
        wheel.serialize(byteBuf);
        model.serialize(byteBuf);
        NettyUtils.writeString8(byteBuf, manufacturer);
        byteBuf.writeLong(vin);
    }
}
