package io.geekya.meow;

import java.util.List;
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

    public static final Parsec<List<Character>> _null = string("null");
    public static final Parsec<List<Character>> _boolean = string("true").or(string("false"));
    public static final Parsec<List<Character>> _number = discardL(character('-'), nat).map(a -> {
        a.add(0, '-');
        return a;
    }).or(nat);
    public static final Parsec<List<Character>> _string = between(
      character('\"'), character('\"'),
      many(choice(List.of(unEscapedChar, escape, unicode))));

    public static final Parsec<List<Character>> _value = choice(List.of(
      _null, _boolean, _number, _string));

    public static final Parsec<List<List<Character>>> _array = between(
      openBracket, closeBracket,
      sepBy(_value, comma));

    public static final Parsec<Pair<String, List<Character>>> _pair = discardR(_string, colon).bind(
      k -> _value.bind(v -> {
          String key = k.stream().map(c -> String.valueOf(c)).reduce("", (c, d) -> c + d);
          return _value.pure(new Pair<>(key, v));
      })
    );

    public static final Parsec<List<Pair<String, List<Character>>>> _object = between(
      openBrace, closeBrace,
      sepBy(_pair, comma));
}
