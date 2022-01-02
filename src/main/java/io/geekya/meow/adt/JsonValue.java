package io.geekya.meow.adt;

public sealed class JsonValue permits JsonNull, JsonBoolean, JsonNumber, JsonString, JsonArray, JsonObject {
}
