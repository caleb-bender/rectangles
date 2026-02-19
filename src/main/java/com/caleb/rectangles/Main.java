package com.caleb.rectangles;

import com.caleb.rectangles.application.RectanglesCollisionAttributesInteractor;
import com.caleb.rectangles.application.RectanglesCollisionAttributesQuery;
import com.caleb.rectangles.domain.operations.AdjacencyFinder;
import com.caleb.rectangles.domain.operations.IntersectionFinder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

public class Main {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new RectanglesCli()).execute(args);
        System.exit(exitCode);
    }

    @Command(
            name = "rectangles",
            mixinStandardHelpOptions = true,
            description = "Compute the attributes of intersection, containment, and adjacency between two rectangles.",
            footer = {
                    "",
                    "Example:",
                    "  java -jar target/rectangles-0.1.0-SNAPSHOT-all.jar --rect1 0 10 5 5 --rect2 3 8 5 5 -i -c -a"
            }
    )
    static class RectanglesCli implements Callable<Integer> {

        @Option(
                names = "--rect1",
                required = true,
                arity = "4",
                paramLabel = "_",
                description = "Rectangle 1 as X Y W H (top-left x, top-left y, width, height)."
        )
        String[] rect1;

        @Option(
                names = "--rect2",
                required = true,
                arity = "4",
                paramLabel = "_",
                description = "Rectangle 2 as X Y W H (top-left x, top-left y, width, height)."
        )
        String[] rect2;

        @Option(names = {"-i", "--intersections"}, description = "Find any intersection points.")
        boolean intersections;

        @Option(names = {"-c", "--containment"}, description = "Check if either rectangle contains the other (inclusive).")
        boolean containment;

        @Option(names = {"-a", "--adjacency"}, description = "Find any adjacent segments and their types.")
        boolean adjacency;

        @Override
        public Integer call() {
            var interactor = new RectanglesCollisionAttributesInteractor(
                    new AdjacencyFinder(),
                    new IntersectionFinder()
            );
            var query = new RectanglesCollisionAttributesQuery(
                    new RectanglesCollisionAttributesQuery.RectangleRawData(
                            rect1[0], rect1[1], rect1[2], rect1[3]
                    ),
                    new RectanglesCollisionAttributesQuery.RectangleRawData(
                            rect2[0], rect2[1], rect2[2], rect2[3]
                    ),
                    intersections,
                    containment,
                    adjacency
            );
            var response = interactor.Execute(query);
            var formatter = new CliOutputFormatter();
            formatter.print(response, System.out, System.err);
            return response.isSuccess() ? 0 : 1;
        }
    }
}
