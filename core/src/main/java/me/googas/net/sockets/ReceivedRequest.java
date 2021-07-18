package me.googas.net.sockets;

import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import me.googas.net.sockets.api.Messenger;

/** This object represents a request that is being read by a {@link Messenger} */
public class ReceivedRequest implements IRequest {

  /** The id of the request/response */
  @NonNull private final UUID id;

  /** The method which should match one from a receptor */
  @NonNull private final String method;

  /** The parameters provided by the messenger */
  @NonNull private final Map<String, JsonElement> parameters;

  /**
   * Create the request
   *
   * @param id the id the request given by the messenger
   * @param method the method to get the receptor
   * @param parameters the parameters for the receptor
   */
  public ReceivedRequest(
      @NonNull UUID id, @NonNull String method, @NonNull Map<String, JsonElement> parameters) {
    this.id = id;
    this.method = method;
    this.parameters = parameters;
  }

  /** @deprecated this must be used only by gson */
  public ReceivedRequest() {
    this(UUID.randomUUID(), "", new HashMap<>());
  }

  @Override
  public @NonNull UUID getId() {
    return this.id;
  }

  @Override
  public @NonNull String getMethod() {
    return this.method;
  }

  @Override
  public @NonNull Map<String, JsonElement> getParameters() {
    return this.parameters;
  }

  @Override
  public String toString() {
    return "ReceivedRequest{"
        + "id="
        + this.id
        + ", method='"
        + this.method
        + '\''
        + ", parameters="
        + this.parameters
        + '}';
  }
}
