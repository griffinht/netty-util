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
    private final Tank tank;

    public Car() {
        wheel = new Wheel();
        model = Model.random();
        manufacturer = RandomUtil.getString();
        vin = RandomUtil.random.nextLong();
        tank = new Tank();
    }

    public Car(ByteBuf byteBuf) throws DeserializationException {
        wheel = new Wheel(byteBuf);
        model = Model.deserialize(byteBuf);
        manufacturer = NettyUtils.readString8(byteBuf);
        vin = byteBuf.readLong();
        tank = new Tank(byteBuf.readFloat(), byteBuf.readBoolean());
    }

    public void serialize(ByteBuf byteBuf) {
        wheel.serialize(byteBuf);
        model.serialize(byteBuf);
        NettyUtils.writeString8(byteBuf, manufacturer);
        byteBuf.writeLong(vin);
        byteBuf.writeFloat(tank.getAmount());
        byteBuf.writeBoolean(tank.isDiesel());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Car car) {
            return
                    wheel.equals(car.wheel) &&
                    model.equals(car.model) &&
                    manufacturer.equals(car.manufacturer) &&
                    vin == car.vin &&
                    tank.equals(car.tank);
        }

        return false;
    }
}
