package io.geekya.meow;

import io.geekya.meow.adt.*;

import java.util.HashMap;
import java.util.List;

import static io.geekya.meow.Chars.*;
import static io.geekya.meow.Combinator.*;

public class Meow {
    private static final Parser.Ref<JsonValue> _value = Parser.ref();

    private static final Parser<JsonValue> _null = string("null").map(s -> JsonNull.INSTANCE);
    private static final Parser<JsonValue> _true = string("true").map(s -> JsonBoolean.TRUE);
    private static final Parser<JsonValue> _false = string("false").map(s -> JsonBoolean.FALSE);

    private static final Parser<JsonValue> _boolean = _true.or(_false);

    private static final Parser<Character> digital = satisfy(c -> c >= '0' && c <= '9');
    private static final Parser<Integer> nat = many1(digital)
      .map(s -> s.stream().map(c -> c - '0').reduce(0, (a, b) -> a * 10 + b));
    private static final Parser<Integer> _integer = character('-').bind(neg -> nat.map(n -> -n)).or(nat);
    private static final Parser<JsonValue> _number = _integer.map(JsonNumber::of);

    private static final Parser<String> _str = between(
      character('\"'), character('\"'),
      many(validChar)
    ).map(a -> a.stream().map(String::valueOf).reduce("", (b, c) -> b + c));

    private static final Parser<JsonValue> _string = _str.map(JsonString::of);

    private static final Parser<JsonValue> _array = between(
      openBracket, closeBracket,
      sepBy(_value, character(',')))
      .map(JsonArray::of);

    private static final Parser<Pair<String, JsonValue>> _field = discardR(_str, colon).bind(
      k -> _value.bind(v -> pure(new Pair<>(k, v))));

    private static final Parser<JsonValue> _object = between(
      openBrace, closeBrace,
      sepBy(_field, comma)
        .map(a -> {
            HashMap<String, JsonValue> map = new HashMap<>();
            for (Pair<String, JsonValue> p : a) {
                map.put(p.fst(), p.snd());
            }
            return JsonObject.of(map);
        })
    );

    // fix forward reference
    static {
        _value.set(choice(List.of(
          _null,
          _boolean,
          _number,
          _string,
          _array,
          _object
        )));
    }
}
