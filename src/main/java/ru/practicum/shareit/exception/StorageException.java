package ru.practicum.shareit.exception;

public class StorageException extends RuntimeException {

        public StorageException() {
            super();
        }

        public StorageException(String message) {
            super(message);
        }

        public StorageException(String message, Throwable cause) {
            super(message, cause);
        }

        public StorageException(Throwable cause) {
            super(cause);
        }
}

