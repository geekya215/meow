package io.geekya.meow;

import org.junit.jupiter.api.Test;

import static io.geekya.meow.Maybe.*;
import static org.junit.jupiter.api.Assertions.*;

public class MaybeTest {

    @Test
    public void testJust() {
        assertEquals(1, just(1).fromJust());
        assertEquals(true, just(true).fromJust());
        assertEquals("monad", just("monad").fromJust());
    }

    @Test
    public void testNoting() {
        NullPointerException e = assertThrows(NullPointerException.class, () -> nothing().fromJust());
        assertEquals("Nothing can not extract element", e.getMessage());
    }

    @Test
    public void testMaybe() {
        assertEquals(just("1"), maybe("1"));
        assertEquals(1, maybe(1).fromJust());
        assertEquals(nothing(), maybe(null));
    }

    @Test
    public void testPure() {
        assertEquals(2, maybe(1).pure(2).fromJust());
        assertEquals(just(2), nothing().pure(2));
    }

    @Test
    public void testMap() {
        assertEquals(maybe(2), maybe(1).map(a -> a + 1));
        assertEquals(maybe("21"), maybe("2").map(a -> a + "1"));
    }

    @Test
    public void testFMap() {
        assertEquals(maybe(2), maybe(1).fmap(maybe(a -> a + 1)));
        assertEquals(nothing(), maybe(1).fmap(nothing()));
        assertNotEquals(maybe(1), maybe(1).fmap(nothing()));
    }

    @Test
    public void testBind() {
        Maybe<Integer> m = maybe(1);
        assertEquals(maybe(2), m.bind(a -> m.pure(a + 1)));
        assertEquals(maybe(true), m.bind(a -> just(true)));
        assertEquals(nothing(), m.bind(a -> nothing()));
    }
}
