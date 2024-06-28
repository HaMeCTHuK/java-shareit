package ru.practicum.shareit.exceptions.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.BookingIsNotAvailableException;
import ru.practicum.shareit.exceptions.DataNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataDataNotFoundException(final DataNotFoundException e) {
        log.error("DataNotFoundException: ", e);
        return new ErrorResponse(String.format("Ошибка: %s", e.getParameter()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingIsNotAvailableException(final BookingIsNotAvailableException e) {
        log.error("BookingIsNotAvailableException: ", e);
        return new ErrorResponse(e.getParameter());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorResponse> handleDataDataNotFoundException(final MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e);

        return e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorResponse(fieldError.getDefaultMessage(), fieldError.getField()))
                .collect(Collectors.toList());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.error("BadRequestException: ", e);
        return new ErrorResponse(e.getParameter());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        log.error("IllegalArgumentException: ", e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Internal server error: ", e);
        return new ErrorResponse(e.getMessage());
    }
}
