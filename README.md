# CSE 220 JavaFX Calculator

Build a simple four function calculator using JavaFX and a stylesheet.
Complete the marked sections in the Java methods and add the missing CSS colors.
Keep the class and method signatures. The FXML, Operation enum, and application
launcher are provided.

## What this assignment evaluates

Read the JavaDoc to understand the required classes and methods.
Open index.html in the docs folder with a web browser.
Practice JavaFX controls, button events, object state, CSS, and unit testing.

## Project manager statement

A user needs four arithmetic operations and the ability to continue using the
displayed answer. Colors should distinguish numbers, operations, and Clear.

## Acceptance criteria

Use Java 24 and the supplied Maven project. Put the Java classes in the
calculator package under src/main/java. Put calculator.css in
src/main/resources/calculator.

Use one display that the user cannot edit and the 17 provided buttons.
Keep all controls visible when the window is resized. Buttons must be at least
40 by 40 pixels at a window content size of 320 by 430 pixels. The longest
answer must fit without scrolling.

## FXML

FXML describes the layout and button connections. The calculator-view.fxml file
is provided in src/main/resources/calculator. JavaFX loads it when the app starts.
You do not need to write, edit, or manage FXML or use Scene Builder.

Keep its button IDs, style classes, and handler names unchanged. The tests use
these IDs. Your controller implements the methods that the buttons call.

## Working in IntelliJ

Open pom.xml inside Assignment as a Maven project. Select Java 24 and enable
the JavaFX plugin. Keep the supplied Maven and module settings.
Run CalculatorApplication after implementing the classes.

## Calculator behavior

Start the display at 0. Digits build the current entry. Allow one decimal point.
Ignore unnecessary leading zeros and extra decimal points.

An operation saves the displayed number as the left operand. The next digit
starts the right operand. Equals calculates and displays the answer.

An operation after an answer uses that answer as the next left operand.
A digit after an answer starts a new calculation.

Calculate from left to right. Changing the operation before entering the right
operand replaces it. Equals without a complete calculation and repeated equals
leave the display unchanged.

Clear resets everything. Division by zero or an answer outside the allowed
range displays Error. Operations and equals leave Error unchanged.
A digit, decimal point, or Clear starts fresh.

## Number limits

Use double values for arithmetic.

- Allow six digits before the decimal point and four after it
- Ignore extra input digits
- Round answers to four decimal places
- Remove trailing zeros after the decimal point
- Display a rounded zero as 0
- Allow answers from negative 999999.9999 through positive 999999.9999
- Check the range after rounding and show Error if the answer does not fit
- Use the displayed answer in the next calculation

The display needs at most 12 characters. Use a period for the decimal point.
Do not use scientific notation or commas in numbers.

One divided by three displays 0.3333. Multiplying that displayed answer by
three gives 0.9999. Java string formatting can handle the rounding for you.

## Stylesheet colors

Define all colors in calculator.css. Use the hexadecimal values below.
Add a hash sign before each value in the stylesheet.

### Normal colors

- Number and decimal buttons use slate blue 334155
- Operation and equals buttons use teal 0f766e
- Clear uses muted red 9f3247
- Button text uses off white f8fafc
- The calculator background uses dark navy 0f172a
- The display background uses near black 020617
- Display text uses off white f8fafc
- Button borders are transparent when not focused
- A focused button has a pale gray border e2e8f0

### Hover and pressed colors

Every button uses slate gray 475569 when the mouse is over it.
The text remains off white f8fafc.

Every button uses light blue 93c5fd while pressed. The text changes to
dark navy 0f172a. Use these same colors for the armed state during keyboard use.
Pressed and armed colors take priority over hover colors.

After release, show the hover colors if the pointer is still over the button.
When the pointer leaves, restore the normal colors.

### Style classes

The provided FXML assigns calculator-button to every button.
Numbers and the decimal point also use number-button.
Operations and equals also use operation-button. Clear also uses clear-button.
Use the calculator class for the background and display for the display.

## Methods

Use the JavaDoc for complete method signatures and validation rules.

### CalculatorModel

Complete the marked sections for decimal entry, selecting an operation, and
calculating an answer. Digit entry, clearing, validation, and number formatting
are provided as examples. Keep this class independent of JavaFX.

### Operation

The complete enum is provided. Keep it unchanged.
Its apply method performs ADD, SUBTRACT, MULTIPLY, or DIVIDE using double values
and throws ArithmeticException for division by zero.

### CalculatorController

Complete the marked model calls in handleDecimal, handleOperation,
handleEquals, and handleClear. Use the completed handleDigit method as an
example. The FXML annotations and display updates are already in place.

### CalculatorApplication

The complete launcher is provided. The start method loads the FXML and shows
the window. The main method launches JavaFX.

## Running and grading

Run mvn clean test from the Assignment folder. You can also run the tests in
IntelliJ. The tests use button IDs and do not open a calculator window.
The starter compiles, but tests will fail until the missing work is completed.

Add your own tests under src/test/java/student. Do not change the instructor
tests, Maven configuration, grading files, or provided FXML.

A successful build with all tests passing earns 100 points.
A failed build, failed test, or missing tests earns 10 points.

Push your work and open the latest grading run in GitHub Actions.
Submit only the complete Grade Token in the Blackboard text field.
