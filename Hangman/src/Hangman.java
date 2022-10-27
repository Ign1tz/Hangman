import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.stream.*;

public class Hangman{
    public static String word;

    public static void menu() throws IOException, InterruptedException {
        Boot.actualNumberOfFiles();
        Utility.clearConsole();
        Boot.wordAlreadyPicked = false;
        Scanner scan = new Scanner(System.in);

        System.out.println("Menu:");
        System.out.println("Play: 1");
        System.out.println("Add words: 2");
        System.out.println("Exit: 3");
        System.out.println("Where do you want to go?");
        int input = scan.nextInt();
        switch (input){
            case 1:
                hangman();
                break;
            case 2:
                Utility.clearConsole();
                System.out.println("Write words you want to add to my repertoire!");
                System.out.println("Type \"/help\" for more infos!");
                System.out.println();
                System.out.println();
                Boot.writeIntoFile();
                break;
            case 3:
                System.out.println("Are you sure?");
                System.out.print("[y/n]: ");
                String choice = scan.next();
                if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                    System.out.println("Thanks for playing");
                    Boot.end();
                }else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "No")) {
                    menu();
                } else {
                    System.out.println("Please select yes or no!");
                }
                break;
            default:
                menu();
        }
    }

    public static void hangman() throws IOException, InterruptedException {
        Utility.clearConsole();
        Scanner scan = new Scanner(System.in);
        String guessedLetters;
        int stop = 0,  numberOfUnderscores = 0, failedTrys = 0;
        StringBuilder builder = new StringBuilder();
        if(!Boot.wordAlreadyPicked) {
            System.out.println("Play with friends?");
            System.out.print("[y/n]: ");
            String choice = scan.next();
            if (Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")) {
                pickWord();
                System.out.println("test");
            } else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "No")) {
                word = readRandomFromFile();
            } else {
                System.out.println("Please select your gamemode!");
                hangman();
            }
        }
        Boot.wordAlreadyPicked = false;
        Utility.clearConsole();
        char[] wordToGuess = word.toCharArray(), wordGuessed = new char[wordToGuess.length];
        if(wordToGuess[0] > 64 && wordToGuess[0] < 91){
            wordToGuess[0] = (char) ((int) wordToGuess[0] + 32);
        }
        for(int i = 0; i < wordToGuess.length; i++){
            wordGuessed[i] = '_';
        }
        System.out.println(wordGuessed);
        System.out.println();
        System.out.println();

        while(failedTrys != 9) {
            numberOfUnderscores = underscores(wordGuessed, numberOfUnderscores);
            if (numberOfUnderscores > 0) {
                System.out.print("Guess: ");
                char guess = scan.next().charAt(0);
                for (int place = 0; place < wordToGuess.length; place++) {
                    if (Objects.equals(guess, wordToGuess[place])) {
                        wordGuessed[place] = guess;
                        stop = 1;
                    }
                }
                System.out.println();
                System.out.println();
                System.out.println(wordGuessed);
                if (stop == 0) {
                    failedTrys++;
                    printHangman(failedTrys);
                    builder.append(guess);
                }else if(failedTrys > 0){
                    printHangman(failedTrys);
                }
                stop = 0;
                guessedLetters = builder.toString();
                System.out.println(guessedLetters);
            }else{
                won();
                System.out.println("Thanks for playing!");
                menu();
            }
            numberOfUnderscores = 0;
        }
        lost(word);
    }
    public static void pickWord() throws IOException, InterruptedException {
        boolean play;
        Scanner scan = new Scanner(System.in);
        System.out.println("Exit via /exit");
        System.out.print("Word: ");
        word = scan.next();
        if(Objects.equals(word, "/exit")){
            play = true;
            Utility.commands(scan, word, play);
        }
        System.out.println("Is \"" + word + "\" your word?" );
        System.out.print("[y/n]: ");
        String choice = scan.next();
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            Boot.wordAlreadyPicked = true;
            Utility.writeIntoFilePassive(word);
            hangman();
        }else if(Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")){
            pickWord();
        }else {
            System.out.println("Please select yes or no!");
        }
    }
    public static int underscores(char[] wordGuessed, int numberOfUnderscores){
        for(int check = 0; check < wordGuessed.length; check++){
            if(wordGuessed[check] == '_'){
                numberOfUnderscores++;
            }
        }
        return numberOfUnderscores;
    }
    public static void won() throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Congratulations, you won!");
        System.out.println();
        System.out.println("Would you like to play again?");
        playAgain(scan);
    }
    public static void lost(String word) throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.println("You lost, mabey next time!");
        System.out.println("The word was: " + word);
        System.out.println();
        System.out.println("Would you like to play again?");
        playAgain(scan);
    }
    public static void playAgain(Scanner scan) throws IOException, InterruptedException {
        System.out.print("[y/n]: ");
        String choice = scan.next();
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")) {
            Utility.clearConsole();
            hangman();
        } else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")){
            menu();
        }
        System.out.println("Please select yes or no!");
        playAgain(scan);
    }
    public static void printHangman(int failedTrys){
        switch (Math.abs(failedTrys)) {
            case 1 -> {
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println("    ____");
                System.out.println("	|  |");
            }
            case 2 -> {
                System.out.println();
                System.out.println("     |");
                System.out.println("     |");
                System.out.println("     |");
                System.out.println("   __|__");
                System.out.println("   |   |");
            }
            case 3 -> {
                System.out.println("     _______");
                System.out.println("     |");
                System.out.println("     |");
                System.out.println("     |");
                System.out.println("   __|__");
                System.out.println("   |   |");
            }
            case 4 -> {
                System.out.println("     _______");
                System.out.println("     | /");
                System.out.println("     |/");
                System.out.println("     |");
                System.out.println("   __|__");
                System.out.println("   |   |");
            }
            case 5 -> {
                System.out.println("     _______");
                System.out.println("     | /   |");
                System.out.println("     |/");
                System.out.println("     |");
                System.out.println("   __|__");
                System.out.println("   |   |");
            }
            case 6 -> {
                System.out.println("     _______");
                System.out.println("     | /   |");
                System.out.println("     |/    O");
                System.out.println("     |");
                System.out.println("   __|__");
                System.out.println("   |   |");;
            }
            case 7 -> {
                System.out.println("     _______");
                System.out.println("     | /   |");
                System.out.println("     |/    O");
                System.out.println("     |     |");
                System.out.println("   __|__");
                System.out.println("   |   |");
            }
            case 8 -> {
                System.out.println("     _______");
                System.out.println("     | /   |");
                System.out.println("     |/   \\O/");
                System.out.println("     |     |");
                System.out.println("   __|__");
                System.out.println("   |   |");
            }
            case 9 -> {
                System.out.println("     _______");
                System.out.println("     | /   |");
                System.out.println("     |/   \\O/");
                System.out.println("     |     |");
                System.out.println("   __|__  / \\");
                System.out.println("   |   |");
            }
            default -> {
            }
        }
    }

    public static String readRandomFromFile() throws IOException {
        int n = (int) Utility.randomNumber();
        String line;
        Stream<String> lines = Files.lines(Paths.get("currentLibrary.json"));
            line = lines.skip(n).findFirst().get();
        return line;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Boot.boot();
        menu();
    }
}
