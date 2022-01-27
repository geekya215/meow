package io.geekya.meow;

public class Pair<A, B> {
    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A fst() {
        return a;
    }

    public B snd() {
        return b;
    }

    @Override
    public String toString() {
        return "(" + a + ", " + b + ')';
    }
}
