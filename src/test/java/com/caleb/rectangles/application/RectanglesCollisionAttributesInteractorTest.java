package com.caleb.rectangles.application;

import com.caleb.rectangles.domain.operations.IAdjacencyFinder;
import com.caleb.rectangles.domain.operations.IIntersectionFinder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static com.caleb.rectangles.application.RectanglesCollisionAttributesQuery.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RectanglesCollisionAttributesInteractorTest {

    @Mock
    private IAdjacencyFinder adjacencyFinder;

    @Mock
    private IIntersectionFinder intersectionFinder;

    @InjectMocks
    private RectanglesCollisionAttributesInteractor interactor;

    @ParameterizedTest
    @CsvSource(
            textBlock = """
                'x', '0.0', '3.0', '2.0',    '0.0', '1.0', '2.0', '2.0', \
                'rectangle1.x', 'The top-left x-coordinate of rectangle1 is not a number'
                \
                '0.0', 'y', '3.0', '2.0',    '0.0', '1.0', '2.0', '2.0', \
                'rectangle1.y', 'The top-left y-coordinate of rectangle1 is not a number'
                \
                '0.0', '0.0', 'w', '2.0',    '0.0', '1.0', '2.0', '2.0', \
                'rectangle1.size.width', 'The width of rectangle1 is not a number'
                \
                '0.0', '0.0', '3.0', 'h',    '0.0', '1.0', '2.0', '2.0', \
                'rectangle1.size.height', 'The height of rectangle1 is not a number'
                \
                '0.0', '0.0', '3.0', '2.0',    'x', '1.0', '2.0', '2.0', \
                'rectangle2.x', 'The top-left x-coordinate of rectangle2 is not a number'
                \
                '0.0', '0.0', '3.0', '2.0',    '0.0', 'y', '2.0', '2.0', \
                'rectangle2.y', 'The top-left y-coordinate of rectangle2 is not a number'
                \
                '0.0', '0.0', '3.0', '2.0',    '0.0', '1.0', 'w', '2.0', \
                'rectangle2.size.width', 'The width of rectangle2 is not a number'
                \
                '0.0', '0.0', '3.0', '2.0',    '0.0', '1.0', '2.0', 'h', \
                'rectangle2.size.height', 'The height of rectangle2 is not a number'
            """
    )
    void givenNumberFieldOfRectangleDataIsNotParsable_whenExecutingInteractor_ThenResponseContainsErrors(
            String r1x, String r1y, String r1w, String r1h,
            String r2x, String r2y, String r2w, String r2h,
            String fieldName, String fieldErrorMessage
    ) {
        // Arrange
        var query = new RectanglesCollisionAttributesQuery(
            new RectangleRawData(r1x, r1y, r1w, r1h),
            new RectangleRawData(r2x, r2y, r2w, r2h),
            true, true, true
        );
        // Act
        var response = interactor.Execute(query);
        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.errors().containsKey(fieldName),
        String.format("The response was expected to have a field error for \"%s\"", fieldName));
        assertArrayEquals(new String[] { fieldErrorMessage }, response.errors().get(fieldName));
    }

    @Test
    void givenAllFlagsAreFalse_whenExecutingInteractor_ThenResponseContainsErrorStatingOneFlagIsRequired() {
        // Arrange
        var query = new RectanglesCollisionAttributesQuery(
                new RectangleRawData("0.0", "0.0", "3.0", "2.0"),
                new RectangleRawData("0.0", "1.0", "2.0", "2.0"),
                false, false, false
        );
        // Act
        var response = interactor.Execute(query);
        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.errors().containsKey("queryFlags"), "Expected an error in response for \"queryFlags\"");
        assertArrayEquals(new String[] { "At least one query flag must be set" }, response.errors().get("queryFlags"));
    }
}