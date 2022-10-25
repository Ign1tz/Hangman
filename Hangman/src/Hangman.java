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
    public static int numberOfFiles() throws IOException {
        int numberOfFiles;
        Stream<String> lines = Files.lines(Paths.get("settings.json"));
         numberOfFiles = Integer.parseInt(lines.skip(4).findFirst().get());
        return numberOfFiles;
    }
    public static int actualNumberOfFiles() throws IOException {
        int actualNumberOfFiles = numberOfFiles();
        return actualNumberOfFiles;
    }
    public static int activeFile() throws IOException {
        int activeFile = numberOfFiles() - 2;
        return activeFile;
    }
    public static String[] fileNames;

    static {
        try {
            fileNames = new String[numberOfFiles()];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isNewFile = false, wordAlreadyPicked;
    public static void boot() throws IOException, InterruptedException {
        numberOfFiles();
        activeFile();
        addFileNames();
        /*fileNames[0] = "settings.json";
        fileNames[1] = "resetLibrary.json";
        fileNames[2] = "currentLibrary.json";*/
        try{
            Path newFilePath = Paths.get(fileNames[0]);
            Files.createFile(newFilePath);
            newFilePath = Paths.get(fileNames[1]);
            Files.createFile(newFilePath);
            newFilePath = Paths.get(fileNames[2]);
            Files.createFile(newFilePath);
        } catch (IOException e) {
        }
        clearConsole();
        prepFile();
        System.out.println("Initializing: ");
        converter.convert();
    }
    public static void addFileNames() throws IOException {
        int count = 1, linesSettings = (int) countLinesSettings(), numberOfFiles = numberOfFiles();
        String temp;
        while(count <= numberOfFiles){
            for(int n = 14; n < linesSettings; n++){
                Stream<String> lines = Files.lines(Paths.get("settings.json"));
                temp = lines.skip(n).findFirst().get();
                fileNames[n-14] = temp;
                count++;
            }
        }
    }
    public static String[] fileNames(){

        return fileNames;
    }
    public static void makeFile(int actualNumberOfFiles,String newFile) throws InterruptedException, IOException {
        Path newFilePath = Paths.get(newFile + ".json");
        Files.createFile(newFilePath);
        String newFileName = newFile + ".json";
        System.out.println("New File \"" + newFile + "\" added");
        System.out.println("3");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("2");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("1");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Saving to new File!");
        converter.readToNewFile(actualNumberOfFiles, newFileName);
    }
    public static void prepFile() throws IOException {
        FileWriter writer = new FileWriter("currentLibrary.json", true);
        BufferedWriter bwriter = new BufferedWriter(writer);
        bwriter.newLine();
    }
    public static void menu() throws IOException, InterruptedException {
        actualNumberOfFiles();
        clearConsole();
        wordAlreadyPicked = false;
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
    public static void writeIntoFile() throws IOException, InterruptedException {
        boolean play = false;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter word: ");
        String input = scan.nextLine();
        char[] wordToWrite = input.toCharArray();
        if(wordToWrite[0] == '/'){
            commands(scan, input, play);
        }
        for(int i = 0; i < wordToWrite.length; i++){
            if(wordToWrite[i] > 122 || wordToWrite[i] < 97){
                System.out.println("Please just use lowercase letters and no special characters!");
                System.out.println();
                writeIntoFile();
            }
        }
        if(checkForDuplicates(input) == 0) {
            FileWriter writer = new FileWriter("currentLibrary.json", true);
            BufferedWriter bwriter  = new BufferedWriter(writer);
            bwriter.write(input);
            bwriter.newLine();
            bwriter.close();
            System.out.println("Successfully written to current library");
            System.out.println();
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
        String guessedLetters;
        int stop = 0,  numberOfUnderscores = 0, failedTrys = 0;
        StringBuilder builder = new StringBuilder();
        if(!wordAlreadyPicked) {
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
        wordAlreadyPicked = false;
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
    public static void pickWord() throws IOException, InterruptedException {
        boolean play;
        Scanner scan = new Scanner(System.in);
        System.out.println("Exit via /exit");
        System.out.print("Word: ");
        word = scan.next();
        if(Objects.equals(word, "/exit")){
            play = true;
            commands(scan, word, play);
        }
        System.out.println("Is \"" + word + "\" your word?" );
        System.out.print("[y/n]: ");
        String choice = scan.next();
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            wordAlreadyPicked = true;
            writeIntoFilePassive(word);
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
            playAgainTrue();
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
    public static void playAgainTrue() throws IOException, InterruptedException {
        clearConsole();
        hangman();
    }
    public static void clearConsole(){
        for(int i = 0; i < Math.pow(2,18); i++){
            System.out.println();
        }
    }
    public static void commands(Scanner scan, String input, boolean play) throws IOException, InterruptedException {
        if(Objects.equals(input, "/exit")){
            System.out.println("Do you want to stop entering words?");
            System.out.print("[y/n]: ");
            String choice = scan.next();
            if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                menu();
            }else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "No")){
                if(!play){
                    writeIntoFile();
                }else{
                    pickWord();
                }
            }
            System.out.println("Please select yes or no!");
            commands(scan, input, play);
        }else if(Objects.equals(input, "/clearLibrary")){
            System.out.println("Do you want to clear the active library?");
            System.out.print("[y/n]: ");
            String choice = scan.nextLine();
            if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                converter.reset();
                writeIntoFile();
            }else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")){
                writeIntoFile();
            }
            System.out.println("Please select yes or no!");
            commands(scan, input, play);
        }else if(Objects.equals(input, "/resetData")){
            System.out.println("Do you want to reset data?");
            System.out.print("[y/n]: ");
            String choice = scan.nextLine();
            if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                converter.resetToStartingWords();
            }else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")){
                writeIntoFile();
            }
            System.out.println("Please select yes or no!");
            commands(scan, input, play);
        }else if(Objects.equals(input, "/saveCurrentLibrary")){
            saveLibrary(scan);
        }else if(Objects.equals(input, "/help")){
            System.out.println();
            System.out.println();
            System.out.println("Possible commands:");
            System.out.println("/exit : Exit to menu.");
            System.out.println("/clearLibrary : Clears active library.");
            System.out.println("/resetData : Resets active library to match source file.");
            System.out.println("/saveCurrentLibrary : Saves active library to new file.");
            System.out.println("/LoadLibrary : Loads different library into active library.");
            System.out.println("/help : Opens help menu.");
        }else if(Objects.equals(input, "/loadLibrary")){
            loadLibrary(scan);
        }
        writeIntoFile();
    }
    public static void loadLibrary(Scanner scan) throws IOException, InterruptedException {
        System.out.println("Do you want to load a different library?");
        System.out.print("[y/n]: ");
        int count = 1, desired;
        String temp;
        String choice = scan.nextLine();
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            for(int i = 0; i < fileNames.length; i++){
                System.out.println( count + ": "+ fileNames[i]);
                count++;
            }
            desired = scan.nextInt();
            temp = fileNames[desired - 1];
            converter.loadLibrary(temp);
        }else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")){
            writeIntoFile();
        }
        System.out.println("Please select yes or no!");
        loadLibrary(scan);
    }
    public static void saveLibrary(Scanner scan) throws IOException, InterruptedException {
        System.out.println("Do you want to save current word library?");
        System.out.print("[y/n]: ");
        String choice = scan.nextLine();
        String newFile;
        int actualNumberOfFiles = actualNumberOfFiles();
        int activeFile = activeFile();
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            System.out.print("Choose a name: ");
            choice = scan.nextLine();
            char[] newFileName = choice.toCharArray();
            for(int count = 0; count < fileNames.length; count++){
                if(Objects.equals(choice, fileNames[count])){
                    System.out.print("This library already exists.");
                    saveLibrary(scan);
                } else{
                    for(int i = 0; i < newFileName.length; i++){
                        if(newFileName[i] > 122 || newFileName[i] < 97){
                            System.out.println("Please just use lowercase letters and no special characters!");
                            System.out.println();
                            saveLibrary(scan);
                        }
                    }
                    actualNumberOfFiles++;
                    activeFile++;
                    updateFileNames();
                    newFile = choice;
                    isNewFile = true;
                    makeFile(actualNumberOfFiles, newFile);
                }
            }
        }else if(Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")){
            writeIntoFile();
        }
        System.out.println("Please select yes or no!");
        saveLibrary(scan);
    }
    public static long countLines() throws IOException {
        long lines = 0;
        BufferedReader reader = new BufferedReader(new FileReader("currentLibrary.json"));
            while(reader.readLine() != null){
                lines++;
            }
        return lines;
    }
    public static long countLinesSettings() throws IOException {
        long lines = 0;
        BufferedReader reader = new BufferedReader(new FileReader("settings.json"));
        while(reader.readLine() != null){
            lines++;
        }
        return lines;
    }
    public static String readRandomFromFile() throws IOException {
        int n = (int) randomNumber();
        String line = null;
        Stream<String> lines = Files.lines(Paths.get("currentLibrary.jsonF"));
            line = lines.skip(n).findFirst().get();
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
            line = Files.readAllLines(Paths.get("currentLibrary.json")).get(n);
            assert line != null;
            if(line.equals(input)){
                duplicate = 1;
                return duplicate;
            }
        }
        return duplicate;
    }
    public static void writeIntoFilePassive(String input) throws IOException {
        char[] wordToGuess = input.toCharArray();
        for(int i = 0; i < wordToGuess.length; i++){
            if(wordToGuess[0] > 96 && wordToGuess[0] < 123){
                if(checkForDuplicates(input) == 0) {
                    FileWriter writer = new FileWriter("currentLibrary.json", true);
                    BufferedWriter bwriter = new BufferedWriter(writer);
                    bwriter.write(input);
                    bwriter.newLine();
                    bwriter.close();
                }
            }
        }
    }
    public static void updateFileNames() throws IOException {
        String[] temp = new String[numberOfFiles()];
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
