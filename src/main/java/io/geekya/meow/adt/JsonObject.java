package io.geekya.meow.adt;

import java.util.Map;

public final class JsonObject extends JsonValue {
    Map<JsonString, JsonValue> value;
}
