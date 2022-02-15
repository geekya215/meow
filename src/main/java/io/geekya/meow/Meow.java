package io.geekya.meow;

import io.geekya.meow.adt.JsonBoolean;
import io.geekya.meow.adt.JsonNull;
import io.geekya.meow.adt.JsonNumber;
import io.geekya.meow.adt.JsonString;

public class Meow {
    private static final Parsec<Integer> digit = Parsec.sat(c -> c >= '0' && c <= '9').map(c -> c - '0');
    private static final Parsec<Integer> nat = digit.some().map(x -> x.stream().reduce(0, (a, b) -> a * 10 + b));
    private static final Parsec<Character> EscapedChar = Parsec.string("\\\"").map(x -> '\"')
      .plus(Parsec.string("\\\\").map(x -> '\\'))
      .plus(Parsec.string("\\/").map(x -> '/'))
      .plus(Parsec.string("\\b").map(x -> '\b'))
      .plus(Parsec.string("\\f").map(x -> '\f'))
      .plus(Parsec.string("\\n").map(x -> '\n'))
      .plus(Parsec.string("\\r").map(x -> '\r'))
      .plus(Parsec.string("\\t").map(x -> '\t'));

    public static final Parsec<JsonNull> JsonNullParser = Parsec.string("null").trimSpace().map(a -> JsonNull.INSTANCE);

    public static final Parsec<JsonBoolean> JsonBooleanParser = Parsec.string("true").trimSpace().map(a -> JsonBoolean.TRUE)
      .plus(Parsec.string("false").trimSpace().map(a -> JsonBoolean.FALSE));

    public static final Parsec<JsonNumber> JsonNumberParser = nat.trimSpace()
      .plus(Parsec.character('-').discardL(nat).trimSpace().map(a -> -a)).map(a -> JsonNumber.of(a));

    public static final Parsec<JsonString> JsonStringParser = Parsec.sat(c -> c != '\"' && c != '\\')
      .plus(EscapedChar).many()
      .between(Parsec.character('\"'), Parsec.character('\"')).trimSpace()
      .map(a -> JsonString.of(a.stream().map(c -> String.valueOf(c)).reduce("", (c, d) -> c + d)));
}
