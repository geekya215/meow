package io.geekya.meow;

import io.geekya.meow.adt.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayParserTest {
    Parsec<JsonValue> parser = Meow._array;

    @Test
    public void testEmptyArray() {
        JsonArray array = JsonArray.of(List.of());
        assertEquals(just(array), parser.parse("[]"));
    }

    @Test
    public void testArrayWithNull() {
        JsonArray array = JsonArray.of(List.of(JsonNull.INSTANCE));
        assertEquals(just(array), parser.parse("[null]"));
    }

    @Test
    public void testArrayWithNullAndBoolean() {
        JsonArray array = JsonArray.of(List.of(JsonNull.INSTANCE, JsonBoolean.FALSE, JsonBoolean.TRUE));
        assertEquals(just(array), parser.parse("[null,false,true]"));
    }

    @Test
    public void testArrayWithNullAndBooleanAndNumber() {
        JsonArray array = JsonArray.of(List.of(JsonNull.INSTANCE, JsonBoolean.FALSE, JsonBoolean.TRUE, JsonNumber.of(42)));
        assertEquals(just(array), parser.parse("[null,false,true,42]"));
    }

    @Test
    public void testArrayWithNullAndBooleanAndNumberAndString() {
        JsonArray array = JsonArray.of(List.of(JsonNull.INSTANCE, JsonBoolean.FALSE, JsonBoolean.TRUE, JsonNumber.of(42), JsonString.of("foo")));
        assertEquals(just(array), parser.parse("[null,false,true,42,\"foo\"]"));
    }

    @Test
    public void testInvalid() {
        assertEquals(nothing(), parser.parse("[\"foo\""));
    }
}
