package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangmanGame implements IEvilHangmanGame{

    private Set<String> wordSet;
    private SortedSet<Character> guessedChars;
    private int wordLength;

    public EvilHangmanGame() {
        wordSet = new TreeSet<>();
        guessedChars = new TreeSet<>();
        wordLength = 0;
    }


    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);
        if (guessedChars.contains(guess)) {
            throw new GuessAlreadyMadeException();
        }
        guessedChars.add(guess);

        Map<String, Set<String>> wordSubSets = new HashMap<>();
        for (String word : wordSet) {
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess) {
                    key.append(1);
                }
                else {
                    key.append(0);
                }
            }

            // Add word to the correct subset
            if (wordSubSets.get(key.toString()) == null) {
                wordSubSets.put(key.toString(), new TreeSet<>());
            }
            wordSubSets.get(key.toString()).add(word);
        }
        StringBuilder defKey = new StringBuilder(wordLength);
        defKey.replace(0, defKey.length(), "0".repeat(wordLength));
        Set<String> keySet = wordSubSets.keySet();
        Set<String> maxSet = wordSubSets.get(defKey.toString());
        Set<String> tieBreak = new TreeSet<>();
        for (String key : keySet) {
            if (maxSet == null) {
                maxSet = wordSubSets.get(key);
                defKey = new StringBuilder(key);
            }
            if (wordSubSets.get(key).size() > maxSet.size()) {
                maxSet = wordSubSets.get(key);
                defKey = new StringBuilder(key);
            }
            else if(wordSubSets.get(key).size() == maxSet.size() && wordSubSets.get(key) != maxSet) {
                tieBreak.add(defKey.toString());
                tieBreak.add(key);
            }
        }

        // If there is a tie breaker choose based on 2 factors
        if (!tieBreak.isEmpty()) {

            String keyToChosenSet = null;
            // Choose based on how many times correct letter appears
            int indexTotal = 100;
            boolean sameLetterTotal = false;
            for (String key : tieBreak) {
                int keyTotal = 0;
                for (int i = 0; i < key.length(); i++) {
                    if (key.charAt(i) == '1') {
                        keyTotal++;
                    }
                }
                if (keyTotal < indexTotal) {
                    indexTotal = keyTotal;
                    keyToChosenSet = key;
                }
                else if (keyTotal == indexTotal && indexTotal != 100) {
                    sameLetterTotal = true;
                }
            }

            if (sameLetterTotal) {
                // If same, Choose tiebreaker based on correct letter position
                indexTotal = 0;
                for (String key : tieBreak) {
                    int keyTotal = 0;
                    for (int i = 0; i < key.length(); i++) {
                        if (key.charAt(i) == '1') {
                            keyTotal += i;
                        }
                    }
                    if (keyTotal >  indexTotal) {
                        indexTotal = keyTotal;
                        keyToChosenSet = key;
                    }
                }
            }

            wordSet = wordSubSets.get(keyToChosenSet);
        }
        else {
            // No tiebreaker
            wordSet = maxSet;
        }
        // Return new wordset
        return wordSet;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedChars;
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        // Clear sets for new game
        wordSet.clear();
        guessedChars.clear();
        this.wordLength = wordLength;

        if (!dictionary.exists() || !dictionary.canRead()) {
            throw new IOException("File does not exist or cannot be read");
        }
        if (dictionary.length() == 0) {
            throw new EmptyDictionaryException("Given dictionary file is empty");
        }


            Scanner sc = new Scanner(dictionary);
            while (sc.hasNext()) {
                String nextWord = sc.next();
                if (nextWord.length() == wordLength) {
                    wordSet.add(nextWord);
                }
            }
            if (wordSet.isEmpty()) {
                sc.close();
                throw new EmptyDictionaryException("No words of length " + wordLength + " found in file");
            }
            sc.close();
    }
}
