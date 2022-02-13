package io.geekya.meow;

@FunctionalInterface
public interface Parser<T> {
    Maybe<Pair<T, String>> runParser(String s);
}
