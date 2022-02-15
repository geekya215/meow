package io.geekya.meow;

import io.geekya.meow.adt.JsonBoolean;
import io.geekya.meow.adt.JsonNull;
import io.geekya.meow.adt.JsonNumber;

public class Meow {
    private static final Parsec<Integer> digit = Parsec.sat(c -> c >= '0' && c <= '9').map(c -> c - '0');
    private static final Parsec<Integer> nat = digit.some().map(x -> x.stream().reduce(0, (a, b) -> a * 10 + b));

    public static final Parsec<JsonNull> JsonNullParser = Parsec.string("null").trimSpace().map(a -> JsonNull.INSTANCE);

    public static final Parsec<JsonBoolean> JsonBooleanParser = Parsec.string("true").trimSpace().map(a -> JsonBoolean.TRUE)
      .plus(Parsec.string("false").trimSpace().map(a -> JsonBoolean.FALSE));

    public static final Parsec<JsonNumber> JsonNumberParser = nat.trimSpace()
      .plus(Parsec.character('-').discardL(nat).trimSpace().map(a -> -a)).map(a -> JsonNumber.of(a));
}
