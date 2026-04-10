package ru.yandex.practicum;

import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.util.*;

public class WordleGame {

    private static final int MAX_ATTEMPTS = 6;

    private WordleDictionary dictionary;
    private final String targetWord;
    private int remainingAttempts;
    private boolean isWon;
    private boolean isLost;

    private final List<String> history;
    private final List<String> hints;
    private final Set<String> usedHints;

    private final Random random;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.targetWord = dictionary.getRandomWord();
        this.remainingAttempts = MAX_ATTEMPTS;

        this.isWon = false;
        this.isLost = false;

        this.history = new ArrayList<>();
        this.hints = new ArrayList<>();
        this.random = new Random();
        this.usedHints = new HashSet<>();
    }

    public String makeTurn(String word) throws WordNotFoundInDictionaryException {
        String normalized = WordleDictionary.normalize(word);

        if (!dictionary.contains(normalized)) {
            throw new WordNotFoundInDictionaryException("Слово " + word + " не найдено в словаре.");
        }

        remainingAttempts--;
        history.add(normalized);

        if (normalized.equals(targetWord)) {
            isWon = true;
        } else if (remainingAttempts == 0) {
            isLost = true;
        }

        return normalized;
    }

    public String checkWord(String inputWord, String target) {
        String input = WordleDictionary.normalize(inputWord);
        String targetNormalized = WordleDictionary.normalize(target);

        if (input.length() != 5 || targetNormalized.length() != 5) {
            throw new IllegalArgumentException("Слово должно быть длиной 5");
        }

        char[] result = new char[5];
        boolean[] targetUsed = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (input.charAt(i) == targetNormalized.charAt(i)) {
                result[i] = '+';
                targetUsed[i] = true;
            } else {
                result[i] = '-';
            }
        }

        for (int i = 0; i < 5; i++) {
            if (result[i] == '-') {
                char c = input.charAt(i);

                boolean found = false;
                for (int j = 0; j < 5; j++) {
                    if (!targetUsed[i] && targetNormalized.charAt(j) == c) {
                        result[i] = '^';
                        targetUsed[j] = true;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    result[i] = '-';
                }
            }
        }

        return new String(result);
    }

    public String getHint() {
        List<String> candidates = new ArrayList<>();

        for (String word : dictionary.getWords()) {
            if (!history.contains(word) && !usedHints.contains(word)) {
                if (isValidWord(word)) {
                    candidates.add(word);
                }
            }
        }

        if (candidates.isEmpty()) {
            for (String word : dictionary.getWords()) {
                if (!history.contains(word) && !usedHints.contains(word)) {
                    candidates.add(word);
                }
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        String hint = candidates.get(random.nextInt(candidates.size()));
        usedHints.add(hint);
        hints.add(hint);

        return hint;
    }

    private boolean isValidWord(String word) {
        for (String attempt : history) {
            String hint = checkWord(attempt, targetWord);

            for (int i = 0; i < 5; i++) {
                if (hint.charAt(i) == '+') {
                    if (word.charAt(i) != attempt.charAt(i)) {
                        return false;
                    }
                } else if (hint.charAt(i) == '^') {
                    boolean found = false;
                    for (int j = 0; j < 5; j++) {
                        if (word.charAt(j) == attempt.charAt(i) && hint.charAt(j) != '+') {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return false;
                    }
                } else if (hint.charAt(i) == '-') {
                    for (int j = 0; j < 5; j++) {
                        if (word.charAt(j) == attempt.charAt(i)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public boolean isWon() {
        return isWon;
    }

    public boolean isLost() {
        return isLost;
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    public List<String> getHints() {
        return hints;
    }

    public boolean isGameOver() {
        return isWon || isLost;
    }
}