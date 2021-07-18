package me.googas.net.sockets.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.NonNull;
import me.googas.net.sockets.AwaitingRequest;
import me.googas.net.sockets.Error;
import me.googas.net.sockets.IRequest;
import me.googas.net.sockets.ReceivedRequest;
import me.googas.net.sockets.Request;
import me.googas.net.sockets.Response;
import me.googas.net.sockets.ThrowableHandler;
import me.googas.net.sockets.api.Message;
import me.googas.net.sockets.api.Messenger;
import me.googas.net.sockets.api.MessengerListenFailException;
import me.googas.net.sockets.json.exception.JsonCommunicationException;
import me.googas.net.sockets.json.exception.JsonExternalCommunicationException;
import me.googas.net.sockets.json.exception.JsonInternalCommunicationException;
import me.googas.net.sockets.json.reflect.JsonReceptor;
import me.googas.net.sockets.json.reflect.JsonReceptorParameter;
import me.googas.net.sockets.json.server.JsonClientThread;

/** A {@link Messenger} that works with json messages */
public interface JsonMessenger extends Messenger, Runnable {

  /**
   * Prints a line in the output stream
   *
   * @param line the line to print
   */
  default void printLine(@NonNull String line) {
    this.getOutput().println(line + "\n---");
  }

  /**
   * Get the awaiting request matching the uuid
   *
   * @param uuid the uuid to match
   * @return the matched request
   */
  default AwaitingRequest<?> getRequest(@NonNull UUID uuid) {
    for (AwaitingRequest<?> request : this.getRequests().keySet()) {
      if (request.getRequest().getId().equals(uuid)) {
        return request;
      }
    }
    return null;
  }

  /**
   * Get the matching receptor for a request
   *
   * @param request the request that needs a receptor
   * @return the receptor if found else null
   */
  default JsonReceptor getReceptor(@NonNull IRequest request) {
    return this.getReceptor(request.getMethod());
  }

  /**
   * Get a receptor by its method
   *
   * @param method the method to match
   * @return the receptor if one with the method is found, null otherwise
   */
  default JsonReceptor getReceptor(@NonNull String method) {
    for (JsonReceptor receptor : this.getReceptors()) {
      if (receptor.getRequestMethod().equalsIgnoreCase(method)) {
        return receptor;
      }
    }
    return null;
  }

  /**
   * Accepts a request
   *
   * @param request the request to be accepted
   */
  default void acceptRequest(@NonNull ReceivedRequest request) {
    CompletableFuture.runAsync(
        () -> {
          JsonReceptor receptor = this.getReceptor(request);
          Response<?> response;
          if (receptor != null) {
            try {
              response =
                  new Response<>(
                      request.getId(), receptor.invoke(this.getParameters(receptor, request)));
              response.setError(false);
            } catch (InvocationTargetException
                | IllegalAccessException
                | JsonCommunicationException e) {
              if (e instanceof JsonExternalCommunicationException) {
                response = new Response<>(request.getId(), new Error(e.getMessage()));
              } else {
                response = new Response<>(request.getId(), new Error("Internal Error"));
                this.getThrowableHandler().handle(e);
              }
            }
          } else {
            response = new Response<>(request.getId(), null);
            response.setError(false);
          }
          this.printLine(this.getGson().toJson(response));
        });
  }

  /**
   * Get the parameters to invoke the receptor to get the response
   *
   * @param receptor the receptor to invoke
   * @param request the request that is using the receptor
   * @return the parameters to execute
   * @throws JsonCommunicationException in case something goes wrong while providing the objects
   */
  @NonNull
  default Object[] getParameters(@NonNull JsonReceptor receptor, @NonNull ReceivedRequest request)
      throws JsonCommunicationException {
    if (receptor.getParameters().isEmpty()) {
      return new Object[0];
    } else {
      Object[] objects = new Object[receptor.getParameters().size()];

      for (int i = 0; i < receptor.getParameters().size(); i++) {
        JsonReceptorParameter<?> parameter = receptor.getParameters().get(i);
        if (JsonMessenger.class == parameter.getClazz()) {
          objects[i] = this;
        } else if (request.getParameters().containsKey(parameter.getName())) {
          try {
            objects[i] =
                this.getGson()
                    .fromJson(
                        request.getParameters().get(parameter.getName()), parameter.getClazz());
          } catch (RuntimeException e) {
            throw new JsonExternalCommunicationException(e + " in request " + request);
          }
        } else {
          throw new JsonExternalCommunicationException(
              "Missing argument '" + parameter.getName() + "' in request " + request);
        }
      }
      return objects;
    }
  }

