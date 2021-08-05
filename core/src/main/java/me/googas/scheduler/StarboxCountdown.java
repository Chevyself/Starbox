package me.googas.scheduler;

import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.starbox.time.Time;
import me.googas.starbox.time.unit.Unit;

/** An implementation for {@link Countdown}. */
public class StarboxCountdown implements Countdown {

  @Setter private long start = System.currentTimeMillis();
  private long pause = 0;
  @NonNull private final int id;
  @NonNull @Getter @Setter private Time time;
  @Setter private long secondsLeft;
  @NonNull @Getter @Setter private Consumer<Time> onSecondPassed;

  @NonNull @Getter @Setter private Runnable onFinish;

  private boolean cancelled = false;

  @Setter private boolean paused = false;

  /**
   * Create the countdown.
   *
   * @param time the time to count down
   * @param id the id of the task
   * @param onSecondPassed a consumer to run when a second has passed of the countdown
   * @param onFinish a runnable to execute when the time has been completed
   */
  public StarboxCountdown(
      @NonNull Time time,
      @NonNull int id,
      @NonNull Consumer<Time> onSecondPassed,
      @NonNull Runnable onFinish) {
    this.time = time;
    this.id = id;
    this.onSecondPassed = onSecondPassed;
    this.onFinish = onFinish;
    this.refresh();
  }

  @Override
  public void run() {
    if (this.paused || this.cancelled) return;
    this.secondsLeft--;
    if (this.secondsLeft > 0) {
      this.onSecondPassed.accept(this.getTimeLeft());
    } else {
      this.onFinish.run();
      this.cancel();
    }
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
  public boolean pause() {
    if (this.cancelled || this.paused) return false;
    this.paused = true;
    this.pause = System.currentTimeMillis();
    return true;
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

  @Override
  @NonNull
  public StarboxCountdown refresh() {
    this.secondsLeft = Math.round(this.time.get(Unit.SECONDS));
    return this;
  }

  @Override
  public long getMillisLeft() {
    return this.secondsLeft * 1000;
  }
}
