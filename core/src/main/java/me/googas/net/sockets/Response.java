package me.googas.net.sockets;

import java.util.UUID;
import lombok.NonNull;
import me.googas.net.sockets.api.Message;

/**
 * This object represents the message to send a client when it is waiting for a {@link Response}
 *
 * @param <T> the type of object that the response expected
 */
public class Response<T> implements Message {

  /** The id of the message */
  @NonNull private final UUID id;

  /** The object which this is responding with */
  private T object;

  /** Whether the response ended with an error */
  private boolean error = true;

  /**
   * Create the response
   *
   * @param id the id of the response
   * @param object the object with which this is responding with
   */
  public Response(@NonNull UUID id, T object) {
    this.id = id;
    this.object = object;
  }

  /**
   * Create the response
   *
   * @param id the id of the response
   */
  public Response(@NonNull UUID id) {
    this(id, null);
  }

  /** @deprecated this constructor may only be used by gson */
  public Response() {
    this(UUID.randomUUID(), null);
  }

  /**
   * Set the object given by the response
   *
   * @param object the new object given by the response
   */
  public void setObject(T object) {
    this.object = object;
  }

  /**
   * Set whether the response is an error
   *
   * @param error the new value if the response was an error
   */
  public void setError(boolean error) {
    this.error = error;
  }

  /**
   * Get the object which was given by the response
   *
   * @return the object given by the response
   */
  public T getObject() {
    return this.object;
  }

  /**
   * Get whether the response is an error
   *
   * @return true if the response is an error
   */
  public boolean isError() {
    return this.error;
  }

  /**
   * Get the unique id of the message
   *
   * @return the unique id of the message
   */
  @Override
  public @NonNull UUID getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return "Response{"
        + "id="
        + this.id
        + ", object="
        + this.object
        + ", error="
        + this.error
        + '}';
  }
}
