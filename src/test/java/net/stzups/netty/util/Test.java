package net.stzups.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.stzups.netty.util.mock.ParkingLot;
import org.junit.jupiter.api.Assertions;

public class Test {
    @org.junit.jupiter.api.Test
    public void test() throws DeserializationException {
        ParkingLot parkingLot = new ParkingLot();

        ByteBuf byteBuf = Unpooled.buffer();
        parkingLot.serialize(byteBuf);
        Assertions.assertEquals(new ParkingLot(byteBuf), parkingLot);
    }
}
