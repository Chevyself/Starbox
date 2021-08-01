package me.googas.net.api.auth;

import lombok.NonNull;
import me.googas.net.api.Messenger;
import me.googas.net.api.StarboxRequest;

/** This authenticates a request */
public interface Authenticator<T extends Messenger> {

  /**
   * Get whether a request is authenticated
   *
   * @param client the client that is doing the request
   * @param request the request to check if it is authenticated
   * @return true if the request is allowed
   */
  boolean isAuthenticated(@NonNull T client, @NonNull StarboxRequest request);
}
