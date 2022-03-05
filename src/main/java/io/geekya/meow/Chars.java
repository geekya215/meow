package io.geekya.meow;

import java.util.List;
import java.util.function.Predicate;

import static io.geekya.meow.Combinator.*;

public class Chars {
    public static final Predicate<Character> isLower = c -> c >= 'a' && c <= 'z';
    public static final Predicate<Character> isUpper = c -> c >= 'A' && c <= 'Z';
    public static final Predicate<Character> isDigit = c -> c >= '0' && c <= '9';
    public static final Predicate<Character> isUnescapedChar = c -> c != '\"' && c != '\\';

    public static final Parser<Character> lower = satisfy(isLower);
    public static final Parser<Character> upper = satisfy(isUpper);
    public static final Parser<Character> letter = lower.or(upper);
    public static final Parser<Character> digit = satisfy(isDigit);
    // Do not support BMP unicode
    public static final Parser<Character> unicode = discardL(string("\\u"), count(4, letter.or(digit)))
      .map(a -> a.stream().map(c -> String.valueOf(c)).reduce("", (c, d) -> c + d))
      .map(c -> (char) Integer.parseInt(c, 16));
    public static final Parser<Character> unEscapedChar = satisfy(isUnescapedChar);
    public static final Parser<Character> comma = character(',');
    public static final Parser<Character> colon = character(':');
    public static final Parser<Character> openBrace = character('{');
    public static final Parser<Character> closeBrace = character('}');
    public static final Parser<Character> openBracket = character('[');
    public static final Parser<Character> closeBracket = character(']');
    // escape sequences
    public static final Parser<Character> doubleQuote = string("\\\"").map(s -> '\"');
    public static final Parser<Character> singleQuote = string("\\'").map(s -> '\'');
    public static final Parser<Character> backslash = string("\\\\").map(s -> '\\');
    public static final Parser<Character> backspace = string("\\b").map(s -> '\b');
    public static final Parser<Character> formfeed = string("\\f").map(s -> '\f');
    public static final Parser<Character> newline = string("\\n").map(s -> '\n');
    public static final Parser<Character> carriageReturn = string("\\r").map(s -> '\r');
    public static final Parser<Character> tab = string("\\t").map(s -> '\t');
    public static final Parser<Character> escape = choice(List.of(
      doubleQuote, singleQuote, backslash, backspace, formfeed, newline, carriageReturn, tab
    ));
    public static final Parser<Character> validChar = choice(List.of(unEscapedChar, escape, unicode));
}
