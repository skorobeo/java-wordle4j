package ru.yandex.practicum;

import ru.yandex.practicum.exception.WordleGameException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private static final String DEFAULT_DICTIONARY_PATH = "words_ru.txt";

    public List<String> load() throws WordleGameException {
        return load(DEFAULT_DICTIONARY_PATH);
    }

    public List<String> load(String filePath) throws WordleGameException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            throw new WordleGameException("Не удалось загрузить словарь: " + filePath, e);
        }

        return words;
    }
}