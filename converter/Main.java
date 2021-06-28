package converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // write your code here

        Scanner scanner = new Scanner(System.in);

        int oldBase;
        int newBase;

        String number;
        Converter.setPrecision(5);

        while (true) {
            System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
            String command = scanner.nextLine();

            if ("/exit".equals(command)) {
                break;
            }

            String[] inputs = command.split(" ");
            oldBase = Integer.parseInt(inputs[0]);
            newBase = Integer.parseInt(inputs[1]);

            while (true) {
                System.out.println("Enter number in base " + oldBase + " to convert to base " + newBase + " (To go back type /back)");
                command = scanner.nextLine();

                if ("/back".equals(command)) {
                    break;
                }
                System.out.println("Conversion result:  " + Converter.convertToBase(command, oldBase, newBase));
            }

        }
    }
}


