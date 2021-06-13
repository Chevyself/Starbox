package me.googas.reflect.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Represents the required version of spigot api to be used */
@Retention(RetentionPolicy.RUNTIME)
public @interface APIVersion {
  /**
   * Get the minimum version that is required to be used
   *
   * @return latest version that at the moment is 1.16 or 16
   */
  int value() default 16;

  /**
   * Get the maximum api version in which it might be used
   *
   * @return the maximum api version
   */
  int max() default 16;
}
