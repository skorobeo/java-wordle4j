package ru.yandex.practicum;

import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;
import ru.yandex.practicum.exception.WordleGameException;

import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    private static final String LOG_FILE = "word.log";

    private final WordleDictionary dictionary;
    private final WordleGame game;
    private final Scanner scanner;
    private final PrintWriter log;

    public Wordle(WordleDictionary dictionary, WordleGame game, PrintWriter log) {
        this.dictionary = dictionary;
        this.game = game;
        this.log = log;
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {

        PrintWriter log;

        try {
            log = new PrintWriter(LOG_FILE);
        } catch (Exception e) {
            System.out.println("Не удалось создать лог.");
            log = new PrintWriter(System.out);
        }

        try {
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary = new WordleDictionary(loader.load());
            WordleGame game = new WordleGame(dictionary);
            Wordle wordle = new Wordle(dictionary, game, log);
            wordle.run();

        } catch (WordleGameException e) {
            System.out.println("Ошибка: " + e.getMessage());
            log.println("Ошибка: " + e.getMessage());
            log.flush();
        }

    }

    public void run() {
        log.println("Игра началась. Загаданное слово: " + game.getTargetWord());

        while (!game.isGameOver()) {
            System.out.println("> ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                String hint = game.getHint();
                if (hint != null) {
                    System.out.println("Подсказка: " + hint);
                    log.println("Подсказка: " + hint);
                }
                continue;
            }
            try {
                String word = WordleDictionary.normalize(input);
                if (word.length() != 5) {
                    System.out.println("Слово должно быть из 5 букв.");
                    continue;
                }
                boolean inRussia = word.matches("[а-яА-ЯёЁ]+");
                if (!inRussia) {
                    System.out.println("Слово должно быть на русском языке");
                    continue;
                }

                String result = game.makeTurn(word);

                String checkResult = game.checkWord(result, game.getTargetWord());

                System.out.println(result);
                System.out.println(checkResult);

                log.println("Введено: " + word + ", результат: " + checkResult);
            } catch (WordNotFoundInDictionaryException e) {
                System.out.println("Слово не найдено в словаре");
                log.println("Ошибка: " + e.getMessage());
            }
        }

        if (game.isWon()) {
            System.out.println("Поздравляю! Победа!");
        } else if (game.isLost()) {
            System.out.println("Игра окончена.  Вы проиграли :(");
        }

        System.out.println("Загаданное слово: " + game.getTargetWord());

        log.println("Игра завершена. Загаданное слово: " + game.getTargetWord());

        log.flush();
    }
}