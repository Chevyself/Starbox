package me.googas.starbox.math;

/** Static utilities for math. */
public class MathUtils {

  /**
   * Get the number queried squared.
   *
   * @param number the number
   * @return the number squared
   */
  public static double square(double number) {
    return number * number;
  }

  /**
   * Get the number queried cubed.
   *
   * @param number the number
   * @return the number cubed
   */
  public static double cubed(double number) {
    return number * number * number;
  }

  /**
   * Get a percentage. Basic formula: number * 100 / total
   *
   * @param total the total that represents the 100%
   * @param num the number to get its percentage from the total
   * @return the percentage
   */
  public static double percentage(double total, double num) {
    return num * 100 / total;
  }

  /**
   * Get a random double.
   *
   * @param min the minimum number
   * @param max the maximum number
   * @return a number between min and max
   */
  public static double nextDouble(double min, double max) {
    if (max < min) {
      return MathUtils.nextDouble(max, min);
    } else {
      return min + ((1 + max - min) * Math.random());
    }
  }

  /**
   * Get a random double and {@link Math#floor(double)}.
   *
   * @param min the minimum number
   * @param max the maximum number
   * @return a floored number between min and max
   */
  public static double nextDoubleFloor(double min, double max) {
    return Math.floor(MathUtils.nextDouble(min, max));
  }
}
