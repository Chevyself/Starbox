package me.googas.net.api;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.NonNull;
import me.googas.net.api.exception.MessengerListenFailException;
import me.googas.net.api.messages.Message;
import me.googas.net.api.messages.StarboxRequest;

/** This object is used to give and receive {@link Message}. */
public interface Messenger {
  /**
   * Listens for incoming messages.
   *
   * @throws MessengerListenFailException if the messenger fails to listen to new messages
   */
  void listen() throws MessengerListenFailException;

  /** Closes the messenger. */
  void close();

  /**
   * Sends a request to this messenger asynchronously.
   *
   * @param request the request that was send and must be processed by this messenger
   * @param consumer the consumer to provide the object when the request gets a response
   * @param <T> the type of object that the request expects
   */
  <T> void sendRequest(@NonNull StarboxRequest<T> request, @NonNull Consumer<Optional<T>> consumer);

  /**
   * Sends a request to get the requested object.
   *
   * @param request the request that was send and must provided the object
   * @param <T> the type of the object
   * @return the provided object wrapped in a {@link Optional} instance
   * @throws MessengerListenFailException if the request times out
   */
  @NonNull
  <T> Optional<T> sendRequest(@NonNull StarboxRequest<T> request)
      throws MessengerListenFailException;
}
