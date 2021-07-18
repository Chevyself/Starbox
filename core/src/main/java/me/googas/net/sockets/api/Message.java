package me.googas.net.sockets.api;

import java.util.UUID;
import lombok.NonNull;

/** A message send between two messengers */
public interface Message {

  /**
   * Get the unique id of the message
   *
   * @return the unique id of the message
   */
  @NonNull
  UUID getId();
}
