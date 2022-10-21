import java.util.Arrays;
import java.util.Scanner;
import java.io.*;
import java.io.File;
import java.io.IOException;

public class CreateFile {
    public static void main(String[] args){
        try{
            File Test1 = new File("words.txt");
            if(Test1.createNewFile()){
                System.out.println("File created: " + Test1.getName());
            } else {
                System.out.println("File allready exists");
            }
        }catch(Exception e){
            System.out.println("wrong");
        }
    }

}
