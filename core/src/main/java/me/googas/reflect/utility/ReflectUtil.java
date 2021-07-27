package me.googas.reflect.utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.NonNull;

/** Static utilities for java reflection */
public class ReflectUtil {

  /**
   * Check whether an array of annotations has certain annotation
   *
   * @param annotations the array of annotations to check if it has an annotation
   * @param clazz the class of annotation to match
   * @return true if the array has the annotation
   */
  public static boolean hasAnnotation(
      @NonNull Annotation[] annotations, @NonNull Class<? extends Annotation> clazz) {
    for (Annotation annotation : annotations) {
      if (annotation.annotationType() == clazz) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get the annotation inside an array
   *
   * @param annotations the array of annotations to get the annotation from
   * @param clazz the class of the annotation to get
   * @param <T> the type of the annotation to get
   * @return the annotation if found else
   * @throws IllegalArgumentException if the array does not contain the annotation use {@link
   *     #hasAnnotation(Method, Class)} to avoid this
   */
  public static <T extends Annotation> T getAnnotation(
      @NonNull Annotation[] annotations, @NonNull Class<T> clazz) {
    for (Annotation annotation : annotations) {
      if (clazz.isAssignableFrom(annotation.annotationType())) {
        return clazz.cast(annotation);
      }
    }
    throw new IllegalArgumentException(
        Arrays.toString(annotations) + " does not contain the annotation " + clazz);
  }

  /**
   * Check whether a method has certain annotation
   *
   * @param method the method to check if it has an annotation
   * @param clazz the class of annotation to match
   * @return true if the method has the annotation
   */
  public static boolean hasAnnotation(
      @NonNull Method method, @NonNull Class<? extends Annotation> clazz) {
    return ReflectUtil.hasAnnotation(method.getAnnotations(), clazz);
  }
}
