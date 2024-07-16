package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import sn.ept.git.seminaire.poc.demo.calculator.Calculator;
import sn.ept.git.seminaire.poc.demo.exception.DivisionByZeroException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
class CJUnitMatchersTest {

    public static final String GIT_EPT = "GIT EPT";


    @Test
    void shouldBeTrue() {
        boolean result = GIT_EPT.startsWith("G");
        assertTrue(result);

        result = GIT_EPT.endsWith("T");
        assertTrue(result, "Should ends with T");

    }


    @Test
    void shouldBeFalse() {
        boolean result = GIT_EPT.startsWith("P");
        assertFalse(result, "should not start with P");
    }

    @Test
    void shouldBeNull() {
        final Object o = null;
        assertNull(o);
    }

    @Test
    void shouldNotBeNull() {
        Object o = new Object();
        assertNotNull(o);
    }


    @Test
    void shouldBeEqual() {
        final Integer actual = 9;
        final Integer expected = 3 * 3;
        assertEquals(expected, actual);
    }


    @Test
    void whenAssertingEqualityWithDelta_thenEqual() {
        final float square = 2 * 2; 
        final float rectangle = 3 * 2; 
        final float delta = 2;
        assertEquals(square, rectangle, delta);
    }


    @Test
    void shouldNotBeEqual() {
        final Integer actual = 9;
        final Integer expected = 8;
        assertNotEquals(expected, actual);
    }


    @Test
    void shouldReferToSameObject() {
        final Object actual = new Object();
        final Object expected = actual;
        assertSame(expected, actual);
    }

    @Test
    void shouldNotReferToSameObject() {
        final Object actual = new Object();
        final Object expected = new Object();
        assertNotSame(expected, actual);
    }


    @Test
    void shouldContainSameIntegers() {
        final int[] actual = new int[]{2, 5, 7};
        final int[] expected = new int[]{2, 5, 7};
        assertArrayEquals(expected, actual);
    }


    @Test
    void shouldContainSameElements() {
        final List<Integer> first = Arrays.asList(1, 2, 3);
        final List<Integer> second = Arrays.asList(1, 2, 3);
        assertIterableEquals(first, second);
    }

    @Test
    void shouldThrowCorrectException() {
        assertThrows(
                DivisionByZeroException.class,
                () -> new Calculator().divide(0, 0)
        );
    }


    @Test
    void shouldNotThrowException() {
        assertDoesNotThrow(
                () -> new Calculator().divide(1, 1)
        );
    }


    @Test
    void shouldReturnCorrectMessageBeforeTimeoutIsExceeded() {
        final String VALUE = "Hello World!";

        assertTimeout(
                Duration.ofMillis(1000),
                () -> {
                    List<Integer> data = new ArrayList<>();
                    IntStream.range(0, 1000000)
                            .forEach(data::add);
                    Awaitility
                            .await()
                            .atMost(800, TimeUnit.MILLISECONDS)
                            .until(() -> data.size() > 10000);

                    return VALUE;
                }
        );

    }


    @Test
    void whenAssertingEqualityListOfStrings_thenEqual() {
        List<String> actual = Arrays.asList("GIT", "11", "JUnit", "+221762236160");

        List<String> expected = Arrays.asList("[a-zA-Z]+", "[0-9]+", "JUnit", "^(\\+221|00221)?(33|78|77|76|75|70)[0-9]{7}$");

        assertLinesMatch(expected, actual);
    }

}
