import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Boot {
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
    try{
        Path newFilePath = Paths.get(fileNames[0]);
        Files.createFile(newFilePath);
        newFilePath = Paths.get(fileNames[1]);
        Files.createFile(newFilePath);
        newFilePath = Paths.get(fileNames[2]);
        Files.createFile(newFilePath);
    } catch (IOException e) {
    }
    Utility.clearConsole();
    prepFile();
    System.out.println("Initializing: ");
    converter.convert();
}
    public static String[] fileNames(){

        return fileNames;
    }
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

    public static void addFileNames() throws IOException {
        int count = 1, linesSettings = (int) converter.countLinesSettings(), numberOfFiles = numberOfFiles();
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
    public static void updateFileNames() throws IOException {
        String[] temp = new String[numberOfFiles()];
        for(int i = 0; i < fileNames.length; i++){
            temp[i] = fileNames[i];
        }
        fileNames = temp;
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
        converter.writeToNewFile(actualNumberOfFiles, newFileName);
    }
    public static void prepFile() throws IOException {
        FileWriter writer = new FileWriter("currentLibrary.json", true);
        BufferedWriter bwriter = new BufferedWriter(writer);
        bwriter.newLine();
    }
    public static void writeIntoFile() throws IOException, InterruptedException {
        boolean play = false;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter word: ");
        String input = scan.nextLine();
        char[] wordToWrite = input.toCharArray();
        if(wordToWrite[0] == '/'){
            Utility.commands(scan, input, play);
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
    public static int checkForDuplicates(String input) throws IOException {
        String line;
        int duplicate = 0;
        for(int n = 0; n < (int) converter.countLinesCurrent(); n++) {
            line = Files.readAllLines(Paths.get("currentLibrary.json")).get(n);
            assert line != null;
            if(line.equals(input)){
                duplicate = 1;
                return duplicate;
            }
        }
        return duplicate;
    }
    public static void end(){
        System.exit(0);
    }
}
