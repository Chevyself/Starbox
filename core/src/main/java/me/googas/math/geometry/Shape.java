package me.googas.math.geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import lombok.NonNull;

/** An object that represents a 3 dimensional shape. */
public interface Shape {

  /**
   * Check if this shape contains a point inside of it.
   *
   * @param point the point to check if it is inside this shape
   * @return true if it is inside
   */
  default boolean contains(@NonNull Point point) {
    return this.getPointsInside().contains(point);
  }

  /**
   * Check in another shape is inside this one.
   *
   * @param another the other shape to check if it is inside this one
   * @return true if it is inside this shape
   */
  default boolean contains(@NonNull Shape another) {
    return another.getPointsInside().size() == this.intersectingPoints(another).size();
  }

  /**
   * Check if another shape intersects with this one.
   *
   * @param another the other shape to check
   * @return true if part of it is inside this shape
   */
  default boolean intersects(@NonNull Shape another) {
    return !this.intersectingPoints(another).isEmpty();
  }

  /**
   * Get where the shapes are intersecting.
   *
   * @param another the shape to check where it is intersecting
   * @return the points where this shapes are intersecting
   */
  @NonNull
  default Points intersectingPoints(@NonNull Shape another) {
    Set<Point> points = new HashSet<>();
    for (Point point : another.getPointsInside().getPoints()) {
      if (this.contains(point)) {
        points.add(point);
      }
    }
    return new Points(points);
  }

  /**
   * Get a random point inside of the shape.
   *
   * @param random the random for randomization
   * @return the random point
   */
  @NonNull
  default Point getRandomPoint(@NonNull Random random) {
    List<Point> pointList = new ArrayList<>(this.getPointsInside().getPoints());
    return pointList.get(random.nextInt(pointList.size()));
  }

  /**
   * The id to identify a shape in runtime.
   *
   * @return the id
   */
  Optional<String> getId();

  /**
   * Get all the points inside the shape.
   *
   * @return the points inside
   */
  @NonNull
  Points getPointsInside();

  /**
   * Get the minimum point of the shape.
   *
   * @return the minimum point of the shape
   */
  @NonNull
  Point getMinimum();

  /**
   * Get the maximum point of the shape.
   *
   * @return the maximum point of the shape
   */
  @NonNull
  Point getMaximum();

  /**
   * Get the volume of the shape.
   *
   * @return the volume of the shape
   */
  double getVolume();
}
