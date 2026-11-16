package calculator;

/** The four calculator operations using ordinary double arithmetic. */
public enum Operation {
    /** Addition. */
    ADD,
    /** Subtraction in left operand then right operand order. */
    SUBTRACT,
    /** Multiplication. */
    MULTIPLY,
    /** Division in left operand then right operand order. */
    DIVIDE;

    /**
     * Applies this operation. The model handles rounding and display limits.
     *
     * @param left the left operand
     * @param right the right operand
     * @return the unformatted double result
     * @throws ArithmeticException if division is attempted with a zero divisor
     */
    public double apply(double left, double right) {
        if (this == DIVIDE && right == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return switch (this) {
            case ADD -> left + right;
            case SUBTRACT -> left - right;
            case MULTIPLY -> left * right;
            case DIVIDE -> left / right;
        };
    }
}
