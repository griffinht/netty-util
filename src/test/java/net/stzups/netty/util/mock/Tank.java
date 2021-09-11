package net.stzups.netty.util.mock;

import net.stzups.netty.util.RandomUtil;

public class Tank {
    private final float amount;
    private final boolean diesel;

    public Tank(float amount, boolean diesel) {
        this.amount = amount;
        this.diesel = diesel;
    }

    Tank() {
        amount = RandomUtil.random.nextFloat();
        diesel = RandomUtil.random.nextBoolean();
    }

    public float getAmount() {
        return amount;
    }

    public boolean isDiesel() {
        return diesel;
    }
}
