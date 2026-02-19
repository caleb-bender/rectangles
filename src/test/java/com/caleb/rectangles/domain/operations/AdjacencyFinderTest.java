package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.LineSegment;
import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Size;
import com.caleb.rectangles.domain.Vector2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyFinderTest {

    private final AdjacencyFinder adjacencyFinder = new AdjacencyFinder();

    @ParameterizedTest
    @CsvSource({
            "0.0, 2.0, 3.0, 4.0,    1.0, 1.0, 1.0, 1.0",
            "3.5, 2.0, 3.0, 4.0,    0.0, 2.0, 3.0, 4.0",
    })
    void givenTwoRectanglesThatAreNotAdjacent_whenFindingAdjacency_thenEmptyIsReturned(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2
    ) {
        // Arrange
        var rect1 = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rect2 = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        // Act
        var adjacency = adjacencyFinder.find(rect1, rect2);
        // Assert
        assertTrue(adjacency.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 2.0, 3.0, 4.0,    3.0, 2.0, 1.0, 4.0,    X, 3.0, -2.0, 2.0",
            "0.0, 3.0, 3.0, 1.0,    0.0, 2.0, 3.0, 4.0,    Y, 2.0, 0.0, 3.0",
            "0.0, 2.0, 3.0, 4.0,    0.0, -2.0, 3.0, 1.0,   Y, -2.0, 0.0, 3.0",
            "-1.0, 2.0, 1.0, 4.0,   0.0, 2.0, 3.0, 4.0,    X, 0.0, -2.0, 2.0"
    })
    void givenTwoRectanglesThatAreAdjacentProper_whenFindingAdjacency_thenAdjacencyTypeIsProperAndSegmentIsCorrect(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2,
            LineSegment.OrthogonalAxis axis, double constant,
            double lower, double upper
    ) {
        // Arrange
        var rect1 = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rect2 = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        var expectedSegment = new LineSegment(axis, constant, new LineSegment.ParallelAxisBounds(lower, upper));
        // Act
        var adjacency = adjacencyFinder.find(rect1, rect2);
        // Assert
        assertTrue(adjacency.isPresent());
        assertEquals(Adjacency.Types.Proper, adjacency.get().type());
        assertEquals(expectedSegment, adjacency.get().segment());
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 2.0, 3.0, 4.0,    3.0, 0.0, 2.0, 3.0,    X, 3.0, -2.0, 0.0",
            "-1.0, 3.0, 2.0, 1.0,   0.0, 2.0, 3.0, 4.0,    Y, 2.0, 0.0, 1.0",
            "0.0, 2.0, 3.0, 4.0,    -1.0, 0.0, 1.0, 3.0,    X, 0.0, -2.0, 0.0",
            "1.0, -2.0, 5.0, 1.0,    0.0, 2.0, 3.0, 4.0,    Y, -2.0, 1.0, 3.0",

    })
    void givenTwoRectanglesThatArePartiallyAdjacent_whenFindingAdjacency_thenAdjacencyTypeIsPartialAndSegmentIsCorrect(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2,
            LineSegment.OrthogonalAxis axis, double constant,
            double lower, double upper
    ) {
        // Arrange
        var rect1 = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rect2 = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        var expectedSegment = new LineSegment(axis, constant, new LineSegment.ParallelAxisBounds(lower, upper));
        // Act
        var adjacency = adjacencyFinder.find(rect1, rect2);
        // Assert
        assertTrue(adjacency.isPresent());
        assertEquals(Adjacency.Types.Partial, adjacency.get().type());
        assertEquals(expectedSegment, adjacency.get().segment());
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 2.0, 3.0, 4.0,    3.0, 2.0, 1.0, 2.0,    X, 3.0, 0.0, 2.0",
            "1.0, 3.0, 1.0, 1.0,    0.0, 2.0, 3.0, 4.0,    Y, 2.0, 1.0, 2.0",
            "0.0, 2.0, 3.0, 4.0,    -1.0, 1.0, 1.0, 3.0,   X, 0.0, -2.0, 1.0",
            "2.0, -2.0, 1.0, 1.0,   0.0, 2.0, 3.0, 4.0,    Y, -2.0, 2.0, 3.0",
            "0.0, -2.0, 5.0, 1.0,    0.0, 2.0, 3.0, 4.0,   Y, -2.0, 0.0, 3.0"
    })
    void givenOneRectangleThatIsSubLineAdjacentToAnother_whenFindingAdjacency_thenAdjacencyTypeIsSubLineAndSegmentIsCorrect(
            double x1, double y1, double w1, double h1,
            double x2, double y2, double w2, double h2,
            LineSegment.OrthogonalAxis axis, double constant,
            double lower, double upper
    ) {
        // Arrange
        var rect1 = new Rectangle(new Vector2(x1, y1), new Size(w1, h1));
        var rect2 = new Rectangle(new Vector2(x2, y2), new Size(w2, h2));
        var expectedSegment = new LineSegment(axis, constant, new LineSegment.ParallelAxisBounds(lower, upper));
        // Act
        var adjacency = adjacencyFinder.find(rect1, rect2);
        // Assert
        assertTrue(adjacency.isPresent());
        assertEquals(Adjacency.Types.SubLine, adjacency.get().type());
        assertEquals(expectedSegment, adjacency.get().segment());
    }
}