package io.geekya.meow;

import io.geekya.meow.adt.JsonNull;
import io.geekya.meow.adt.JsonValue;
import org.junit.jupiter.api.Test;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NullParserTest {
    Parsec<JsonValue> parser = Meow._null;

    @Test
    void testValid() {
        assertEquals(just(JsonNull.INSTANCE), parser.parse("null"));
    }

    @Test
    void testInvalid() {
        assertEquals(nothing(), parser.parse("nul"));
    }
}
