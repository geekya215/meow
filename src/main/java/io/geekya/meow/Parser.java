package io.geekya.meow;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Parser<A> {
    static <A> Ref<A> ref() {
        return new Ref<>();
    }

    static <A> Ref<A> ref(Parser<A> p) {
        return new Ref<>(p);
    }

    Maybe<Pair<A, String>> runParser(String s);

    default Maybe<A> parse(String s) {
        return runParser(s).bind(p -> Maybe.just(p.fst()));
    }

    default <B> Parser<B> discardL(Parser<B> p) {
        return Combinator.discardL(this, p);
    }

    default <B> Parser<A> discardR(Parser<B> p) {
        return Combinator.discardR(this, p);
    }

    default Parser<A> or(Parser<A> p) {
        return Combinator.or(this, p);
    }

    default Parser<A> choice(List<Parser<A>> ps) {
        return Combinator.choice(ps);
    }

    default Parser<List<A>> many() {
        return Combinator.many(this);
    }

    default Parser<List<A>> many1() {
        return Combinator.many1(this);
    }

    default Parser<List<A>> sepBy(Parser<Character> sep) {
        return Combinator.sepBy(this, sep);
    }

    default Parser<List<A>> sepBy1(Parser<Character> sep) {
        return Combinator.sepBy1(this, sep);
    }

    default Parser<A> between(Parser<Character> open, Parser<Character> close) {
        return Combinator.between(open, close, this);
    }

    default Parser<A> pure(A a) {
        return Combinator.pure(a);
    }

    default <B> Parser<B> map(Function<A, B> f) {
        return Combinator.map(this, f);
    }

    default <B> Parser<B> bind(Function<A, Parser<B>> f) {
        return Combinator.bind(this, f);
    }

    class Ref<A> implements Supplier<Parser<A>>, Parser<A> {
        private Parser<A> parser;

        private Ref(Parser<A> parser) {
            this.parser = parser;
        }

        private Ref() {
            this(null);
        }

        public void set(Parser<A> parser) {
            this.parser = parser;
        }

        @Override
        public Parser<A> get() {
            if (parser == null) {
                throw new IllegalStateException("Parser is not set");
            }
            return parser;
        }

        @Override
        public Maybe<Pair<A, String>> runParser(String s) {
            return parser.runParser(s);
        }
    }
}
