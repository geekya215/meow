package io.geekya.meow;

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
        check("null", JsonValue.NULL);
        check("null ", JsonValue.NULL);
        check(" null", JsonValue.NULL);
        check(" null ", JsonValue.NULL);
    }

    @Test
    public void testTrue() {
        check("true", JsonValue.TRUE);
        check("true ", JsonValue.TRUE);
        check(" true", JsonValue.TRUE);
        check(" true ", JsonValue.TRUE);
    }

    @Test
    public void testFalse() {
        check("false", JsonValue.FALSE);
        check("false ", JsonValue.FALSE);
        check(" false", JsonValue.FALSE);
        check(" false ", JsonValue.FALSE);
    }

    @Test
    public void testZero() {
        check("0", JsonValue.of(0f));
        check("0 ", JsonValue.of(0f));
        check(" 0", JsonValue.of(0f));
        check(" 0 ", JsonValue.of(0f));
        check("-0", JsonValue.of(-0f));
    }

    @Test
    public void testPositiveInteger() {
        check("12", JsonValue.of(12F));
        check("12 ", JsonValue.of(12F));
        check(" 12", JsonValue.of(12F));
        check(" 12 ", JsonValue.of(12F));
        check("123", JsonValue.of(123F));
        check("123 ", JsonValue.of(123F));
        check(" 123", JsonValue.of(123F));
        check(" 123 ", JsonValue.of(123F));
    }

    @Test
    public void testNegativeInteger() {
        check("-12", JsonValue.of(-12F));
        check("-12 ", JsonValue.of(-12F));
        check(" -12", JsonValue.of(-12F));
        check(" -12 ", JsonValue.of(-12F));
        check("-123", JsonValue.of(-123F));
        check("-123 ", JsonValue.of(-123F));
        check(" -123", JsonValue.of(-123F));
        check(" -123 ", JsonValue.of(-123F));
    }

    @Test
    public void testPositiveFloat() {
        check("12.3", JsonValue.of(12.3F));
        check("12.3 ", JsonValue.of(12.3F));
        check(" 12.3", JsonValue.of(12.3F));
        check(" 12.3 ", JsonValue.of(12.3F));
        check("123.4", JsonValue.of(123.4F));
        check("123.4 ", JsonValue.of(123.4F));
        check(" 123.4", JsonValue.of(123.4F));
        check(" 123.4 ", JsonValue.of(123.4F));
    }

    @Test
    public void testNegativeFloat() {
        check("-12.3", JsonValue.of(-12.3F));
        check("-12.3 ", JsonValue.of(-12.3F));
        check(" -12.3", JsonValue.of(-12.3F));
        check(" -12.3 ", JsonValue.of(-12.3F));
        check("-123.4", JsonValue.of(-123.4F));
        check("-123.4 ", JsonValue.of(-123.4F));
        check(" -123.4", JsonValue.of(-123.4F));
        check(" -123.4 ", JsonValue.of(-123.4F));
    }

    @Test
    public void testExponent() {
        check("12.3e4", JsonValue.of(12.3e4F));
        check("12.3e4 ", JsonValue.of(12.3e4F));
        check(" 12.3e4", JsonValue.of(12.3e4F));
        check(" 12.3e4 ", JsonValue.of(12.3e4F));
        check("-123.4e5", JsonValue.of(-123.4e5F));
        check("123.4e-5 ", JsonValue.of(123.4e-5F));
        check(" 123.4e5", JsonValue.of(123.4e5F));
        check(" 123.4e5 ", JsonValue.of(123.4e5F));
    }

    @Test
    public void testUnicode() {
        check("\"\\u0041\"", JsonValue.of("A"));
        check("\"\\u0041 \"", JsonValue.of("A "));
    }

    @Test
    public void testString() {
        check("\"\"", JsonValue.of(""));
        check("\" \"", JsonValue.of(" "));
        check("\"\\b\"", JsonValue.of("\b"));
        check("\"\\f\"", JsonValue.of("\f"));
        check("\"\\n\"", JsonValue.of("\n"));
        check("\"\\r\"", JsonValue.of("\r"));
        check("\"\\t\"", JsonValue.of("\t"));
        check("\"\\\"\"", JsonValue.of("\""));
        check("\"\\\\\"", JsonValue.of("\\"));
        check("\"\\/\"", JsonValue.of("/"));
        check("\"abc\\u0041\"", JsonValue.of("abcA"));
        check("\"abc\\bde\\nfg\\r\\u0041 \"", JsonValue.of("abc\bde\nfg\rA "));
    }

    @Test
    public void testArray() {
        check("[]", JsonValue.of(List.of()));
        check(" [ 1, 2 ] ", JsonValue.of(List.of(JsonValue.of(1F), JsonValue.of(2F))));
        check(" [ 1, true, false, \"hello\", null] ", JsonValue.of(List.of(JsonValue.of(1F), JsonValue.TRUE, JsonValue.FALSE, JsonValue.of("hello"), JsonValue.NULL)));
    }

    @Test
    public void testNestedArray() {
        check(" [[1,true], [3,false], [null, \"array\"]] ", JsonValue.of(List.of(JsonValue.of(List.of(JsonValue.of(1F), JsonValue.TRUE)), JsonValue.of(List.of(JsonValue.of(3F), JsonValue.FALSE)), JsonValue.of(List.of(JsonValue.NULL, JsonValue.of("array"))))));
    }

    @Test
    public void testObject() {
        check("{}", JsonValue.of(Map.of()));
        check(" { \"a\": 1, \"b\": true, \"c\": false, \"d\": \"hello\", \"e\": null } ", JsonValue.of(Map.of("a", JsonValue.of(1F), "b", JsonValue.TRUE, "c", JsonValue.FALSE, "d", JsonValue.of("hello"), "e", JsonValue.NULL)));
    }

    @Test
    public void testNestedObject() {
        check(" { \"a\": { \"b\": 1, \"c\": true }, \"d\": { \"e\": 3, \"f\": false }, \"g\": { \"h\": null, \"i\": \"object\" } } ", JsonValue.of(Map.of("a", JsonValue.of(Map.of("b", JsonValue.of(1F), "c", JsonValue.TRUE)), "d", JsonValue.of(Map.of("e", JsonValue.of(3F), "f", JsonValue.FALSE)), "g", JsonValue.of(Map.of("h", JsonValue.NULL, "i", JsonValue.of("object"))))));
    }

    @Test
    public void testArrayOfObjects() {
        check(" [ { \"a\": 1, \"b\": true }, { \"c\": false, \"d\": \"hello\" }, { \"e\": null } ] ", JsonValue.of(List.of(JsonValue.of(Map.of("a", JsonValue.of(1F), "b", JsonValue.TRUE)), JsonValue.of(Map.of("c", JsonValue.FALSE, "d", JsonValue.of("hello"))), JsonValue.of(Map.of("e", JsonValue.NULL)))));
    }

    @Test
    public void testObjectOfArrays() {
        check(" { \"a\": [ 1, true ], \"b\": [ false, \"hello\" ], \"c\": [ null ] } ", JsonValue.of(Map.of("a", JsonValue.of(List.of(JsonValue.of(1F), JsonValue.TRUE)), "b", JsonValue.of(List.of(JsonValue.FALSE, JsonValue.of("hello"))), "c", JsonValue.of(List.of(JsonValue.NULL)))));
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
          JsonValue.of(Map.of("project", JsonValue.of(Map.of("name", JsonValue.of("meow"), "description", JsonValue.of("a tiny json parser library"), "author", JsonValue.of("geekya215"), "license", JsonValue.of("MIT"), "extras", JsonValue.NULL, "language", JsonValue.of(List.of(JsonValue.of("Java"), JsonValue.of("Kotlin"))), "info", JsonValue.of(Map.of("version", JsonValue.of("0.1.0"), "branch", JsonValue.of("master"))))))));

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
          """, JsonValue.of(List.of(JsonValue.of(Map.of("isActive", JsonValue.FALSE, "picture", JsonValue.of("http://placehold.it/32x32"), "phone", JsonValue.of("+1 (921) 442-2611"), "latitude", JsonValue.of(-29.43267F), "longitude", JsonValue.of(5.57151F), "tags", JsonValue.of(List.of(JsonValue.of("dolore"), JsonValue.of("ut"))), "friends", JsonValue.of(List.of(JsonValue.of(Map.of("id", JsonValue.of(0F), "name", JsonValue.of("Althea House"))), JsonValue.of(Map.of("id", JsonValue.of(1F), "name", JsonValue.of("Janell Best"))), JsonValue.of(Map.of("id", JsonValue.of(2F), "name", JsonValue.of("Daisy Jennings"))))))), JsonValue.of(Map.of("isActive", JsonValue.TRUE, "picture", JsonValue.of("http://placehold.it/32x32"), "phone", JsonValue.of("+1 (938) 456-2706"), "latitude", JsonValue.of(8.519416F), "longitude", JsonValue.of(-51.122108F), "tags", JsonValue.of(List.of(JsonValue.of("consequat"), JsonValue.of("qui"))), "friends", JsonValue.of(List.of(JsonValue.of(Map.of("id", JsonValue.of(0F), "name", JsonValue.of("Robbins Kemp"))), JsonValue.of(Map.of("id", JsonValue.of(1F), "name", JsonValue.of("Morgan Chapman"))), JsonValue.of(Map.of("id", JsonValue.of(2F), "name", JsonValue.of("Briana Rivas"))))))))));
    }
}
