package me.googas.net.sockets.json.server;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.net.api.Error;
import me.googas.net.api.auth.Authenticator;
import me.googas.net.api.messages.AwaitingRequest;
import me.googas.net.api.messages.Response;
import me.googas.net.sockets.json.JsonMessenger;
import me.googas.net.sockets.json.JsonReceptor;
import me.googas.net.sockets.json.ReceivedJsonRequest;

/**
 * A guido client thread is the {@link Thread} where a client connected to the {@link
 * JsonSocketServer}.
 */
public class JsonClientThread extends Thread implements JsonMessenger {

  /** The builder to build json strings */
  @NonNull @Getter private final StringBuilder builder = new StringBuilder();

  /** The socket that is connected to the client */
  @NonNull @Getter private final Socket socket;

  /** The line that is being an input into the server */
  @NonNull @Getter private final BufferedReader input;

  /** The output used to send requests to the client */
  @NonNull @Getter private final PrintWriter output;

  /** The server to which this client is connected to */
  @NonNull @Getter private final JsonSocketServer server;

  /** The request that are waiting for a response */
  @NonNull @Getter private final Map<AwaitingRequest<?>, Long> requests = new HashMap<>();

  /** The time to timeout requests */
  @Getter private final long timeout;

  /** Whether the messenger is closed */
  @Getter @Setter private boolean closed;

  /** The millis of when the last message was sent */
  @Getter @Setter private long lastMessage;

  /**
   * Create the client thread
   *
   * @param socket the socket that connected to the server
   * @param server the server to which this client is connected to
   * @param timeout the time to timeout requests
   * @throws IOException in case the streams are already closed
   */
  public JsonClientThread(Socket socket, @NonNull JsonSocketServer server, long timeout)
      throws IOException {
    this.socket = socket;
    this.output =
        new PrintWriter(
            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    this.input =
        new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    this.server = server;
    this.timeout = timeout;
  }

  @Override
  public @NonNull Collection<JsonReceptor> getReceptors() {
    return this.server.getReceptors();
  }

  @Override
  public void run() {
    JsonMessenger.super.run();
  }

  @Override
  public @NonNull Gson getGson() {
    return this.server.getGson();
  }

  @Override
  public @NonNull Consumer<Throwable> getThrowableHandler() {
    return this.server.getThrowableHandler();
  }

  @Override
  public void close() {
    this.setClosed(true);
    this.requests.clear();
    this.output.close();
    try {
      this.socket.close();
      this.input.close();
    } catch (IOException e) {
      this.server.getThrowableHandler().accept(e);
    }
    this.server.remove(this);
    this.requests.clear();
  }

  @Override
  public void acceptRequest(@NonNull ReceivedJsonRequest request) {
    Optional<Authenticator<JsonClientThread>> optional = this.server.getAuthenticator();
    if (optional.isPresent()) {
      if (optional.get().isAuthenticated(this, request)) {
        JsonMessenger.super.acceptRequest(request);
      } else {
        this.printLine(
            this.getGson()
                .toJson(new Response<>(request.getId(), new Error("Authentication failed"))));
      }
    } else {
      JsonMessenger.super.acceptRequest(request);
    }
  }
}
