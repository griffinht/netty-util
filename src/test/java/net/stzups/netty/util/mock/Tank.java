package net.stzups.netty.util.mock;

import net.stzups.netty.DebugString;
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

    @Override
    public boolean equals(Object object) {
        if (object instanceof Tank tank) {
            return
                    amount == tank.amount &&
                    diesel == tank.diesel;
        }

        return false;
    }

    @Override
    public String toString() {
        return DebugString.get(getClass())
                .add("amount", amount)
                .add("diesel", diesel)
                .toString();
    }
}
