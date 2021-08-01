package me.googas.math.geometry;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.math.MathUtils;

/** A sphere */
public class Sphere implements Shape {

  private final String id;
  /** The center of the sphere */
  @NonNull @Getter @Setter private Point center;
  /** The radius of the sphere */
  @Getter @Setter private double radius;

  /**
   * Create the sphere
   *
   * @param id the id of the sphere
   * @param center the center of the sphere
   * @param radius the radius of the sphere
   */
  public Sphere(String id, @NonNull Point center, double radius) {
    this.id = id;
    this.center = center;
    this.radius = radius;
  }

  @Override
  public Optional<String> getId() {
    return Optional.ofNullable(this.id);
  }

  @Override
  public boolean contains(@NonNull Point point) {
    return (MathUtils.square(point.getX() - this.center.getX())
            + MathUtils.square(point.getY() - this.center.getY())
            + MathUtils.square(point.getZ() - this.center.getZ())
        <= MathUtils.square(this.radius));
  }

  @Override
  public double getVolume() {
    return 4 / 3f * Math.PI * MathUtils.square(this.radius);
  }

  @Override
  public @NonNull Points getPointsInside() {
    return Shapes.getPoints(this, this.getMinimum(), this.getMaximum());
  }

  @Override
  public @NonNull Point getMinimum() {
    return new Point(
        this.center.getX() - this.radius,
        this.center.getY() - this.radius,
        this.center.getZ() - this.radius);
  }

  @Override
  public @NonNull Point getMaximum() {
    return new Point(
        this.center.getX() + this.radius,
        this.center.getY() + this.radius,
        this.center.getZ() + this.radius);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Sphere.class.getSimpleName() + "[", "]")
        .add("id='" + id + "'")
        .add("center=" + center)
        .add("radius=" + radius)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    Sphere sphere = (Sphere) o;
    return Objects.equals(id, sphere.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public @NonNull Point getRandomPoint(@NonNull Random random) {
    double x =
        this.getCenter().getX()
            + MathUtils.nextDouble(0, this.radius) * Math.sin(MathUtils.nextDouble(0, 360));
    double y =
        this.getCenter().getY()
            + MathUtils.nextDouble(0, this.radius) * Math.sin(MathUtils.nextDouble(0, 360));
    double z =
        this.getCenter().getZ()
            + MathUtils.nextDouble(0, this.radius) * Math.sin(MathUtils.nextDouble(0, 360));
    return new Point(x, y, z);
  }
}
