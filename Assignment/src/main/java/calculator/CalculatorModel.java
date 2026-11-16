package calculator;

import java.util.Locale;
import java.util.Objects;

/**
 * Stores a calculator's entry, pending operation, and left operand.
 * Operations are evaluated from left to right. This class has no JavaFX dependency.
 * Numbers have at most six integer digits and four fractional digits.
 * Each calculation rounds its answer before that answer is used again.
 */
public final class CalculatorModel {
    private String display;
    private double leftOperand;
    private Operation pendingOperation;
    private boolean startNewNumber;
    private boolean error;

    /** Creates a calculator displaying zero with no pending operation. */
    public CalculatorModel() {
        clear();
    }

    /**
     * Reads the single display without changing the calculation.
     *
     * @return the current entry, formatted answer, or {@code Error}
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Enters a digit. The first digit after an answer starts a new calculation.
     * The first digit after an operation starts its right operand.
     * Extra digits beyond six integer digits or four fractional digits are ignored.
     *
     * @param digit a value from zero through nine
     * @throws IllegalArgumentException if the digit is outside that range
     */
    public void inputDigit(int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("Digit must be between 0 and 9");
        }
        prepareEntry();
        int decimalPoint = display.indexOf('.');
        if (decimalPoint == -1) {
            if (display.equals("0")) {
                display = Integer.toString(digit);
            } else if (display.length() < 6) {
                display += digit;
            }
        } else if (display.length() - decimalPoint - 1 < 4) {
            display += digit;
        }
    }

    /** Adds a decimal point, ignoring a second point within the same entry. */
    public void inputDecimalPoint() {
        prepareEntry();
        if (!display.contains(".")) {
            // Something needs to go here
        }
    }

    /**
     * Uses the displayed number, including any rounding, as the left operand.
     * If a complete operation
     * is pending, evaluates it first. Consecutive operations replace the pending
     * operation until a right operand is entered. Does nothing during an error.
     *
     * @param operation the next operation
     * @throws NullPointerException if the operation is null
     */
    public void selectOperation(Operation operation) {
        Objects.requireNonNull(operation, "Operation must not be null");
        if (error) {
            return;
        }
        if (pendingOperation != null && !startNewNumber) {
            evaluate();
            if (error) {
                return;
            }
        }
        // Something needs to go here
    }

    /**
     * Evaluates a pending operation with an entered right operand.
     * Without both, leaves the display unchanged. Repeated equals does not
     * repeat an operation. Rounds answers to four decimal places using
     * {@code String.format(Locale.ROOT, "%.4f", result)} and removes trailing
     * fractional zeros. Rounded zero is displayed as {@code 0}, never {@code -0}.
     * Division by zero or a rounded answer outside -999999.9999 through
     * 999999.9999 displays {@code Error} and clears the pending calculation.
     * A digit, decimal point, or Clear recovers.
     */
    public void evaluate() {
        if (error || pendingOperation == null || startNewNumber) {
            return;
        }
        try {
            // Something needs to go here
        } catch (ArithmeticException exception) {
            display = "Error";
            error = true;
        }
        leftOperand = 0;
        pendingOperation = null;
        startNewNumber = true;
    }

    /** Restores zero and discards all operands, operations, and error state. */
    public void clear() {
        display = "0";
        leftOperand = 0;
        pendingOperation = null;
        startNewNumber = true;
        error = false;
    }

    private String formatResult(double result) {
        if (!Double.isFinite(result)) {
            throw new ArithmeticException("Result is outside the display range");
        }
        String formatted = String.format(Locale.ROOT, "%.4f", result);
        double rounded = Double.parseDouble(formatted);
        if (Math.abs(rounded) >= 1_000_000) {
            throw new ArithmeticException("Result is outside the display range");
        }
        if (rounded == 0) {
            return "0";
        }
        while (formatted.endsWith("0")) {
            formatted = formatted.substring(0, formatted.length() - 1);
        }
        if (formatted.endsWith(".")) {
            formatted = formatted.substring(0, formatted.length() - 1);
        }
        return formatted;
    }

    private void prepareEntry() {
        if (error) {
            clear();
        }
        if (startNewNumber) {
            display = "0";
            startNewNumber = false;
        }
    }
}
