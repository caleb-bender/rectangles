package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Size;
import com.caleb.rectangles.domain.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionFinderTest {

    private final IntersectionFinder intersectionFinder;

    IntersectionFinderTest() {
        intersectionFinder = new IntersectionFinder();
    }

    @Test
    void givenTwoRectanglesThatDoNotOverlap_whenFindingIntersections_thenIntersectionsIsEmpty() {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(0, 0), new Size(1,1));
        var rectangleB = new Rectangle(new Vector2(2, 0), new Size(1,1));
        // Act
        var intersections = intersectionFinder.findAll(rectangleA, rectangleB);
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
    void givenTwoRectanglesWithTwoPointsOfOverlap_whenFindingIntersections_thenThereAreTwoIntersections(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2
    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        // Act
        var intersections = intersectionFinder.findAll(rectangleA, rectangleB);
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
    void givenTwoRectanglesWithOnePointOfOverlap_whenFindingIntersections_thenThereIsASingleIntersection(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2
    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        // Act
        var intersections = intersectionFinder.findAll(rectangleA, rectangleB);
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
    void givenTwoRectanglesWithThreePointsOfOverlap_whenFindingIntersections_thenThereAreThreeIntersections(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2
    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        // Act
        var intersections = intersectionFinder.findAll(rectangleA, rectangleB);
        // Assert
        assertEquals(3, intersections.length);
    }

    @Test
    void givenTwoRectanglesWithFourPointsOfOverlap_whenFindingIntersections_thenThereAreFourIntersections() {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(0.0, 0.0), new Size(3.0, 2.0));
        var rectangleB = new Rectangle(new Vector2(0.0, 0.0), new Size(3.0, 2.0));
        // Act
        var intersections = intersectionFinder.findAll(rectangleA, rectangleB);
        // Assert
        assertEquals(4, intersections.length);
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 0.0, 3.0, 2.0,    3.0, -2.0, 1.0, 1.0,    3.0, -2.0",
            "0.0, 0.0, 3.0, 2.0,    -1.0, -2.0, 1.0, 1.0,    0.0, -2.0",
            "0.0, 0.0, 3.0, 2.0,    -1.0, 1.0, 1.0, 1.0,    0.0, 0.0",
            "0.0, 0.0, 3.0, 2.0,    3.0, 1.0, 1.0, 1.0,    3.0, 0.0",
    })
    void givenTwoRectanglesWithOnePointOfOverlap_whenFindingIntersections_thenTheSingleIntersectionIsCorrect(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2,
            double ix1, double iy1
    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        var expectedIntersections = new Vector2[] { new Vector2(ix1, iy1) };
        // Act
        var intersections = intersectionFinder.findAll(rectangleA, rectangleB);
        // Assert
        assertContainsExactlyExpectedIntersections(intersections, expectedIntersections);
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 0.0, 3.0, 2.0,    2.0, -1.0, 2.0, 2.0,    2.0, -2.0,    3.0, -1.0",
            "0.0, 0.0, 3.0, 2.0,    -1.0, 1.0, 3.0, 2.0,    0.0, -1.0,    2.0, 0.0",
            "0.0, 0.0, 3.0, 2.0,    -1.0, -1.0, 2.0, 2.0,    0.0, -1.0,    1.0, -2.0",
            "0.0, 0.0, 3.0, 2.0,    2.0, 1.0, 2.0, 2.0,    2.0, 0.0,    3.0, -1.0"
    })
    void givenTwoRectanglesWithTwoPointsOfOverlap_whenFindingIntersections_thenTheTwoIntersectionsAreCorrect(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2,
            double ix1, double iy1,
            double ix2, double iy2
    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        var expectedIntersections = new Vector2[] { new Vector2(ix1, iy1), new Vector2(ix2, iy2) };
        // Act
        var intersections = intersectionFinder.findAll(rectangleA, rectangleB);
        // Assert
        assertContainsExactlyExpectedIntersections(intersections, expectedIntersections);
    }

    @ParameterizedTest
    @CsvSource({
            "-2.0, 1.0, 4.0, 2.0,    1.0, 1.0, 1.0, 1.0,    1.0, 1.0,    2.0, 1.0,    2.0, 0.0",
            "-2.0, 1.0, 4.0, 2.0,    -2.0, 1.0, 2.0, 1.0,    -2.0, 1.0,    0.0, 1.0,    -2.0, 0.0",
            "-2.0, 1.0, 4.0, 2.0,    -2.0, 0.0, 2.0, 1.0,    -2.0, 0.0,    -2.0, -1.0,    0.0, -1.0",
            "-2.0, 1.0, 4.0, 2.0,    1.0, 0.0, 1.0, 1.0,    2.0, 0.0,    2.0, -1.0,    1.0, -1.0"
    })
    void givenTwoRectanglesWithThreePointsOfOverlap_whenFindingIntersections_thenTheThreeIntersectionsAreCorrect(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2,
            double ix1, double iy1,
            double ix2, double iy2,
            double ix3, double iy3
    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        var expectedIntersections = new Vector2[]{
                new Vector2(ix1, iy1), new Vector2(ix2, iy2), new Vector2(ix3, iy3)
        };
        // Act
        var intersections = intersectionFinder.findAll(rectangleA, rectangleB);
        // Assert
        assertContainsExactlyExpectedIntersections(intersections, expectedIntersections);
    }

    @ParameterizedTest
    @CsvSource({
            "1.0, 1.0, 1.0, 1.0,    1.0, 1.0, 1.0, 1.0,    1.0, 1.0,    2.0, 1.0,    2.0, 0.0,    1.0, 0.0",
            "-2.0, 1.0, 4.0, 2.0,   -1.0, 1.0, 2.0, 2.0,   -1.0, 1.0,   1.0, 1.0,    1.0, -1.0,   -1.0, -1.0",
            "-2.0, 1.0, 4.0, 2.0,   -1.0, 2.0, 2.0, 4.0,   -1.0, 1.0,   1.0, 1.0,    1.0, -1.0,   -1.0, -1.0",
    })
    void givenTwoRectanglesWithThreePointsOfOverlap_whenFindingIntersections_thenTheThreeIntersectionsAreCorrect(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2,
            double ix1, double iy1, double ix2, double iy2,
            double ix3, double iy3, double ix4, double iy4

    ) {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rectangleB = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        var expectedIntersections = new Vector2[]{
                new Vector2(ix1, iy1), new Vector2(ix2, iy2), new Vector2(ix3, iy3),
                new Vector2(ix4, iy4)
        };
        // Act
        var intersections = intersectionFinder.findAll(rectangleA, rectangleB);
        // Assert
        assertContainsExactlyExpectedIntersections(intersections, expectedIntersections);
    }

    @Test
    void givenTwoRectanglesThatOverlap_whenFindingIntersections_itIsCommutative() {
        // Arrange
        var rectangleA = new Rectangle(new Vector2(-2.0, 1.0), new Size(4.0, 2.0));
        var rectangleB = new Rectangle(new Vector2(-1.0, 2.0), new Size(5.0, 2.0));
        var expectedIntersections = new Vector2[]{
                new Vector2(-1.0, 1.0), new Vector2(2.0, 0.0)
        };
        // Act
        var intersectionsAToB = intersectionFinder.findAll(rectangleA, rectangleB);
        var intersectionsBToA = intersectionFinder.findAll(rectangleB, rectangleA);
        // Assert
        assertContainsExactlyExpectedIntersections(intersectionsAToB, expectedIntersections);
        assertContainsExactlyExpectedIntersections(intersectionsBToA, expectedIntersections);
    }

    private void assertContainsExactlyExpectedIntersections(Vector2[] intersectionsToTest, Vector2[] expectedIntersections) {
        assertEquals(expectedIntersections.length, intersectionsToTest.length);
        for (Vector2 expectedIntersection : expectedIntersections) {
            var containsIntersection = Arrays.asList(intersectionsToTest).contains(expectedIntersection);
            assertTrue(containsIntersection);
        }
    }
}