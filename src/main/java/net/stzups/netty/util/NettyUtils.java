package net.stzups.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NettyUtils {
    /**
     * Write variable length string up to 256 bytes (not letters!)
     */
    public static void writeString8(ByteBuf byteBuf, String string) {
        ByteBuf b = Unpooled.buffer();
        int length = b.writeCharSequence(string, StandardCharsets.UTF_8);
        if (length > 256) {
            throw new RuntimeException("String too long (" + string.length() + ", " + length + " bytes)");
        }
        byteBuf.writeByte((byte) length);
        byteBuf.writeBytes(b);
    }

    public static String readString8(ByteBuf byteBuf) {
        return byteBuf.readCharSequence(byteBuf.readUnsignedByte(), StandardCharsets.UTF_8).toString();
    }

    public static <T extends Serializable> void writeArray8(ByteBuf byteBuf, T[] array) {
        if (array.length > 256) {
            throw new RuntimeException("Array of length " + array.length + " is too long");
        }
        byteBuf.writeByte((byte) array.length);
        for (T element : array) {
            element.serialize(byteBuf);
        }
    }

    public static <A, T extends Serializer<A>> void writeArray8(ByteBuf byteBuf, A[] array, T serializer) {
        if (array.length > 256) {
            throw new RuntimeException("Array of length " + array.length + " is too long");
        }
        byteBuf.writeByte((byte) array.length);
        for (A element : array) {
            serializer.serialize(byteBuf, element);
        }
    }

    @SuppressWarnings("unchecked")
    public static <A, T extends Deserializer<A>> A[] readArray8(ByteBuf byteBuf, T deserializer) {
        int length = byteBuf.readUnsignedByte();
        A[] array = null;

        for (int i = 0; i < length; i++) {
            A object = deserializer.deserialize(byteBuf);
            // :(
            if (array == null) array = (A[]) Array.newInstance(object.getClass(), length);
            array[i] = object;
        }

        return array;
    }

    /**
     * Get ByteBuf to read or write from specified file
     */
    public static ByteBuf getFileByteBuffer(File file, FileChannel.MapMode mode) throws IOException {
        return Unpooled.wrappedBuffer(new RandomAccessFile(file, mode == FileChannel.MapMode.READ_ONLY ? "r" : "rw").getChannel().map(mode, 0, file.length()));
    }
}
