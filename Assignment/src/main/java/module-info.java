/** A calculator built with JavaFX controls and FXML. */
module calculator {
    requires javafx.controls;
    requires javafx.fxml;

    opens calculator to javafx.fxml;
    exports calculator;
}
