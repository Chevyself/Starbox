package me.googas.net.cache;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.starbox.scheduler.Scheduler;
import me.googas.starbox.time.Time;
import me.googas.starbox.time.unit.Unit;

/** Simple implementation for {@link Cache}. */
public class MemoryCache extends TimerTask implements Cache {

  /** The map required for the cache. */
  @NonNull @Delegate
  private final Map<SoftReference<Catchable>, Long> map = new ConcurrentHashMap<>();

  @NonNull @Getter private Consumer<Throwable> handler = Throwable::printStackTrace;

  /**
   * Register this cache in a {@link Timer}. This will register the timer to be run every second
   *
   * @param timer the timer to register this cache
   * @return this same instance
   */
  @NonNull
  public MemoryCache register(@NonNull Timer timer) {
    timer.schedule(this, 1000, 1000);
    return this;
  }

  /**
   * Register this cache in a {@link Scheduler}. This will register the timer to be run every second
   *
   * @param scheduler the scheduler to register this cache
   * @return this same instance
   */
  @NonNull
  public MemoryCache register(@NonNull Scheduler scheduler) {
    Time time = Time.of(1, Unit.MILLIS);
    scheduler.repeat(time, time, this);
    return this;
  }

  @Override
  public void run() {
    Cache.super.run();
  }

  @Override
  public @NonNull Map<SoftReference<Catchable>, Long> getMap() {
    return this.map;
  }

  @Override
  public @NonNull Cache handle(@NonNull Consumer<Throwable> handler) {
    this.handler = handler;
    return this;
  }
}
