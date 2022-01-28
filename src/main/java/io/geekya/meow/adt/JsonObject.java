package io.geekya.meow.adt;

import java.util.Map;

public final class JsonObject extends JsonValue {
    private final Map<String, JsonValue> value;

    public JsonObject(Map<String, JsonValue> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonObject " + value;
    }
}
