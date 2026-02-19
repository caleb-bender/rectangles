package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.LineSegment;
import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Size;
import com.caleb.rectangles.domain.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.assertj.core.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        var adjacencyList = adjacencyFinder.findAll(rect1, rect2);
        // Assert
        assertEquals(0, adjacencyList.length);
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
        var adjacencyList = adjacencyFinder.findAll(rect1, rect2);
        // Assert
        assertEquals(1, adjacencyList.length);
        var adjacency = adjacencyList[0];
        assertEquals(Adjacency.Types.Proper, adjacency.type());
        assertEquals(expectedSegment, adjacency.segment());
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 2.0, 3.0, 4.0,    3.0, 0.0, 2.0, 3.0,    X, 3.0, -2.0, 0.0",
            "-1.0, 3.0, 2.0, 1.0,   0.0, 2.0, 3.0, 4.0,    Y, 2.0, 0.0, 1.0",
            "0.0, 2.0, 3.0, 4.0,    -1.0, 0.0, 1.0, 3.0,    X, 0.0, -2.0, 0.0",
            "1.0, -2.0, 5.0, 1.0,    0.0, 2.0, 3.0, 4.0,    Y, -2.0, 1.0, 3.0",

            "1.0, 3.0, 1.0, 1.0,    0.0, 2.0, 3.0, 4.0,    Y, 2.0, 1.0, 2.0",
            "3.0, 2.0, 1.0, 2.0,    0.0, 2.0, 3.0, 4.0,    X, 3.0, 0.0, 2.0",
            "2.0, -2.0, 1.0, 1.0,   0.0, 2.0, 3.0, 4.0,    Y, -2.0, 2.0, 3.0",
            "4.0, 6.0, 6.0, 4.0,    10.0, 8.0, 2.0, 8.0,   X, 10.0, 2.0, 6.0"

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
        var adjacencyList = adjacencyFinder.findAll(rect1, rect2);
        // Assert
        assertEquals(1, adjacencyList.length);
        var adjacency = adjacencyList[0];
        assertEquals(Adjacency.Types.Partial, adjacency.type());
        assertEquals(expectedSegment, adjacency.segment());
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 2.0, 3.0, 4.0,    3.0, 2.0, 1.0, 2.0,    X, 3.0, 0.0, 2.0",
            "0.0, 2.0, 3.0, 4.0,    1.0, 3.0, 1.0, 1.0,    Y, 2.0, 1.0, 2.0",
            "0.0, 2.0, 3.0, 4.0,    -1.0, 1.0, 1.0, 3.0,   X, 0.0, -2.0, 1.0",
            "0.0, 2.0, 3.0, 4.0,    2.0, -2.0, 1.0, 1.0,    Y, -2.0, 2.0, 3.0",
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
        var adjacencyList = adjacencyFinder.findAll(rect1, rect2);
        // Assert
        assertEquals(1, adjacencyList.length);
        var adjacency = adjacencyList[0];
        assertEquals(Adjacency.Types.SubLine, adjacency.type());
        assertEquals(expectedSegment, adjacency.segment());
    }

    @Test
    void givenOneRectangleThatSubLinesAnotherOnBothSides_whenFindingAdjacencyList_thenAdjacencyTypesAreSubLinesAndSegmentsAreCorrect() {
        // Arrange
        var rect1 = new Rectangle(new Vector2(0.0, 2.0), new Size(3.0, 4.0));
        var rect2 = new Rectangle(new Vector2(0.0, 1.0), new Size(3.0, 2.0));
        var expectedLeftSegment = new LineSegment(
                LineSegment.OrthogonalAxis.X, 0.0,
                new LineSegment.ParallelAxisBounds(-1.0, 1.0)
        );
        var expectedRightSegment = new LineSegment(
                LineSegment.OrthogonalAxis.X, 3.0,
                new LineSegment.ParallelAxisBounds(-1.0, 1.0)
        );
        // Act
        var adjacencyList = adjacencyFinder.findAll(rect1, rect2);
        // Assert
        assertEquals(2, adjacencyList.length);
        assertThat(adjacencyList).allMatch(adjacency -> {
            return adjacency.type() == Adjacency.Types.SubLine;
        });
        assertThat(adjacencyList).anyMatch(adjacency -> {
            return adjacency.segment().equals(expectedLeftSegment);
        });
        assertThat(adjacencyList).anyMatch(adjacency -> {
            return adjacency.segment().equals(expectedRightSegment);
        });
    }

    @Test
    void givenOneRectangleThatPartialsAnotherOnBothSides_whenFindingAdjacencyList_thenAdjacencyTypesArePartialAndSegmentsAreCorrect() {
        // Arrange
        var rect1 = new Rectangle(new Vector2(0.0, 2.0), new Size(3.0, 4.0));
        var rect2 = new Rectangle(new Vector2(0.0, 3.0), new Size(3.0, 2.0));
        var expectedLeftSegment = new LineSegment(
                LineSegment.OrthogonalAxis.X, 0.0,
                new LineSegment.ParallelAxisBounds(1.0, 2.0)
        );
        var expectedRightSegment = new LineSegment(
                LineSegment.OrthogonalAxis.X, 3.0,
                new LineSegment.ParallelAxisBounds(1.0, 2.0)
        );
        // Act
        var adjacencyList = adjacencyFinder.findAll(rect1, rect2);
        // Assert
        assertEquals(2, adjacencyList.length);
        assertThat(adjacencyList).allMatch(adjacency -> {
            return adjacency.type() == Adjacency.Types.Partial;
        });
        assertThat(adjacencyList).anyMatch(adjacency -> {
            return adjacency.segment().equals(expectedLeftSegment);
        });
        assertThat(adjacencyList).anyMatch(adjacency -> {
            return adjacency.segment().equals(expectedRightSegment);
        });
    }

    @Test
    void givenOneRectangleThatIsSubLineAndProperOnAnotherOnMultipleSides_whenFindingAdjacencyList_thenAdjacencyTypesAndSegmentsAreCorrect() {
        // Arrange
        var rect1 = new Rectangle(new Vector2(0.0, 2.0), new Size(3.0, 4.0));
        var rect2 = new Rectangle(new Vector2(0.0, 2.0), new Size(3.0, 2.0));
        var expectedLeftAdjacency = new Adjacency(Adjacency.Types.SubLine,
                new LineSegment(
                    LineSegment.OrthogonalAxis.X, 0.0,
                    new LineSegment.ParallelAxisBounds(0.0, 2.0)
                )
        );
        var expectedRightAdjacency = new Adjacency(Adjacency.Types.SubLine,
                new LineSegment(
                        LineSegment.OrthogonalAxis.X, 3.0,
                        new LineSegment.ParallelAxisBounds(0.0, 2.0)
                )
        );
        var expectedTopAdjacency = new Adjacency(Adjacency.Types.Proper,
                new LineSegment(
                        LineSegment.OrthogonalAxis.Y, 2.0,
                        new LineSegment.ParallelAxisBounds(0.0, 3.0)
                )
        );
        // Act
        var adjacencyList = adjacencyFinder.findAll(rect1, rect2);
        // Assert
        assertEquals(3, adjacencyList.length);
        assertThat(adjacencyList).anyMatch(adjacency -> {
            return adjacency.equals(expectedLeftAdjacency);
        });
        assertThat(adjacencyList).anyMatch(adjacency -> {
            return adjacency.equals(expectedRightAdjacency);
        });
        assertThat(adjacencyList).anyMatch(adjacency -> {
            return adjacency.equals(expectedTopAdjacency);
        });
    }

    @Test
    void givenOneRectangleThatIsIdenticalToAnother_whenFindingAdjacencyList_thenAdjacencyTypesAreAllProper() {
        // Arrange
        var rect1 = new Rectangle(new Vector2(0.0, 2.0), new Size(3.0, 4.0));
        var rect2 = new Rectangle(new Vector2(0.0, 2.0), new Size(3.0, 4.0));
        // Act
        var adjacencyList = adjacencyFinder.findAll(rect1, rect2);
        // Assert
        assertEquals(4, adjacencyList.length);
        assertThat(adjacencyList).allMatch(adjacency -> {
           return adjacency.type() == Adjacency.Types.Proper;
        });
    }

}