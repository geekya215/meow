package io.geekya.meow;


import io.geekya.meow.adt.JsonNumber;
import org.junit.jupiter.api.Test;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonNumberParserTest {
    Parsec<JsonNumber> parser = Meow.JsonNumberParser;

    @Test
    public void testSuccess() {
        assertEquals(just(JsonNumber.of(1)), parser.parse("1"));
        assertEquals(just(JsonNumber.of(1)), parser.parse(" 1"));
        assertEquals(just(JsonNumber.of(1)), parser.parse("1 "));
        assertEquals(just(JsonNumber.of(1)), parser.parse(" 1 "));
        assertEquals(just(JsonNumber.of(1)), parser.parse(" 1 "));
        assertEquals(just(JsonNumber.of(1)), parser.parse(" 1 a"));

        assertEquals(just(JsonNumber.of(-1)), parser.parse("-1"));
        assertEquals(just(JsonNumber.of(-1)), parser.parse(" -1"));
        assertEquals(just(JsonNumber.of(-1)), parser.parse("-1 "));
        assertEquals(just(JsonNumber.of(-1)), parser.parse(" -1 "));
        assertEquals(just(JsonNumber.of(-1)), parser.parse(" -1 "));
        assertEquals(just(JsonNumber.of(-1)), parser.parse(" -1 a"));
        assertEquals(just(JsonNumber.of(-1)), parser.parse(" -1a"));
    }

    @Test
    public void testFail() {
        assertEquals(nothing(), parser.parse("a"));
        assertEquals(nothing(), parser.parse("a11"));
        assertEquals(nothing(), parser.parse("-a11"));
    }
}
