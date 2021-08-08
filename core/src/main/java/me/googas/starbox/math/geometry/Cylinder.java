package me.googas.starbox.math.geometry;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.starbox.math.MathUtils;

/** A cylinder. */
public class Cylinder implements Shape {

  private final String id;

  /** The base of the cylinder. */
  @NonNull @Getter @Setter private Point base;
  /** The radius of the cylinder. */
  @Getter @Setter private double radius;
  /** The height of the cylinder. */
  @Getter @Setter private double height;

  /**
   * Create a cylinder.
   *
   * @param id the id of the cylinder
   * @param base the base of the cylinder
   * @param radius the radius of the cylinder
   * @param height the height of the cylinder
   */
  public Cylinder(String id, @NonNull Point base, double radius, double height) {
    this.id = id;
    this.base = base;
    this.radius = radius;
    this.height = height;
  }

  /**
   * Get the height + the y coordinate of the base.
   *
   * @return the total height
   */
  public double getTotalHeight() {
    return this.base.getY() + this.height;
  }

  @NonNull
  @Override
  public Point getMinimum() {
    return new Point(
        this.base.getX() - this.radius, this.base.getY(), this.base.getZ() - this.radius);
  }

  @NonNull
  @Override
  public Point getMaximum() {
    return new Point(
        this.base.getX() + this.radius,
        this.base.getY() + this.height,
        this.base.getZ() + this.radius);
  }

  @Override
  public Optional<String> getId() {
    return Optional.ofNullable(this.id);
  }

  @Override
  public boolean contains(@NonNull Point point) {
    return point.getY() >= this.base.getY()
        && point.getY() <= this.getTotalHeight()
        && (MathUtils.square(point.getX() - this.base.getX())
                + MathUtils.square(point.getZ() - this.base.getZ())
            <= MathUtils.square(this.radius));
  }

  @Override
  public double getVolume() {
    return this.height * Math.PI * MathUtils.square(this.radius);
  }

  @NonNull
  @Override
  public Points getPointsInside() {
    return Shapes.getPoints(this, this.getMinimum(), this.getMaximum());
  }

  @Override
  public @NonNull Point getRandomPoint(@NonNull Random random) {
    double x =
        this.getBase().getX()
            + MathUtils.nextDouble(0, this.radius) * Math.sin(MathUtils.nextDouble(0, 360));
    double y = MathUtils.nextDouble(this.base.getY() - this.height, this.base.getY() + this.height);
    double z =
        this.getBase().getZ()
            + MathUtils.nextDouble(0, this.radius) * Math.sin(MathUtils.nextDouble(0, 360));
    return new Point(x, y, z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    Cylinder cylinder = (Cylinder) o;
    return Objects.equals(id, cylinder.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Cylinder.class.getSimpleName() + "[", "]")
        .add("id='" + id + "'")
        .add("base=" + base)
        .add("radius=" + radius)
        .add("height=" + height)
        .toString();
  }
}
