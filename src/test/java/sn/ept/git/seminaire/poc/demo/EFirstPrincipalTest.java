package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import sn.ept.git.seminaire.poc.demo.calculator.Calculator;
import sn.ept.git.seminaire.poc.demo.calculator.ICalculator;
import sn.ept.git.seminaire.poc.demo.exception.BadPhoneException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class EFirstPrincipalTest {


    private static ICalculator calculator;
    private static double resultOne, resultTwo;
    private double a, b;

    @BeforeAll
    static void beforeAll() {
        calculator = new Calculator();
    }

    @BeforeEach
    void beforeEach() {
        a = 11;
        b = 22;
    }



    @Nested
    class Fast {

        @RepeatedTest(1000)
        void addShouldReturnTheSumOfPositiveNumbers() {
            double result = calculator.add(b, a);
            double expected = a + b;
            assertThat(result).isEqualTo(expected);
        }
    }


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Isolation {

        @Order(0)
        @Test
        void addShouldReturnTheSumOfTwoPositiveNumbers() {
            a = 11;
            b = 22;
            resultOne = calculator.add(a, b); //resultOne =33
            assertThat(resultOne).isEqualTo(33);
        }

        @Order(1)
        @Test
        void givenTwoPositiveIntegers_whenMultiply_thenCorrectResult() {
            a = 11;
            resultOne = 33;
            resultTwo = calculator.multiply(a, resultOne);
            //resultTwo = 11*33 = 363
            assertThat(resultTwo).isEqualTo(363);
        }
    }

    @Nested
    class Repeatable {

        MyFileReader fileReader = new MyFileReader();


        @Test
        void readFileShouldReturnCorrectLines() throws IOException {

            try (MockedStatic<Files> reader = Mockito.mockStatic(Files.class)) {

                List<String> lines = Arrays.asList("Hello", "all", "from", "my", "mocked", "reader", "!");
                reader.when(() -> Files.readAllLines(ArgumentMatchers.any(Path.class)))
                        .thenReturn(lines);

                //tests doubles
                String path = "any_directory" + File.separator + "file_to_read.txt";
                List<String> result = fileReader.read(path);
                assertThat(result).isNotNull().isNotEmpty()
                        .hasSize(lines.size())
                        .containsExactlyInAnyOrderElementsOf(lines);
            }

        }


    }


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SelfValidating {

        @Test
        void addShouldReturnTheSumOfTwoPositiveNumbers() {
            a = 10;
            b = 34;
            double expected = a + b;
 
            resultTwo = calculator.add(b, a);
            assertThat(resultTwo)
                    .as("Les deux resultats doivent etre egaux")
                    .isEqualTo(expected);
        }


        @Test
        void addShouldReturnTheSumOfTwoNegativeNumbers() {
            double expected = -(a + b);
            resultTwo = calculator.add(-b, -a);
            assertThat(resultTwo).isEqualTo(expected);
        }


    }



    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ThoroughAndTimely {


        String indicatif;
        String operator;
        static String groupe7Chifffres = "9876543";
        //indicatif-code operateur-groupe de 7
        String template = "%s%s%s";
        String phone;
        Operator result;



        static Stream<Arguments> argumentsValideMobilePhone() {
            return Stream.of(
                    Arguments.of("+221", "77", Operator.ORANGE),
                    Arguments.of("+221", "78", Operator.ORANGE),
                    Arguments.of("00221", "77", Operator.ORANGE),
                    Arguments.of("00221", "78", Operator.ORANGE),
                    Arguments.of("", "77", Operator.ORANGE),
                    Arguments.of("", "78", Operator.ORANGE),

                    Arguments.of("+221", "76", Operator.FREE),
                    Arguments.of("00221", "76", Operator.FREE),
                    Arguments.of("", "76", Operator.FREE),

                    Arguments.of("+221", "70", Operator.EXPRESSO),
                    Arguments.of("00221", "70", Operator.EXPRESSO),
                    Arguments.of("", "70", Operator.EXPRESSO),

                    Arguments.of("+221", "75", Operator.PROMOBILE),
                    Arguments.of("00221", "75", Operator.PROMOBILE),
                    Arguments.of("", "75", Operator.PROMOBILE)
            );
        }


        @ParameterizedTest
        @MethodSource("argumentsValideMobilePhone")
        void getMobileOperator_shouldReturnCorrectOperator(

                String indicatif, String codeOperateur, Operator operateurCorrespondant

        ) throws BadPhoneException {
            //Arrange
            phone = template.formatted(indicatif, codeOperateur, groupe7Chifffres);
            //Act
            Operator operateur = Validator.getSnMobileOperator(phone);
            //Assert
            assertThat(operateur).isEqualTo(operateurCorrespondant);
        }


        @ParameterizedTest
        @MethodSource("argumentsInvalideMobilePhone")
        void getMobileOperator_shouldThrowException(
                String indicatif, String codeOperateur, String dernierGroupe
        ) {
            phone = template.formatted(indicatif, codeOperateur, dernierGroupe);
            //Act and Assert
            assertThrows(
                    BadPhoneException.class,
                    () -> Validator.getSnMobileOperator(phone)
            );
        }


        static Stream<Arguments> argumentsInvalideMobilePhone() {
            return Stream.of(
                    Arguments.of("+222", "70", "9876543"),
                    Arguments.of("+221", "71", "9876543"),
                    Arguments.of("+221", "70", "987"),
                    Arguments.of("+221", "70", "98765432"),
                    Arguments.of("+221", "70", "987654n"),
                    Arguments.of("+221", "", "9876543")
            );
        }


    }
}
