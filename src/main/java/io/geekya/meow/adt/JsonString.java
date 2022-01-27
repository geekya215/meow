package io.geekya.meow.adt;

public final class JsonString extends JsonValue {
    private final String value;

    public JsonString(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonString " + "\"" + value + '\"';
    }
}
