package io.geekya.meow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Parsec<A> implements Parser<A>, Monad<A, Parsec<?>> {
    private final Function<String, Maybe<Pair<A, String>>> pf;

    private Parsec(Function<String, Maybe<Pair<A, String>>> pf) {
        this.pf = pf;
    }

    public static Parsec<Character> item() {
        return new Parsec<>(s -> s.isEmpty()
          ? Maybe.nothing()
          : Maybe.just(new Pair<>(s.charAt(0), s.substring(1))));
    }

    public static Parsec<Character> satisfy(Predicate<Character> p) {
        return item().bind(
          x -> p.test(x)
            ? item().result(x) : item().fail());
    }

    public static Parsec<Character> character(Character x) {
        return satisfy(c -> c == x);
    }

    public static Parsec<List<Character>> string(String s) {
        return s.isEmpty()
          ? item().result(new ArrayList<>())
          : character(s.charAt(0)).bind(x -> string(s.substring(1)).bind(xs -> {
            xs.add(0, x);
            return item().result(xs);
        }));
    }

    public static <B> Parsec<List<B>> many(Parsec<B> p) {
        return new Parsec<>(inp -> {
            Maybe<Pair<B, String>> m = p.runParser(inp);
            if (m.isNothing()) {
                return Maybe.just(new Pair<>(new ArrayList<>(), inp));
            } else {
                Pair<B, String> pair = m.fromJust();
                Maybe<Pair<List<B>, String>> r = many(p).runParser(pair.snd());
                r.fromJust().fst().add(0, pair.fst());
                return r;
            }
        });
    }

    public static <B> Parsec<List<B>> many1(Parsec<B> p) {
        return p.bind(a -> many(p).bind(b -> {
            b.add(0, a);
            return p.result(b);
        }));
    }

    public static <B> Parsec<List<B>> count(Integer n, Parsec<B> p) {
        return n == 0
          ? p.result(new ArrayList<>())
          : p.bind(a -> count(n - 1, p).bind(b -> {
            b.add(0, a);
            return p.result(b);
        }));
    }

    @SafeVarargs
    public static <B> Parsec<List<B>> seq(Parsec<B>... ps) {
        return ps.length == 0
          ? item().result(new ArrayList<>())
          : ps[0].bind(a -> seq(Arrays.copyOfRange(ps, 1, ps.length)).bind(b -> {
            b.add(0, a);
            return item().result(b);
        }));
    }

    public static <B> Parsec<List<B>> sepBy(Parsec<B> p, Parsec<Character> sep) {
        return sepBy1(p, sep).or(p.result(new ArrayList<>()));
    }

    public static <B> Parsec<List<B>> sepBy1(Parsec<B> p, Parsec<Character> sep) {
        return p.bind(a -> many(sep.bind(b -> p)).bind(b -> {
            b.add(0, a);
            return p.result(b);
        }));
    }

    public static <L, R> Parsec<R> discardL(Parsec<L> l, Parsec<R> r) {
        return l.bind(a -> r.bind(b -> l.result(b)));
    }

    public static <L, R> Parsec<L> discardR(Parsec<L> l, Parsec<R> r) {
        return l.bind(a -> r.bind(b -> l.result(a)));
    }

    public Parsec<A> or(Parsec<A> a) {
        return new Parsec<>(inp -> {
            Maybe<Pair<A, String>> m = runParser(inp);
            return m.isNothing() ? a.runParser(inp) : m;
        });
    }

    public static <B> Parsec<B> choice(List<Parsec<B>> ps) {
        return ps.isEmpty()
          ? new Parsec<>(inp -> Maybe.nothing())
          : ps.get(0).or(choice(ps.subList(1, ps.size())));
    }

    public static <B> Parsec<B> between(Parsec<Character> open, Parsec<Character> close, Parsec<B> p) {
        return discardR(discardL(open, p), close);
    }

    private <B> Parsec<B> result(B b) {
        return pure(b);
    }

    private Parsec<A> fail() {
        return new Parsec<>(s -> Maybe.nothing());
    }

    public Maybe<A> parse(String inp) {
        return runParser(inp).bind(p -> Maybe.just(p.fst()));
    }

    @Override
    public Maybe<Pair<A, String>> runParser(String inp) {
        return pf.apply(inp);
    }

    @Override
    public <B> Parsec<B> pure(B b) {
        return new Parsec<>(inp -> Maybe.just(new Pair<>(b, inp)));
    }

    @Override
    public <B> Parsec<B> map(Function<? super A, ? extends B> f) {
        return bind(a -> pure(f.apply(a)));
    }

    @Override
    public <B> Parsec<B> fmap(Applicative<Function<? super A, ? extends B>, Parsec<?>> af) {
        return (Parsec<B>) af.map(f -> bind(a -> pure(f.apply(a))));
    }

    @Override
    public <B> Parsec<B> bind(Function<? super A, ? extends Monad<B, Parsec<?>>> mf) {
        return new Parsec<>(inp -> runParser(inp).bind(p -> {
            Parsec<B> a = (Parsec<B>) mf.apply(p.fst());
            return a.runParser(p.snd());
        }));
    }
}
