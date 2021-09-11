package net.stzups.netty.util.mock;

import io.netty.buffer.ByteBuf;
import net.stzups.netty.DebugString;
import net.stzups.netty.util.DeserializationException;
import net.stzups.netty.util.RandomUtil;
import net.stzups.netty.util.Serializable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Model implements Serializable {
    FORD(0),
    TOYOTA(1),
    HONDA(2),
    ;

    private static final Map<Integer, Model> map = new HashMap<>();
    static {
        EnumSet<Model> enumSet = EnumSet.allOf(Model.class);
        for (Model model : enumSet) {
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
        int bound = map.size();
        int index = RandomUtil.random.nextInt(bound);
        return map.get(index);
    }
}
