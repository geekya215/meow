package io.geekya.meow;

import io.geekya.meow.adt.JsonBoolean;
import org.junit.jupiter.api.Test;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonBooleanParserTest {
    Parsec<JsonBoolean> parser = Meow.JsonBooleanParser;

    @Test
    public void parseSuccess() {
        assertEquals(just(JsonBoolean.TRUE), parser.parse("true"));
        assertEquals(just(JsonBoolean.TRUE), parser.parse("  true"));
        assertEquals(just(JsonBoolean.TRUE), parser.parse("true  "));
        assertEquals(just(JsonBoolean.TRUE), parser.parse("  true  "));
        assertEquals(just(JsonBoolean.TRUE), parser.parse("  true  a"));

        assertEquals(just(JsonBoolean.FALSE), parser.parse("false"));
        assertEquals(just(JsonBoolean.FALSE), parser.parse("  false"));
        assertEquals(just(JsonBoolean.FALSE), parser.parse("false  "));
        assertEquals(just(JsonBoolean.FALSE), parser.parse("  false  "));
        assertEquals(just(JsonBoolean.FALSE), parser.parse("  false  a"));
    }

    @Test
    public void parseFail() {
        assertEquals(nothing(), parser.parse("tre"));
        assertEquals(nothing(), parser.parse("fals"));
    }
}
