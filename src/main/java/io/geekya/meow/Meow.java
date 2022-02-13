package io.geekya.meow;

import io.geekya.meow.adt.JsonBoolean;
import io.geekya.meow.adt.JsonNull;

import java.util.List;

public class Meow {
    private static final Parsec<List<Character>> space = Parsec.character(' ').many();

    public static final Parsec<JsonNull> JsonNullParser = space.discardL(Parsec.string("null")).discardR(space).map(a -> JsonNull.INSTANCE);

    public static final Parsec<JsonBoolean> JsonBooleanParser = space.discardL(Parsec.string("true")).discardR(space).map(a -> JsonBoolean.TRUE)
            .plus(space.discardL(Parsec.string("false")).discardR(space).map(a -> JsonBoolean.FALSE));
}