  /**
   * Adds all the given receptors
   *
   * @param receptors the receptors to add
   */
  default void addReceptors(@NonNull JsonReceptor... receptors) {
    this.getReceptors().addAll(Arrays.asList(receptors));
  }

  /**
   * Adds all the given receptors
   *
   * @param receptors the receptors to add
   */
  default void addReceptors(@NonNull Collection<JsonReceptor> receptors) {
    this.getReceptors().addAll(receptors);
  }

  /**
   * Set whether this messenger is closed
   *
   * @param bol the new value of closed
   */
  void setClosed(boolean bol);

  /**
   * @see Messenger#sendRequest(Request, Consumer). This method will give you the option to change
   *     what to do in case of an exception such as a timeout
   * @param request the request to send
   * @param consumer the method to execute when the result is given
   * @param exception the method to execute in case an exception is thrown
   * @param <T> the type of object requested
   */
  default <T> void sendRequest(
      @NonNull Request<T> request,
      @NonNull Consumer<Optional<T>> consumer,
      @NonNull Consumer<Throwable> exception) {
    this.getRequests()
        .put(
            new AwaitingRequest<>(request, request.getClazz(), consumer, exception),
            System.currentTimeMillis());
    this.printLine(this.getGson().toJson(request));
  }

  /**
   * Get the output line to send messages
   *
   * @return the output line to send messages
   */
  @NonNull
  PrintWriter getOutput();

  /**
   * Get the input line to receive messages
   *
   * @return the input line
   */
  @NonNull
  BufferedReader getInput();

  /**
   * Get the receptors that the messenger is capable of using
   *
   * @return the collection of receptors
   */
  @NonNull
  Collection<JsonReceptor> getReceptors();

  /**
   * Get when request may timeout
   *
   * @return the time to timeout in millis
   */
  long getTimeout();

  /**
   * Get whether this messenger is closed
   *
   * @return true if the messenger is closed
   */
  boolean isClosed();

  /**
   * Get the request that this messenger has sent and the time when they were sent
   *
   * @return the request that this messenger has sent
   */
  @NonNull
  Map<AwaitingRequest<?>, Long> getRequests();

  /**
   * Get the gson instance that this messenger may use
   *
   * @return the gson instance
   */
  @NonNull
  Gson getGson();

  /**
   * Get the throwable handler that this messenger uses in case of a wrong request
   *
   * @return the throwable handler
   */
  @NonNull
  ThrowableHandler getThrowableHandler();

  /**
   * Set the millis of the last message sent
   *
   * @param millis the millis of the last message sent
   */
  void setLastMessage(long millis);

  /**
   * Get the socket that this messenger is on
   *
   * @return the messenger
   */
  @NonNull
  Socket getSocket();

  @Override
  default void run() {
    while (true) {
      try {
        if (this.isClosed()) {
          break;
        } else {
          this.listen();
        }
      } catch (MessengerListenFailException e) {
        this.getThrowableHandler().handle(e);
        this.close();
        break;
      }
    }
  }

  /**
   * Get the string builder that the messenger can use
   *
   * @return the string builder
   */
  @NonNull
  StringBuilder getBuilder();

  /** Checks if there's request that can are taking too low. if so timeout */
  default void checkTimeout() {
    Set<AwaitingRequest<?>> toRemove = new HashSet<>();
    HashMap<AwaitingRequest<?>, Long> copy = new HashMap<>(this.getRequests());
    copy.forEach(
        (request, start) -> {
          if (System.currentTimeMillis() - start > this.getTimeout()) {
            toRemove.add(request);
            request
                .getExceptionConsumer()
                .accept(
                    new MessengerListenFailException(
                        "The request "
                            + request
                            + " has timed out after "
                            + this.getTimeout()
                            + "ms"));
          }
        });
    if (!toRemove.isEmpty()) {
      for (AwaitingRequest<?> request : toRemove) {
        this.getRequests().remove(request);
      }
    }
  }

