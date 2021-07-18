package me.googas.net.sockets.json.server;

import lombok.NonNull;
import me.googas.net.sockets.IRequest;

/** This authenticates a request */
public interface Authenticator {

  /**
   * Get whether a request is authenticated
   *
   * @param client the client that is doing the request
   * @param request the request to check if it is authenticated
   * @return true if the request is allowed
   */
  boolean isAuthenticated(@NonNull JsonClientThread client, @NonNull IRequest request);
}
