package io.geekya.meow.adt;

import java.util.Objects;

public final class JsonString extends JsonValue {
    private final String value;

    private JsonString(String value) {
        this.value = value;
    }

    public static JsonString of(String value) {
        return new JsonString(value);
    }

    @Override
    public String toString() {
        return "JsonString " + "\"" + value + '\"';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonString that = (JsonString) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
