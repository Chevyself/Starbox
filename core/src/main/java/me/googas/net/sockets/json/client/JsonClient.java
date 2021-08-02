package me.googas.net.sockets.json.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.net.api.messages.AwaitingRequest;
import me.googas.net.api.messages.Message;
import me.googas.net.sockets.json.JsonMessenger;
import me.googas.net.sockets.json.JsonReceptor;
import me.googas.net.sockets.json.adapters.MessageDeserializer;
import me.googas.net.sockets.json.reflect.ReflectJsonReceptor;
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

  public JsonClient(
      @NonNull Socket socket,
      @NonNull PrintWriter output,
      @NonNull BufferedReader input,
      @NonNull Consumer<Throwable> throwableHandler,
      @NonNull Gson gson,
      @NonNull Set<JsonReceptor> receptors,
      long timeout,
      boolean closed,
      long lastMessage) {
    this.socket = socket;
    this.output = output;
    this.input = input;
    this.throwableHandler = throwableHandler;
    this.gson = gson;
    this.receptors = receptors;
    this.timeout = timeout;
    this.closed = closed;
    this.lastMessage = lastMessage;
  }

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
  @Deprecated
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
  @Deprecated
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
    } catch (IOException e) {
      this.throwableHandler.accept(e);
    }
    try {
      this.socket.close();
    } catch (IOException e) {
      this.throwableHandler.accept(e);
    }
    this.receptors.clear();
    this.requests.clear();
  }

  /**
   * Starts the builder for a client
   *
   * @param host the host to which the client will be connected
   * @param port the port of the host
   * @return the client builder
   */
  @NonNull
  public static ClientBuilder join(@NonNull String host, int port) {
    return new ClientBuilder(host, port);
  }

  @Override
  public void run() {
    JsonMessenger.super.run();
  }

  /** This class is used to create instances of clients in a neat way */
  public static class ClientBuilder {

    @NonNull private final String host;
    private final int port;
    @NonNull private final Set<JsonReceptor> receptors;
    @NonNull private GsonBuilder gson;
    @NonNull private Consumer<Throwable> handler;
    private long timeout;

    /**
     * Create the builder
     *
     * @param host the host to which the client will be connected
     * @param port the port of the host
     */
    private ClientBuilder(@NonNull String host, int port) {
      this.host = host;
      this.port = port;
      this.receptors = new HashSet<>();
      this.gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageDeserializer());
      this.handler = Throwable::printStackTrace;
      this.timeout = 1000;
    }

    /**
     * Adds the parsed receptors from the given object. This will get the receptors from the object
     * using {@link ReflectJsonReceptor#getReceptors(Object)} and add them to the set
     *
     * @param objects the objects to add as receptors
     * @return this same builder instance
     */
    @NonNull
    public ClientBuilder addReceptors(@NonNull Object... objects) {
      for (Object object : objects) {
        this.addReceptors(ReflectJsonReceptor.getReceptors(object));
      }
      return this;
    }

    /**
     * Adds all the given receptors
     *
     * @param receptors the receptors to add
     * @return this same builder instance
     */
    @NonNull
    public ClientBuilder addReceptors(@NonNull JsonReceptor... receptors) {
      this.receptors.addAll(Arrays.asList(receptors));
      return this;
    }

    /**
     * Adds all the given receptors
     *
     * @param receptors the receptors to add
     * @return this same builder instance
     */
    @NonNull
    public ClientBuilder addReceptors(@NonNull Collection<JsonReceptor> receptors) {
      this.receptors.addAll(receptors);
      return this;
    }

    /**
     * Set the exception handler that the client may use
     *
     * @param handler the new exception handler
     * @return this same builder instance
     */
    @NonNull
    public ClientBuilder handle(@NonNull Consumer<Throwable> handler) {
      this.handler = handler;
      return this;
    }

    /**
     * Set the maximum time that the client will tolerate
     *
     * @param timeout the new maximum time in millis
     * @return this same builder instance
     */
    @NonNull
    public ClientBuilder maxWait(long timeout) {
      this.timeout = timeout;
      return this;
    }

    /**
     * Starts the client
     *
     * @return the client instance
     * @throws IOException if the server could not be found or the input/output could not be open
     */
    @NonNull
    public JsonClient start() throws IOException {
      Socket socket = new Socket(host, port);
      JsonClient client =
          new JsonClient(
              socket,
              new PrintWriter(
                  new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true),
              new BufferedReader(
                  new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)),
              handler,
              gson.create(),
              this.receptors,
              timeout,
              false,
              0);
      client.start();
      return client;
    }

    /**
     * Set the instance of {@link GsonBuilder}
     *
     * @see #getGsonBuilder()
     * @param gson the new builder
     * @return this same instance
     */
    @NonNull
    public ClientBuilder setGson(@NonNull GsonBuilder gson) {
      this.gson = gson;
      return this;
    }

    /**
     * Get the instance of {@link GsonBuilder} that will create the {@link Gson} of the client to
     * read messages
     *
     * @return the builder
     */
    @NonNull
    public GsonBuilder getGsonBuilder() {
      return this.gson;
    }
  }
}
