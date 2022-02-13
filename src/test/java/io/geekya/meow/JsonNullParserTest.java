package io.geekya.meow;

import io.geekya.meow.adt.JsonNull;
import org.junit.jupiter.api.Test;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonNullParserTest {
    Parsec<JsonNull> parser = Meow.JsonNullParser;

    @Test
    public void parseSuccess() {
        Maybe<JsonNull> expected = just(JsonNull.INSTANCE);

        assertEquals(expected, parser.parse("null"));
        assertEquals(expected, parser.parse("  null"));
        assertEquals(expected, parser.parse("null  "));
        assertEquals(expected, parser.parse("  null  "));
        assertEquals(expected, parser.parse("  null  a"));
    }

    @Test
    public void parseFail() {
        Parsec<JsonNull> parser = Meow.JsonNullParser;
        assertEquals(nothing(), parser.parse("nul"));
        assertEquals(nothing(), parser.parse("nul  "));
    }
}
