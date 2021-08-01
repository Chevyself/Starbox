package me.googas.net.api.messages;

import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;

/**
 * This object represents the message to send a client when it is waiting for a {@link Response}
 *
 * @param <T> the type of object that the response expected
 */
public class Response<T> implements Message {

  /** The id of the message */
  @NonNull @Getter private final UUID id;

  /** The object which this is responding with */
  private T object;

  /** Whether the response ended with an error */
  @Getter private boolean error = true;

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
  @NonNull
  public Response<T> setObject(T object) {
    this.object = object;
    return this;
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
   * @return an {@link Optional} instance containing the object from the response or empty
   */
  @NonNull
  public Optional<T> getObject() {
    return Optional.ofNullable(this.object);
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
