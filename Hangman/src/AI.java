import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

public class AI {
    public static double a = 0.097;
    public static double b = 0.098;
    public static double c = 0.099;
    public static double d = 0.1;
    public static double e = 0.101;
    public static double f = 0.102;
    public static double g = 0.103;
    public static double h = 0.104;
    public static double i = 0.105;
    public static double j = 0.106;
    public static double k = 0.107;
    public static double l = 0.108;
    public static double m = 0.109;
    public static double n = 0.11;
    public static double o = 0.111;
    public static double p = 0.112;
    public static double q = 0.113;
    public static double r = 0.114;
    public static double s = 0.115;
    public static double t = 0.116;
    public static double u = 0.117;
    public static double v = 0.118;
    public static double w = 0.119;
    public static double x = 0.12;
    public static double y = 0.121;
    public static double z = 0.122;

    public static void preperation() throws IOException {
        String line;
        char tempChar;
        for(int count = 0; count < (int) countLines(); count++){
            Stream<String> lines = Files.lines(Paths.get("currentLibrary.json"));
            line = lines.skip(count).findFirst().get();
            char[] temp = line.toCharArray();
            for(int letter = 0; letter < line.length(); letter++){
                tempChar = temp[letter];
                letter(tempChar);
            }
        }
        Double[] frequency = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z};
        Arrays.sort(frequency, Collections.reverseOrder());
        for(int count = 0; count < frequency.length; count++){
            int temp = (int) ((frequency[count] * 1000) % 1000);
            int temp2 = (int) (frequency[count] - temp / 1000);
            System.out.println( (char) temp + ": " + temp2 + "; ");
        }
    }
    public static void letter(char temp){
        switch (temp){
            case 'a':
                a++;
                break;
            case 'b':
                b++;
                break;
            case 'c':
                c++;
                break;
            case 'd':
                d++;
                break;
            case 'e':
                e++;
                break;
            case 'f':
                f++;
                break;
            case 'g':
                g++;
                break;
            case 'h':
                h++;
                break;
            case 'i':
                i++;
                break;
            case 'j':
                j++;
                break;
            case 'k':
                k++;
                break;
            case 'l':
                l++;
                break;
            case 'm':
                m++;
                break;
            case 'n':
                n++;
                break;
            case 'o':
                o++;
                break;
            case 'p':
                p++;
                break;
            case 'q':
                q++;
                break;
            case 'r':
                r++;
                break;
            case 's':
                s++;
                break;
            case 't':
                t++;
                break;
            case 'u':
                u++;
                break;
            case 'v':
                v++;
                break;
            case 'w':
                w++;
                break;
            case 'x':
                x++;
                break;
            case 'y':
                y++;
                break;
            case 'z':
                z++;
                break;
        }
    }
    public static double countLines() throws IOException {
        double lines = 0;
        BufferedReader reader = new BufferedReader(new FileReader("currentLibrary.json"));
        while(reader.readLine() != null){
            lines++;
        }
        return lines;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        /*double temp = 97;
        for(int i = 1; i <= 26; i++) {
            System.out.println("public static double "+ (char) temp + " = " + temp/1000 + ";");
            temp++;
        }*/
        preperation();
    }
}
