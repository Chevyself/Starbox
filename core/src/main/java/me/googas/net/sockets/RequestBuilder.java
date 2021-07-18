package me.googas.net.sockets;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.NonNull;
import me.googas.net.sockets.api.Messenger;
import me.googas.net.sockets.api.MessengerListenFailException;
import me.googas.net.sockets.api.Server;

public class RequestBuilder<T> {

  @NonNull private final Class<T> clazz;
  @NonNull private final Map<String, Object> parameters;
  @NonNull private String method;

  public RequestBuilder(
      @NonNull Class<T> clazz, @NonNull String method, @NonNull Map<String, Object> parameters) {
    this.clazz = clazz;
    this.method = method;
    this.parameters = parameters;
  }

  public RequestBuilder(@NonNull Class<T> clazz, @NonNull String method) {
    this(clazz, method, new HashMap<>());
  }

  public RequestBuilder(@NonNull Class<T> clazz) {
    this(clazz, "none", new HashMap<>());
  }

  @NonNull
  public RequestBuilder<T> put(@NonNull String key, @NonNull Object value) {
    this.parameters.put(key, value);
    return this;
  }

  @NonNull
  public RequestBuilder<T> putAll(@NonNull Map<String, ?> map) {
    this.parameters.putAll(map);
    return this;
  }

  @NonNull
  public Request<T> build() {
    return new Request<>(this.clazz, this.method, this.parameters);
  }

  public T send(Messenger messenger) throws MessengerListenFailException {
    if (messenger == null) return null;
    return messenger.sendRequest(this.build());
  }

  public void send(Messenger messenger, @NonNull Consumer<Optional<T>> consumer) {
    if (messenger == null) {
      consumer.accept(Optional.empty());
      return;
    }
    messenger.sendRequest(this.build(), consumer);
  }

  public void queue(Messenger messenger) {
    this.send(messenger, (optional) -> {});
  }

  @NonNull
  public Map<Messenger, T> send(Server server) {
    if (server == null) return new HashMap<>();
    return server.sendRequest(this.build());
  }

  public void send(Server server, @NonNull BiConsumer<Messenger, Optional<T>> consumer) {
    server.sendRequest(this.build(), consumer);
  }

  public void queue(@NonNull Server server) {
    this.send(server, (messenger, optional) -> {});
  }

  @NonNull
  public RequestBuilder<T> setMethod(@NonNull String method) {
    this.method = method;
    return this;
  }
}
