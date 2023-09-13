# Evil Hangman
A hangman game that actively cheats by switching the target word as the player guesses. After a guess is made, a new possible word set is computed and the word is 'switched'.  
It is possible to win.

To compile and run:
```sh
javac ./*.java
java EvilHangman ./dict.txt
```
Where `dict.txt` is the total word bank, one word per line
