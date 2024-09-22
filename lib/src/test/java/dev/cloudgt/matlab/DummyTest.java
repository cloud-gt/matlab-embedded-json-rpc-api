package dev.cloudgt.matlab;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DummyTest {

    @Test
    void name() {
        assertEquals("hello", Hello.call());

    }
}
