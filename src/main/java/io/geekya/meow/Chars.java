package io.geekya.meow;

import java.util.List;
import java.util.function.Predicate;

import static io.geekya.meow.Parsec.*;
import static io.geekya.meow.Parsec.count;

public class Chars {
    private static final Predicate<Character> isLower = c -> c >= 'a' && c <= 'z';
    private static final Predicate<Character> isUpper = c -> c >= 'A' && c <= 'Z';
    private static final Predicate<Character> isDigit = c -> c >= '0' && c <= '9';
    private static final Predicate<Character> isUnescapedChar = c -> c != '\"' && c != '\\';

    public static final Parsec<Character> lower = satisfy(isLower);
    public static final Parsec<Character> upper = satisfy(isUpper);
    public static final Parsec<Character> digit = satisfy(isDigit);
    public static final Parsec<Character> unEscapedChar = satisfy(isUnescapedChar);
    public static final Parsec<Character> letter = lower.or(upper);
    public static final Parsec<Character> comma = character(',');
    public static final Parsec<Character> colon = character(':');
    public static final Parsec<Character> openBrace = character('{');
    public static final Parsec<Character> closeBrace = character('}');
    public static final Parsec<Character> openBracket = character('[');
    public static final Parsec<Character> closeBracket = character(']');

    // escape sequences
    private static final Parsec<Character> doubleQuote = string("\\\"").map(s -> '\"');
    private static final Parsec<Character> singleQuote = string("\\\'").map(s -> '\'');
    private static final Parsec<Character> backslash = string("\\\\").map(s -> '\\');
    private static final Parsec<Character> backspace = string("\\b").map(s -> '\b');
    private static final Parsec<Character> formfeed = string("\\f").map(s -> '\f');
    private static final Parsec<Character> newline = string("\\n").map(s -> '\n');
    private static final Parsec<Character> carriageReturn = string("\\r").map(s -> '\r');
    private static final Parsec<Character> tab = string("\\t").map(s -> '\t');

    public static final Parsec<Character> escape = choice(List.of(
      doubleQuote, singleQuote, backslash, backspace, formfeed, newline, carriageReturn, tab
    ));

    // Do not support BMP unicode
    public static final Parsec<Character> unicode = discardL(string("\\u"), count(4, letter.or(digit)))
      .map(a -> a.stream().map(c -> String.valueOf(c)).reduce("", (c, d) -> c + d))
      .map(c -> (char) Integer.parseInt(c, 16));
}
