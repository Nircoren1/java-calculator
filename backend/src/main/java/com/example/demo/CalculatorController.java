package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.math.BigDecimal;

@RestController
public class CalculatorController {

    public static String sum = "0";
    public static String secNum = "";
    public static Character action = null;

    public static String add(String operand1, String operand2) {
        double result = parseDouble(operand1) + parseDouble(operand2);
        return formatResult(result);
    }

    public static String subtract(String operand1, String operand2) {
        double result = parseDouble(operand1) - parseDouble(operand2);
        return formatResult(result);
    }

    public static String multiply(String operand1, String operand2) {
        double result = parseDouble(operand1) * parseDouble(operand2);
        return formatResult(result);
    }

    public static String divide(String numerator, String denominator) {
        double num = parseDouble(numerator);
        double denom = parseDouble(denominator);

        if (denom != 0) {
            double result = num / denom;
            return formatResult(result);
        } else {
            throw new ArithmeticException("Cannot divide by zero");
        }
    }

    private static double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + str);
        }
    }

    private static String formatResult(double result) {
        String doubleStr = String.format("%.8f", result);
        return doubleStr.indexOf(".") < 0 ? doubleStr : doubleStr.replaceAll("0*$", "").replaceAll("\\.$", "");

    }

    // When calculation, strings are turned to numbers, calculated and then back to strings.
    private static String calcAction(Character value) {
        switch (action) {
            case '+':
                sum = add(sum, secNum);
                break;
            case '-':
                sum = subtract(sum, secNum);
                break;
            case '*':
                sum = multiply(sum, secNum);
                break;
            case '/':
                sum = divide(sum, secNum);
                break;
        }
        secNum = "";
        action = value;
        sum = removeTrailingZeros(sum);
        return sum;

    }

    private String onDecimalPoint(Character input) {
        if (action == null) {
            sum = updateVarOnDecimalPoint(sum, input);
            return sum;
        } else if (action == '='){
                action = null;
                sum = "0" + input;
                return sum;
        } else {
            if (secNum.isEmpty()) {
                secNum = "0";
            }
            secNum = updateVarOnDecimalPoint(secNum, input);
            return secNum;
        }
    }

    private String updateVarOnDecimalPoint(String num, Character input) {
        if (num.contains(".")) {
            return num;
        }
        num += input;
        return num;
    }

    private String onDigitInput(Character input) {
        if (action == null) {
            sum = handleAddDigit(input, sum);
            return sum;
        } // If action is '=', we reset sum and add digit input.
        else if( action == '=') {
            action = null;
            sum = "" + input;
            return sum;
        }
        else {
            secNum = handleAddDigit(input, secNum);
            return secNum;
        }
    }

    private String handleAddDigit(Character input, String num) {
        if( num.equals("0")) {
            num = "";
        }
        num += input;
        return num;
    }

    private static String removeTrailingZeros(String input) {
        BigDecimal bigDecimal = new BigDecimal(input);
        return bigDecimal.stripTrailingZeros().toPlainString();
    }

    private static boolean isDecimal(double number) {
        return number % 1 != 0;
    }

    private static boolean isDecimalNumber(String str) {
        try {
            double parsedValue = Double.parseDouble(str);
            return parsedValue % 1 != 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String resetCalculator() {
        sum = "0";
        secNum = "";
        action = null;
        return sum;
    }

    @PostMapping("/calc")
    public String handlePostRequest(@RequestBody PostData post){
        Character input = post.getValue();
        try {
            switch (input) {
                case '=':
                    if (action != null) {
                        if (secNum.isEmpty()) {
                            return sum;
                        }
                        return calcAction('=');
                    }
                    return sum;
                case 'C':
                    return resetCalculator();
                case '.':
                    return onDecimalPoint(input);
                case '%':
                    if (secNum.isEmpty()) {
                        sum = divide(sum, "100");
                        return sum;
                    } else {
                        secNum = divide(secNum, "100");
                        return secNum;
                    }
                default:
                    if (Character.isDigit(input)) {
                        return onDigitInput(input);
                    }
                    // If no secNum, change action to input action.
                    else if (action == null || secNum.isEmpty()) {
                        action = input;
                        return sum;
                    }
                    // If sum and secNum and action exists, make calculation.
                    else {
                        return calcAction(input);
                    }
            }

        } catch (Exception e) {
            resetCalculator();
            return "can't devide by 0";
        }
    }
}

