package io.geekya.meow;

import io.geekya.meow.adt.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectParserTest {
    Parsec<JsonValue> parser = Meow._object;

    @Test
    public void testSimple() {
        assertEquals(just(JsonObject.of(Map.of())), parser.parse("{}"));
    }

    @Test
    public void testSimpleWithValue() {
        Map<String, JsonValue> map = Map.of("key", JsonString.of("value"));
        assertEquals(just(JsonObject.of(map)), parser.parse("{\"key\":\"value\"}"));
    }

    @Test
    public void testObjectWithNull() {
        Map<String, JsonValue> map = Map.of("null", JsonNull.INSTANCE);
        assertEquals(just(JsonObject.of(map)), parser.parse("{\"null\":null}"));
    }

    @Test
    public void testObjectWithEmptyString() {
        Map<String, JsonValue> map = Map.of("empty", JsonString.of(""));
        assertEquals(just(JsonObject.of(map)), parser.parse("{\"empty\":\"\"}"));
    }

    @Test
    public void testObjectWithBoolean() {
        Map<String, JsonValue> map = Map.of("true", JsonBoolean.TRUE, "false", JsonBoolean.FALSE);
        assertEquals(just(JsonObject.of(map)), parser.parse("{\"true\":true,\"false\":false}"));
    }

    @Test
    public void testObjectWithNumber() {
        Map<String, JsonValue> map = Map.of("zero", JsonNumber.of(0), "one", JsonNumber.of(1));
        assertEquals(just(JsonObject.of(map)), parser.parse("{\"zero\":0,\"one\":1}"));
    }

    @Test
    public void testWithTailComma() {
        assertEquals(nothing(), parser.parse("{\"key\":\"value\",}"));
    }

    @Test
    public void testEmpty() {
        assertEquals(nothing(), parser.parse(""));
    }
}
