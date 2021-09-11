package net.stzups.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NettyUtils {
    public enum Length {
        BYTE {
            @Override
            public int deserialize(ByteBuf byteBuf) { //todo allow for signed and unsigned types
                return byteBuf.readUnsignedByte();
            }

            public void serialize(ByteBuf byteBuf, int value) {
                if (value > Byte.MAX_VALUE) throw new IllegalArgumentException("Value is too long to fit in a byte");
                byteBuf.writeByte((byte) value);
            }
        },
        SHORT {
            @Override
            public int deserialize(ByteBuf byteBuf) {
                return byteBuf.readUnsignedShort();
            }

            public void serialize(ByteBuf byteBuf, int value) {
                if (value > Short.MAX_VALUE) throw new IllegalArgumentException("Value is too long to fit in a short");
                byteBuf.writeShort((short) value);
            }
        },
        INT {
            @Override
            public int deserialize(ByteBuf byteBuf) {
                return byteBuf.readInt();
            }

            public void serialize(ByteBuf byteBuf, int value) {
                byteBuf.writeInt(value);
            }
        },
        ;

        abstract public int deserialize(ByteBuf byteBuf);
        abstract public void serialize(ByteBuf byteBuf, int value);
    }

    public static void writeString(ByteBuf byteBuf, String string) {
        writeString(byteBuf, string, Length.INT);
    }

    public static void writeString(ByteBuf byteBuf, String string, Length l) {
        ByteBuf b = Unpooled.buffer();
        int length = b.writeCharSequence(string, StandardCharsets.UTF_8);
        try {
            l.serialize(byteBuf, length);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Exception while serializing string with length " + string.length() + " (actual length " + length + ")", e);
        }
        byteBuf.writeBytes(b);
    }

    public static String readString(ByteBuf byteBuf) {
        return readString(byteBuf, Length.INT);
    }

    public static String readString(ByteBuf byteBuf, Length l) {
        return byteBuf.readCharSequence(l.deserialize(byteBuf), StandardCharsets.UTF_8).toString();
    }

    public static <T extends Serializable> void writeArray(ByteBuf byteBuf, T[] array) {
        writeArray(byteBuf, array, Length.INT);
    }
    public static <T extends Serializable> void writeArray(ByteBuf byteBuf, T[] array, Length l) {
        writeArray(byteBuf, array, (b, o) -> o.serialize(b), l);
    }

    public static <A, T extends Serializer<A>> void writeArray(ByteBuf byteBuf, A[] array, T serializer) {
        writeArray(byteBuf, array, serializer, Length.INT);
    }
    public static <A, T extends Serializer<A>> void writeArray(ByteBuf byteBuf, A[] array, T serializer, Length l) {
        l.serialize(byteBuf, array.length);
        for (A element : array) {
            serializer.serialize(byteBuf, element);
        }
    }


    public static <A, T extends Deserializer<A>> A[] readArray(Class<A> clazz, ByteBuf byteBuf, T deserializer) throws DeserializationException {
        return readArray(clazz, byteBuf, deserializer, Length.INT);
    }
    public static <A, T extends Deserializer<A>> A[] readArray(Class<A> clazz, ByteBuf byteBuf, T deserializer, Length l) throws DeserializationException {
        int length = l.deserialize(byteBuf);

        @SuppressWarnings("unchecked")
        A[] array = (A[]) Array.newInstance(clazz, length);

        for (int i = 0; i < length; i++) {
            A object = deserializer.deserialize(byteBuf);
            array[i] = object;
        }

        return array;
    }

    public static <K, V, KK extends Deserializer<K>, VV extends Deserializer<V>> HashMap<K, V> readHashMap(ByteBuf byteBuf, KK kk, VV vv) throws DeserializationException {
        return readHashMap(byteBuf, kk, vv, Length.INT);
    }
    public static <K, V, KK extends Deserializer<K>, VV extends Deserializer<V>> HashMap<K, V> readHashMap(ByteBuf byteBuf, KK kk, VV vv, Length l) throws DeserializationException {
        int length = l.deserialize(byteBuf);
        HashMap<K, V> map = new HashMap<>();

        for (int i = 0; i < length; i++) {
            map.put(kk.deserialize(byteBuf), vv.deserialize(byteBuf));
        }

        return map;
    }

    public static <K, V, KK extends Serializer<K>, VV extends Serializer<V>> void writeHashMap(ByteBuf byteBuf, Map<K, V> map, KK kk, VV vv) {
        writeHashMap(byteBuf, map, kk, vv, Length.INT);
    }
    public static <K, V, KK extends Serializer<K>, VV extends Serializer<V>> void writeHashMap(ByteBuf byteBuf, Map<K, V> map, KK kk, VV vv, Length l) {
        l.serialize(byteBuf, map.size());

        for (Map.Entry<K, V> entry : map.entrySet()) {
            kk.serialize(byteBuf, entry.getKey());
            vv.serialize(byteBuf, entry.getValue());
        }
    }

    /**
     * Get ByteBuf to read or write from specified file
     */
    public static ByteBuf getFileByteBuffer(File file, FileChannel.MapMode mode) throws IOException {
        return Unpooled.wrappedBuffer(new RandomAccessFile(file, mode == FileChannel.MapMode.READ_ONLY ? "r" : "rw").getChannel().map(mode, 0, file.length()));
    }






    public static byte[] readBytes(ByteBuf byteBuf, int length) {
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
