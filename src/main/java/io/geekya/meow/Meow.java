package io.geekya.meow;

import io.geekya.meow.adt.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static io.geekya.meow.Parsec.*;


public class Meow {
    private static final Predicate<Character> isLower = c -> c >= 'a' && c <= 'z';
    private static final Predicate<Character> isUpper = c -> c >= 'A' && c <= 'Z';
    private static final Predicate<Character> isDigit = c -> c >= '0' && c <= '9';
    private static final Predicate<Character> isUnescapedChar = c -> c != '\"' && c != '\\';

    private static final Parsec<Character> lower = satisfy(isLower);
    private static final Parsec<Character> upper = satisfy(isUpper);
    private static final Parsec<Character> digit = satisfy(isDigit);
    private static final Parsec<Character> unEscapedChar = satisfy(isUnescapedChar);
    private static final Parsec<Character> letter = lower.or(upper);
    private static final Parsec<Character> comma = character(',');
    private static final Parsec<Character> colon = character(':');
    private static final Parsec<Character> openBrace = character('{');
    private static final Parsec<Character> closeBrace = character('}');
    private static final Parsec<Character> openBracket = character('[');
    private static final Parsec<Character> closeBracket = character(']');

    // escape sequences
    private static final Parsec<Character> doubleQuote = string("\\\"").map(s -> '\"');
    private static final Parsec<Character> singleQuote = string("\\\'").map(s -> '\'');
    private static final Parsec<Character> backslash = string("\\\\").map(s -> '\\');
    private static final Parsec<Character> backspace = string("\\b").map(s -> '\b');
    private static final Parsec<Character> formfeed = string("\\f").map(s -> '\f');
    private static final Parsec<Character> newline = string("\\n").map(s -> '\n');
    private static final Parsec<Character> carriageReturn = string("\\r").map(s -> '\r');
    private static final Parsec<Character> tab = string("\\t").map(s -> '\t');

    private static final Parsec<Character> escape = choice(List.of(
      doubleQuote, singleQuote, backslash, backspace, formfeed, newline, carriageReturn, tab
    ));

    // Do not support BMP unicode
    private static final Parsec<Character> unicode = discardL(string("\\u"), count(4, letter.or(digit)))
      .map(a -> a.stream().map(c -> String.valueOf(c)).reduce("", (c, d) -> c + d))
      .map(c -> (char) Integer.parseInt(c, 16));

    private static final Parsec<List<Character>> nat = many1(digit);
    private static final Parsec<Integer> _num = nat.map(a -> a.stream().map(c -> c - '0').reduce(0, (c, d) -> c * 10 + d));

    public static final Parsec<JsonValue> _null = string("null").map(s -> JsonNull.INSTANCE);
    public static final Parsec<JsonValue> _true = string("true").map(s -> JsonBoolean.TRUE);
    public static final Parsec<JsonValue> _false = string("false").map(s -> JsonBoolean.FALSE);
    public static final Parsec<JsonValue> _number = discardL(character('-'), _num).map(x -> -x).or(_num).map(x -> JsonNumber.of(x));

    public static final Parsec<String> _str = between(
      character('\"'), character('\"'),
      many(choice(List.of(unEscapedChar, escape, unicode)))
    ).map(a -> a.stream().map(c -> String.valueOf(c)).reduce("", (c, d) -> c + d));

    public static final Parsec<JsonValue> _string = _str.map(s -> JsonString.of(s));

    // can not recursive definition of JsonValue
    // because of forward reference
    public static final Parsec<JsonValue> _value = choice(List.of(
      _null, _true, _false, _number, _string));

    public static final Parsec<JsonValue> _array = between(
      openBracket, closeBracket,
      sepBy(_value, comma))
      .map(a -> JsonArray.of(a));

    public static final Parsec<Pair<String, JsonValue>> _pair = discardR(_str, colon).bind(
      k -> _value.bind(v -> _value.pure(new Pair<>(k, v))));

    public static final Parsec<JsonValue> _object = between(
      openBrace, closeBrace,
      sepBy(_pair, comma))
      .map(a -> {
          Map<String, JsonValue> map = new HashMap<>();
          for (Pair<String, JsonValue> pair : a) {
              map.put(pair.fst(), pair.snd());
          }
          return JsonObject.of(map);
      });

    public static final Parsec<JsonValue> _json = choice(List.of(
      _value, _array, _object));
}
