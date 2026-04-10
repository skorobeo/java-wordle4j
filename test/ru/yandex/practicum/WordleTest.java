package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        List<String> words = Arrays.asList(
                "гашиш",
                "гейша",
                "гевея",
                "ткань",
                "ранец",
                "комод"
        );

        dictionary = new WordleDictionary(words);
    }

    @Test
    void testNormalize() {
        assertEquals("гашиш", WordleDictionary.normalize("Гашиш"));
        assertEquals("гашиш", WordleDictionary.normalize("ГАШИШ"));
        assertEquals("гашиш", WordleDictionary.normalize("гашиш"));
    }

    @Test
    void testContains() {
        assertTrue(dictionary.contains("гашиш"));
        assertTrue(dictionary.contains("Гашиш"));
        assertTrue(dictionary.contains("ГАШИШ"));
        assertFalse(dictionary.contains("амеба"));
    }

    @Test
    void testGetRandomWord() {
        String word = dictionary.getRandomWord();
        assertNotNull(word);
        assertEquals(5, word.length());
    }

    @Test
    void testDictionarySize() {
        assertEquals(6, dictionary.size());
    }

    @Test
    void testCheckWordExactMatch() {
        WordleDictionary dict = new WordleDictionary(Arrays.asList("гашиш", "гейша", "гевея"));
        WordleGame game = new WordleGame(dict);
        String result = game.checkWord("гашиш", "гашиш");
        assertEquals("+++++", result);
    }

    @Test
    void testCheckWordNoMatch() {
        WordleDictionary dict = new WordleDictionary(Arrays.asList("гашиш", "гейша", "гевея"));
        WordleGame game = new WordleGame(dict);
        String result = game.checkWord("комод", "гашиш");
        assertTrue(result.contains("-"));
    }

    @Test
    void testCheckWordPartialMatch() {
        WordleDictionary dict = new WordleDictionary(Arrays.asList("гашиш", "гейша", "гевея"));
        WordleGame game = new WordleGame(dict);
        String result = game.checkWord("гейша", "гашиш");
        assertTrue(result.contains("+"));
    }

    @Test
    void testMakeTurnValidWord() throws WordNotFoundInDictionaryException {
        WordleDictionary dict = new WordleDictionary(Arrays.asList("гашиш", "гейша", "гевея"));
        WordleGame game = new WordleGame(dict);
        String result = game.makeTurn("гашиш");
        assertEquals("гашиш", result);
    }

    @Test
    void testMakeTurnInvalidWord() throws WordNotFoundInDictionaryException {
        WordleDictionary dict = new WordleDictionary(Arrays.asList("гашиш", "гейша", "гевея"));
        WordleGame game = new WordleGame(dict);
        assertThrows(WordNotFoundInDictionaryException.class, () -> game.makeTurn("амеба"));
    }

    @Test
    void testGameWin() throws WordNotFoundInDictionaryException {
        WordleDictionary dict = new WordleDictionary(Arrays.asList("аура", "гашиш", "гейша"));
        WordleGame game = new WordleGame(dict);
        String target = game.getTargetWord();
        game.makeTurn(target);
        assertTrue(game.isWon());
        assertFalse(game.isLost());
        assertTrue(game.isGameOver());
    }

    @Test
    void testGameLose() throws WordNotFoundInDictionaryException {
        WordleDictionary dict = new WordleDictionary(Arrays.asList(
                "гашиш", "гейша", "гевея", "ткань"
        ));
        WordleGame game = new WordleGame(dict);
        for (int i = 0; i < 6; i++) {
            game.makeTurn("гейша");
        }
        assertFalse(game.isWon());
        assertTrue(game.isLost());
        assertTrue(game.isGameOver());
    }

    @Test
    void testRemainingAttempts() throws WordNotFoundInDictionaryException {
        WordleDictionary dict = new WordleDictionary(Arrays.asList("гашиш", "гейша"));
        WordleGame game = new WordleGame(dict);
        assertEquals(6, game.getRemainingAttempts());
        game.makeTurn("гашиш");
        assertEquals(5, game.getRemainingAttempts());
    }

    @Test
    void testHint() {
        WordleDictionary dict = new WordleDictionary(Arrays.asList("гашиш", "гейша", "гевея", "ранец"));
        WordleGame game = new WordleGame(dict);
        String hint = game.getHint();
        assertNotNull(hint);
        assertEquals(5, hint.length());
    }
}