package me.googas.net.sockets.json.client;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.net.api.messages.AwaitingRequest;
import me.googas.net.sockets.json.JsonMessenger;
import me.googas.net.sockets.json.JsonReceptor;
import me.googas.net.sockets.json.server.JsonSocketServer;

/** This object represents a client that can be used to connect to the {@link JsonSocketServer} */
public class JsonClient extends Thread implements JsonMessenger {

  /** The builder to build json strings */
  @NonNull @Getter private final StringBuilder builder = new StringBuilder();

  /** The socket that the client is using */
  @NonNull @Getter private final Socket socket;

  /** The output channel */
  @NonNull @Getter private final PrintWriter output;
  /** The input channel */
  @NonNull @Getter private final BufferedReader input;

  /**
   * The throwable handler in case something goes wrong and the user wants to handle it differently
   */
  @NonNull @Getter private final Consumer<Throwable> throwableHandler;

  /** The gson instance to serialize and deserialize objects */
  @NonNull @Getter private final Gson gson;

  /** The receptors to accept requests */
  @NonNull @Getter private final Set<JsonReceptor> receptors;

  /** The request that are waiting for a response */
  @NonNull @Getter private final HashMap<AwaitingRequest<?>, Long> requests = new HashMap<>();

  /** The time to timeout requests */
  @Getter private final long timeout;

  /** Whether the messenger is closed */
  @Getter @Setter private boolean closed;

  /** The millis of when the last message was sent */
  @Getter @Setter private long lastMessage;

  /**
   * Create the guido client with a given socket
   *
   * @param socket the socket that the client must use
   * @param throwableHandler the exception handler in case a request goes wrong
   * @param gson the gson instance to serialize and deserialize objects
   * @param receptors the receptors to accept requests
   * @param timeout the time to timeout requests
   * @throws IOException if the streams of the socket are closed
   */
  public JsonClient(
      @NonNull Socket socket,
      @NonNull Consumer<Throwable> throwableHandler,
      @NonNull Gson gson,
      @NonNull Set<JsonReceptor> receptors,
      long timeout)
      throws IOException {
    this.socket = socket;
    this.throwableHandler = throwableHandler;
    this.gson = gson;
    this.receptors = receptors;
    this.timeout = timeout;
    this.output =
        new PrintWriter(
            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    this.input =
        new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
  }

  /**
   * Create the json client with the default given providers and receptors
   *
   * @param socket the socket that the client must use
   * @param throwableHandler the exception handler in case a request goes wrong
   * @param gson the gson instance to serialize and deserialize objects
   * @param timeout the time to timeout requests
   * @throws IOException if the streams of the socket are closed
   */
  public JsonClient(
      @NonNull Socket socket,
      @NonNull Consumer<Throwable> throwableHandler,
      @NonNull Gson gson,
      long timeout)
      throws IOException {
    this(socket, throwableHandler, gson, new HashSet<>(), timeout);
  }

  @Override
  public void close() {
    this.setClosed(true);
    this.output.close();
    try {
      this.input.close();
      this.socket.close();
    } catch (IOException e) {
      this.throwableHandler.accept(e);
    }
    this.receptors.clear();
    this.requests.clear();
  }

  @Override
  public void run() {
    JsonMessenger.super.run();
  }
}
