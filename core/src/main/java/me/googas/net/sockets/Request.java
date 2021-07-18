package me.googas.net.sockets;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import me.googas.net.sockets.json.reflect.JsonReceptor;

/**
 * This object represents a request which is a message that makes the client which sent it wait for
 * a {@link Response}
 */
public class Request<T> implements IRequest {

  /** The class that is being requested */
  @NonNull private final transient Class<T> clazz;

  /** The id of the request */
  @NonNull private final UUID id;

  /** The method which must match a {@link JsonReceptor} to give a response */
  @NonNull private final String method;

  /** The parameters that the {@link JsonReceptor} requires to give a response */
  @NonNull private final Map<String, ?> parameters;

  /**
   * Create the request
   *
   * @param clazz the class that is being requested
   * @param id the id of the request
   * @param method the method to get the receptor
   * @param parameters the parameters to execute in the receptor
   */
  public Request(
      @NonNull Class<T> clazz,
      @NonNull UUID id,
      @NonNull String method,
      @NonNull Map<String, ?> parameters) {
    this.clazz = clazz;
    this.id = id;
    this.method = method;
    this.parameters = parameters;
  }

  /**
   * Create the request
   *
   * @param clazz the class that is being requested
   * @param method the method of the request
   * @param parameters the method to get the receptor
   */
  public Request(
      @NonNull Class<T> clazz, @NonNull String method, @NonNull Map<String, ?> parameters) {
    this(clazz, UUID.randomUUID(), method, parameters);
  }

  /**
   * Create the request
   *
   * @param clazz the class that is being requested
   * @param method the id of the request
   */
  public Request(@NonNull Class<T> clazz, @NonNull String method) {
    this(clazz, UUID.randomUUID(), method, new HashMap<>());
  }

  public static <T> RequestBuilder<T> builder(@NonNull Class<T> clazz, @NonNull String method) {
    return new RequestBuilder<>(clazz, method);
  }

  /**
   * Get the class that is being requested
   *
   * @return the class that is being requested
   */
  @NonNull
  public Class<T> getClazz() {
    return this.clazz;
  }

  @Override
  @NonNull
  public String getMethod() {
    return this.method;
  }

  @Override
  @NonNull
  public Map<String, ?> getParameters() {
    return this.parameters;
  }

  @Override
  public String toString() {
    return "Request{"
        + "clazz="
        + this.clazz
        + ", id="
        + this.id
        + ", method='"
        + this.method
        + '\''
        + ", parameters="
        + this.parameters
        + '}';
  }

  @Override
  public @NonNull UUID getId() {
    return this.id;
  }
}
