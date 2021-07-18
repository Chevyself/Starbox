package me.googas.net.sockets.json.server;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import lombok.NonNull;
import me.googas.net.sockets.Request;
import me.googas.net.sockets.ThrowableHandler;
import me.googas.net.sockets.api.Messenger;
import me.googas.net.sockets.api.MessengerListenFailException;
import me.googas.net.sockets.api.Server;
import me.googas.net.sockets.json.reflect.JsonReceptor;

/** An implementation for socket servers for guido */
public class JsonSocketServer extends Thread implements Server {

  /** The actual server socket */
  @NonNull private final ServerSocket server;

  /** The set of clients that are connected to the server */
  @NonNull private final Set<JsonClientThread> clients = new HashSet<>();

  /** The receptors to accept requests */
  @NonNull private final Set<JsonReceptor> receptors;

  /** To handle exceptions thrown */
  @NonNull private final ThrowableHandler throwableHandler;
  /** the gson instance for the server and clients deserialization */
  @NonNull private final Gson gson;
  /** The time to timeout requests */
  private final long timeout;
  /** The authenticator for the requests */
  private Authenticator authenticator;

  /**
   * Creates the guido socket server
   *
   * @param port the port to which the server will be listening to
   * @param receptors the receptors to accept requests
   * @param throwableHandler to handle exceptions thrown
   * @param authenticator the authenticator for requests
   * @param gson the gson instance for the server and clients deserialization
   * @param timeout the time too timeout requests
   * @throws IOException if the port is already in use
   */
  public JsonSocketServer(
      int port,
      @NonNull Set<JsonReceptor> receptors,
      @NonNull ThrowableHandler throwableHandler,
      Authenticator authenticator,
      @NonNull Gson gson,
      long timeout)
      throws IOException {
    this.server = new ServerSocket(port);
    this.receptors = receptors;
    this.throwableHandler = throwableHandler;
    this.authenticator = authenticator;
    this.gson = gson;
    this.timeout = timeout;
  }

  /**
   * Creates the guido socket server with the default receptors and providers
   *
   * @param port the port to which the server will be listening to
   * @param throwableHandler to handle exceptions thrown
   * @param authenticator the authenticator for requests
   * @param gson the gson instance for the server and clients deserialization
   * @param timeout the time too timeout requests
   * @throws IOException if the port is already in use
   */
  public JsonSocketServer(
      int port,
      @NonNull ThrowableHandler throwableHandler,
      Authenticator authenticator,
      @NonNull Gson gson,
      long timeout)
      throws IOException {
    this(port, new HashSet<>(), throwableHandler, authenticator, gson, timeout);
  }

  /**
   * Remove a client from the set of clients
   *
   * @param client the client to remove from the set
   */
  public void remove(@NonNull JsonClientThread client) {
    this.clients.remove(client);
    this.onRemove(client);
  }

  /**
   * Called when a client is already disconnected and {@link #remove(JsonClientThread)} was called
   *
   * @param client the client that was removed
   */
  protected void onRemove(@NonNull JsonClientThread client) {
    System.out.println(client + " got disconnected");
  }

  /**
   * Disconnects a client from the server
   *
   * @param client the client that disconnected
   */
  public void disconnect(@NonNull JsonClientThread client) {
    client.close();
    this.remove(client);
  }

  /**
   * Called when a client gets connected to the server
   *
   * @param client the client connecting to the server
   */
  protected void onConnection(@NonNull JsonClientThread client) {
    System.out.println(client + " got connected");
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
   * Get the authenticator for request
   *
   * @return the authenticator or null if not set
   */
  public Authenticator getAuthenticator() {
    return this.authenticator;
  }

  /**
   * Set the authenticator for request
   *
   * @param authenticator the new authenticator
   */
  public void setAuthenticator(Authenticator authenticator) {
    this.authenticator = authenticator;
  }

  /**
   * Get the gson instance that the server and clients are using
   *
   * @return the gson instance
   */
  @NonNull
  public Gson getGson() {
    return this.gson;
  }

  /**
   * Get the receptors to accept requests
   *
   * @return the receptors for requests
   */
  @NonNull
  public Set<JsonReceptor> getReceptors() {
    return this.receptors;
  }

  /**
   * Get the way to handle thrown exceptions
   *
   * @return the throwable handler
   */
  @NonNull
  public ThrowableHandler getThrowableHandler() {
    return this.throwableHandler;
  }

  @Override
  public boolean requiresAuthentication() {
    return this.authenticator != null;
  }

  @Override
  public void close() throws IOException {
    List<JsonClientThread> copy = new ArrayList<>(this.getClients());
    for (JsonClientThread client : copy) {
      this.disconnect(client);
    }
    this.server.close();
    this.receptors.clear();
  }

  @Override
  public void setRequiresAuthentication(boolean bol) {
    throw new UnsupportedOperationException(
        "If you wish to remove authentication use #setAuthenticator");
  }

  @Override
  public void run() {
    while (true) {
      try {
        Socket socket = this.server.accept();
        JsonClientThread client = new JsonClientThread(socket, this, this.timeout);
        client.start();
        this.clients.add(client);
        this.onConnection(client);
      } catch (IOException e) {
        this.throwableHandler.handle(e);
        break;
      }
    }
  }

  /**
   * Get the clients that are connected to the server
   *
   * @return the set of clients connected to the server
   */
  @NonNull
  @Override
  public Set<JsonClientThread> getClients() {
    return this.clients;
  }

  @Override
  public <T> void sendRequest(
      @NonNull Request<T> request, BiConsumer<Messenger, Optional<T>> consumer) {
    for (JsonClientThread client : this.clients) {
      client.sendRequest(request, consumer);
    }
  }

  @Override
  @NonNull
  public <T> Map<Messenger, T> sendRequest(@NonNull Request<T> request) {
    HashMap<Messenger, T> responses = new HashMap<>();
    for (JsonClientThread client : this.clients) {
      try {
        responses.put(client, client.sendRequest(request));
      } catch (MessengerListenFailException e) {
        this.throwableHandler.handle(e);
      }
    }
    return responses;
  }
}
