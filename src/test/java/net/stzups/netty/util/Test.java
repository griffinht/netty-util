package net.stzups.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.stzups.netty.util.mock.ParkingLot;
import org.junit.jupiter.api.Assertions;

public class Test {
    @org.junit.jupiter.api.Test
    public void serializeDeserialize() throws DeserializationException {
        // generate random test data
        ParkingLot parkingLot = new ParkingLot(); // mock classes have no arg constructors that initialize the object with random values

        ByteBuf byteBuf = Unpooled.buffer();
        // serialize to byteBuf
        parkingLot.serialize(byteBuf);

        // deserialize from byteBuf
        ParkingLot newParkingLot = new ParkingLot(byteBuf);

        // should still be equal
        Assertions.assertEquals(parkingLot, newParkingLot);
    }
}
