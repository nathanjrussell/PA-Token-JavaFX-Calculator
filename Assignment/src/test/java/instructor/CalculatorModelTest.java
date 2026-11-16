package instructor;

import calculator.CalculatorModel;
import calculator.Operation;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorModelTest {
    @ParameterizedTest(name = "{0} displays {1}")
    @CsvSource(delimiter = '|', value = {
        "12 + 3 = | 15",
        "12 - 3 = | 9",
        "12 * 3 = | 36",
        "12 / 3 = | 4",
        "3 - 12 = | -9",
        "3 / 12 = | 0.25",
        "0 - 4 = * 3 = | -12",
        "0 - 4 = / 2 = | -2",
        "0 - 4 = + 4 = | 0",
        "0 * 7 = | 0",
        "0 / 7 = | 0",
        "8 + 5 = * 2 = | 26",
        "8 + 5 = - 2 = | 11",
        "8 + 5 = + 2 = | 15",
        "8 + 4 = / 2 = | 6",
        "8 + 5 * 2 = | 26",
        "2 + 3 * 4 - 8 / 2 = | 6",
        "8 + - 3 = | 5",
        "8 / * 3 = | 24",
        "8 + - * / 2 = | 4",
        "8 + = 2 = | 10",
        "8 = = | 8",
        "8 + 2 = = = | 10",
        "8 + 2 = 4 + 1 = | 5",
        "8 + 2 = . 5 + 1 = | 1.5",
        "0.1 + 0.2 = | 0.3",
        "1.50 * 2 = | 3",
        "1 / 3 = | 0.3333",
        "2 / 3 = | 0.6667",
        "1 / 32 = | 0.0313",
        "0 - 1 = / 32 = | -0.0313",
        "1 / 3 = * 3 = | 0.9999",
        "1 / 3 * 3 = | 0.9999",
        "0.0001 / 3 = | 0",
        "0 - 0.0001 = / 3 = | 0",
        "0.0001 / 3 * 3 = | 0",
        ".5 + .25 = | 0.75",
        "1 . . 2 . 3 | 1.23",
        "1. + 2. = | 3",
        "0 0 0 7 | 7",
        "0 . 0 0 7 | 0.007",
        "1234567 | 123456",
        "1234567 . 89123 | 123456.8912",
        "1.23456 | 1.2345",
        "0.00001 | 0.0000",
        "000000007 | 7",
        "999999.9999 + 0 = | 999999.9999",
        "0 - 999999.9999 = | -999999.9999",
        "999999.9998 + 0.0001 = | 999999.9999",
        "999999.9999 + 0.0001 = | Error",
        "999999 + 1 = | Error",
        "0 - 999999 = - 1 = | Error",
        "999999 * 2 = | Error",
        "99.9999 * 10000.01 = | Error",
        "0 - 99.9999 = * 10000.01 = | Error",
        "999999 / 0.0001 = | Error",
        "999999 + 1 * | Error",
        "999999 + 1 = C 2 + 3 = | 5",
        "999999 + 1 = 2 + 3 = | 5",
        "999999 + 1 = . 5 | 0.5",
        "1234567 + 2 = | 123458",
        "1.23456 + .0001 = | 1.2346",
        "999999 + 0 = 1 | 1",
        "8 / 0 = | Error",
        "0 / 0 = | Error",
        "8 / 0.00 = | Error",
        "8 / 0 + | Error",
        "8 / 0 = + - * / = | Error",
        "8 / 0 = 4 + 2 = | 6",
        "8 / 0 = . 5 * 2 = | 1",
        "8 / 0 = C | 0",
        "8 + C 2 = | 2",
        "8 + 3 C 4 + 5 = | 9",
        "8 + 2 = C 5 = | 5",
        "0. + . = | 0",
        "+ 4 = | 4",
        "- 4 = | -4",
        "= | 0"
    })
    void calculatesButtonSequences(String sequence, String expected) {
        CalculatorModel model = new CalculatorModel();
        press(model, sequence);
        assertEquals(expected, model.getDisplay());
        assertTrue(model.getDisplay().length() <= 12);
    }

    @Test
    void startsAtZero() {
        assertEquals("0", new CalculatorModel().getDisplay());
    }

    @Test
    void operationKeepsLeftOperandVisibleUntilEntryBegins() {
        CalculatorModel model = new CalculatorModel();
        press(model, "12 +");
        assertEquals("12", model.getDisplay());
        model.inputDigit(3);
        assertEquals("3", model.getDisplay());
        model.evaluate();
        assertEquals("15", model.getDisplay());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 10, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void invalidDigitDoesNotChangePendingCalculation(int digit) {
        CalculatorModel model = new CalculatorModel();
        press(model, "7 +");
        assertThrows(IllegalArgumentException.class, () -> model.inputDigit(digit));
        assertEquals("7", model.getDisplay());
        press(model, "2 =");
        assertEquals("9", model.getDisplay());
    }

    @Test
    void nullOperationDoesNotChangePendingCalculation() {
        CalculatorModel model = new CalculatorModel();
        press(model, "7 +");
        assertThrows(NullPointerException.class, () -> model.selectOperation(null));
        press(model, "2 =");
        assertEquals("9", model.getDisplay());
    }

    @Test
    void separateCalculatorsHaveIndependentState() {
        CalculatorModel first = new CalculatorModel();
        CalculatorModel second = new CalculatorModel();
        press(first, "7 +");
        press(second, "2 * 3 =");
        press(first, "4 =");
        assertEquals("11", first.getDisplay());
        assertEquals("6", second.getDisplay());
    }

    @Test
    void decimalPointDoesNotDependOnComputerLocale() {
        Locale original = Locale.getDefault(Locale.Category.FORMAT);
        try {
            Locale.setDefault(Locale.Category.FORMAT, Locale.GERMANY);
            CalculatorModel model = new CalculatorModel();
            press(model, "1 / 3 = * 3 =");
            assertEquals("0.9999", model.getDisplay());
        } finally {
            Locale.setDefault(Locale.Category.FORMAT, original);
        }
    }

    private static void press(CalculatorModel model, String sequence) {
        for (String token : sequence.split(" ")) {
            switch (token) {
                case "+" -> model.selectOperation(Operation.ADD);
                case "-" -> model.selectOperation(Operation.SUBTRACT);
                case "*" -> model.selectOperation(Operation.MULTIPLY);
                case "/" -> model.selectOperation(Operation.DIVIDE);
                case "=" -> model.evaluate();
                case "C" -> model.clear();
                default -> {
                    for (char character : token.toCharArray()) {
                        if (character == '.') {
                            model.inputDecimalPoint();
                        } else {
                            model.inputDigit(character - '0');
                        }
                    }
                }
            }
        }
    }
}
