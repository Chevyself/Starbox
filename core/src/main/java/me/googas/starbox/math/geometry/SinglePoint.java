package me.googas.starbox.math.geometry;

import java.util.HashSet;
import java.util.Optional;
import lombok.NonNull;

/** Represents a single point shape. */
public class SinglePoint implements Shape {

  private final String id;
  @NonNull private final Point point;

  public SinglePoint(String id, @NonNull Point point) {
    this.id = id;
    this.point = point;
  }

  @Override
  public Optional<String> getId() {
    return Optional.ofNullable(id);
  }

  @Override
  public @NonNull Points getPointsInside() {
    HashSet<Point> set = new HashSet<>();
    set.add(point);
    return new Points(set);
  }

  @Override
  public @NonNull Point getMinimum() {
    return this.point;
  }

  @Override
  public @NonNull Point getMaximum() {
    return this.point;
  }

  @Override
  public double getVolume() {
    return this.point.magnitude();
  }
}
