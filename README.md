# Evil Hangman
A hangman game that actively cheats by switching the target word as the player guesses. After a guess is made, a new possible word set is computed and the word is 'switched'.  
It is possible to win.

To compile and run:
```sh
javac ./*.java
java EvilHangman 5 ./dict.txt
```
Where `5` is the length of the target word and `dict.txt` is the word list file, one word per line
