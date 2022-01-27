package io.geekya.meow;

@FunctionalInterface
public interface IParser<T> {
    Maybe<Pair<T, String>> runParser(String s);
}
