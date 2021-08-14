package me.googas.net.sockets.json;

import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import me.googas.net.api.Messenger;
import me.googas.net.api.messages.Request;

/** This object represents a request that is being read by a {@link Messenger}. */
public class ReceivedJsonRequest implements Request {

  /** The id of the request/response. */
  @NonNull @Getter private final UUID id;

  /** The method which should match one from a receptor. */
  @NonNull @Getter private final String method;

  /** The parameters provided by the messenger. */
  @NonNull @Getter private final Map<String, JsonElement> parameters;

  /**
   * Create the request.
   *
   * @param id the id the request given by the messenger
   * @param method the method to get the receptor
   * @param parameters the parameters for the receptor
   */
  public ReceivedJsonRequest(
      @NonNull UUID id, @NonNull String method, @NonNull Map<String, JsonElement> parameters) {
    this.id = id;
    this.method = method;
    this.parameters = parameters;
  }

  /** @deprecated this must be used only by gson */
  public ReceivedJsonRequest() {
    this(UUID.randomUUID(), "", new HashMap<>());
  }

  @Override
  public String toString() {
    return "ReceivedJsonRequest{"
        + "id="
        + this.id
        + ", method='"
        + this.method
        + '\''
        + ", parameters="
        + this.parameters
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    ReceivedJsonRequest that = (ReceivedJsonRequest) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
