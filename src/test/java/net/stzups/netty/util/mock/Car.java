package net.stzups.netty.util.mock;

import io.netty.buffer.ByteBuf;
import net.stzups.netty.DebugString;
import net.stzups.netty.util.DeserializationException;
import net.stzups.netty.util.NettyUtils;
import net.stzups.netty.util.RandomUtil;
import net.stzups.netty.util.Serializable;

import java.awt.*;
import java.util.Arrays;

public class Car implements Serializable {
    private final Wheel wheel;
    private final Model model;
    private final String manufacturer;
    private final long vin;
    private final Tank tank;
    private final Glass[] windows;

    public Car() {
        wheel = new Wheel();
        model = Model.random();
        manufacturer = RandomUtil.getString();
        vin = RandomUtil.random.nextLong();
        tank = new Tank();
        windows = new Glass[RandomUtil.random.nextInt(10) + 1];//todo try with 0
        for (int i = 0; i < windows.length; i++) {
            windows[i] = new Glass();
        }
    }

    public Car(ByteBuf byteBuf) throws DeserializationException {
        wheel = new Wheel(byteBuf);
        model = Model.deserialize(byteBuf);
        manufacturer = NettyUtils.readString8(byteBuf);
        vin = byteBuf.readLong();
        tank = new Tank(byteBuf.readFloat(), byteBuf.readBoolean());
        windows = NettyUtils.readArray8(
                byteBuf,
                b -> new Glass(
                        b.readFloat(),
                        new Color(b.readInt())
                )
        );
    }

    public void serialize(ByteBuf byteBuf) {
        wheel.serialize(byteBuf);
        model.serialize(byteBuf);
        NettyUtils.writeString8(byteBuf, manufacturer);
        byteBuf.writeLong(vin);
        byteBuf.writeFloat(tank.getAmount());
        byteBuf.writeBoolean(tank.isDiesel());
        NettyUtils.writeArray8(
                byteBuf,
                windows,
                (b, glass) -> {
                    b.writeFloat(glass.getTint());
                    b.writeInt(glass.getColor().getRGB());
                }
        );
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Car car) {
            return
                    wheel.equals(car.wheel) &&
                    model.equals(car.model) &&
                    manufacturer.equals(car.manufacturer) &&
                    vin == car.vin &&
                    tank.equals(car.tank) &&
                    Arrays.equals(windows, car.windows);
        }

        return false;
    }

    @Override
    public String toString() {
        return DebugString.get(getClass())
                .add("wheel", wheel)
                .add("model", model)
                .add("manufacturer", manufacturer)
                .add("vin", vin)
                .add("tank", tank)
                .add("windows", windows)
                .toString();
    }
}
