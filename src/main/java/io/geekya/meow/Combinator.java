package io.geekya.meow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Combinator {
    public static Parser<Character> item() {
        return inp -> inp.isEmpty()
          ? Maybe.nothing()
          : Maybe.just(new Pair<>(inp.charAt(0), inp.substring(1)));
    }

    public static Parser<Character> satisfy(Predicate<Character> p) {
        return bind(item(), c -> p.test(c) ? pure(c) : fail());
    }

    public static Parser<Character> character(Character c) {
        return satisfy(x -> x == c);
    }

    public static Parser<String> string(String s) {
        return s.isEmpty()
          ? pure("")
          : bind(character(s.charAt(0)), a -> bind(string(s.substring(1)), b -> pure(a + b)));
    }

    public static <L, R> Parser<R> discardL(Parser<L> l, Parser<R> r) {
        return bind(r, a -> bind(l, b -> pure(a)));
    }

    public static <L, R> Parser<L> discardR(Parser<L> l, Parser<R> r) {
        return bind(l, a -> bind(r, b -> pure(a)));
    }

    public static <A> Parser<A> or(Parser<A> p, Parser<A> q) {
        return inp -> {
            Maybe<Pair<A, String>> mp = p.runParser(inp);
            return mp.isNothing() ? q.runParser(inp) : mp;
        };
    }

    public static <A> Parser<A> choice(List<Parser<A>> ps) {
        return ps.isEmpty()
          ? fail()
          : or(ps.get(0), choice(ps.subList(1, ps.size())));
    }

    public static <A> Parser<List<A>> count(Integer n, Parser<A> p) {
        return n == 0
          ? pure(new ArrayList<>())
          : bind(p, a -> bind(count(n - 1, p), b -> {
            b.add(0, a);
            return pure(b);
        }));
    }

    public static <A> Parser<List<A>> many(Parser<A> p) {
        return inp -> {
            Maybe<Pair<A, String>> m = p.runParser(inp);
            if (m.isNothing()) {
                return Maybe.just(new Pair<>(new ArrayList<>(), inp));
            } else {
                Pair<A, String> pair = m.fromJust();
                Maybe<Pair<List<A>, String>> r = many(p).runParser(pair.snd());
                r.fromJust().fst().add(0, pair.fst());
                return r;
            }
        };
    }

    public static <A> Parser<List<A>> many1(Parser<A> p) {
        return bind(p, a -> bind(many(p), as -> {
            as.add(0, a);
            return pure(as);
        }));
    }

    public static <A> Parser<List<A>> sepBy(Parser<A> p, Parser<Character> sep) {
        return or(sepBy1(p, sep), pure(new ArrayList<>()));
    }

    public static <A> Parser<List<A>> sepBy1(Parser<A> p, Parser<Character> sep) {
        return bind(p, a -> bind(many(bind(sep, b -> p)), as -> {
            as.add(0, a);
            return pure(as);
        }));
    }

    public static <A> Parser<A> between(Parser<Character> open, Parser<Character> close, Parser<A> p) {
        return bind(open, a -> bind(p, b -> bind(close, c -> pure(b))));
    }

    public static <A> Parser<A> pure(A a) {
        return inp -> Maybe.just(new Pair<>(a, inp));
    }

    public static <A> Parser<A> fail() {
        return inp -> Maybe.nothing();
    }

    public static <A, B> Parser<B> map(Parser<A> p, Function<A, B> f) {
        return bind(p, a -> pure(f.apply(a)));
    }

    public static <A, B> Parser<B> bind(Parser<A> p, Function<A, Parser<B>> f) {
        return inp -> p.runParser(inp)
          .bind(pair -> f.apply(pair.fst()).runParser(pair.snd()));
    }
}
