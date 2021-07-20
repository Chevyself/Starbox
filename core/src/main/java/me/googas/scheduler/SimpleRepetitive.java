package me.googas.scheduler;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class SimpleRepetitive implements Repetitive {

  private final int id;
  @Setter private long start = System.currentTimeMillis();
  private long pause = 0;
  private boolean cancelled;

  private boolean paused;

  @NonNull @Getter @Setter private Runnable repetitive;

  public SimpleRepetitive(int id, @NonNull Runnable repetitive) {
    this.id = id;
    this.repetitive = repetitive;
  }

  @Override
  public void run() {
    if (this.cancelled || this.paused) return;
    this.repetitive.run();
  }

  @Override
  public boolean cancel() {
    if (this.cancelled) return false;
    this.cancelled = true;
    return true;
  }

  @Override
  public boolean pause() {
    if (this.cancelled || this.paused) return false;
    this.paused = true;
    this.pause = System.currentTimeMillis();
    return true;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public boolean isPaused() {
    return this.paused;
  }

  @Override
  public long startedAt() {
    return this.start;
  }

  @Override
  public long lastPause() {
    return this.pause;
  }
}
