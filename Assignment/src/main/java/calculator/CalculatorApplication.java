package calculator;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Launches the FXML calculator. Run this class from IntelliJ or Maven. */
public class CalculatorApplication extends Application {
    /** Creates the JavaFX application. */
    public CalculatorApplication() {
    }

    /**
     * Loads the calculator layout and shows the window.
     *
     * @param stage the primary JavaFX stage
     * @throws IOException if the FXML cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(CalculatorApplication.class.getResource("calculator-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.setMinWidth(340);
        stage.setMinHeight(470);
        stage.show();
    }

    /**
     * Starts JavaFX.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
