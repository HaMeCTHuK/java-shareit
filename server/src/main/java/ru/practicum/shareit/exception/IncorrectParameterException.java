package ru.practicum.shareit.exception;

public class IncorrectParameterException extends RuntimeException {
    private final String parameter;

    public String getParameter() {
        return parameter;
    }

    public IncorrectParameterException(String parameter) {
        this.parameter = parameter;
    }
}