import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

public class AI {
        public static int a = 0;
        public static int b = 0;
        public static int c = 0;
        public static int d = 0;
        public static int e = 0;
        public static int f = 0;
        public static int g = 0;
        public static int h = 0;
        public static int i = 0;
        public static int j = 0;
        public static int k = 0;
        public static int l = 0;
        public static int m = 0;
        public static int n = 0;
        public static int o = 0;
        public static int p = 0;
        public static int q = 0;
        public static int r = 0;
        public static int s = 0;
        public static int t = 0;
        public static int u = 0;
        public static int v = 0;
        public static int w = 0;
        public static int x = 0;
        public static int y = 0;
        public static int z = 0;

    public static void preperation() throws IOException {
        String line;
        char tempChar;
        for(int count = 0; count < (int) countLines(); count++){
            Stream<String> lines = Files.lines(Paths.get("currentLibrary.txt"));
            line = lines.skip(count).findFirst().get();
            char[] temp = line.toCharArray();
            for(int letter = 0; letter < line.length(); letter++){
                tempChar = temp[letter];
                letter(tempChar);
            }
        }
        Integer[] frequency = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z};
        Arrays.sort(frequency, Collections.reverseOrder());
        System.out.println( Arrays.toString(frequency));
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
        BufferedReader reader = new BufferedReader(new FileReader("currentLibrary.txt"));
        while(reader.readLine() != null){
            lines++;
        }
        return lines;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        /*int temp = 97;
        for(int i = 1; i <= 26; i++) {
            System.out.print((char) temp +", ");
            temp++;
        }*/
        preperation();
    }
}
