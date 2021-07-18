package me.googas.net.sockets;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.NonNull;
import me.googas.net.sockets.api.Messenger;

/**
 * A request that is waiting for a {@link Response} in a {@link Messenger}
 *
 * @param <T> the type of object that the request wanted
 */
public class AwaitingRequest<T> {

  /** The request waiting for the response */
  @NonNull private final Request<T> request;

  /** The class of the object requested */
  @NonNull private final Class<T> clazz;

  /** The consumer to execute when the response is received */
  @NonNull private final Consumer<Optional<T>> consumer;

  /** The consumer in case an exception happens */
  @NonNull private final Consumer<Throwable> exceptionConsumer;

  /**
   * Create the awaiting request
   *
   * @param request the request that is waiting for a response
   * @param clazz the class of the object that the request is waiting
   * @param consumer the consumer to execute when the response is received
   * @param exception the consumer in case of an exception
   */
  public AwaitingRequest(
      @NonNull Request<T> request,
      @NonNull Class<T> clazz,
      @NonNull Consumer<Optional<T>> consumer,
      @NonNull Consumer<Throwable> exception) {
    this.request = request;
    this.clazz = clazz;
    this.consumer = consumer;
    this.exceptionConsumer = exception;
  }

  /**
   * Create the awaiting request. With a simple print trace if something goes wrong
   *
   * @param request the request that is waiting for a response
   * @param clazz the class of the object that the request is waiting
   * @param consumer the consumer to execute when the response is received
   */
  public AwaitingRequest(
      @NonNull Request<T> request,
      @NonNull Class<T> clazz,
      @NonNull Consumer<Optional<T>> consumer) {
    this(request, clazz, consumer, Throwable::printStackTrace);
  }

  /**
   * Get the request waiting for the response
   *
   * @return the request
   */
  @NonNull
  public Request<T> getRequest() {
    return this.request;
  }

  /**
   * Get the class of the object requested
   *
   * @return the class
   */
  @NonNull
  public Class<T> getClazz() {
    return this.clazz;
  }

  /**
   * Get the consumer to execute when the response is received
   *
   * @return the consumer
   */
  @NonNull
  public Consumer<Optional<T>> getConsumer() {
    return this.consumer;
  }

  /**
   * The consumer in case an exception happens
   *
   * @return the consumer ofr exceptions
   */
  @NonNull
  public Consumer<Throwable> getExceptionConsumer() {
    return this.exceptionConsumer;
  }

  @Override
  public String toString() {
    return this.request.toString();
  }
}
