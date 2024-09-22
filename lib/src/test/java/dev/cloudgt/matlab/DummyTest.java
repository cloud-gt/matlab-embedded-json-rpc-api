package dev.cloudgt.matlab;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DummyTest {

    @Test
    void name() {
        assertThat(Hello.call()).isEqualTo("hello");
    }
}
