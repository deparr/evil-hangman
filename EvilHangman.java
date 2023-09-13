import hangman.EvilHangmanGame;
import hangman.GuessAlreadyMadeException;
import java.io.File;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.SortedSet;

public class EvilHangman {

    public static void main(String[] args) {
        String dictFilename = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);
        if (guesses < 0) {
            System.out.println("Need non-zero value for guesses (argv[2])");
            return;
        }
        File dict = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + dictFilename);

        // Initialize game using given dictionary file
        EvilHangmanGame hangman = new EvilHangmanGame();
        try {
            hangman.startGame(dict, wordLength);
        }
        catch (Exception e) {
            System.out.println(e);
            return;
        }

        System.out.printf("\n\nWelcome to Totally Fair Hangman!\nWord length is %d, and you have %d guesses!\n\n", wordLength, guesses);

        Scanner sc = new Scanner(System.in);
        StringBuilder curWord = new StringBuilder(wordLength);
        curWord.replace(0, curWord.length(), "_".repeat(wordLength));
        SortedSet<Character> guessedChars;
        Set<String> possibleWords = null;
        boolean isWin = true;
        while (guesses > 0) {
            guessedChars = hangman.getGuessedLetters();
            System.out.printf("You have %d guesses left\n", guesses);
            System.out.println("Guessed letters: " + guessedChars);
            System.out.printf("Word: " + curWord + "\n");


            String guess = sc.next();
            guess = guess.trim();
            if (guess.length() > 1 || !Character.isAlphabetic(guess.charAt(0))) {
                System.out.println("Invalid guess: '" + guess + "', guess again");
            }
            else {
                try {
                    possibleWords = hangman.makeGuess(guess.charAt(0));

                    boolean guessInWord = false;
                    for (String word : possibleWords) {
                        for (int i = 0; i < word.length(); i++) {
                            if (word.charAt(i) == guess.charAt(0)) {
                                curWord.replace(i, i + 1, guess);
                                guessInWord = true;
                            }
                        }
                        break;
                    }
                    if (!guessInWord) guesses--;
                }
                catch (GuessAlreadyMadeException e) {
                    guess = guess.toLowerCase();
                    System.out.println("You've already guessed '" + guess + "', guess again!");
                    continue;
                }

                isWin = true;
                for (int i = 0; i < curWord.length(); i++) {
                    if (curWord.charAt(i) == '_') {
                        isWin = false;
                    }
                }
                if (isWin) {
                    break;
                }
            }
        }

        if (isWin) {
            System.out.println("Word: " + curWord);
            System.out.println("Hey you actually got it!");
        }
        else {
            System.out.println("No more guesses, you lost!");
            for (String word : possibleWords) {
                System.out.println("The word was: " + word);
                break;
            }
        }

        sc.close();
        return;
    }

}
