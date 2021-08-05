package me.googas.math.geometry;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.math.MathUtils;

/**
 * An object that represents a point inside the cartesian coordinates system. This means this object
 * is represented by an X, Y and Z value.
 */
public class Point {

  /** The x position of the point. */
  @Getter @Setter private double x;
  /** The y position of the point. */
  @Getter @Setter private double y;
  /** The z position of the point. */
  @Getter @Setter private double z;

  /**
   * Create the point.
   *
   * @param x the x position of the point
   * @param y the y position of the point
   * @param z the z position of the point
   */
  public Point(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Creates a new instance of this same point.
   *
   * @return the new instance of this point
   */
  @NonNull
  public Point copy() {
    return new Point(this.x, this.y, this.z);
  }

  /**
   * Get the distance between two points.
   *
   * @param another another point to check the distance
   * @return the distance between the two points
   */
  public double distance(@NonNull Point another) {
    return Math.sqrt(
        MathUtils.square(another.getX() - this.x)
            + MathUtils.square(another.getY() - this.y)
            + MathUtils.square(another.getZ() - this.z));
  }

  /**
   * Sums this point with another.
   *
   * <p>The three values ({@link #x}, {@link #y} and {@link #z}) will be summed for both points.
   *
   * @param point the other point to sum
   * @return this point with the sum of the two points
   */
  @NonNull
  public Point sum(@NonNull Point point) {
    this.x += point.getX();
    this.y += point.getY();
    this.z += point.getZ();
    return this;
  }

  /**
   * Get the size of the point. The size if the sum of all the coordinates <br>
   * ({@link #x}, {@link #y} and {@link #z})
   *
   * @return the size of the point
   */
  public double size() {
    return this.x + this.y + this.z;
  }

  /**
   * Subtracts this points with another.
   *
   * <p>The three values ({@link #x}, {@link #y} and {@link #z}) will be subtracted for both points.
   *
   * @param point the other point to subtract
   * @return this point with the subtraction of the two points
   */
  @NonNull
  public Point subtract(@NonNull Point point) {
    this.x = -point.getX();
    this.y = -point.getY();
    this.z = -point.getZ();
    return this;
  }

  /**
   * Checks if this point is smaller than another point.
   *
   * @param point the point to check if it is bigger than this one
   * @return true if this point is smaller than the queried one
   */
  public boolean lowerThan(@NonNull Point point) {
    return this.size() < point.size();
  }

  /**
   * Checks if this point is bigger than another point.
   *
   * @param point the point to check if it is smaller than this one
   * @return true if this point is bigger than the queried one
   */
  public boolean greaterThan(@NonNull Point point) {
    return this.size() > point.size();
  }

  /**
   * Get the dot component with another point. The dot component is each component multiplied and
   * then summed
   *
   * @param point the other point to get the dot component
   * @return the dot component between the points
   */
  public double dot(@NonNull Point point) {
    return (this.x * point.getX()) + (this.y * point.getY()) + (this.z * point.getZ());
  }

  /** Floors the {@link #x}, {@link #y} and {@link #z} values. */
  public void floor() {
    this.x = Math.floor(this.x);
    this.y = Math.floor(this.y);
    this.z = Math.floor(this.z);
  }

  /**
   * Get whether this point is infinite. If any of {@link #x}, {@link #y} and {@link #z} are {@link
   * Double#NEGATIVE_INFINITY} or {@link Double#POSITIVE_INFINITY} it will be true. Using {@link
   * Double#isInfinite()}
   *
   * @return true if the point is infinite if any of its coordinates
   */
  public boolean isInfinite() {
    return Double.isInfinite(this.x) || Double.isInfinite(this.y) || Double.isInfinite(this.z);
  }

  /**
   * Get the magnitude of this point. The magnitude of the point is the square root of x, y and z
   * squared using {@link MathUtils#square(double)}
   *
   * @return the magnitude
   */
  public double magnitude() {
    return Math.sqrt(
        MathUtils.square(this.x) + MathUtils.square(this.y) + MathUtils.square(this.z));
  }

  @Override
  public String toString() {
    return "x=" + this.x + ", y=" + this.y + ", z=" + this.z;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof Point)) return false;

    Point point = (Point) object;

    if (Double.compare(point.x, this.x) != 0) return false;
    if (Double.compare(point.y, this.y) != 0) return false;
    return Double.compare(point.z, this.z) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(this.x);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(this.y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(this.z);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
