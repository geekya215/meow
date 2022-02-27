package io.geekya.meow;

import io.geekya.meow.adt.JsonNumber;
import io.geekya.meow.adt.JsonValue;
import org.junit.jupiter.api.Test;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberParserTest {
    Parsec<JsonValue> parser = Meow._number;

    @Test
    public void testParseInt() {
        assertEquals(just(JsonNumber.of(123)), parser.parse("123"));
    }

    @Test
    public void testParseNegativeInt() {
        assertEquals(just(JsonNumber.of(-123)), parser.parse("-123"));
    }

    @Test
    public void testParseZero() {
        assertEquals(just(JsonNumber.of(0)), parser.parse("0"));
    }

    @Test
    public void testInvalid() {
        assertEquals(nothing(), parser.parse("a123"));
    }
}
