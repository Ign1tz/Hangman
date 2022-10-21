import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class converter {
    public static String[] fileName = Hangman.fileNames();
    public static int fileCounter = Hangman.fileCounter();
    public static boolean newFile = Hangman.newFile();
    public static int inputFileCounter = 0;
    public static void convert() throws IOException, InterruptedException {
        lowercase();
    }
    public static int isNewFile(){
        if(newFile){
            inputFileCounter = 1;
        }else {
            inputFileCounter = 0;
        }
        return inputFileCounter;
    }
    public static double countLines() throws IOException {
        double lines = 0;
        BufferedReader reader = new BufferedReader(new FileReader(fileName[isNewFile()]));
        while(reader.readLine() != null){
            lines++;
        }
        return lines;
    }
    public static double countLines2() throws IOException {
        double lines = 0;
        BufferedReader reader = new BufferedReader(new FileReader(fileName[fileCounter - 1]));
        while(reader.readLine() != null){
            lines++;
        }
        return lines;
    }
    public static void lowercase() throws IOException, InterruptedException {
        double percent, start = countLines();;
        int count = (int) countLines();
        String line;
        double n = 0;
        while(n <  count) {
            Stream<String> lines = Files.lines(Paths.get(fileName[isNewFile()]));
            line = lines.skip((int) n).findFirst().get();
            char[] temp = line.toCharArray();
            for (int x = 0; x < temp.length; x++) {
                if ((int) temp[x] < 91 && (int) temp[x] > 64) {
                    temp[x] = (char) (temp[x] + 32);
                } else if (temp[x] == 'ä' || temp[x] == 'Ä' || temp[x] == 'ö' || temp[x] == 'Ö' || temp[x] == 'ü' || temp[x] == 'Ü') {
                    break;
                } else if(x == temp.length - 1){
                    readWrite(line, temp);
                }
            }
            n++;
            percent = (n / start) * 100;
            System.out.print(percent((int) percent) + (int) percent + "%\r");
        }
        TimeUnit.SECONDS.sleep(1);
        Hangman.menu();
    }
    public static void readWrite(String line, char[] temp) throws IOException {
        String  testLine;
        boolean clear = false;
        if(countLines2() == 0){
            FileWriter writer = new FileWriter(fileName[fileCounter - 1], clear);
            clear = true;
            BufferedWriter bwriter = new BufferedWriter(writer);
            bwriter.write(temp);
            bwriter.newLine();
            bwriter.close();
        }else if (!newFile){
            for (int i = 0; i < countLines2(); i++) {
                Stream<String> testLines = Files.lines(Paths.get(fileName[fileCounter - 1]));
                testLine = testLines.skip(i).findFirst().get();
                char[] temp2 = testLine.toCharArray();
                if (Arrays.equals(temp2, temp)) {
                    break;
                } else if (i == (countLines2() - 1)) {
                    clear = true;
                    FileWriter writer = new FileWriter(fileName[fileCounter - 1], clear);
                    BufferedWriter bwriter = new BufferedWriter(writer);
                    bwriter.write(temp);
                    bwriter.newLine();
                    bwriter.close();
                    break;
                }
            }
        }else{
            for (int i = 0; i < countLines(); i++) {
                clear = true;
                FileWriter writer = new FileWriter(fileName[fileCounter - 1], clear);
                BufferedWriter bwriter = new BufferedWriter(writer);
                bwriter.write(temp);
                bwriter.newLine();
                bwriter.close();
            }
        }
    }
    public static void reset() throws IOException, InterruptedException {
        new FileWriter(fileName[1], false).close();
        lowercase();
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

    public static void main(String[] args) throws IOException, InterruptedException {
        lowercase();
    }
}
