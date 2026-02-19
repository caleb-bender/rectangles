package com.caleb.rectangles.domain.rectangle;

import com.caleb.rectangles.domain.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RectanglePropertiesTest {

    private final LineSegment expectedTop = new LineSegment(
            LineSegment.OrthogonalAxis.Y, 1.0, new LineSegment.ParallelAxisBounds(-2.0, 2.0)
    );
    private final LineSegment expectedRight = new LineSegment(
            LineSegment.OrthogonalAxis.X, 2.0, new LineSegment.ParallelAxisBounds(-1.0, 1.0)
    );
    private final LineSegment expectedBottom = new LineSegment(
            LineSegment.OrthogonalAxis.Y, -1.0, new LineSegment.ParallelAxisBounds(-2.0, 2.0)
    );
    private final LineSegment expectedLeft = new LineSegment(
            LineSegment.OrthogonalAxis.X, -2.0, new LineSegment.ParallelAxisBounds(-1.0, 1.0)
    );
    private final Rectangle rectangle = new Rectangle(new Vector2(-2.0, 1.0), new Size(4.0, 2.0));

    @Test
    void whenGettingARectanglesSegments_thenTheyAreCorrect() {
        // Act
        var segments = rectangle.segments();
        // Assert
        assertEquals(expectedTop, segments[Rectangle.TOP_SEGMENT]);
        assertEquals(expectedRight, segments[Rectangle.RIGHT_SEGMENT]);
        assertEquals(expectedBottom, segments[Rectangle.BOTTOM_SEGMENT]);
        assertEquals(expectedLeft, segments[Rectangle.LEFT_SEGMENT]);
    }

    @Test
    void whenCheckingIfARectangleContainsItself_thenTrueIsReturned() {
        // Act
        var containsSelf = rectangle.contains(rectangle);
        // Assert
        assertTrue(containsSelf);
    }

    @Test
    void givenTwoRectanglesThatDoNotOverlap_whenCheckingContains_thenFalseIsIsReturned() {
        // Arrange
        var rectangle1 = new Rectangle(new Vector2(0.0, 0.0), new Size(1.0, 1.0));
        var rectangle2 = new Rectangle(new Vector2(10.0, 10.0), new Size(1.0, 1.0));
        // Act
        var rect1ContainsRect2 = rectangle1.contains(rectangle2);
        // Assert
        assertFalse(rect1ContainsRect2);
    }

    @Test
    void givenTwoRectanglesThatOverlapAndAreNotIdentical_whenCheckingContains_ThenFalseIsReturned() {
        // Arrange
        var rectangle1 = new Rectangle(new Vector2(0.0, 0.0), new Size(2.0, 1.0));
        var rectangle2 = new Rectangle(new Vector2(1.0, 1.0), new Size(1.0, 2.0));
        // Act
        var rect1ContainsRect2 = rectangle1.contains(rectangle2);
        // Assert
        assertFalse(rect1ContainsRect2);
    }

    @Test
    void givenTwoRectanglesThatDoNotOverlapAndOneIsSmaller_whenCheckingContainsBothWays_thenSmallIsInBigButBigIsNotInSmall() {
        // Arrange
        var big = new Rectangle(new Vector2(0.0, 2.0), new Size(3.0, 3.0));
        var small = new Rectangle(new Vector2(1.0, 1.0), new Size(1.0, 1.0));
        // Act
        var bigContainsSmall = big.contains(small);
        var smallContainsBig = small.contains(big);
        // Assert
        assertTrue(bigContainsSmall);
        assertFalse(smallContainsBig);
    }
}
