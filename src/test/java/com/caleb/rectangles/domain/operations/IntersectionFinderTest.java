package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Size;
import com.caleb.rectangles.domain.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionFinderTest {

    private final IntersectionFinder intersectionFinder;

    IntersectionFinderTest() {
        intersectionFinder = new IntersectionFinder();
    }

    @Test
    void givenTwoRectanglesThatDoNotOverlap_whenFindingIntersections_ThenIntersectionsIsEmpty() {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(0, 0), new Size(1,1));
        var rectangleB = new Rectangle(new Vector2(2, 0), new Size(1,1));
        // Act
        var intersections = intersectionFinder.FindAll(rectangleA, rectangleB);
        // Assert
        assertEquals(0, intersections.length);
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 0.0, 3.0, 2.0,    2.0, -1.0, 2.0, 2.0",
            "0.0, 0.0, 3.0, 2.0,    -1.0, 1.0, 3.0, 2.0",
            "0.0, 0.0, 3.0, 2.0,    -1.0, -1.0, 2.0, 2.0",
            "0.0, 0.0, 3.0, 2.0,    2.0, 1.0, 2.0, 2.0"
    })
    void givenTwoRectanglesWithTwoPointsOfOverlap_whenFindingIntersections_ThenThereAreTwoIntersections(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2
    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        // Act
        var intersections = intersectionFinder.FindAll(rectangleA, rectangleB);
        // Assert
        assertEquals(2, intersections.length);
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 0.0, 3.0, 2.0,    3.0, -2.0, 1.0, 1.0",
            "0.0, 0.0, 3.0, 2.0,    -1.0, 1.0, 1.0, 1.0",
            "0.0, 0.0, 3.0, 2.0,    -1.0, -2.0, 1.0, 1.0",
            "0.0, 0.0, 3.0, 2.0,    3.0, 1.0, 1.0, 1.0",
    })
    void givenTwoRectanglesWithOnePointOfOverlap_whenFindingIntersections_ThenThereIsASingleIntersection(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2
    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        // Act
        var intersections = intersectionFinder.FindAll(rectangleA, rectangleB);
        // Assert
        assertEquals(1, intersections.length);
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 0.0, 3.0, 2.0,    2.0, -1.0, 1.0, 1.0",
            "0.0, 0.0, 3.0, 2.0,    0.0, -1.0, 1.0, 1.0",
            "0.0, 0.0, 3.0, 2.0,    0.0, 0.0, 1.0, 1.0",
            "0.0, 0.0, 3.0, 2.0,    2.0, 0.0, 1.0, 1.0",
    })
    void givenTwoRectanglesWithThreePointsOfOverlap_whenFindingIntersections_ThenThereAreThreeIntersections(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2
    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        // Act
        var intersections = intersectionFinder.FindAll(rectangleA, rectangleB);
        // Assert
        assertEquals(3, intersections.length);
    }

    @Test
    void givenTwoRectanglesWithFourPointsOfOverlap_whenFindingIntersections_ThenThereAreFourIntersections() {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(0.0, 0.0), new Size(3.0, 2.0));
        var rectangleB = new Rectangle(new Vector2(0.0, 0.0), new Size(3.0, 2.0));
        // Act
        var intersections = intersectionFinder.FindAll(rectangleA, rectangleB);
        // Assert
        assertEquals(4, intersections.length);
    }


}