package ru.yandex.practicum;

import ru.yandex.practicum.exception.WordleGameException;

import java.util.*;

public class WordleDictionary {

    private List<String> words;
    private final Random random;

    public WordleDictionary(List<String> rawWords) throws WordleGameException {
        if (rawWords == null || rawWords.isEmpty()) {
            throw new WordleGameException("Словарь пуст");
        }

        this.words = filterAndNormalize(rawWords);
        this.random = new Random();

        if (this.words.isEmpty()) {
            throw new WordleGameException("Нет слов с 5 буквами");
        }
    }

    private List<String> filterAndNormalize(List<String> rawWords) {
        List<String> filtered = new ArrayList<>();

        for (String word : rawWords) {
            String normalized = normalize(word);

            if (normalized.length() == 5) {
                filtered.add(normalized);
            }
        }
        return filtered;
    }

    public static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.toLowerCase().replace('ё', 'е');
    }

    public boolean contains(String word) {
        return words.contains(normalize(word));
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    public int size() {
        return words.size();
    }

    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }
}