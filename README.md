# meow
Meow is a simple JSON parsing library.
It implements a simplified version of [Parsec](https://github.com/haskell/parsec).
This parser has no error hinting, all parsing results are wrapped by Maybe.
It returns the `Just result` if the parsing succeeds and `Nothing` if it fails.

**Note: because meow use sealed interface to simulate ADT, so the minimal required version of Java is 17.**

## Defination
The parser prototype defined by Haskell as below
```haskell
newtype Parser a = Parser {
  runParser :: String -> Maybe (a, String)
}
```


## Example
```java
// get json parser
var parser = Grammar._json;

// parse number
parser.parse("123");
// result: Just JsonNumber 123

// parse string
parser.parse("\"hello world!\\n\"");
// result: Just JsonString "hello world!\n"

// parse array
parser.parse("[1, 2, 3]");
// result: Just JsonArray [1, 2, 3]

// parse object
parser.parse("{\"one\": 1, \"two\": 2, \"three\": 3}");
// result: Just JsonObject {one=JsonNumber 1, two=JsonNumber 2, three=JsonNumber 3}

// parse fail
parser.parse("\"hello");
// result: Nothing
```
For more available examples see [here](https://github.com/geekya215/meow/blob/master/src/test/java/io/geekya/meow/ParserTest.java).

## Acknowledgments
- [Writing a JSON parser from scratch](https://fsharpforfunandprofit.com/posts/understanding-parser-combinators-4/)
- [parsecj](https://github.com/jon-hanson/parsecj)
- [JSON Parser 100% From Scratch in Haskell (only 111 lines)](https://www.youtube.com/watch?v=N9RUqGYuGfw)
