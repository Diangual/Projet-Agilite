package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

@Slf4j
class FTDDTest {



    @ParameterizedTest
    @MethodSource("argumentsSumOfSquaresOfEvenNumbers")
    void shouldReturnSumOfSquaresOfEvenNumbers(
            List<Integer> values, Integer expected
    ) {
        assertThat(Validator.sumOfSquaresOfEvenNumbers(values)).isEqualTo(expected);

    }

    static Stream<Arguments> argumentsSumOfSquaresOfEvenNumbers() {
        return Stream.of(
                of(List.of(0,1,2,11,3),   4),
                of(List.of(1,2), 4),
                of(List.of(4,2), 20),
                of(List.of(-1,-2,-4,-3),20),
                of(List.of(2),4),
                of(List.of(),0),
                of(List.of(-1),0),
                of(List.of(1,2,4,6),56)
        );
    }






}
