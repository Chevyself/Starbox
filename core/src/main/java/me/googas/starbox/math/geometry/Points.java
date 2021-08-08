package me.googas.starbox.math.geometry;

import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

/** A set of points inside a region. */
public class Points {

  /** The contained points . */
  @NonNull @Delegate @Getter private final Set<Point> points;

  /**
   * Create the collection of points.
   *
   * @param points the set of points
   */
  public Points(@NonNull Set<Point> points) {
    this.points = points;
  }

  /**
   * Add all the points contained in other collection of points.
   *
   * @param points the other collection of points
   * @return true if at least one point was added
   */
  public boolean addAll(@NonNull Points points) {
    return this.points.addAll(points.getPoints());
  }

  /**
   * Get the smallest point.
   *
   * <p>The default value is a point x=0, y=0, z=0
   *
   * @return the smallest point
   */
  @NonNull
  public Point getMinimum() {
    Point min = new Point(0, 0, 0);
    for (Point point : this.points) {
      if (point.lowerThan(min)) {
        min = point;
      }
    }
    return min;
  }

  /**
   * Get whether the contained points are infinite.
   *
   * @return true if there's an infinite amount of points
   */
  public boolean isInfinite() {
    return false;
  }

  /**
   * Get the biggest point.
   *
   * <p>The default value is a point x=0, y=0, z=0
   *
   * @return the biggest point
   */
  @NonNull
  public Point getMaximum() {
    Point max = new Point(0, 0, 0);
    for (Point point : this.points) {
      if (point.greaterThan(max)) {
        max = point;
      }
    }
    return max;
  }
}
