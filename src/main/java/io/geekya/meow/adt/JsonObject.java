package io.geekya.meow.adt;

import java.util.Map;
import java.util.Objects;

public final class JsonObject extends JsonValue {
    private final Map<String, JsonValue> value;

    public JsonObject(Map<String, JsonValue> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonObject " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonObject that = (JsonObject) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
