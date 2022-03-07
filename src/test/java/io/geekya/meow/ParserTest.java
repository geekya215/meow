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
        check("0", JsonNumber.of(0f));
        check("0 ", JsonNumber.of(0f));
        check(" 0", JsonNumber.of(0f));
        check(" 0 ", JsonNumber.of(0f));
        check("-0", JsonNumber.of(-0f));
    }

    @Test
    public void testPositiveInteger() {
        check("12", JsonNumber.of(12F));
        check("12 ", JsonNumber.of(12F));
        check(" 12", JsonNumber.of(12F));
        check(" 12 ", JsonNumber.of(12F));
        check("123", JsonNumber.of(123F));
        check("123 ", JsonNumber.of(123F));
        check(" 123", JsonNumber.of(123F));
        check(" 123 ", JsonNumber.of(123F));
    }

    @Test
    public void testNegativeInteger() {
        check("-12", JsonNumber.of(-12F));
        check("-12 ", JsonNumber.of(-12F));
        check(" -12", JsonNumber.of(-12F));
        check(" -12 ", JsonNumber.of(-12F));
        check("-123", JsonNumber.of(-123F));
        check("-123 ", JsonNumber.of(-123F));
        check(" -123", JsonNumber.of(-123F));
        check(" -123 ", JsonNumber.of(-123F));
    }

    @Test
    public void testPositiveFloat() {
        check("12.3", JsonNumber.of(12.3F));
        check("12.3 ", JsonNumber.of(12.3F));
        check(" 12.3", JsonNumber.of(12.3F));
        check(" 12.3 ", JsonNumber.of(12.3F));
        check("123.4", JsonNumber.of(123.4F));
        check("123.4 ", JsonNumber.of(123.4F));
        check(" 123.4", JsonNumber.of(123.4F));
        check(" 123.4 ", JsonNumber.of(123.4F));
    }

    @Test
    public void testNegativeFloat() {
        check("-12.3", JsonNumber.of(-12.3F));
        check("-12.3 ", JsonNumber.of(-12.3F));
        check(" -12.3", JsonNumber.of(-12.3F));
        check(" -12.3 ", JsonNumber.of(-12.3F));
        check("-123.4", JsonNumber.of(-123.4F));
        check("-123.4 ", JsonNumber.of(-123.4F));
        check(" -123.4", JsonNumber.of(-123.4F));
        check(" -123.4 ", JsonNumber.of(-123.4F));
    }

    @Test
    public void testExponent() {
        check("12.3e4", JsonNumber.of(12.3e4F));
        check("12.3e4 ", JsonNumber.of(12.3e4F));
        check(" 12.3e4", JsonNumber.of(12.3e4F));
        check(" 12.3e4 ", JsonNumber.of(12.3e4F));
        check("-123.4e5", JsonNumber.of(-123.4e5F));
        check("123.4e-5 ", JsonNumber.of(123.4e-5F));
        check(" 123.4e5", JsonNumber.of(123.4e5F));
        check(" 123.4e5 ", JsonNumber.of(123.4e5F));
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
        check(" [ 1, 2 ] ", JsonArray.of(List.of(JsonNumber.of(1F), JsonNumber.of(2F))));
        check(" [ 1, true, false, \"hello\", null] ", JsonArray.of(List.of(JsonNumber.of(1F), JsonBoolean.TRUE, JsonBoolean.FALSE, JsonString.of("hello"), JsonNull.INSTANCE)));
    }

    @Test
    public void testNestedArray() {
        check(" [[1,true], [3,false], [null, \"array\"]] ", JsonArray.of(List.of(JsonArray.of(List.of(JsonNumber.of(1F), JsonBoolean.TRUE)), JsonArray.of(List.of(JsonNumber.of(3F), JsonBoolean.FALSE)), JsonArray.of(List.of(JsonNull.INSTANCE, JsonString.of("array"))))));
    }

    @Test
    public void testObject() {
        check("{}", JsonObject.of(Map.of()));
        check(" { \"a\": 1, \"b\": true, \"c\": false, \"d\": \"hello\", \"e\": null } ", JsonObject.of(Map.of("a", JsonNumber.of(1F), "b", JsonBoolean.TRUE, "c", JsonBoolean.FALSE, "d", JsonString.of("hello"), "e", JsonNull.INSTANCE)));
    }

    @Test
    public void testNestedObject() {
        check(" { \"a\": { \"b\": 1, \"c\": true }, \"d\": { \"e\": 3, \"f\": false }, \"g\": { \"h\": null, \"i\": \"object\" } } ", JsonObject.of(Map.of("a", JsonObject.of(Map.of("b", JsonNumber.of(1F), "c", JsonBoolean.TRUE)), "d", JsonObject.of(Map.of("e", JsonNumber.of(3F), "f", JsonBoolean.FALSE)), "g", JsonObject.of(Map.of("h", JsonNull.INSTANCE, "i", JsonString.of("object"))))));
    }

    @Test
    public void testArrayOfObjects() {
        check(" [ { \"a\": 1, \"b\": true }, { \"c\": false, \"d\": \"hello\" }, { \"e\": null } ] ", JsonArray.of(List.of(JsonObject.of(Map.of("a", JsonNumber.of(1F), "b", JsonBoolean.TRUE)), JsonObject.of(Map.of("c", JsonBoolean.FALSE, "d", JsonString.of("hello"))), JsonObject.of(Map.of("e", JsonNull.INSTANCE)))));
    }

    @Test
    public void testObjectOfArrays() {
        check(" { \"a\": [ 1, true ], \"b\": [ false, \"hello\" ], \"c\": [ null ] } ", JsonObject.of(Map.of("a", JsonArray.of(List.of(JsonNumber.of(1F), JsonBoolean.TRUE)), "b", JsonArray.of(List.of(JsonBoolean.FALSE, JsonString.of("hello"))), "c", JsonArray.of(List.of(JsonNull.INSTANCE)))));
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
                  "branch": "master"
                  }
              }
            }
            """,
          JsonObject.of(Map.of("project", JsonObject.of(Map.of("name", JsonString.of("meow"), "description", JsonString.of("a tiny json parser library"), "author", JsonString.of("geekya215"), "license", JsonString.of("MIT"), "extras", JsonNull.INSTANCE, "language", JsonArray.of(List.of(JsonString.of("Java"), JsonString.of("Kotlin"))), "info", JsonObject.of(Map.of("version", JsonString.of("0.1.0"), "branch", JsonString.of("master"))))))));

        check("""
          [
            {
              "isActive": false,
              "picture": "http://placehold.it/32x32",
              "phone": "+1 (921) 442-2611",
              "latitude": -29.43267,
              "longitude": 5.57151,
              "tags": [
                "dolore",
                "ut"
              ],
              "friends": [
                {
                  "id": 0,
                  "name": "Althea House"
                },
                {
                  "id": 1,
                  "name": "Janell Best"
                },
                {
                  "id": 2,
                  "name": "Daisy Jennings"
                }
              ]
            },
            {
              "isActive": true,
              "picture": "http://placehold.it/32x32",
              "phone": "+1 (938) 456-2706",
              "latitude": 8.519416,
              "longitude": -51.122108,
              "tags": [
                "consequat",
                "qui"
              ],
              "friends": [
                {
                  "id": 0,
                  "name": "Robbins Kemp"
                },
                {
                  "id": 1,
                  "name": "Morgan Chapman"
                },
                {
                  "id": 2,
                  "name": "Briana Rivas"
                }
              ]
            }
          ]
          """, JsonArray.of(List.of(JsonObject.of(Map.of("isActive", JsonBoolean.FALSE, "picture", JsonString.of("http://placehold.it/32x32"), "phone", JsonString.of("+1 (921) 442-2611"), "latitude", JsonNumber.of(-29.43267F), "longitude", JsonNumber.of(5.57151F), "tags", JsonArray.of(List.of(JsonString.of("dolore"), JsonString.of("ut"))), "friends", JsonArray.of(List.of(JsonObject.of(Map.of("id", JsonNumber.of(0F), "name", JsonString.of("Althea House"))), JsonObject.of(Map.of("id", JsonNumber.of(1F), "name", JsonString.of("Janell Best"))), JsonObject.of(Map.of("id", JsonNumber.of(2F), "name", JsonString.of("Daisy Jennings"))))))), JsonObject.of(Map.of("isActive", JsonBoolean.TRUE, "picture", JsonString.of("http://placehold.it/32x32"), "phone", JsonString.of("+1 (938) 456-2706"), "latitude", JsonNumber.of(8.519416F), "longitude", JsonNumber.of(-51.122108F), "tags", JsonArray.of(List.of(JsonString.of("consequat"), JsonString.of("qui"))), "friends", JsonArray.of(List.of(JsonObject.of(Map.of("id", JsonNumber.of(0F), "name", JsonString.of("Robbins Kemp"))), JsonObject.of(Map.of("id", JsonNumber.of(1F), "name", JsonString.of("Morgan Chapman"))), JsonObject.of(Map.of("id", JsonNumber.of(2F), "name", JsonString.of("Briana Rivas"))))))))));
    }
}
