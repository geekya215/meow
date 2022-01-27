package io.geekya.meow.adt;

public final class JsonBoolean extends JsonValue {
    private final Boolean value;

    public JsonBoolean(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonBoolean " + (value ? "True" : "False");
    }
}
