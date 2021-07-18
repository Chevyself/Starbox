package me.googas.reflect.wrappers;

public interface Wrapper {

  /**
   * Get the wrapped object
   *
   * @return the wrapped object
   */
  Object get();

  /**
   * Set the wrapped object
   *
   * @param object the new wrapped object
   */
  void set(Object object);
}
