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
        String sa = a == null ? "null" : a.toString();
        String sb = b == null ? "null" : b.toString();

        if (a instanceof String) {
            sa = "\"" + sa + "\"";
        } else if (a instanceof Character) {
            sa = "'" + sa + "'";
        }

        if (b instanceof String) {
            sb = "\"" + sb + "\"";
        } else if (b instanceof Character) {
            sb = "'" + sb + "'";
        }

        return "(" + sa + ", " + sb + ')';
    }
}
