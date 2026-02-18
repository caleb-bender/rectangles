package com.caleb.rectangles.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LineSegmentTest {


    @Test
    void whenCreatingLineSegment_thenItHasCorrectParallelAxisAndOrthogonalBounds() {
        // Arrange
        var axis = LineSegment.OrthogonalAxis.X;
        var continuousValue = 2.0;
        var bounds = new LineSegment.ParallelAxisBounds(1, 3);
        // Act
        var lineSegment = new LineSegment(axis, continuousValue, bounds);
        // Assert
        assertEquals(axis, lineSegment.axis());
        assertEquals(continuousValue, lineSegment.continuousValue());
        assertEquals(bounds, lineSegment.bounds());
    }

    @Test
    void whenGettingIntersectionOfTwoParallelLinesWithSameBounds_thenTheyDoNot() {
        // Arrange
        var line1 = new LineSegment(
                LineSegment.OrthogonalAxis.X,
                2.0,
                new LineSegment.ParallelAxisBounds(1.0, 3.0)
        );
        var line2 = new LineSegment(
                LineSegment.OrthogonalAxis.X,
                4.0,
                new LineSegment.ParallelAxisBounds(1.0, 3.0)
        );
        // Act
        var intersection = line1.intersectionWith(line2);
        // Assert
        assertFalse(intersection.isPresent());
    }

    @Test
    void whenGettingIntersectionOfTwoParallelLinesWithNonOverlappingBoundsIntersect_thenTheyDoNot() {
        // Arrange
        var line1 = new LineSegment(
                LineSegment.OrthogonalAxis.Y,
                2.0,
                new LineSegment.ParallelAxisBounds(1.0, 3.0)
        );
        var line2 = new LineSegment(
                LineSegment.OrthogonalAxis.Y,
                4.0,
                new LineSegment.ParallelAxisBounds(-2.0, -1.0)
        );
        // Act
        var intersection = line1.intersectionWith(line2);
        // Assert
        assertFalse(intersection.isPresent());
    }

    @ParameterizedTest
    @CsvSource({
            "X, 2.0, -1.0, 2.0,    Y, 1.0, 1.0, 3.0,    2.0, 1.0",
            "X, -2.0, 2.0, 3.0,    Y, 3.0, -3.0, -1.0,  -2.0, 3.0"
    })
    void givenTwoOrthogonalOverlappingLines_whenGettingIntersection_thenTheIntersectionExistsAndIsCommutative(
            LineSegment.OrthogonalAxis axis1, double continuousValue1, double lower1, double upper1,
            LineSegment.OrthogonalAxis axis2, double continuousValue2, double lower2, double upper2,
            double ix, double iy
    ) {
        // Arrange
        var line1 = new LineSegment(axis1, continuousValue1, new LineSegment.ParallelAxisBounds(lower1, upper1));
        var line2 = new LineSegment(axis2, continuousValue2, new LineSegment.ParallelAxisBounds(lower2, upper2));
        var expectedIntersection = new Vector2(ix, iy);
        // Act
        var line1ThroughLine2 = line1.intersectionWith(line2);
        var line2ThroughLine1 = line2.intersectionWith(line1);
        // Assert
        assertTrue(line1ThroughLine2.isPresent());
        assertEquals(expectedIntersection, line1ThroughLine2.get());
        assertTrue(line2ThroughLine1.isPresent());
        assertEquals(expectedIntersection, line2ThroughLine1.get());
    }

}