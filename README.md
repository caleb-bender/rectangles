# rectangles

A simple command-line program that computes collision attributes such as intersection points, containment, and adjacency between two rectangles.

## Prerequisites
- Java (JDK 21+)
- Maven

Maven install guide [here](https://maven.apache.org/install.html).

## Build
Run tests and build:
```bash
mvn package
```

Skip tests and build:
```bash
mvn -q -DskipTests package
```

This creates two JARs:
- `target/rectangles-0.1.0-SNAPSHOT-all.jar` (fat JAR with dependencies, recommended)
- `target/rectangles-0.1.0-SNAPSHOT.jar` (thin JAR, no dependencies)

## Run
Use the fat JAR:
```bash
java -jar target/rectangles-0.1.0-SNAPSHOT-all.jar --rect1 0 10 5 5 --rect2 3 8 5 5 -i -c -a
```

## CLI Usage
```bash
java -jar target/rectangles-0.1.0-SNAPSHOT-all.jar --help
```

Options:
- `--rect1 X Y W H` Rectangle 1 as top-left `x`, top-left `y`, `width`, `height`.
- `--rect2 X Y W H` Rectangle 2 as top-left `x`, top-left `y`, `width`, `height`.
- `-i`, `--intersections` Find intersection points.
- `-c`, `--containment` Check if either rectangle contains the other (inclusive).
- `-a`, `--adjacency` Find adjacent segments and their types.

## Example
Command:
```bash
java -jar target/rectangles-0.1.0-SNAPSHOT-all.jar --rect1 0 10 5 5 --rect2 3 8 5 5 -i -c -a
```

Sample output:
```
Success

Intersections
x         y       
--------  --------
5.000000  8.000000
3.000000  5.000000

Containment
Relation            Value
------------------  -----
rect1ContainsRect2  false
rect2ContainsRect1  false

Adjacency
none
```
