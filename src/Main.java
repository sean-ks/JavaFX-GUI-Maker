//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        File file = new File("sample.txt");
        Scanner input = new Scanner(file);
        while(input.hasNextLine()){
            String nodePath = input.next();
            System.out.println("nodePath: " + nodePath);
            String type= input.next();
            System.out.println("type: " + type);
            String text = input.nextLine();
            System.out.println("text: " + text + "\n");
        }

    }
}