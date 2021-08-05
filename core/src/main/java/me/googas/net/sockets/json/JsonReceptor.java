package me.googas.net.sockets.json;

import java.util.List;
import lombok.NonNull;
import me.googas.net.sockets.json.exception.JsonExternalCommunicationException;
import me.googas.net.sockets.json.exception.JsonInternalCommunicationException;
import me.googas.net.sockets.json.reflect.JsonReceptorParameter;

/**
 * This object represents the {@link Receptor} registered inside a {@link JsonMessenger}. This means
 * that this is the object after reflection was made
 */
public interface JsonReceptor {

  /**
   * Invokes the receptor.
   *
   * @param objects the parameters which the receptor needs
   * @return the object can be either any object or nothing if it is void
   * @throws JsonInternalCommunicationException if the receptor could not be invoked due to an
   *     internal error
   * @throws JsonExternalCommunicationException if the receptor could not be invoked due to an
   *     external error
   */
  Object invoke(@NonNull Object... objects)
      throws JsonInternalCommunicationException, JsonExternalCommunicationException;

  /**
   * Get the method which request must use to prepare this receptor.
   *
   * @return the method as a string
   */
  @NonNull
  String getRequestMethod();

  /**
   * Get the parameters that the receptor needs to be executed.
   *
   * @return the list of parameters that the receptor needs to be executed
   */
  @NonNull
  List<JsonReceptorParameter<?>> getParameters();
}