  @Override
  default <T> void sendRequest(
      @NonNull Request<T> request, @NonNull Consumer<Optional<T>> consumer) {
    this.getRequests()
        .put(
            new AwaitingRequest<>(request, request.getClazz(), consumer),
            System.currentTimeMillis());
    this.printLine(this.getGson().toJson(request));
  }

  /**
   * Get the millis since the last message was sent
   *
   * @return the millis of the last message sent
   */
  long getLastMessage();

  @Override
  default void listen() throws MessengerListenFailException {
    try {
      StringBuilder builder = this.getBuilder();
      builder.setLength(0);
      String line;
      boolean closed = false;
      boolean timedOut = false;
      while ((line = this.getInput().readLine()) != null
          || (closed = this.getInput().read() == -1)
          || (timedOut =
              !this.getRequests().isEmpty()
                  && (System.currentTimeMillis() - this.getLastMessage()) > this.getTimeout())) {
        if (closed) break;
        if (timedOut) {
          this.checkTimeout();
          break;
        }
        if (line.startsWith("Invalid Message:")) {
          this.getThrowableHandler().handle(new JsonCommunicationException(line));
          builder.setLength(0);
          break;
        }
        if (line.equalsIgnoreCase("---") || !this.getInput().ready()) {
          break;
        } else {
          builder.append(line).append("\n");
        }
      }
      this.setLastMessage(System.currentTimeMillis());
      if (builder.length() != 0) {
        Gson gson = this.getGson();
        String json = builder.toString();
        try {
          Message message = gson.fromJson(json, Message.class);
          if (message instanceof ReceivedRequest) {
            this.acceptRequest((ReceivedRequest) message);
          } else if (message instanceof Response) {
            AwaitingRequest<?> awaitingRequest = this.getRequest(message.getId());
            JsonObject object = gson.fromJson(json, JsonObject.class);
            if (awaitingRequest != null) {
              if (((Response<?>) message).isError()) {
                if (!object.get("object").isJsonNull()) {
                  awaitingRequest
                      .getExceptionConsumer()
                      .accept(
                          new JsonInternalCommunicationException(
                              gson.fromJson(object.get("object"), Error.class).getCause()));
                } else {
                  awaitingRequest.getConsumer().accept(Optional.empty());
                }
              } else {
                if (!object.get("object").isJsonNull()) {
                  awaitingRequest
                      .getConsumer()
                      .accept(
                          Optional.ofNullable(
                              gson.fromJson(
                                  object.get("object"), (Type) awaitingRequest.getClazz())));
                } else {
                  awaitingRequest.getConsumer().accept(Optional.empty());
                }
              }
              this.getRequests().remove(awaitingRequest);
            }
          }
        } catch (RuntimeException e) {
          if (this instanceof JsonClientThread) {
            this.printLine("Invalid Message: " + e.getMessage());
          } else {
            this.getThrowableHandler().handle(e);
          }
        }
      }
    } catch (IOException e) {
      throw new MessengerListenFailException(null, e);
    }
  }

  @Override
  default <T> T sendRequest(@NonNull Request<T> request) throws MessengerListenFailException {
    long start = System.currentTimeMillis();
    AtomicReference<T> reference = new AtomicReference<>();
    AtomicReference<Throwable> throwable = new AtomicReference<>();
    AtomicBoolean received = new AtomicBoolean(false);
    this.sendRequest(
        request,
        obj -> {
          obj.ifPresent(reference::set);
          received.set(true);
        },
        exception -> {
          received.set(true);
          throwable.set(exception);
        });
    while (!received.get()) {
      if ((System.currentTimeMillis() - start) > this.getTimeout()) {
        throw new MessengerListenFailException(
            "The request " + request + " has timed out after " + this.getTimeout() + "ms");
      }
    }
    if (throwable.get() != null) {
      throw new MessengerListenFailException(null, throwable.get());
    }
    return reference.get();
  }
}
