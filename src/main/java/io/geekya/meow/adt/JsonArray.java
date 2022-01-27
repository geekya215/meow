package io.geekya.meow.adt;

import java.util.List;

public final class JsonArray extends JsonValue {
    private final List<JsonValue> value;

    public JsonArray(List<JsonValue> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonArray " + value;
    }
}
