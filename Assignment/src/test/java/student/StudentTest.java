package student;

import calculator.CalculatorModel;
import calculator.Operation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentTest {
    @Test
    void answerCanBecomeTheNextLeftOperand() {
        CalculatorModel calculator = new CalculatorModel();
        calculator.inputDigit(8);
        calculator.selectOperation(Operation.ADD);
        calculator.inputDigit(5);
        calculator.evaluate();
        calculator.selectOperation(Operation.MULTIPLY);
        calculator.inputDigit(2);
        calculator.evaluate();
        assertEquals("26", calculator.getDisplay());
    }
}
