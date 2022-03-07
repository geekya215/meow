package io.geekya.meow;

import io.geekya.meow.adt.*;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import static io.geekya.meow.Combinator.*;

// grammar reference: https://github.com/antlr/grammars-v4/blob/master/json/JSON.g4
public class Grammar {
    // skip tail whitespace
    public static <T> Parser<T> tokenizer(Parser<T> p) {
        return p.bind(r -> many(whiteSpace).discardL(pure(r)));
    }

    public static final Predicate<Character> isWhiteSpace = c -> c == ' ' || c == '\t' || c == '\n' || c == '\r';
    public static final Predicate<Character> isDigit = c -> c >= '0' && c <= '9';
    public static final Predicate<Character> isLetter = c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');

    public static final Parser<Character> whiteSpace = satisfy(isWhiteSpace);
    public static final Parser<Character> digit = satisfy(isDigit);
    public static final Parser<Character> letter = satisfy(isLetter);

    // create dummy parser
    public static Parser.Ref<JsonValue> _value = Parser.ref();

    public static Parser<JsonValue> _null = tokenizer(string("null")).discardL(pure(JsonNull.INSTANCE));

    public static Parser<JsonValue> _true = string("true").discardL(pure(JsonBoolean.TRUE));
    public static Parser<JsonValue> _false = string("false").discardL(pure(JsonBoolean.FALSE));
    public static Parser<JsonValue> _boolean = tokenizer(_true.or(_false));

    public static Parser<String> _int = string("0").or(satisfy(c -> c >= '1' && c <= '9')
      .bind(c -> many(digit)
        .bind(d -> {
            d.add(0, c);
            return pure(d.stream().map(e -> e - '0').reduce(0, (a, b) -> a * 10 + b).toString());
        })));

    public static Parser<String> _exponent = string("e").or(string("E"))
      .bind(
        e -> string("+").or(string("-").or(string(""))).
          bind(s -> _int.
            bind(i -> pure(e + s + i))));

    public static Parser<String> _fraction = string(".")
      .bind(d -> many1(digit).map(a -> a.stream().map(c -> String.valueOf(c)).reduce("", (_1, _2) -> _1 + _2))
        .bind(n -> pure(d + n)));

    // all number represented as float
    public static Parser<Float> _float = string("-").or(string("")).bind(
      s -> _int.bind(
        i -> _fraction.or(string("")).bind(
          f -> _exponent.or(string("")).bind(
            e -> pure(Float.parseFloat(s + i + f + e))
          )
        )
      ));

    public static Parser<JsonValue> _number = tokenizer(_float.map(f -> JsonNumber.of(f)));

    public static Parser<Character> unicode = count(4, digit.or(letter))
      .map(a -> a.stream().map(b -> Character.digit(b, 16)).reduce(0, (c, d) -> c * 16 + d))
      .map(c -> (char) c.intValue());

    public static Parser<Character> esc = choice(List.of(
      character('"'),
      character('\\'),
      character('/'),
      character('b').discardL(pure('\b')),
      character('f').discardL(pure('\f')),
      character('n').discardL(pure('\n')),
      character('r').discardL(pure('\r')),
      character('t').discardL(pure('\t')),
      character('u').discardL(unicode)
    ));

    public static Parser<Character> _char = (character('\\').discardL(esc)).or(satisfy(c -> c != '"' && c != '\\'));

    public static Parser<String> _str = tokenizer(between(
      character('\"'), character('\"'),
      many(_char)
    ).map(a -> a.stream().map(b -> String.valueOf(b)).reduce("", (c, d) -> c + d)));

    public static Parser<JsonValue> _string = _str.map(s -> JsonString.of(s));

    public static Parser<JsonValue> _array = between(
      tokenizer(character('[')), tokenizer(character(']')),
      sepBy(_value, tokenizer(character(',')))
    ).map(a -> JsonArray.of(a));

    public static Parser<Pair<String, JsonValue>> _pair = _str.bind(
      k -> tokenizer(character(':')).discardL(_value).map(v -> new Pair<>(k, v)));

    public static Parser<JsonValue> _object = between(
      tokenizer(character('{')), tokenizer(character('}')),
      sepBy(_pair, tokenizer(character(',')))
    ).map(a -> {
        HashMap<String, JsonValue> map = new HashMap<>();
        for (Pair<String, JsonValue> pair : a) {
            map.put(pair.fst(), pair.snd());
        }
        return JsonObject.of(map);
    });

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

    public static Parser<JsonValue> _json = many(whiteSpace).discardL(_value);
}
