package instructor;

import calculator.CalculatorApplication;
import calculator.CalculatorController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Window;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorViewTest {
    private static final List<String> NUMBER_IDS = List.of(
        "digit0Button", "digit1Button", "digit2Button", "digit3Button", "digit4Button",
        "digit5Button", "digit6Button", "digit7Button", "digit8Button", "digit9Button", "decimalButton");
    private static final List<String> OPERATION_IDS = List.of(
        "addButton", "subtractButton", "multiplyButton", "divideButton", "equalsButton");

    @BeforeAll
    static void startJavaFx() throws Exception {
        FxTestSupport.startToolkit();
    }

    @Test
    void loadsSingleReadOnlyDisplayAndUniqueButtonIds() throws Exception {
        FxTestSupport.run(() -> {
            FXMLLoader loader = loader();
            Parent root = loader.load();
            prepare(root, 360, 480);
            assertInstanceOf(CalculatorController.class, loader.getController());
            TextField display = display(root);
            assertEquals("0", display.getText());
            assertFalse(display.isEditable());
            assertEquals(1, root.lookupAll(".text-field").size());
            assertEquals(17, root.lookupAll(".button").size());

            Set<String> foundIds = new HashSet<>();
            for (Node node : root.lookupAll(".button")) {
                assertNotNull(node.getId(), "Every button needs a unique fx:id");
                assertTrue(foundIds.add(node.getId()), "Duplicate button ID");
                assertSame(node, loader.getNamespace().get(node.getId()));
            }
            assertEquals(new HashSet<>(allButtonIds()), foundIds);
            assertTrue(Window.getWindows().isEmpty(), "Tests must not show a calculator window");
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
    void eachDigitButtonWorksById(int digit) throws Exception {
        FxTestSupport.run(() -> {
            Parent root = view();
            button(root, "digit" + digit + "Button").fire();
            assertEquals(Integer.toString(digit), display(root).getText());
        });
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "digit8Button addButton digit5Button equalsButton | 13",
        "digit8Button subtractButton digit5Button equalsButton | 3",
        "digit8Button multiplyButton digit5Button equalsButton | 40",
        "digit8Button divideButton digit5Button equalsButton | 1.6",
        "digit2Button subtractButton digit8Button equalsButton | -6",
        "digit8Button addButton digit5Button equalsButton multiplyButton digit2Button equalsButton | 26",
        "digit8Button addButton digit5Button multiplyButton digit2Button equalsButton | 26",
        "digit8Button addButton subtractButton digit5Button equalsButton | 3",
        "digit8Button addButton digit5Button equalsButton digit2Button | 2",
        "digit8Button addButton digit5Button clearButton digit2Button equalsButton | 2",
        "digit0Button decimalButton digit1Button addButton decimalButton digit2Button equalsButton | 0.3",
        "digit1Button decimalButton decimalButton digit2Button | 1.2",
        "digit1Button divideButton digit3Button equalsButton | 0.3333",
        "digit1Button divideButton digit3Button equalsButton multiplyButton digit3Button equalsButton | 0.9999",
        "digit1Button digit2Button digit3Button digit4Button digit5Button digit6Button digit7Button | 123456",
        "digit1Button decimalButton digit2Button digit3Button digit4Button digit5Button digit6Button | 1.2345",
        "digit9Button digit9Button digit9Button digit9Button digit9Button digit9Button addButton digit1Button equalsButton | Error",
        "digit8Button divideButton digit0Button equalsButton | Error",
        "digit8Button divideButton digit0Button equalsButton addButton equalsButton | Error",
        "digit8Button divideButton digit0Button equalsButton clearButton | 0",
        "digit8Button divideButton digit0Button equalsButton digit3Button addButton digit4Button equalsButton | 7",
        "digit8Button divideButton digit0Button equalsButton decimalButton digit5Button | 0.5",
        "digit8Button addButton equalsButton digit2Button equalsButton equalsButton | 10"
    })
    void fxmlButtonsDriveCalculation(String ids, String expected) throws Exception {
        FxTestSupport.run(() -> {
            Parent root = view();
            for (String id : ids.split(" ")) {
                button(root, id).fire();
            }
            assertEquals(expected, display(root).getText());
            assertTrue(display(root).getText().length() <= 12);
        });
    }

    @Test
    void longestAnswerFitsDisplayAtMinimumSize() throws Exception {
        FxTestSupport.run(() -> {
            Parent root = loader().load();
            prepare(root, 320, 430);
            button(root, "subtractButton").fire();
            for (int i = 0; i < 6; i++) {
                button(root, "digit9Button").fire();
            }
            button(root, "decimalButton").fire();
            for (int i = 0; i < 4; i++) {
                button(root, "digit9Button").fire();
            }
            button(root, "equalsButton").fire();
            TextField display = display(root);
            assertEquals("-999999.9999", display.getText());
            Text text = new Text(display.getText());
            text.setFont(display.getFont());
            double availableWidth = display.getWidth()
                - display.getInsets().getLeft() - display.getInsets().getRight();
            assertTrue(text.getLayoutBounds().getWidth() <= availableWidth,
                "The longest answer must fit without horizontal scrolling");
        });
    }

    @Test
    void stylesheetDefinesThreeDistinctButtonGroups() throws Exception {
        FxTestSupport.run(() -> {
            Parent root = view();
            assertTrue(root.getStylesheets().stream().anyMatch(url -> url.endsWith("/calculator.css")));
            Paint numbers = background(button(root, NUMBER_IDS.getFirst()));
            Paint operations = background(button(root, OPERATION_IDS.getFirst()));
            Paint clear = background(button(root, "clearButton"));
            assertEquals(3, Set.of(numbers, operations, clear).size());
            assertGroup(root, NUMBER_IDS, "number-button", numbers);
            assertGroup(root, OPERATION_IDS, "operation-button", operations);
            assertGroup(root, List.of("clearButton"), "clear-button", clear);
            for (Node node : root.lookupAll("*")) {
                assertTrue(node.getStyle().isBlank(), "Appearance belongs in the stylesheet");
            }
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"hover", "pressed", "armed"})
    void everyButtonChangesToSameColorAndReturnsToItsGroup(String state) throws Exception {
        FxTestSupport.run(() -> {
            Parent root = view();
            PseudoClass pseudoClass = PseudoClass.getPseudoClass(state);
            Paint sharedActiveColor = null;
            for (String id : allButtonIds()) {
                Button button = button(root, id);
                Paint original = background(button);
                if (state.equals("armed")) {
                    button.arm();
                } else {
                    button.pseudoClassStateChanged(pseudoClass, true);
                }
                root.applyCss();
                Paint active = background(button);
                assertNotEquals(original, active, id + " must change color for " + state);
                if (sharedActiveColor == null) {
                    sharedActiveColor = active;
                }
                assertEquals(sharedActiveColor, active, "All button groups share the active color");
                if (state.equals("armed")) {
                    button.disarm();
                } else {
                    button.pseudoClassStateChanged(pseudoClass, false);
                }
                root.applyCss();
                assertEquals(original, background(button));
            }
        });
    }

    @Test
    void pressedStyleTakesPriorityWhileHoveredAndFocused() throws Exception {
        FxTestSupport.run(() -> {
            Parent root = view();
            for (String id : allButtonIds()) {
                Button button = button(root, id);
                button.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), true);
                root.applyCss();
                Paint pressed = background(button);
                button.pseudoClassStateChanged(PseudoClass.getPseudoClass("hover"), true);
                button.pseudoClassStateChanged(PseudoClass.getPseudoClass("focused"), true);
                root.applyCss();
                assertEquals(pressed, background(button));
            }
        });
    }

    @ParameterizedTest
    @CsvSource({"320, 430", "360, 480", "520, 650"})
    void controlsFitWithoutOverlappingAtDifferentSizes(double width, double height) throws Exception {
        FxTestSupport.run(() -> {
            Parent root = loader().load();
            prepare(root, width, height);
            List<Node> controls = new ArrayList<>();
            controls.add(display(root));
            for (String id : allButtonIds()) {
                Button button = button(root, id);
                assertTrue(button.getWidth() >= 40);
                assertTrue(button.getHeight() >= 40);
                controls.add(button);
            }
            for (int i = 0; i < controls.size(); i++) {
                Bounds bounds = controls.get(i).localToScene(controls.get(i).getBoundsInLocal());
                assertTrue(bounds.getMinX() >= 0 && bounds.getMaxX() <= width);
                assertTrue(bounds.getMinY() >= 0 && bounds.getMaxY() <= height);
                for (int j = i + 1; j < controls.size(); j++) {
                    assertFalse(bounds.intersects(controls.get(j).localToScene(controls.get(j).getBoundsInLocal())),
                        "Controls must not overlap");
                }
            }
        });
    }

    private static void assertGroup(Parent root, List<String> ids, String styleClass, Paint color) {
        for (String id : ids) {
            Button button = button(root, id);
            assertTrue(button.getStyleClass().contains("calculator-button"));
            assertTrue(button.getStyleClass().contains(styleClass));
            assertEquals(color, background(button));
        }
    }

    private static List<String> allButtonIds() {
        List<String> ids = new ArrayList<>(NUMBER_IDS);
        ids.addAll(OPERATION_IDS);
        ids.add("clearButton");
        return ids;
    }

    private static FXMLLoader loader() {
        return new FXMLLoader(CalculatorApplication.class.getResource("calculator-view.fxml"));
    }

    private static Parent view() throws Exception {
        Parent root = loader().load();
        prepare(root, 360, 480);
        return root;
    }

    private static void prepare(Parent root, double width, double height) {
        new Scene(root, width, height);
        root.applyCss();
        root.resize(width, height);
        root.layout();
    }

    private static Button button(Parent root, String id) {
        return assertInstanceOf(Button.class, root.lookup("#" + id), "Missing button " + id);
    }

    private static TextField display(Parent root) {
        return assertInstanceOf(TextField.class, root.lookup("#display"));
    }

    private static Paint background(Button button) {
        assertNotNull(button.getBackground());
        assertFalse(button.getBackground().getFills().isEmpty());
        return button.getBackground().getFills().getLast().getFill();
    }
}
