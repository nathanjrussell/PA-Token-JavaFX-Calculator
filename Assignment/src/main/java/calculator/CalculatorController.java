package calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/** Connects FXML button actions to the model and refreshes the single display. */
public final class CalculatorController {
    private final CalculatorModel model = new CalculatorModel();

    @FXML
    private TextField display;

    /** Creates the controller. The FXML loader supplies its display. */
    public CalculatorController() {
    }

    @FXML
    private void initialize() {
        refreshDisplay();
    }

    @FXML
    private void handleDigit(ActionEvent event) {
        Button button = (Button) event.getSource();
        model.inputDigit(Integer.parseInt(button.getUserData().toString()));
        refreshDisplay();
    }

    @FXML
    private void handleDecimal() {
        // Something needs to go here
        refreshDisplay();
    }

    @FXML
    private void handleOperation(ActionEvent event) {
        Button button = (Button) event.getSource();
        // Something needs to go here
        refreshDisplay();
    }

    @FXML
    private void handleEquals() {
        // Something needs to go here
        refreshDisplay();
    }

    @FXML
    private void handleClear() {
        // Something needs to go here
        refreshDisplay();
    }

    private void refreshDisplay() {
        display.setText(model.getDisplay());
        display.positionCaret(display.getLength());
    }
}
