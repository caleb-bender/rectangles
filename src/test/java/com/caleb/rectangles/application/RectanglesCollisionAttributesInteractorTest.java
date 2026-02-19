package com.caleb.rectangles.application;

import com.caleb.rectangles.domain.LineSegment;
import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Vector2;
import com.caleb.rectangles.domain.operations.Adjacency;
import com.caleb.rectangles.domain.operations.IAdjacencyFinder;
import com.caleb.rectangles.domain.operations.IIntersectionFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.caleb.rectangles.application.RectanglesCollisionAttributesResponse.*;
import static com.caleb.rectangles.application.RectanglesCollisionAttributesQuery.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RectanglesCollisionAttributesInteractorTest {

    @Mock
    private IAdjacencyFinder adjacencyFinder;
    @Mock
    private IIntersectionFinder intersectionFinder;
    @InjectMocks
    private RectanglesCollisionAttributesInteractor interactor;

    private final Adjacency[] dummyAdjacencyList = new Adjacency[]{
        new Adjacency(
            Adjacency.Types.Partial,
            new LineSegment(LineSegment.OrthogonalAxis.X, 0.0,
                    new LineSegment.ParallelAxisBounds(0.0, 1.0)
            )
        )
    };

    private final Vector2[] dummyIntersections = new Vector2[] { new Vector2(-1.0, -1.0) };

    @BeforeEach
    void setupMocks() {
        lenient().when(intersectionFinder.findAll(isA(Rectangle.class), isA(Rectangle.class)))
                .thenReturn(dummyIntersections);
        lenient().when(adjacencyFinder.findAll(isA(Rectangle.class), isA(Rectangle.class)))
                .thenReturn(dummyAdjacencyList);
    }

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
    void givenNumberFieldOfRectangleDataIsNotParsable_whenExecutingInteractor_thenResponseContainsErrors(
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
        assertIsErrorResponse(response);
        assertTrue(response.errors().containsKey(fieldName),
        String.format("The response was expected to have a field error for \"%s\"", fieldName));
        assertArrayEquals(new String[] { fieldErrorMessage }, response.errors().get(fieldName));
    }

    @Test
    void givenAllFlagsAreFalse_whenExecutingInteractor_thenResponseContainsErrorStatingOneFlagIsRequired() {
        // Arrange
        var query = new RectanglesCollisionAttributesQuery(
            new RectangleRawData("0.0", "0.0", "3.0", "2.0"),
            new RectangleRawData("0.0", "1.0", "2.0", "2.0"),
            false, false, false
        );
        // Act
        var response = interactor.Execute(query);
        // Assert
        assertIsErrorResponse(response);
        assertTrue(response.errors().containsKey("queryFlags"), "Expected an error in response for \"queryFlags\"");
        assertArrayEquals(new String[] { "At least one query flag must be set" }, response.errors().get("queryFlags"));
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        '0.0', '0.0', '-3.0', '2.0',    '0.0', '1.0', '2.0', '2.0', \
        'rectangle1.size.width', 'The width must be greater than zero'
        \
        '0.0', '0.0', '3.0', '-2.0',    '0.0', '1.0', '2.0', '2.0', \
        'rectangle1.size.height', 'The height must be greater than zero'
        \
        '0.0', '0.0', '3.0', '2.0',    '0.0', '1.0', '-2.0', '2.0', \
        'rectangle2.size.width', 'The width must be greater than zero'
        \
        '0.0', '0.0', '3.0', '2.0',    '0.0', '1.0', '2.0', '-2.0', \
        'rectangle2.size.height', 'The height must be greater than zero'
    """)
    void givenRectangleSizeDimensionIsInvalid_whenExecutingInteractor_thenResponseContainsErrorsForEachRectangleDimension(
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
        assertIsErrorResponse(response);
        assertTrue(response.errors().containsKey(fieldName),
                String.format("Expected \"%s\" in response errors", fieldName));
        assertArrayEquals(new String[] { fieldErrorMessage },
                response.errors().get(fieldName));
    }

    @Test
    void givenOnlyIntersectionsFlagIsSet_whenExecutingInteractor_thenResponseIsSuccessfulAndContainsOnlyIntersections() {
        // Arrange
        var query = new RectanglesCollisionAttributesQuery(
            new RectangleRawData("0.0", "0.0", "3.0", "2.0"),
            new RectangleRawData("0.0", "1.0", "2.0", "2.0"),
            true, false, false
        );
        // Act
        var response = interactor.Execute(query);
        // Assert
        assertIsSuccessfulResponse(response);
        assertFalse(response.adjacencyList().isPresent());
        assertFalse(response.containmentInfo().isPresent());
        assertTrue(response.intersections().isPresent());
        assertArrayEquals(dummyIntersections, response.intersections().get());
    }

    @Test
    void givenOnlyIntersectionsFlagIsSet_whenExecutingInteractor_thenIntersectionFinderIsInvokedCorrectly() {
        // Arrange
        var query = new RectanglesCollisionAttributesQuery(
            new RectangleRawData("0.0", "0.0", "3.0", "2.0"),
            new RectangleRawData("0.0", "1.0", "2.0", "2.0"),
            true, false, false
        );
        // Act
        var response = interactor.Execute(query);
        // Assert
        assertIsSuccessfulResponse(response);
        assertRect1AndRect2ArePassedToIntersectionFinder(response);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        '0.0', '0.0', '3.0', '2.0',    '0.0', '1.0', '2.0', '2.0', \
        false, false
        \
        '0.0', '2.0', '3.0', '4.0',    '0.0', '1.0', '2.0', '2.0', \
        true, false
        \
        '0.0', '0.0', '1.0', '1.0',    '0.0', '0.0', '3.0', '2.0', \
        false, true
        \
        '0.0', '0.0', '1.0', '1.0',    '0.0', '0.0', '1.0', '1.0', \
        true, true
    """)
    void givenOnlyContainmentFlagIsSet_whenExecutingInteractor_thenResponseIsSuccessfulAndContainsOnlyContainmentInfo(
            String r1x, String r1y, String r1w, String r1h,
            String r2x, String r2y, String r2w, String r2h,
            boolean rect1ContainsRect2, boolean rect2ContainsRect1
    ) {
        // Arrange
        var query = new RectanglesCollisionAttributesQuery(
            new RectangleRawData(r1x, r1y, r1w, r1h),
            new RectangleRawData(r2x, r2y, r2w, r2h),
            false, true, false
        );
        var expectedContainmentInfo = new ContainmentInfo(rect1ContainsRect2, rect2ContainsRect1);
        // Act
        var response = interactor.Execute(query);
        // Assert
        assertIsSuccessfulResponse(response);
        assertFalse(response.intersections().isPresent());
        assertFalse(response.adjacencyList().isPresent());
        assertTrue(response.containmentInfo().isPresent());
        assertEquals(expectedContainmentInfo, response.containmentInfo().get());
    }

    @Test
    void givenOnlyAdjacencyFlagIsSet_whenExecutingInteractor_thenResponseIsSuccessfulAndContainsOnlyAdjacency() {
        // Arrange
        var query = new RectanglesCollisionAttributesQuery(
            new RectangleRawData("0.0", "0.0", "3.0", "2.0"),
            new RectangleRawData("0.0", "1.0", "2.0", "2.0"),
            false, false, true
        );
        // Act
        var response = interactor.Execute(query);
        // Assert
        assertIsSuccessfulResponse(response);
        assertFalse(response.intersections().isPresent());
        assertFalse(response.containmentInfo().isPresent());
        assertTrue(response.adjacencyList().isPresent());
        assertArrayEquals(dummyAdjacencyList, response.adjacencyList().get());
    }

    @Test
    void givenOnlyAdjacencyFlagIsSet_whenExecutingInteractor_thenAdjacencyFinderIsInvokedCorrectly() {
        // Arrange
        var query = new RectanglesCollisionAttributesQuery(
                new RectangleRawData("0.0", "0.0", "3.0", "2.0"),
                new RectangleRawData("0.0", "1.0", "2.0", "2.0"),
                false, false, true
        );
        // Act
        var response = interactor.Execute(query);
        // Assert
        assertIsSuccessfulResponse(response);
        assertRect1AndRect2ArePassedToAdjacencyFinder(response);
    }

    @Test
    void givenAllFlagsAreSet_whenExecutingInteractor_thenResponseIsSuccessfulAndContainsAllAttributes() {
        // Arrange
        var query = new RectanglesCollisionAttributesQuery(
                new RectangleRawData("0.0", "0.0", "3.0", "2.0"),
                new RectangleRawData("0.0", "1.0", "2.0", "2.0"),
                true, true, true
        );
        // Act
        var response = interactor.Execute(query);
        // Assert
        assertIsSuccessfulResponse(response);
        assertEveryCollisionAttributeIsSet(response);
    }

    private void assertEveryCollisionAttributeIsSet(RectanglesCollisionAttributesResponse response) {
        assertTrue(response.intersections().isPresent());
        assertTrue(response.containmentInfo().isPresent());
        assertTrue(response.adjacencyList().isPresent());
        assertArrayEquals(dummyIntersections, response.intersections().get());
        assertArrayEquals(dummyAdjacencyList, response.adjacencyList().get());
    }

    private void assertRect1AndRect2ArePassedToIntersectionFinder(RectanglesCollisionAttributesResponse response) {
        ArgumentCaptor<Rectangle> captorForRect1 = ArgumentCaptor.forClass(Rectangle.class);
        ArgumentCaptor<Rectangle> captorForRect2 = ArgumentCaptor.forClass(Rectangle.class);
        verify(intersectionFinder).findAll(captorForRect1.capture(), captorForRect2.capture());
        assertEquals(response.rectangle1().get(), captorForRect1.getValue());
        assertEquals(response.rectangle2().get(), captorForRect2.getValue());
    }

    private void assertRect1AndRect2ArePassedToAdjacencyFinder(RectanglesCollisionAttributesResponse response) {
        ArgumentCaptor<Rectangle> captorForRect1 = ArgumentCaptor.forClass(Rectangle.class);
        ArgumentCaptor<Rectangle> captorForRect2 = ArgumentCaptor.forClass(Rectangle.class);
        verify(adjacencyFinder).findAll(captorForRect1.capture(), captorForRect2.capture());
        assertEquals(response.rectangle1().get(), captorForRect1.getValue());
        assertEquals(response.rectangle2().get(), captorForRect2.getValue());
    }

    private static void assertIsSuccessfulResponse(RectanglesCollisionAttributesResponse response) {
        assertTrue(response.isSuccess());
        assertNotNull(response.errors());
        assertEquals(0, response.errors().size());
        assertTrue(response.rectangle1().isPresent());
        assertTrue(response.rectangle2().isPresent());
    }

    private static void assertIsErrorResponse(RectanglesCollisionAttributesResponse response) {
        assertFalse(response.isSuccess());
        assertNotNull(response.errors());
        assertFalse(response.errors().isEmpty());
        assertTrue(response.rectangle1().isEmpty());
        assertTrue(response.rectangle2().isEmpty());
    }

}