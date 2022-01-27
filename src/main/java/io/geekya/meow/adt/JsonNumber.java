package io.geekya.meow.adt;

public final class JsonNumber extends JsonValue {
    // Todo
    // support float number in the future.
    private final Integer value;

    public JsonNumber(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonNumber " + value;
    }
}
