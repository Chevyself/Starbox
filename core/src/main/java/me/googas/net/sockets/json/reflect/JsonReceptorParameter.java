package me.googas.net.sockets.json.reflect;

import lombok.NonNull;

/**
 * A parameter that the receptor needs to be used.
 *
 * @param <T> the type of object that the parameter uses
 */
public class JsonReceptorParameter<T> {

  /** The name of the parameter. */
  @NonNull private final String name;

  /** The class that is required for the receptor to be executed. */
  @NonNull private final Class<T> clazz;

  /**
   * Create the receptor parameter.
   *
   * @param name the name of the parameter
   * @param clazz the class required of the parameter
   */
  public JsonReceptorParameter(@NonNull String name, @NonNull Class<T> clazz) {
    this.name = name;
    this.clazz = clazz;
  }

  /**
   * Get the name of the parameter.
   *
   * @return the name of the parameter
   */
  @NonNull
  public String getName() {
    return this.name;
  }

  /**
   * Get the class that is required for the receptor to be executed.
   *
   * @return the class
   */
  @NonNull
  public Class<T> getClazz() {
    return this.clazz;
  }
}
