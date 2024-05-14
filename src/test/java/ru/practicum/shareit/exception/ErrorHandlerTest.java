package ru.practicum.shareit.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.exception.model.ErrorResponse;

public class ErrorHandlerTest {

    @Test
    public void testHandleValidationException() {
        ValidationException exception = new ValidationException("Invalid parameter");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleValidationException(exception);
        assertEquals("Invalid parameter", response.getError());
    }

    @Test
    public void testHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        ValidationException exception1 = new ValidationException("Invalid parameter");
        when(exception.getMessage()).thenReturn("Invalid argument");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleMethodArgumentNotValidException(exception1);
        assertEquals("Ошибка валидации ", response.getError());
    }

    @Test
    public void testHandleIncorrectParameterException() {
        IncorrectParameterException exception = new IncorrectParameterException("Incorrect field");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleIncorrectParameterException(exception);
        assertEquals("Ошибка с полем \"Incorrect field\". ", response.getError());
    }

    @Test
    public void testHandlePostNotFoundException() {
        DataNotFoundException exception = new DataNotFoundException("Post not found");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handlePostNotFoundException(exception);
        assertEquals("Данные не найдены Post not found", response.getError());
    }

    @Test
    public void testHandleDataAlreadyExistException() {
        DataAlreadyExistException exception = new DataAlreadyExistException("Data already exists");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleDataAlreadyExistException(exception);
        assertEquals("Данные уже используются Data already exists", response.getError());
    }

    @Test
    public void testHandleStorageException() {
        StorageException exception = new StorageException("Storage error");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleStorageException(exception);
        assertEquals("Ошибка Storage Storage error", response.getError());
    }

    @Test
    public void testHandleThrowable() {
        Throwable exception = new RuntimeException("Unexpected error");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleThrowable(exception);
        assertEquals("Произошла непредвиденная ошибка. Unexpected error", response.getError());
    }
}
