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
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import me.googas.net.api.exception.MessengerListenFailException;
import me.googas.net.api.messages.Request;
import me.googas.net.api.Server;
import me.googas.net.api.auth.Authenticator;
import me.googas.net.sockets.json.JsonReceptor;

/** An implementation for socket servers for guido */
public class JsonSocketServer extends Thread implements Server<JsonClientThread> {

  /** The actual server socket */
  @NonNull private final ServerSocket server;

  /** The set of clients that are connected to the server */
  @NonNull @Getter private final Set<JsonClientThread> clients = new HashSet<>();

  /** The receptors to accept requests */
  @NonNull @Getter private final Set<JsonReceptor> receptors;

  /** To handle exceptions thrown */
  @NonNull @Getter private final Consumer<Throwable> throwableHandler;
  /** the gson instance for the server and clients deserialization */
  @NonNull @Getter private final Gson gson;
  /** The time to timeout requests */
  @Getter private final long timeout;
  /** The authenticator for the requests */
  private Authenticator<JsonClientThread> authenticator;

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
      @NonNull Consumer<Throwable> throwableHandler,
      Authenticator<JsonClientThread> authenticator,
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
      @NonNull Consumer<Throwable> throwableHandler,
      Authenticator<JsonClientThread> authenticator,
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

  @Override
  @NonNull
  public Optional<Authenticator<JsonClientThread>> getAuthenticator() {
    return Optional.ofNullable(this.authenticator);
  }

  @Override
  public boolean hasAuthentication() {
    return this.authenticator != null;
  }

  @Override
  public @NonNull JsonSocketServer setAuthenticator(
      @NonNull Authenticator<JsonClientThread> authenticator) {
    this.authenticator = authenticator;
    return this;
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
  public void run() {
    while (true) {
      try {
        Socket socket = this.server.accept();
        JsonClientThread client = new JsonClientThread(socket, this, this.timeout);
        client.start();
        this.clients.add(client);
        this.onConnection(client);
      } catch (IOException e) {
        this.throwableHandler.accept(e);
        break;
      }
    }
  }

  @Override
  public <T> void sendRequest(
      @NonNull Request<T> request, BiConsumer<JsonClientThread, Optional<T>> consumer) {
    this.clients.forEach(
        client -> {
          try {
            consumer.accept(client, client.sendRequest(request));
          } catch (MessengerListenFailException e) {
            this.throwableHandler.accept(e);
          }
        });
  }

  @Override
  @NonNull
  public <T> Map<JsonClientThread, Optional<T>> sendRequest(@NonNull Request<T> request) {
    Map<JsonClientThread, Optional<T>> responses = new HashMap<>();
    this.clients.forEach(
        client -> {
          try {
            responses.put(client, client.sendRequest(request));
          } catch (MessengerListenFailException e) {
            this.throwableHandler.accept(e);
          }
        });
    return responses;
  }
}
