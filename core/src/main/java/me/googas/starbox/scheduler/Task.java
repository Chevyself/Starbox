package me.googas.starbox.scheduler;

import lombok.NonNull;
import me.googas.starbox.time.Time;

/** Represents a task that may be registered in a {@link Scheduler} */
public interface Task extends Runnable {

  /**
   * Cancel task.
   *
   * @return true if the task was cancelled
   */
  boolean cancel();

  /**
   * Pauses the task.
   *
   * @return whether the task was paused
   */
  boolean pause();

  /**
   * Get the {@link System#currentTimeMillis()} when the task was started.
   *
   * @return the time when task was started
   */
  long startedAt();

  /**
   * Get the time that has passed since {@link #startedAt()}.
   *
   * @return the time since started at
   */
  @NonNull
  default Time sinceStartedAt() {
    long millis = System.currentTimeMillis() - this.startedAt();
    return Time.ofMillis(millis < 0 ? 0 : millis, true);
  }

  /**
   * Get the time that has passed since {@link #lastPause()}.
   *
   * @return the time since the task was last paused
   */
  @NonNull
  default Time sinceLastPause() {
    long millis = System.currentTimeMillis() - this.lastPause();
    return Time.ofMillis(millis < 0 ? 0 : millis, true);
  }

  /**
   * Get the id of the task.
   *
   * @return the id of the task as an integer
   */
  int getId();

  /**
   * Get whether the task is cancelled.
   *
   * @return true if the task is cancelled
   */
  boolean isCancelled();

  /**
   * Get the {@link System#currentTimeMillis()} when the task was last paused. 0 if the task hasn't
   * been paused
   *
   * @return the time when the task was last paused
   */
  long lastPause();

  /**
   * Get whether the task is paused and can run again.
   *
   * @return true if the task is paused
   */
  boolean isPaused();
}
