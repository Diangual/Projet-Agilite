package sn.ept.git.seminaire.cicd.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import sn.ept.git.seminaire.cicd.ReplaceCamelCase;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.models.ErrorModel;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceCamelCase.class)
class TodoExceptionHandlerTest {

    @Mock
    private WebRequest request;
    private static final String DESC = "request description";
    private static final String MSG = "error message";
    private ResponseEntity<ErrorModel> response;
    private final TodoExceptionHandler handler = new TodoExceptionHandler();


    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;


    @Mock
    private FieldError fieldError;

    @BeforeEach
    void setUp() {
        response = null;
        Mockito
                .when(request.getDescription(Mockito.anyBoolean()))
                .thenReturn(DESC);
    }

    @Test
    void testHandleNotFound() {
        ItemNotFoundException exception = new ItemNotFoundException(MSG);
        response = handler.notFound(exception, request);
        verifyFormattedError(response, HttpStatus.NOT_FOUND, MSG, DESC);
    }

    @Test
    void testHandleConflict() {
        ItemExistsException exception = new ItemExistsException(MSG);
        response = handler.conflict(exception, request);
        verifyFormattedError(response, HttpStatus.CONFLICT, MSG, DESC);
    }


    @ParameterizedTest
    @MethodSource("argumentsOthersExceptions")
    void testOthersExceptions(Exception exception) {
        response = handler.othersExceptions(exception, request);
        verifyFormattedError(response, HttpStatus.INTERNAL_SERVER_ERROR, MSG, DESC);
    }


    public static Stream<Arguments> dataResponseStatusException() {
        return Stream.of(
                of(500),
                of(504),
                of(403)
        );
    }

    @MethodSource("dataResponseStatusException")
    @ParameterizedTest
    void testResponseStatusException(int status) {
        ResponseStatusException exception = new ResponseStatusException(HttpStatusCode.valueOf(status));
        response = handler.responseStatusException(exception, request);
        verifyFormattedError(response, HttpStatus.resolve(status), exception.getMessage(), DESC);
    }

    public static Stream<Arguments> argumentsOthersExceptions() {
        return Stream.of(
                of(new Exception(MSG)),
                of(new RuntimeException(MSG))
        );
    }

    @ParameterizedTest
    @MethodSource("argumentFieldsErrors")
    void testInvalideArgument(String field, String message) {

        Mockito.when(fieldError.getField())
                .thenReturn(field);
        Mockito.when(fieldError.getDefaultMessage())
                .thenReturn(message);

        Mockito.when(methodArgumentNotValidException.getAllErrors())
                .thenReturn(List.of(fieldError));

        response = handler.invalideArgument(methodArgumentNotValidException, request);
        verifyFormattedError(response, HttpStatus.BAD_REQUEST, TodoExceptionHandler.TEMPLATE_CHAMP_EN_ERREUR.formatted(field, message), DESC);
    }


    @ParameterizedTest
    @MethodSource("argumentFieldsErrors")
    void testInvalideArgumentWithErrorNotInstanceFieldError(String field, String message) {
        Mockito.when(methodArgumentNotValidException.getAllErrors())
                .thenReturn(List.of(new ObjectError(field, message)));
        response = handler.invalideArgument(methodArgumentNotValidException, request);
        verifyFormattedError(response, HttpStatus.BAD_REQUEST, message, DESC);
    }



    public static Stream<Arguments> argumentFieldsErrors() {
        return Stream.of(
                of("title", "must be between 2 and 8"),
                of("title", "must be between 2 and 8"),
                of("title", "must not be blank")
        );
    }

    private void verifyFormattedError(final ResponseEntity<ErrorModel> response, final HttpStatus httpStatus, final String message) {
        Predicate<? super Object> messagePredicate = msg -> MSG.toString().contains(message);
        Predicate<? super Object> datePredicate = date -> {
            LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
            LocalDateTime errorDate = (LocalDateTime) date;
            return Math.abs(now.toEpochSecond(ZoneOffset.UTC) - errorDate.toEpochSecond(ZoneOffset.UTC)) <= 10;
        };

        assertThat(response)
                .isNotNull()
                .extracting("status")
                .isExactlyInstanceOf(HttpStatus.class)
                .isEqualTo(httpStatus);

        ErrorModel body = response.getBody();

        assertThat(body)
                .isNotNull();

        assertThat(body)
                .extracting("status")
                .isExactlyInstanceOf(Integer.class)
                .isEqualTo(httpStatus.value());


        assertThat(body)
                .extracting("message")
                .isExactlyInstanceOf(String.class)
                .matches(messagePredicate);

        assertThat(body)
                .extracting("date")
                .isExactlyInstanceOf(LocalDateTime.class)
                .matches(datePredicate);

    }

    @Test
    void testValidationException() {
        ValidationException exception = new ValidationException(MSG);
        response = handler.violation(exception, request);
        verifyFormattedError(response, HttpStatus.BAD_REQUEST, MSG, DESC);
    }


}
