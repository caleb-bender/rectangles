package com.caleb.rectangles.application;

import com.caleb.rectangles.domain.InvalidRectangleException;
import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Size;
import com.caleb.rectangles.domain.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.caleb.rectangles.application.RectanglesCollisionAttributesQuery.*;

class RectangleRawDataParser {

    private record RectangleFieldInfo(String fieldSuffix, String friendlyName) {}

    private static final RectangleFieldInfo[] rectFieldInfos = new RectangleFieldInfo[] {
            new RectangleFieldInfo(".x", "top-left x-coordinate"),
            new RectangleFieldInfo(".y", "top-left y-coordinate"),
            new RectangleFieldInfo(".size.width", "width"),
            new RectangleFieldInfo(".size.height", "height")
    };

    /**
     * Attempts to parse string fields as doubles for a rectangle object
     * @param rectangleRawData
     * @param rectangleName
     * @param errors
     * @return parsed rectangle data or empty
     */
    Optional<Rectangle> parse(
            RectangleRawData rectangleRawData, String rectangleName,
            HashMap<String, String[]> errors
    ) {
        var rectangleFields = getRectangleFields(rectangleRawData, rectangleName, errors);
        return getRectangleOrEmpty(rectangleName, errors, rectangleFields);
    }

    private ArrayList<Double> getRectangleFields(RectangleRawData rectangleRawData, String rectangleName, HashMap<String, String[]> errors) {
        int i = 0;
        var rectangleFields = new ArrayList<Double>();
        for (RectangleFieldInfo info : rectFieldInfos) {
            try {
                var value = Double.parseDouble(rectangleRawData.fieldAt(i++));
                rectangleFields.add(value);
            }
            catch (NumberFormatException err) {
                errors.put(rectangleName + info.fieldSuffix,
                formattedErrorMessage(rectangleName, info.friendlyName));
            }
        }
        return rectangleFields;
    }

    private static Optional<Rectangle> getRectangleOrEmpty(String rectangleName, HashMap<String, String[]> errors, ArrayList<Double> rectangleFields) {
        if (rectangleFields.size() != 4) return Optional.empty();
        try {
            var rectangle = new Rectangle(
                    new Vector2(rectangleFields.get(0), rectangleFields.get(1)),
                    new Size(rectangleFields.get(2), rectangleFields.get(3))
            );
            return Optional.of(rectangle);
        }
        catch (InvalidRectangleException err) {
            prefixErrorsFromRectangle(rectangleName, errors, err);
            return Optional.empty();
        }
    }

    private static void prefixErrorsFromRectangle(String rectangleName, HashMap<String, String[]> errors, InvalidRectangleException err) {
        Map<String, String[]> prefixedMap = err.invariantViolations().entrySet().stream()
            .collect(Collectors.toMap(
                    entry -> rectangleName + "." + entry.getKey(),
                    Map.Entry::getValue
            ));
        errors.putAll(prefixedMap);
    }

    private String[] formattedErrorMessage(String rectangleName, String friendlyName) {

        return new String[] {
                String.format("The %s of %s is not a number", friendlyName, rectangleName)
        };
    }
}
