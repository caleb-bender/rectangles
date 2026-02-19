package com.caleb.rectangles.application;

import java.util.HashMap;
import java.util.Optional;
import static com.caleb.rectangles.application.RectanglesCollisionAttributesQuery.*;

public class RectangleRawDataParser {

    public record RectangleData(double x, double y, double w, double h) {}

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
    Optional<RectangleData> parse(
            RectangleRawData rectangleRawData, String rectangleName,
            HashMap<String, String[]> errors
    ) {
        int i = 0;
        for (RectangleFieldInfo info : rectFieldInfos) {
            try { var value = Double.parseDouble(rectangleRawData.fieldAt(i++)); }
            catch (NumberFormatException err) {
                errors.put(rectangleName + info.fieldSuffix,
                formattedErrorMessage(rectangleName, info.friendlyName));
            }
        }
        return Optional.empty();
    }

    private String[] formattedErrorMessage(String rectangleName, String friendlyName) {

        return new String[] {
                String.format("The %s of %s is not a number", friendlyName, rectangleName)
        };
    }
}
