package me.googas.math.geometry;

import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;

/** Static utilities for shapes */
public class Shapes {

  /**
   * Get all the points that are inside a shape from a minimum and a maximum point
   *
   * @param shape the shape to check if the point is inside
   * @param min the minimum point
   * @param max the maximum point
   * @return the container of points with the points inside of the parameter shape
   */
  @NonNull
  public static Points getPoints(@NonNull Shape shape, @NonNull Point min, @NonNull Point max) {
    Set<Point> set = new HashSet<>();
    for (Point point : new Box(min, max, null).getPointsInside().getPoints()) {
      if (shape.contains(point)) set.add(point);
    }
    return new Points(set);
  }
}
