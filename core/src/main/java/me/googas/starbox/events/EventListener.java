package me.googas.starbox.events;

import lombok.NonNull;

public interface EventListener {

  void call(@NonNull Event event);

  default int getPriority() {
    return 1;
  }
}
