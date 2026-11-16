package instructor;

import calculator.Operation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {
    @ParameterizedTest
    @CsvSource({
        "ADD, 12, 3, 15",
        "SUBTRACT, 3, 12, -9",
        "MULTIPLY, -3, 4, -12",
        "DIVIDE, 3, 12, 0.25",
        "ADD, 0.1, 0.2, 0.3",
        "DIVIDE, 1, 3, 0.3333333333333333",
        "MULTIPLY, 0, 123456789, 0",
        "DIVIDE, -12, -3, 4"
    })
    void appliesOperationInOrder(Operation operation, double left, double right, double expected) {
        assertEquals(expected, operation.apply(left, right), 1e-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.0})
    void rejectsZeroDivisor(double zero) {
        assertThrows(ArithmeticException.class,
            () -> Operation.DIVIDE.apply(1, zero));
    }
}
