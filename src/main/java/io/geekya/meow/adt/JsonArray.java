package io.geekya.meow.adt;

import java.util.List;
import java.util.Objects;

public final class JsonArray extends JsonValue {
    private final List<JsonValue> value;

    private JsonArray(List<JsonValue> value) {
        this.value = value;
    }

    public static JsonArray of(List<JsonValue> value) {
        return new JsonArray(value);
    }

    @Override
    public String toString() {
        return "JsonArray " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonArray jsonArray = (JsonArray) o;
        return Objects.equals(value, jsonArray.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
