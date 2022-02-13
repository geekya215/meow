package io.geekya.meow.adt;

public final class JsonNull extends JsonValue {
    public static final JsonNull INSTANCE = new JsonNull();

    private JsonNull() {
    }

    @Override
    public String toString() {
        return "JsonNull";
    }
}
