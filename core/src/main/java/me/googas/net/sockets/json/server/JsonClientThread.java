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
import lombok.NonNull;
import me.googas.net.sockets.AwaitingRequest;
import me.googas.net.sockets.Error;
import me.googas.net.sockets.ReceivedRequest;
import me.googas.net.sockets.Response;
import me.googas.net.sockets.ThrowableHandler;
import me.googas.net.sockets.json.JsonMessenger;
import me.googas.net.sockets.json.reflect.JsonReceptor;

/**
 * A guido client thread is the {@link Thread} where a client connected to the {@link
 * JsonSocketServer}.
 */
public class JsonClientThread extends Thread implements JsonMessenger {

  /** The builder to build json strings */
  @NonNull private final StringBuilder builder = new StringBuilder();

  /** The socket that is connected to the client */
  @NonNull private final Socket socket;

  /** The line that is being an input into the server */
  @NonNull private final BufferedReader input;

  /** The output used to send requests to the client */
  @NonNull private final PrintWriter output;

  /** The server to which this client is connected to */
  @NonNull private final JsonSocketServer server;

  /** The request that are waiting for a response */
  @NonNull private final HashMap<AwaitingRequest<?>, Long> requests = new HashMap<>();

  /** The time to timeout requests */
  private final long timeout;

  /** Whether the messenger is closed */
  private boolean closed;

  /** The millis of when the last message was sent */
  private long lastMessage;

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

  /**
   * Get the server to which the thread is connected
   *
   * @return the socket server
   */
  @NonNull
  public JsonSocketServer getServer() {
    return this.server;
  }

  /**
   * Get the output line to send messages
   *
   * @return the output line to send messages
   */
  @Override
  public @NonNull PrintWriter getOutput() {
    return this.output;
  }

  /**
   * Get the input line to receive messages
   *
   * @return the input line
   */
  @Override
  public @NonNull BufferedReader getInput() {
    return this.input;
  }

  /**
   * Get the receptors that the messenger is capable of using
   *
   * @return the collection of receptors
   */
  @Override
  public @NonNull Collection<JsonReceptor> getReceptors() {
    return this.server.getReceptors();
  }

  @NonNull
  @Override
  public HashMap<AwaitingRequest<?>, Long> getRequests() {
    return this.requests;
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

  @Override
  public @NonNull Socket getSocket() {
    return this.socket;
  }

  @Override
  public @NonNull StringBuilder getBuilder() {
    return this.builder;
  }

  @Override
  public long getLastMessage() {
    return this.lastMessage;
  }

  @Override
  public void setLastMessage(long millis) {
    this.lastMessage = millis;
  }

  /**
   * Get the gson instance that this messenger may use
   *
   * @return the gson instance
   */
  @Override
  public @NonNull Gson getGson() {
    return this.server.getGson();
  }

  /**
   * Get the throwable handler that this messenger uses in case of a wrong request
   *
   * @return the throwable handler
   */
  @Override
  public @NonNull ThrowableHandler getThrowableHandler() {
    return this.server.getThrowableHandler();
  }

  /** Closes the messenger */
  @Override
  public void close() {
    this.setClosed(true);
    this.requests.clear();
    this.output.close();
    try {
      this.socket.close();
    } catch (IOException e) {
      this.server.getThrowableHandler().handle(e);
    }
    try {
      this.input.close();
    } catch (IOException e) {
      this.server.getThrowableHandler().handle(e);
    }
    this.server.remove(this);
    this.requests.clear();
  }

  @Override
  public boolean isClosed() {
    return this.closed;
  }

  @Override
  public void setClosed(boolean bol) {
    this.closed = bol;
  }

  @Override
  public void acceptRequest(@NonNull ReceivedRequest request) {
    Authenticator authenticator = this.server.getAuthenticator();
    if (authenticator != null) {
      if (authenticator.isAuthenticated(this, request)) {
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
