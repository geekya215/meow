package io.geekya.meow;

import io.geekya.meow.adt.JsonNull;

import java.util.List;

public class Meow {
    private static final Parsec<List<Character>> space = Parsec.character(' ').many();

    public static final Parsec<JsonNull> JsonNullParser = space.discardL(Parsec.string("null")).discardR(space).map(a -> JsonNull.INSTANCE);
}
