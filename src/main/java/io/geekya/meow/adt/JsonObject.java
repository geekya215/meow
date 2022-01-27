package io.geekya.meow.adt;

import java.util.Map;

public final class JsonObject extends JsonValue {
    private final Map<JsonString, JsonValue> value;

    public JsonObject(Map<JsonString, JsonValue> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonObject " + value;
    }
}
