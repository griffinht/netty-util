package net.stzups.netty.util.mock;

import net.stzups.netty.DebugString;
import net.stzups.netty.util.RandomUtil;

import java.awt.*;

public class Glass {
    private final float tint;
    private final Color color;

    public Glass() {
        tint = RandomUtil.random.nextFloat();
        color = new Color(RandomUtil.random.nextInt());
    }

    public Glass(float tint, Color color) {
        this.tint = tint;
        this.color = color;
    }

    public float getTint() {
        return tint;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Glass glass) {
            return
                    tint == glass.tint &&
                    color.equals(glass.color);
        }

        return false;
    }

    @Override
    public String toString() {
        return DebugString.get(getClass())
                .add("tint", tint)
                .add("color", color)
                .toString();
    }
}
