import java.nio.file.Path;
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
    public static int fileCounter = 2;
    public static String[] fileNames = new String[fileCounter()];
    public static boolean isNewFile = false;
    public static boolean newFile(){

        return isNewFile;
    }
    public static int fileCounter(){

        return fileCounter;
    }
    public static String[] fileNames(){

        return fileNames;
    }
    //TODO get fileCounter form config file
    //TODO get all fileNames form config/fileNames file
    public static void boot() throws IOException, InterruptedException {
        fileNames[0] = "resetLibrary.txt";
        fileNames[1] = "currentLibrary.txt";
        try{
            Path newFilePath = Paths.get(fileNames[0]);
            Files.createFile(newFilePath);
            newFilePath = Paths.get(fileNames[1]);
            Files.createFile(newFilePath);
        } catch (IOException e) {
        }
        clearConsole();
        prepFile();
        System.out.println("Initializing: ");
        converter.convert();
    }
    //TODO add config file
    //TODO (add fileNames file)
    public static void makeFile( String newFile) throws InterruptedException, IOException {
        Path newFilePath = Paths.get(newFile + ".txt");
        Files.createFile(newFilePath);
        String newFileName = newFile + ".txt";
        System.out.println("New File \"" + newFile + "\" added");
        System.out.println("3");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("2");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("1");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Saving to new File!");
        System.out.println(newFileName);
        converter.readToNewFile(newFileName);
    }
    public static void prepFile() throws IOException {
        FileWriter writer = new FileWriter(fileNames[1], true);
        BufferedWriter bwriter = new BufferedWriter(writer);
        bwriter.newLine();
    }
    public static void menu() throws IOException, InterruptedException {
        clearConsole();
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
                clearConsole();
                System.out.println("Write words you want to add to my repertoire!");
                System.out.println("Type \"/help\" for more infos!");
                System.out.println();
                System.out.println();
                writeIntoFile();
                break;
            case 3:
                System.out.println("Are you sure?");
                System.out.print("[y/n]: ");
                String choice = scan.next();
                if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                    System.out.println("Thanks for playing");
                    end();
                }
                menu();
                break;
            default:
                menu();
        }
    }
    public static void writeIntoFile() throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter word: ");
        String input = scan.nextLine();
        char[] wordToWrite = input.toCharArray();
        if(wordToWrite[0] == '/'){
            commands(scan, input);
        }
        for(int i = 0; i < wordToWrite.length; i++){
            if(wordToWrite[i] > 122 || wordToWrite[i] < 97){
                System.out.println("Please just use lowercase letters and no special characters!");
                System.out.println();
                writeIntoFile();
            }
        }
        if(checkForDuplicates(input) == 0) {
            FileWriter writer = new FileWriter(fileNames[1], true);
            BufferedWriter bwriter  = new BufferedWriter(writer);
            bwriter.write(input);
            bwriter.newLine();
            bwriter.close();
            writeIntoFile();
        }else if(checkForDuplicates(input) == 1){
            System.out.println("This word is already in my repertoire!");
            System.out.println();
            writeIntoFile();
        }
        writeIntoFile();
    }
    public static void hangman() throws IOException, InterruptedException {
        clearConsole();
        Scanner scan = new Scanner(System.in);

        int stop = 0,  numberOfUnderscores = 0, failedTrys = 0;
        StringBuilder builder = new StringBuilder();
        System.out.println("Play with friends?");
        System.out.println("[y/n]");
        String choice = scan.next();
        String word = null, guessedLetters;
        if(Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")) {
            word = readRandomFromFile();
        }else if (Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            word = pickWord();
            writeIntoFilePassive(word);
        }else{
            System.out.println("Please select your gamemode!");
            hangman();
        }
        clearConsole();
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
    public static String pickWord() throws IOException, InterruptedException { //include /exit clause
        Scanner scan = new Scanner(System.in);
        System.out.print("Exit via /exit");
        System.out.print("Word: ");
        String word = scan.next();
        if(Objects.equals(word, "/exit")){
            System.out.println("Do you want to stop?");
            System.out.print("[y/n]: ");
            String choice = scan.nextLine();
            if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                menu();
            }else{
                pickWord();
            }
        }
        System.out.println("Is this \"" + word + "\" your word?" );
        System.out.print("[y/n] ");
        String choice = scan.next();
        if(Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")) {
            pickWord();
        }else if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            return word;
        }else {
            System.out.println("Please select yes or no!");
        }
        return word;
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
        System.out.print("[y/n] ");
        String choice = scan.next();
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")) {
            playAgain();
        }
        menu();
    }
    public static void lost(String word) throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.println("You lost, mabey next time!");
        System.out.println("The word was: " + word);
        System.out.println();
        System.out.println("Would you like to play again?");
        System.out.print("[y/n] ");
        String choice = scan.next();
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            playAgain();
        }
        menu();
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
    public static void playAgain() throws IOException, InterruptedException {
        clearConsole();
        hangman();
    }
    public static void clearConsole(){
        for(int i = 0; i < Math.pow(2,18); i++){
            System.out.println();
        }
    }
    public static void commands(Scanner scan, String input) throws IOException, InterruptedException {
        if(Objects.equals(input, "/exit")){
            System.out.println("Do you want to stop entering words?");
            System.out.print("[y/n]: ");
            String choice = scan.nextLine();
            if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                menu();
            }else{
                writeIntoFile();
            }
        }
        if(Objects.equals(input, "/loadNewData")){
            System.out.println("Do you want to load new data?");
            System.out.print("[y/n]: ");
            String choice = scan.nextLine();
            if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                converter.lowercase();
            }else{
                writeIntoFile();
            }
        }
        if(Objects.equals(input, "/resetData")){
            System.out.println("Do you want to reset data?");
            System.out.print("[y/n]: ");
            String choice = scan.nextLine();
            if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                converter.reset();
            }else{
                writeIntoFile();
            }
        }
        if(Objects.equals(input, "/saveCurrentLibrary")){
            System.out.println("Do you want to save current word library?");
            System.out.print("[y/n]: ");
            String choice = scan.nextLine();
            saveLibrary(choice, scan);
        }
        if(Objects.equals(input, "/help")){
            System.out.println();
            System.out.println();
            System.out.println("Possible commands:");
            System.out.println("/exit : Exit to menu.");
            System.out.println("/loadNewData : Loads new words from source file.");
            System.out.println("/resetData : Resets active library to match source file.");
            System.out.println("/saveCurrentLibrary : Saves current library of word to new file.");
            System.out.println("/help : Opens help menu.");
        }
        writeIntoFile();
    }
    public static void saveLibrary(String choice, Scanner scan) throws IOException, InterruptedException {
        String newFile;
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            System.out.print("Choose a name: ");
            choice = scan.nextLine();
            char[] newFileName = choice.toCharArray();
            for(int count = 0; count < fileNames.length; count++){
                if(Objects.equals(choice, fileNames[count])){
                    System.out.print("This library already exists.");
                    saveLibrary(choice, scan);
                } else{
                    for(int i = 0; i < newFileName.length; i++){
                        if(newFileName[i] > 122 || newFileName[i] < 97){
                            System.out.println("Please just use lowercase letters and no special characters!");
                            System.out.println();
                            saveLibrary(choice, scan);
                        }
                    }
                    fileCounter = fileNames.length + 1;
                    updateFileNames();
                    newFile = choice;
                    isNewFile = true;
                    makeFile(newFile);
                }
            }
        }else{
            writeIntoFile();
        }
    }
    public static long countLines() throws IOException {
        long lines = 0;
        BufferedReader reader = new BufferedReader(new FileReader(fileNames[1]));
            while(reader.readLine() != null){
                lines++;
            }
        return lines;
    }
    public static String readRandomFromFile() throws IOException {
        int n = (int) randomNumber();
        String line = null;
        Stream<String> lines = Files.lines(Paths.get(fileNames[1]));
            line = lines.skip(n).findFirst().get();
            System.out.println(line);

        return line;
    }
    public static double randomNumber() throws IOException {
        int low = 0, random;
        long high = countLines();
        Random rand = new Random();
        random =  rand.nextInt((int) high) + low;
        return random;
    }
    public static int checkForDuplicates(String input) throws IOException {
        String line;
        int duplicate = 0;
        for(int n = 0; n < (int) countLines(); n++) {
            line = Files.readAllLines(Paths.get(fileNames[1])).get(n);
            assert line != null;
            if(line.equals(input)){
                duplicate = 1;
                return duplicate;
            }
        }
        return duplicate;
    }
    public static void writeIntoFilePassive(String input) throws IOException {
        char[] wordToGuess = input.toCharArray(), wordGuessed = new char[wordToGuess.length];
        for(int i = 0; i < wordToGuess.length; i++){
            if(wordToGuess[0] > 96 && wordToGuess[0] < 123){
                if(!Objects.equals(input, "ext") && checkForDuplicates(input) == 0) {
                    FileWriter writer = new FileWriter(fileNames[1], true);
                    BufferedWriter bwriter = new BufferedWriter(writer);
                    bwriter.write(input);
                    bwriter.newLine();
                    bwriter.close();
                }
            }
        }
    }
    public static void updateFileNames(){
        String[] temp = new String[fileNames.length + 1];
        for(int i = 0; i < fileNames.length; i++){
            temp[i] = fileNames[i];
        }
        fileNames = temp;
    }
    public static void end(){
        System.exit(0);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        boot();
        menu();
    }
}
