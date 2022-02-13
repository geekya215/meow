package io.geekya.meow.adt;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonNumber that = (JsonNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
