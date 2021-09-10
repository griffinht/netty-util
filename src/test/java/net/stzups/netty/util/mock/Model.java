package net.stzups.netty.util.mock;

import io.netty.buffer.ByteBuf;
import net.stzups.netty.util.DeserializationException;
import net.stzups.netty.util.RandomUtil;
import net.stzups.netty.util.Serializable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Model implements Serializable {
    FORD(1),
    TOYOTA(2),
    HONDA(3),
    ;

    private static final Map<Integer, Model> map = new HashMap<>();
    static {
        for (Model model : EnumSet.allOf(Model.class)) {
            map.put(model.id, model);
        }
    }
    private final int id;

    Model(int id) {
        this.id = id;
    }

    @Override
    public void serialize(ByteBuf byteBuf) {
        byteBuf.writeByte((byte) id); //todo bounds checking
    }

    public static Model deserialize(ByteBuf byteBuf) throws DeserializationException {
        Model model = map.get((int) byteBuf.readUnsignedByte());
        if (model == null) {
            throw new DeserializationException("Unknown " + Model.class.getSimpleName() + " id");
        }
        return model;
    }

    public static Model random() {
        return map.get(RandomUtil.random.nextInt(map.size()));
    }
}
