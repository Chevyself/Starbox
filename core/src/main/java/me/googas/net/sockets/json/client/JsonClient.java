package me.googas.net.sockets.json.client;

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
import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;
import me.googas.net.sockets.AwaitingRequest;
import me.googas.net.sockets.ThrowableHandler;
import me.googas.net.sockets.json.JsonMessenger;
import me.googas.net.sockets.json.reflect.JsonReceptor;
import me.googas.net.sockets.json.server.JsonSocketServer;

/** This object represents a client that can be used to connect to the {@link JsonSocketServer} */
public class JsonClient extends Thread implements JsonMessenger {

  /** The builder to build json strings */
  @NonNull private final StringBuilder builder = new StringBuilder();

  /** The socket that the client is using */
  @NonNull private final Socket socket;

  /** The output channel */
  @NonNull private final PrintWriter out;
  /** The input channel */
  @NonNull private final BufferedReader in;

  /**
   * The throwable handler in case something goes wrong and the user wants to handle it differently
   */
  @NonNull private final ThrowableHandler throwableHandler;

  /** The gson instance to serialize and deserialize objects */
  @NonNull private final Gson gson;

  /** The receptors to accept requests */
  @NonNull private final Set<JsonReceptor> receptors;

  /** The request that are waiting for a response */
  @NonNull private final HashMap<AwaitingRequest<?>, Long> requests = new HashMap<>();

  /** The time to timeout requests */
  private final long timeout;

  /** Whether the messenger is closed */
  private boolean closed;

  /** The millis of when the last message was sent */
  private long lastMessage;

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
      @NonNull ThrowableHandler throwableHandler,
      @NonNull Gson gson,
      @NonNull Set<JsonReceptor> receptors,
      long timeout)
      throws IOException {
    this.socket = socket;
    this.throwableHandler = throwableHandler;
    this.gson = gson;
    this.receptors = receptors;
    this.timeout = timeout;
    this.out =
        new PrintWriter(
            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    this.in =
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
      @NonNull ThrowableHandler throwableHandler,
      @NonNull Gson gson,
      long timeout)
      throws IOException {
    this(socket, throwableHandler, gson, new HashSet<>(), timeout);
  }

  /** Closes the messenger */
  @Override
  public void close() {
    this.setClosed(true);
    this.out.close();
    try {
      this.in.close();
    } catch (IOException e) {
      this.throwableHandler.handle(e);
    }
    try {
      this.socket.close();
    } catch (IOException e) {
      this.throwableHandler.handle(e);
    }
    this.receptors.clear();
    this.requests.clear();
  }

  /**
   * Adds the parsed receptors from the given object. This will get the receptors from the object
   * using {@link JsonReceptor#getReceptors(Object)} and add them to the set
   *
   * @param objects the objects to add as receptors
   */
  public void addReceptors(@NonNull Object... objects) {
    for (Object object : objects) {
      this.receptors.addAll(JsonReceptor.getReceptors(object));
    }
  }

  /**
   * Get the output line to send messages
   *
   * @return the output line to send messages
   */
  @Override
  public @NonNull PrintWriter getOutput() {
    return this.out;
  }

  /**
   * Get the input line to receive messages
   *
   * @return the input line
   */
  @Override
  public @NonNull BufferedReader getInput() {
    return this.in;
  }

  /**
   * Get the receptors that the messenger is capable of using
   *
   * @return the collection of receptors
   */
  @Override
  public @NonNull Collection<JsonReceptor> getReceptors() {
    return this.receptors;
  }

  /**
   * Get when request may timeout
   *
   * @return the time to timeout in millis
   */
  @Override
  public long getTimeout() {
    return this.timeout;
  }

  @Override
  public void run() {
    JsonMessenger.super.run();
  }

  @NonNull
  @Override
  public HashMap<AwaitingRequest<?>, Long> getRequests() {
    return this.requests;
  }

  @Override
  public @NonNull Socket getSocket() {
    return this.socket;
  }

  @Override
  public long getLastMessage() {
    return this.lastMessage;
  }

  @Override
  public void setLastMessage(long millis) {
    this.lastMessage = millis;
  }

  @Override
  public @NonNull StringBuilder getBuilder() {
    return this.builder;
  }

  /**
   * Get the gson instance that this messenger may use
   *
   * @return the gson instance
   */
  @Override
  public @NonNull Gson getGson() {
    return this.gson;
  }

  @Override
  public boolean isClosed() {
    return this.closed;
  }

  @Override
  public void setClosed(boolean bol) {
    this.closed = bol;
  }

  /**
   * Get the throwable handler that this messenger uses in case of a wrong request
   *
   * @return the throwable handler
   */
  @Override
  public @NonNull ThrowableHandler getThrowableHandler() {
    return this.throwableHandler;
  }
}
