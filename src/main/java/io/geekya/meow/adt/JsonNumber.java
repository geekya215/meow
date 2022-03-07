package io.geekya.meow.adt;

import java.util.Objects;

public final class JsonNumber extends JsonValue {
    private final Float value;

    private JsonNumber(Float value) {
        this.value = value;
    }

    public static JsonNumber of(Float value) {
        return new JsonNumber(value);
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
