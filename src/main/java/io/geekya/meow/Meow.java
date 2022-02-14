package io.geekya.meow;

import io.geekya.meow.adt.JsonBoolean;
import io.geekya.meow.adt.JsonNull;
import io.geekya.meow.adt.JsonNumber;

import java.util.List;

public class Meow {
    private static final Parsec<List<Character>> space = Parsec.character(' ').many();
    public static final Parsec<JsonNull> JsonNullParser = space.discardL(Parsec.string("null")).discardR(space).map(a -> JsonNull.INSTANCE);
    public static final Parsec<JsonBoolean> JsonBooleanParser = space.discardL(Parsec.string("true")).discardR(space).map(a -> JsonBoolean.TRUE)
      .plus(space.discardL(Parsec.string("false")).discardR(space).map(a -> JsonBoolean.FALSE));
    private static final Parsec<Integer> digit = Parsec.sat(Character::isDigit).map(Character::getNumericValue);
    private static final Parsec<Integer> nat = digit.some().map(x -> x.stream().reduce(0, (a, b) -> a * 10 + b));
    public static final Parsec<JsonNumber> JsonNumberParser = space.discardL(nat).discardR(space)
      .plus(space.discardL(Parsec.character('-').discardL(nat).discardR(space)).map(a -> -a)).map(a -> JsonNumber.of(a));
}
