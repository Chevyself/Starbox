package me.googas.net.sockets.json;

import com.google.gson.Gson;
import lombok.NonNull;
import me.googas.net.sockets.json.exception.JsonExternalCommunicationException;
import me.googas.net.sockets.json.exception.JsonInternalCommunicationException;

/**
 * This receptors represents a {@link me.googas.net.api.messages.Message} handler. It may execute
 * different actions from a {@link ReceivedJsonRequest} from a simple ping to a complete object sent
 * thru sockets.
 *
 * <p>TODO EJ
 */
public interface JsonReceptor {

  /**
   * Executes the receptor.
   *
   * @param request the request awaiting for the response
   * @param gson the gson of the {@link JsonMessenger} to the serialization/deserialization of
   *     messages
   * @return any type of object
   * @throws JsonExternalCommunicationException if the receptor could not be executed due to an
   *     incorrect input from the other messenger
   * @throws JsonInternalCommunicationException if the receptor could not be executed due to a fail
   *     in this messenger
   */
  Object execute(@NonNull ReceivedJsonRequest request, @NonNull Gson gson)
      throws JsonExternalCommunicationException, JsonInternalCommunicationException;

  /**
   * Get the method which request must use to prepare this receptor.
   *
   * @return the method as a string
   */
  @NonNull
  String getRequestMethod();
}
