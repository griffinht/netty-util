package net.stzups.netty.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    public static final Random random = new Random();

    public static String getString() {
        return UUID.randomUUID().toString();
    }
}
