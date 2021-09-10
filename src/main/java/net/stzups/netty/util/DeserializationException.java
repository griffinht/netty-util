package net.stzups.netty.util;

public class DeserializationException extends Exception {
    public DeserializationException(String s) {
        super(s);
    }
    public DeserializationException(String s, Exception e) {
        super(s, e);
    }
    public DeserializationException(Exception e) {
        super(e);
    }
}
