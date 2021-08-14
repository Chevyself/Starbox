package me.googas.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Represents the required version of Spigot API to be used. */
@Retention(RetentionPolicy.RUNTIME)
public @interface APIVersion {
  /**
   * Get the minimum version that is required to be used.
   *
   * @return latest version that at the moment is 1.17 or 17
   */
  int since() default 17;

  /**
   * Get the maximum api version in which it might be used.
   *
   * @return the maximum API version
   */
  int max() default 17;
}
