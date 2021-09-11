package net.stzups.netty.util.mock;

import io.netty.buffer.ByteBuf;
import net.stzups.netty.DebugString;
import net.stzups.netty.util.DeserializationException;
import net.stzups.netty.util.NettyUtils;
import net.stzups.netty.util.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParkingLot implements Serializable {
    private final Map<UUID, Car> map;

    public ParkingLot() {
        map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(UUID.randomUUID(), new Car());
        }
    }

    public ParkingLot(ByteBuf byteBuf) throws DeserializationException {
        map = NettyUtils.readHashMap(byteBuf, b -> new UUID(b.readLong(), b.readLong()), Car::new);
    }

    @Override
    public void serialize(ByteBuf byteBuf) {
        NettyUtils.writeHashMap(byteBuf, map, (b, uuid) -> {
            b.writeLong(uuid.getMostSignificantBits());
            b.writeLong(uuid.getLeastSignificantBits());
        }, (b, car) -> car.serialize(b));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ParkingLot parkingLot) {
            return parkingLot.map.equals(map);
        }

        return false;
    }

    @Override
    public String toString() {
        return DebugString.get(getClass())
                .add("map", map)
                .toString();
    }
}
