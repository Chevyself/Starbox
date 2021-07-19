package me.googas.math;

/** Static utilities for math */
public class MathUtils {

  /**
   * Get the number queried squared
   *
   * @param number the number
   * @return the number squared
   */
  public static double square(double number) {
    return number * number;
  }

  /**
   * Get the number queried cubed
   *
   * @param number the number
   * @return the number cubed
   */
  public static double cubed(double number) {
    return number * number * number;
  }

  public static double percentage(double total, double num) {
    return num * 100 / total;
  }

  public static double nextDouble(double min, double max) {
    if (max < min) {
      return MathUtils.nextDouble(max, min);
    } else {
      return min + ((1 + max - min) * Math.random());
    }
  }

  public static double nextDoubleFloor(double min, double max) {
    return Math.floor(nextDouble(min, max));
  }
}
