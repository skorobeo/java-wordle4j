package ru.yandex.practicum.exception;

public class WordNotFoundInDictionaryException extends Exception {

    public WordNotFoundInDictionaryException(String message) {
        super(message);
    }
}