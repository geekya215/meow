package io.geekya.meow.adt;

import java.util.Objects;

public final class JsonBoolean extends JsonValue {
    private final Boolean value;

    public JsonBoolean(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonBoolean " + (value ? "True" : "False");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonBoolean that = (JsonBoolean) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
