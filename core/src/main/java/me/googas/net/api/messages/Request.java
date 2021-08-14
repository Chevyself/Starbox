package me.googas.net.api.messages;

import java.util.Map;
import lombok.NonNull;

/** An extension for messages to receive a {@link me.googas.net.api.messages.Response} */
public interface Request extends Message {

  /**
   * Create a builder for a request.
   *
   * @param clazz the class that is being requested
   * @param method the method that must match a receptor
   * @param <T> the type of the requested object
   * @return the builder for the request
   */
  @NonNull
  static <T> RequestBuilder<T> builder(@NonNull Class<T> clazz, @NonNull String method) {
    return new RequestBuilder<>(clazz, method);
  }

  /**
   * Get the method of the receptor this request is trying to prepare.
   *
   * @return the method as a string
   */
  @NonNull
  String getMethod();

  /**
   * Get the parameters that the receptor needs to give a response.
   *
   * @return the parameters
   */
  @NonNull
  Map<String, ?> getParameters();
}
