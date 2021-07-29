package me.googas.scheduler;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class StarboxRunlater implements RunLater {

  @Setter private long start = System.currentTimeMillis();
  private final int id;
  @NonNull @Setter @Getter private Runnable onEnd;
  private boolean cancelled;

  public StarboxRunlater(int id, @NonNull Runnable onEnd) {
    this.id = id;
    this.onEnd = onEnd;
  }

  @Override
  public void run() {
    if (!this.cancelled) this.onEnd.run();
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public boolean cancel() {
    if (this.cancelled) return false;
    this.cancelled = true;
    return true;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public long startedAt() {
    return this.start;
  }
}
