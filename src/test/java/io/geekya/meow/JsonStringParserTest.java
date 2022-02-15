package io.geekya.meow;

import io.geekya.meow.adt.JsonString;
import org.junit.jupiter.api.Test;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonStringParserTest {
    Parsec<JsonString> parser = Meow.JsonStringParser;

    @Test
    public void parseSuccess() {
        assertEquals(just(JsonString.of("")), parser.parse("\"\""));
        assertEquals(just(JsonString.of("  ")), parser.parse("\"  \""));
        assertEquals(just(JsonString.of("hello")), parser.parse("\"hello\""));
        assertEquals(just(JsonString.of("hello\nworld!")), parser.parse("\"hello\\nworld!\""));
        assertEquals(just(JsonString.of("\\ / \b \f \n \r \t")), parser.parse("\"\\\\ \\/ \\b \\f \\n \\r \\t\""));
    }

    @Test
    public void parseFail() {
        assertEquals(nothing(), parser.parse("\""));
        assertEquals(nothing(), parser.parse("\"hello"));
        assertEquals(nothing(), parser.parse("hello"));
    }
}
