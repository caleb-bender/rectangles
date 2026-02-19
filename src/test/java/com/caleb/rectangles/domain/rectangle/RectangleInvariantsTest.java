package com.caleb.rectangles.domain.rectangle;

import com.caleb.rectangles.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class RectangleInvariantsTest {

    @Test
    void givenValidTopLeftAndSize_whenCreatingRectangle_thenTopLeftAndSizeAreCorrect() {
        // Arrange
        var topLeft = new Vector2(1.0,1.0);
        var size = new Size(1.0,1.0);
        // Act
        var rectangle = new Rectangle(topLeft, size);
        // Assert
        assertEquals(topLeft, rectangle.topLeft());
        assertEquals(size, rectangle.size());
    }

    @ParameterizedTest
    @CsvSource({
            "-1.0, 1.0, 'width'",
            "0.0, 1.0, 'width'",
            "1.0, -1.0, 'height'",
            "1.0, 0.0, 'height'"
    })
    void givenInvalidWidthOrHeight_whenCreatingRectangle_thenInvalidRectangleExceptionIsThrownWithOneInvariantViolation(
            double width, double height, String fieldName
    ) {
        // Arrange
        var topLeft = new Vector2(0.0, 0.0);
        var size = new Size(width, height);
        // Act / Assert
        var exception = assertThrows(
                InvalidRectangleException.class,
                () -> { var rectangle = new Rectangle(topLeft, size); }
        );
        assertEquals(1, exception.invariantViolations().size());
        assertContainsFullFieldName(fieldName, exception);
        assertFieldErrorMessageIsMustBeGreaterThanZero(fieldName, exception);
    }

    @ParameterizedTest
    @CsvSource({
            "-1.0, -1.0",
            "0.0, 0.0",
    })
    void givenInvalidWidthAndHeight_whenCreatingRectangle_thenInvalidRectangleExceptionIsThrownWithTwoInvariantViolations(
            double width, double height
    ) {
        // Arrange
        var topLeft = new Vector2(0.0, 0.0);
        var size = new Size(width, height);
        // Act / Assert
        var exception = assertThrows(
                InvalidRectangleException.class,
                () -> { var rectangle = new Rectangle(topLeft, size); }
        );
        assertEquals(2, exception.invariantViolations().size());
        assertFieldErrorsArePresentForWidthAndHeight(exception);
    }

    private static void assertFieldErrorsArePresentForWidthAndHeight(InvalidRectangleException exception) {
        assertContainsFullFieldName("width", exception);
        assertFieldErrorMessageIsMustBeGreaterThanZero("width", exception);
        assertContainsFullFieldName("height", exception);
        assertFieldErrorMessageIsMustBeGreaterThanZero("height", exception);
    }

    private static void assertFieldErrorMessageIsMustBeGreaterThanZero(
            String fieldName, InvalidRectangleException exception
    ) {
        var fullFieldName = "size." + fieldName;
        var actualErrorMessages = exception.invariantViolations().get(fullFieldName);
        var expectedErrorMessage = String.format("The %s must be greater than zero.", fieldName);
        assertArrayEquals(new String[] {expectedErrorMessage}, actualErrorMessages);
    }

    private static void assertContainsFullFieldName(String fieldName, InvalidRectangleException exception) {
        var fullFieldName = "size." + fieldName;
        assertTrue(exception.invariantViolations().containsKey(fullFieldName),
                String.format("Expected invariantViolations to contain \"%s\".", fullFieldName));
    }
}