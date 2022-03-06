package io.geekya.meow;

import io.geekya.meow.adt.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.geekya.meow.Maybe.just;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    Parser<JsonValue> parser = Grammar._json;

    void check(String input, JsonValue expected) {
        assertEquals(just(expected), parser.parse(input));
    }

    @Test
    public void testNull() {
        check("null", JsonNull.INSTANCE);
        check("null ", JsonNull.INSTANCE);
        check(" null", JsonNull.INSTANCE);
        check(" null ", JsonNull.INSTANCE);
    }

    @Test
    public void testTrue() {
        check("true", JsonBoolean.TRUE);
        check("true ", JsonBoolean.TRUE);
        check(" true", JsonBoolean.TRUE);
        check(" true ", JsonBoolean.TRUE);
    }

    @Test
    public void testFalse() {
        check("false", JsonBoolean.FALSE);
        check("false ", JsonBoolean.FALSE);
        check(" false", JsonBoolean.FALSE);
        check(" false ", JsonBoolean.FALSE);
    }

    @Test
    public void testZero() {
        check("0", JsonNumber.of(0));
        check("0 ", JsonNumber.of(0));
        check(" 0", JsonNumber.of(0));
        check(" 0 ", JsonNumber.of(0));
        check("-0", JsonNumber.of(0));
    }

    @Test
    public void testPositiveNumber() {
        check("12", JsonNumber.of(12));
        check("12 ", JsonNumber.of(12));
        check(" 12", JsonNumber.of(12));
        check(" 12 ", JsonNumber.of(12));
        check("123", JsonNumber.of(123));
        check("123 ", JsonNumber.of(123));
        check(" 123", JsonNumber.of(123));
        check(" 123 ", JsonNumber.of(123));
    }

    @Test
    public void testNegativeNumber() {
        check("-12", JsonNumber.of(-12));
        check("-12 ", JsonNumber.of(-12));
        check(" -12", JsonNumber.of(-12));
        check(" -12 ", JsonNumber.of(-12));
        check("-123", JsonNumber.of(-123));
        check("-123 ", JsonNumber.of(-123));
        check(" -123", JsonNumber.of(-123));
        check(" -123 ", JsonNumber.of(-123));
    }

    @Test
    public void testUnicode() {
        check("\"\\u0041\"", JsonString.of("A"));
        check("\"\\u0041 \"", JsonString.of("A "));
    }

    @Test
    public void testString() {
        check("\"\"", JsonString.of(""));
        check("\" \"", JsonString.of(" "));
        check("\"\\b\"", JsonString.of("\b"));
        check("\"\\f\"", JsonString.of("\f"));
        check("\"\\n\"", JsonString.of("\n"));
        check("\"\\r\"", JsonString.of("\r"));
        check("\"\\t\"", JsonString.of("\t"));
        check("\"\\\"\"", JsonString.of("\""));
        check("\"\\\\\"", JsonString.of("\\"));
        check("\"\\/\"", JsonString.of("/"));
        check("\"abc\\u0041\"", JsonString.of("abcA"));
        check("\"abc\\bde\\nfg\\r\\u0041 \"", JsonString.of("abc\bde\nfg\rA "));
    }

    @Test
    public void testArray() {
        check("[]", JsonArray.of(List.of()));
        check(" [ 1, 2 ] ", JsonArray.of(List.of(JsonNumber.of(1), JsonNumber.of(2))));
        check(" [ 1, true, false, \"hello\", null] ", JsonArray.of(List.of(JsonNumber.of(1), JsonBoolean.TRUE, JsonBoolean.FALSE, JsonString.of("hello"), JsonNull.INSTANCE)));
    }

    @Test
    public void testNestedArray() {
        check(" [[1,true], [3,false], [null, \"array\"]] ", JsonArray.of(List.of(JsonArray.of(List.of(JsonNumber.of(1), JsonBoolean.TRUE)), JsonArray.of(List.of(JsonNumber.of(3), JsonBoolean.FALSE)), JsonArray.of(List.of(JsonNull.INSTANCE, JsonString.of("array"))))));
    }

    @Test
    public void testObject() {
        check("{}", JsonObject.of(Map.of()));
        check(" { \"a\": 1, \"b\": true, \"c\": false, \"d\": \"hello\", \"e\": null } ", JsonObject.of(Map.of("a", JsonNumber.of(1), "b", JsonBoolean.TRUE, "c", JsonBoolean.FALSE, "d", JsonString.of("hello"), "e", JsonNull.INSTANCE)));
    }

    @Test
    public void testNestedObject() {
        check(" { \"a\": { \"b\": 1, \"c\": true }, \"d\": { \"e\": 3, \"f\": false }, \"g\": { \"h\": null, \"i\": \"object\" } } ", JsonObject.of(Map.of("a", JsonObject.of(Map.of("b", JsonNumber.of(1), "c", JsonBoolean.TRUE)), "d", JsonObject.of(Map.of("e", JsonNumber.of(3), "f", JsonBoolean.FALSE)), "g", JsonObject.of(Map.of("h", JsonNull.INSTANCE, "i", JsonString.of("object"))))));
    }

    @Test
    public void testArrayOfObjects() {
        check(" [ { \"a\": 1, \"b\": true }, { \"c\": false, \"d\": \"hello\" }, { \"e\": null } ] ", JsonArray.of(List.of(JsonObject.of(Map.of("a", JsonNumber.of(1), "b", JsonBoolean.TRUE)), JsonObject.of(Map.of("c", JsonBoolean.FALSE, "d", JsonString.of("hello"))), JsonObject.of(Map.of("e", JsonNull.INSTANCE)))));
    }

    @Test
    public void testObjectOfArrays() {
        check(" { \"a\": [ 1, true ], \"b\": [ false, \"hello\" ], \"c\": [ null ] } ", JsonObject.of(Map.of("a", JsonArray.of(List.of(JsonNumber.of(1), JsonBoolean.TRUE)), "b", JsonArray.of(List.of(JsonBoolean.FALSE, JsonString.of("hello"))), "c", JsonArray.of(List.of(JsonNull.INSTANCE)))));
    }

    @Test
    public void testUnformatString() {
        check("""
            {
              "project": {
                 "name":  "meow",
                "description"       : "a tiny json parser library",
               "author":       "geekya215",
                    "license": "MIT",
               "extras": null,     
               
               "language": ["Java"  , "Kotlin"],
               "info": {
               
                  "version" :      "0.1.0",
                  "branch": "master",
                  "commits": 123
               }
               
              }
            }
            """,
          JsonObject.of(Map.of("project", JsonObject.of(Map.of("name", JsonString.of("meow"), "description", JsonString.of("a tiny json parser library"), "author", JsonString.of("geekya215"), "license", JsonString.of("MIT"), "extras", JsonNull.INSTANCE, "language", JsonArray.of(List.of(JsonString.of("Java"), JsonString.of("Kotlin"))), "info", JsonObject.of(Map.of("version", JsonString.of("0.1.0"), "branch", JsonString.of("master"), "commits", JsonNumber.of(123))))))));
    }
}
