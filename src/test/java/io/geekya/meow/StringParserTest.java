package io.geekya.meow;

import io.geekya.meow.adt.JsonString;
import io.geekya.meow.adt.JsonValue;
import org.junit.jupiter.api.Test;

import static io.geekya.meow.Maybe.just;
import static io.geekya.meow.Maybe.nothing;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class StringParserTest {
    Parsec<JsonValue> parser = Meow._string;

    @Test
    public void testString() {
        assertEquals(just(JsonString.of("hello")), parser.parse("\"hello\""));
    }

    @Test
    public void testStringWithEscapedQuotes() {
        assertEquals(just(JsonString.of("hello\"")), parser.parse("\"hello\\\"\""));
    }

    @Test
    public void testStringWithEscapedBackslash() {
        assertEquals(just(JsonString.of("hello\\")), parser.parse("\"hello\\\\\""));
    }

    @Test
    public void testStringWithNewLine() {
        assertEquals(just(JsonString.of("hello\n")), parser.parse("\"hello\\n\""));
    }

    @Test
    public void testStringWithTab() {
        assertEquals(just(JsonString.of("hello\t")), parser.parse("\"hello\\t\""));
    }

    @Test
    public void testStringWithBackspace() {
        assertEquals(just(JsonString.of("hello\b")), parser.parse("\"hello\\b\""));
    }

    @Test
    public void testStringWithFormfeed() {
        assertEquals(just(JsonString.of("hello\f")), parser.parse("\"hello\\f\""));
    }

    @Test
    public void testStringWithCarriageReturn() {
        assertEquals(just(JsonString.of("hello\r")), parser.parse("\"hello\\r\""));
    }

    @Test
    public void testEmpty() {
        assertEquals(just(JsonString.of("")), parser.parse("\"\""));
    }

    @Test
    public void testStringWithEscapedChar() {
        assertEquals(just(JsonString.of("h\te\fllo\n\r\b\u2029")), parser.parse("\"h\\te\\fllo\\n\\r\\b\\u2029\""));
    }

    @Test
    public void testStringWithUnicode() {
        assertEquals(just(JsonString.of("hello\u2028")), parser.parse("\"hello\\u2028\""));
    }

    @Test
    public void testInvalid() {
        assertEquals(nothing(), parser.parse("\"hello"));
    }
}
