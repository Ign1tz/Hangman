import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Utility {
    public static void commands(Scanner scan, String input, boolean play) throws IOException, InterruptedException {
        if(Objects.equals(input, "/exit")){
            System.out.println("Do you want to stop entering words?");
            System.out.print("[y/n]: ");
            String choice = scan.next();
            if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
                Hangman.menu();
            }else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "No")){
                if(!play){
                    Boot.writeIntoFile();
                }else{
                    Hangman.pickWord();
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
                Boot.writeIntoFile();
            }else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")){
                Boot.writeIntoFile();
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
                Boot.writeIntoFile();
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
        Boot.writeIntoFile();
    }
    public static void loadLibrary(Scanner scan) throws IOException, InterruptedException {
        System.out.println("Do you want to load a different library?");
        System.out.print("[y/n]: ");
        int count = 1, desired;
        String temp;
        String choice = scan.nextLine();
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            for(int i = 0; i < Boot.fileNames.length; i++){
                System.out.println( count + ": "+ Boot.fileNames[i]);
                count++;
            }
            desired = scan.nextInt();
            temp = Boot.fileNames[desired - 1];
            converter.loadLibrary(temp);
        }else if (Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")){
            Boot.writeIntoFile();
        }
        System.out.println("Please select yes or no!");
        loadLibrary(scan);
    }
    public static void saveLibrary(Scanner scan) throws IOException, InterruptedException {
        System.out.println("Do you want to save current word library?");
        System.out.print("[y/n]: ");
        String choice = scan.nextLine();
        String newFile;
        int actualNumberOfFiles = Boot.actualNumberOfFiles();
        int activeFile = Boot.activeFile();
        if(Objects.equals(choice, "y") || Objects.equals(choice, "Y") || Objects.equals(choice, "yes") || Objects.equals(choice, "Yes")){
            System.out.print("Choose a name: ");
            choice = scan.nextLine();
            char[] newFileName = choice.toCharArray();
            for(int count = 0; count < Boot.fileNames.length; count++){
                if(Objects.equals(choice, Boot.fileNames[count])){
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
                    Boot.updateFileNames();
                    newFile = choice;
                    Boot.isNewFile = true;
                    Boot.makeFile(actualNumberOfFiles, newFile);
                }
            }
        }else if(Objects.equals(choice, "n") || Objects.equals(choice, "N") || Objects.equals(choice, "no") || Objects.equals(choice, "NO")){
            Boot.writeIntoFile();
        }
        System.out.println("Please select yes or no!");
        saveLibrary(scan);
    }
    public static double randomNumber() throws IOException {
        int low = 0, random;
        long high = (long) converter.countLinesCurrent();
        Random rand = new Random();
        random =  rand.nextInt((int) high) + low;
        return random;
    }

    public static void writeIntoFilePassive(String input) throws IOException {
        char[] wordToGuess = input.toCharArray();
        for(int i = 0; i < wordToGuess.length; i++){
            if(wordToGuess[0] > 96 && wordToGuess[0] < 123){
                if(Boot.checkForDuplicates(input) == 0) {
                    FileWriter writer = new FileWriter("currentLibrary.json", true);
                    BufferedWriter bwriter = new BufferedWriter(writer);
                    bwriter.write(input);
                    bwriter.newLine();
                    bwriter.close();
                }
            }
        }
    }
    public static void clearConsole(){
        for(int i = 0; i < Math.pow(2,18); i++){
            System.out.println();
        }
    }
}
