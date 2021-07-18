package me.googas.net.sockets.api;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.NonNull;
import me.googas.net.sockets.Request;

/** This object is used to give and receive {@link Message} */
public interface Messenger {
  /**
   * Listens for incoming messages
   *
   * @throws MessengerListenFailException if the messenger fails to listen to new messages
   */
  void listen() throws MessengerListenFailException;

  /** Closes the messenger */
  void close();

  /**
   * Sends a request to this messenger
   *
   * @param request the request that was send and must be processed by this messenger
   * @param consumer the consumer to provide the object when the request gets a response
   * @param <T> the type of object that the request expects
   */
  <T> void sendRequest(@NonNull Request<T> request, @NonNull Consumer<Optional<T>> consumer);

  /**
   * Sends a request to get the requested object
   *
   * @param request the request that was send and must provided the object
   * @param <T> the type of the object
   * @return the provided object
   * @throws MessengerListenFailException if the request times out
   */
  <T> T sendRequest(@NonNull Request<T> request) throws MessengerListenFailException;

  default <T> void sendRequest(
      @NonNull Request<T> request, @NonNull BiConsumer<Messenger, Optional<T>> consumer) {
    this.sendRequest(request, optional -> consumer.accept(this, optional));
  }
}
