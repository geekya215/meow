package io.geekya.meow;

import io.geekya.meow.adt.JsonBoolean;
import io.geekya.meow.adt.JsonValue;
import org.junit.jupiter.api.Test;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BooleanParserTest {
    Parsec<JsonValue> parser = Meow._boolean;

    @Test
    void testTrue() {
        assertEquals(just(JsonBoolean.TRUE), parser.parse("true"));
    }

    @Test
    void testFalse() {
        assertEquals(just(JsonBoolean.FALSE), parser.parse("false"));
    }

    @Test
    void testInvalid() {
        assertEquals(nothing(), parser.parse("tru"));
    }
}
