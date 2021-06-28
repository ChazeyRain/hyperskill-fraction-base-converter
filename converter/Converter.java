package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

public class Converter {

    private static int precision = 100;

    public static void setPrecision(int precision) {
        Converter.precision = precision + 1;
    }

    public static String convertToBase(String input, int inBase, int outBase) throws IllegalArgumentException {

        input = input.toUpperCase(Locale.ROOT);

        if (!input.matches("[0-9A-Z]+\\.?[0-9A-Z]*")) {
            throw new IllegalArgumentException("Error: your number has incorrect literals");
        }

        if (inBase < 1 || outBase < 1 || inBase > 35 || outBase > 35) {
            throw new IllegalArgumentException("Error: base should be bigger than 0 and lower than 35");
        }

        if (inBase < 11) {
            if (!input.matches("[0-" + (inBase - 1) + "]+\\.?[0-" + (inBase - 1) + "]*")) {
                throw new IllegalArgumentException("Error: your number have numerals bigger than base: " + inBase);
            }
        } else {
            char inBaseChar = (char) (55 + inBase);
            if (!input.matches("[0-9A-" + inBaseChar + "]+\\.?[0-9A-" + inBaseChar + "]*")) {
                throw new IllegalArgumentException("Error: your number have numerals bigger than base: " + inBase);
            }
        }

        if (input.contains(".")) {
            String intPart = input.substring(0, input.indexOf("."));
            String fractionPart = input.substring(input.indexOf(".") + 1);
            return convertInteger(intPart, inBase, outBase) + "." + convertFraction(fractionPart, inBase, outBase);
        }

        return convertInteger(input, inBase, outBase);
    }

    private static String convertInteger(String input, int inBase, int outBase) {

        StringBuilder newNumber = new StringBuilder();
        BigInteger number = convertToDec(input, inBase);

        while (number.max(BigInteger.valueOf(outBase - 1)).intValue() != outBase - 1) {
            newNumber.append(
                    getNumeral(number.mod(BigInteger.valueOf(outBase)).intValue())
            );
            number = number.divide(BigInteger.valueOf(outBase));
        }

        newNumber.append(getNumeral(number.intValue()));

        return newNumber.reverse().toString();
    }

    private static String convertFraction(String input, int inBase, int outBase) {

        StringBuilder newNumber = new StringBuilder();

        BigDecimal number = BigDecimal.ZERO;

        for (int i = 0; i < input.length(); i++) {
            number = number.add(BigDecimal.valueOf(((double) getNumber(input.charAt(i)) / Math.pow(inBase, i + 1))));
        }


        for (int currentPoint = 1; currentPoint <= precision; currentPoint++) {
            if (number.subtract(BigDecimal.valueOf((double) 1 / Math.pow(outBase, currentPoint))).signum() == 1) {
                for (int i = outBase; i > 0; i--) {
                    if (number.signum() == -1 || number.signum() == 0) {
                        break;
                    }
                    if (number.subtract(BigDecimal.valueOf((double) i / Math.pow(outBase, currentPoint))).signum() == 1) {
                        number = number.subtract(BigDecimal.valueOf((double) (i) / Math.pow(outBase, currentPoint)));
                        newNumber.append(getNumeral(i));
                    }
                }
            } else {
                newNumber.append("0");
            }
        }

        return round(newNumber.toString(), outBase);
        //return newNumber.toString();
    }

    private static BigInteger convertToDec(String number, int base) {
        BigInteger newNumber = BigInteger.ZERO;

        for(int i = 0; i < number.length(); i++) {
            newNumber = newNumber.add(BigInteger
                    .valueOf(getNumber(number.charAt(number.length() - i - 1)))
                    .multiply(BigInteger.valueOf(base).pow(i)));
        }

        return newNumber;
    }

    private static int getNumber(char number) {
        if (number > 47 && number < 58) {
            return number - 48;
        }
        return number - 55;
    }

    private static char getNumeral(int number) {
        if (number < 10) {
            return (char) (48 + number);
        }
        return (char) (55 + number);
    }

    private static String round(String number, int base) {

        int n = getNumber(number.charAt(number.length() - 1)) + 1;

        char[] numericalArray = number.toCharArray();

        for (int i = numericalArray.length - 1; i > 0; i--) {
            if (n == base) {
                numericalArray[i] = '0';
                numericalArray[i - 1] = (char) getNumeral(getNumber(numericalArray[i - 1]) + 1);
                n = getNumber(numericalArray[i - 1]);
            } else {
                break;
            }
        }
        return String.valueOf(numericalArray).substring(0, number.length() - 1);
    }
}
