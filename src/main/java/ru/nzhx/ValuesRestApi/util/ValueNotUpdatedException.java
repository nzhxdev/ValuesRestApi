package ru.nzhx.ValuesRestApi.util;

public class ValueNotUpdatedException extends RuntimeException{
    public ValueNotUpdatedException(String message) {
        super(message);
    }
}
