package me.googas.starbox.events;

/** An {@link Event} that can be cancelled */
public interface Cancellable {

  /**
   * Sets whether the event is cancelled
   *
   * @param bol the new value to set if the event is cancelled
   */
  void setCancelled(boolean bol);

  /**
   * Get whether the event is cancelled
   *
   * @return true if the event is cancelled
   */
  boolean isCancelled();
}
