package com.caleb.rectangles;

import com.caleb.rectangles.application.RectanglesCollisionAttributesResponse;
import com.caleb.rectangles.domain.Vector2;
import com.caleb.rectangles.domain.operations.Adjacency;
import picocli.CommandLine;

import java.io.PrintStream;
import java.util.Map;

class CliOutputFormatter {

    private static final String NONE = "none";

    void print(RectanglesCollisionAttributesResponse response, PrintStream out, PrintStream err) {
        if (!response.isSuccess()) {
            printErrors(response, err);
            return;
        }

        out.println(color("@|green Success|@"));

        response.intersections().ifPresent(intersections -> {
            printSectionHeader(out, "Intersections");
            if (intersections.length == 0) {
                out.println(NONE);
                return;
            }
            printTable(
                    out,
                    new String[] { "x", "y" },
                    intersectionsToRows(intersections)
            );
        });

        response.containmentInfo().ifPresent(info -> {
            printSectionHeader(out, "Containment");
            printTable(
                    out,
                    new String[] { "Relation", "Value" },
                    new String[][] {
                            new String[] { "rect1ContainsRect2", String.valueOf(info.rect1ContainsRect2()) },
                            new String[] { "rect2ContainsRect1", String.valueOf(info.rect2ContainsRect1()) }
                    }
            );
        });

        response.adjacencyList().ifPresent(adjacencyList -> {
            printSectionHeader(out, "Adjacency");
            if (adjacencyList.length == 0) {
                out.println(NONE);
                return;
            }
            printTable(
                    out,
                    new String[] { "Type", "Function", "Lower", "Upper" },
                    adjacencyListToRows(adjacencyList)
            );
        });
    }

    private void printErrors(RectanglesCollisionAttributesResponse response, PrintStream err) {
        err.println(color("@|red Errors|@"));
        for (Map.Entry<String, String[]> entry : response.errors().entrySet()) {
            for (String message : entry.getValue()) {
                err.printf("%s: %s%n", entry.getKey(), message);
            }
        }
    }

    private static String[][] intersectionsToRows(Vector2[] intersections) {
        var rows = new String[intersections.length][2];
        for (int i = 0; i < intersections.length; i++) {
            var point = intersections[i];
            rows[i][0] = formatNumber(point.x());
            rows[i][1] = formatNumber(point.y());
        }
        return rows;
    }

    private static String[][] adjacencyListToRows(Adjacency[] adjacencyList) {
        var rows = new String[adjacencyList.length][4];
        for (int i = 0; i < adjacencyList.length; i++) {
            var adjacency = adjacencyList[i];
            var segment = adjacency.segment();
            var axisName = segment.axis() == com.caleb.rectangles.domain.LineSegment.OrthogonalAxis.X ? "x" : "y";
            rows[i][0] = adjacency.type().toString();
            rows[i][1] = axisName + " = " + formatNumber(segment.constant());
            rows[i][2] = formatNumber(segment.bounds().lower());
            rows[i][3] = formatNumber(segment.bounds().upper());
        }
        return rows;
    }

    private static String formatNumber(double value) {
        return String.format("%.6f", value);
    }

    private static void printSectionHeader(PrintStream out, String title) {
        out.println();
        out.println(color("@|cyan " + title + "|@"));
    }

    private static void printTable(PrintStream out, String[] headers, String[][] rows) {
        int[] widths = columnWidths(headers, rows);
        out.println(formatRow(headers, widths));
        out.println(formatDivider(widths));
        for (String[] row : rows) {
            out.println(formatRow(row, widths));
        }
    }

    private static int[] columnWidths(String[] headers, String[][] rows) {
        var widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }
        return widths;
    }

    private static String formatRow(String[] row, int[] widths) {
        var builder = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            if (i > 0) builder.append("  ");
            builder.append(padRight(row[i], widths[i]));
        }
        return builder.toString();
    }

    private static String formatDivider(int[] widths) {
        var builder = new StringBuilder();
        for (int i = 0; i < widths.length; i++) {
            if (i > 0) builder.append("  ");
            builder.append("-".repeat(widths[i]));
        }
        return builder.toString();
    }

    private static String padRight(String value, int width) {
        if (value.length() >= width) return value;
        return value + " ".repeat(width - value.length());
    }

    private static String color(String value) {
        return CommandLine.Help.Ansi.AUTO.string(value);
    }
}
