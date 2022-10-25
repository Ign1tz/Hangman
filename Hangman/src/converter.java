import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class converter {
    public static String[] fileName = Boot.fileNames();
    public static boolean newFile = Boot.isNewFile;
    public static int actualNumberOfFiles;

    static {
        try {
            actualNumberOfFiles = Boot.actualNumberOfFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void convert() throws IOException, InterruptedException {
        lowercase();
    }
    public static void lowercase() throws IOException, InterruptedException {
        double percent, start = countLinesReset();;
        int count = (int) countLinesReset();
        String line;
        double n = 0;
        while(n <  count) {
            Stream<String> lines = Files.lines(Paths.get("resetLibrary.json"));
            line = lines.skip((int) n).findFirst().get();
            char[] temp = line.toCharArray();
            for (int x = 0; x < temp.length; x++) {
                if ((int) temp[x] < 91 && (int) temp[x] > 64) {
                    temp[x] = (char) (temp[x] + 32);
                } else if (temp[x] == 'ä' || temp[x] == 'Ä' || temp[x] == 'ö' || temp[x] == 'Ö' || temp[x] == 'ü' || temp[x] == 'Ü') {
                    break;
                } else if(x == temp.length - 1){
                    Write(temp);
                }
            }
            n++;
            percent = (n / start) * 100;
            System.out.print(percent((int) percent) + (int) percent + "%\r");
        }
        TimeUnit.SECONDS.sleep(1);
        Hangman.menu();
    }
    public static double countLinesReset() throws IOException {
        double lines = 0;
        BufferedReader reader = new BufferedReader(new FileReader("resetLibrary.json"));
        while(reader.readLine() != null){
            lines++;
        }
        return lines;
    }
    public static double countLinesCurrent() throws IOException {
        double lines = 0;
        BufferedReader reader = new BufferedReader(new FileReader("currentLibrary.json"));
        while(reader.readLine() != null){
            lines++;
        }
        return lines;
    }
    public static double countLinesNew(String whatToLoad) throws IOException {
        double lines = 0;
        BufferedReader reader = new BufferedReader(new FileReader(whatToLoad));
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
    public static void Write(char[] temp) throws IOException {
        String  testLine;
        boolean clear = false;
        double lineCount = countLinesCurrent();
        if(lineCount == 0){
            FileWriter writer = new FileWriter("currentLibrary.json", clear);
            BufferedWriter bwriter = new BufferedWriter(writer);
            bwriter.write(temp);
            bwriter.newLine();
            bwriter.close();
        }else if (!newFile){
            for (int i = 0; i < lineCount; i++) {
                Stream<String> testLines = Files.lines(Paths.get("currentLibrary.json"));
                testLine = testLines.skip(i).findFirst().get();
                char[] temp2 = testLine.toCharArray();
                if (Arrays.equals(temp2, temp)) {
                    break;
                } else if (i == (lineCount - 1)) {
                    clear = true;
                    FileWriter writer = new FileWriter("currentLibrary.json", clear);
                    BufferedWriter bwriter = new BufferedWriter(writer);
                    bwriter.write(temp);
                    bwriter.newLine();
                    bwriter.close();
                    break;
                }
            }
        }
    }
    public static void resetToStartingWords() throws IOException, InterruptedException {
        new FileWriter("currentLibrary.json", false).close();
        lowercase();
    }
    public static void writeToNewFile(int actualNumberOfFiles, String newFile) throws IOException, InterruptedException {
        String word;
        int count = (int) countLinesCurrent();
        for(int x = 0; x < count; x++){
            Stream<String> testLines = Files.lines(Paths.get("currentLibrary.json"));
            word = testLines.skip(x).findFirst().get();
            char[] temp2 = word.toCharArray();
            saveToNewFile(newFile, temp2);

        }
        editSettings(newFile, actualNumberOfFiles);
        Hangman.menu();
    }
    public static void loadLibrary(String whatToLoad) throws IOException, InterruptedException {
        reset();
        int newLibraryLines = (int) countLinesNew(whatToLoad);
        loadToCurrentLibrary(newLibraryLines, whatToLoad);
    }
    public static void loadToCurrentLibrary(int count, String whatToLoad) throws IOException, InterruptedException {
        String word;
        for(int x = 0; x < count; x++){
            Stream<String> testLines = Files.lines(Paths.get(whatToLoad));
            word = testLines.skip(x).findFirst().get();
            char[] temp2 = word.toCharArray();
            saveToCurrentFile(temp2);

        }
        Hangman.menu();
    }
    public static void saveToNewFile(String newFile, char[] word) throws IOException {
        FileWriter writer = new FileWriter(newFile, true);
        BufferedWriter bwriter = new BufferedWriter(writer);
        bwriter.write(word);
        bwriter.newLine();
        bwriter.close();
    }
    public static void saveToCurrentFile(char[] word) throws IOException {
        FileWriter writer = new FileWriter("currentLibrary.json", true);
        BufferedWriter bwriter = new BufferedWriter(writer);
        bwriter.write(word);
        bwriter.newLine();
        bwriter.close();
    }
    public static void editSettings(String newFile, int actualNumberOfFiles) throws IOException {
        FileWriter writer = new FileWriter("settings.json");
        BufferedWriter bwriter = new BufferedWriter(writer);
        LineNumberReader reader = new LineNumberReader(new FileReader("settings.json"));
        reader.setLineNumber(5);
        for(int i = 1; i < reader.getLineNumber(); i++){
            bwriter.newLine();
        }
        bwriter.write(String.valueOf(actualNumberOfFiles));
        bwriter.newLine();
        reader.setLineNumber(10);
        for(int i = 1; i < reader.getLineNumber(); i++){
            bwriter.newLine();
        }
        for(int i = 0; i < fileName.length; i++) {
            bwriter.write(fileName[i]);
            bwriter.newLine();
        }
        bwriter.write(newFile);
        bwriter.close();
    }
    public static String percent(int percent) throws IOException {
        String visualPercent = "[          ]";
        int test = percent / 10;
        switch (test){
            case 1:
                visualPercent = "[#         ]";
                break;
            case 2:
                visualPercent = "[##        ]";
                break;
            case 3:
                visualPercent = "[###       ]";
                break;
            case 4:
                visualPercent = "[####      ]";
                break;
            case 5:
                visualPercent = "[#####     ]";
                break;
            case 6:
                visualPercent = "[######    ]";
                break;
            case 7:
                visualPercent = "[#######   ]";
                break;
            case 8:
                visualPercent = "[########  ]";
                break;
            case 9:
                visualPercent = "[######### ]";
                break;
            case 10:
                visualPercent = "[##########]";
                break;
        }

        return visualPercent;
    }
    public static void reset() throws IOException{
        new FileWriter("currentLibrary.json", false).close();
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        lowercase();
    }
}
