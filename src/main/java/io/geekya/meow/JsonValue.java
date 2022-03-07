package io.geekya.meow;

import java.util.List;
import java.util.Map;

public sealed interface JsonValue {

    JsonValue NULL = JsonNull.INSTANCE;

    JsonValue TRUE = JsonBoolean.TRUE;

    JsonValue FALSE = JsonBoolean.FALSE;

    static JsonValue of(Float value) {
        return new JsonNumber(value);
    }

    static JsonValue of(String value) {
        return new JsonString(value);
    }

    static JsonValue of(List<JsonValue> value) {
        return new JsonArray(value);
    }

    static JsonValue of(Map<String, JsonValue> value) {
        return new JsonObject(value);
    }

    record JsonNull() implements JsonValue {
        private static final JsonNull INSTANCE = new JsonNull();
    }

    record JsonBoolean(Boolean value) implements JsonValue {
        private static final JsonBoolean TRUE = new JsonBoolean(true);
        private static final JsonBoolean FALSE = new JsonBoolean(false);
    }

    record JsonNumber(Float value) implements JsonValue {
    }

    record JsonString(String value) implements JsonValue {
    }

    record JsonArray(List<JsonValue> value) implements JsonValue {
    }

    record JsonObject(Map<String, JsonValue> value) implements JsonValue {
    }
}
