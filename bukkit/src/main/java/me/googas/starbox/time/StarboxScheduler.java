package me.googas.starbox.time;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;
import me.googas.scheduler.Countdown;
import me.googas.scheduler.Repetitive;
import me.googas.scheduler.RunLater;
import me.googas.scheduler.Scheduler;
import me.googas.scheduler.Task;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StarboxScheduler implements Scheduler {

  @NonNull private final Plugin plugin;

  @NonNull private final Set<Task> tasks = new HashSet<>();

  public StarboxScheduler(@NonNull Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public @NonNull Countdown countdown(@NonNull Time time, @NonNull Countdown task) {
    return (Countdown) this.repeat(time, time, task);
  }

  @Override
  public @NonNull RunLater later(@NonNull Time time, @NonNull RunLater task) {
    new BukkitRunnable() {
      @Override
      public void run() {
        if (task.isCancelled()) {
          this.cancel();
          StarboxScheduler.this.tasks.remove(task);
          return;
        }
        task.run();
      }
    }.runTaskLater(this.plugin, time.getAs(MinecraftUnit.TICK).getValueRound());
    this.tasks.add(task);
    return task;
  }

  @Override
  public @NonNull Repetitive repeat(
      @NonNull Time initial, @NonNull Time period, @NonNull Repetitive task) {
    long initialValue = initial.getAs(MinecraftUnit.TICK).getValueRound();
    long periodValue = period.getAs(MinecraftUnit.TICK).getValueRound();
    new BukkitRunnable() {
      @Override
      public void run() {
        if (task.isCancelled()) {
          this.cancel();
          StarboxScheduler.this.tasks.remove(task);
          return;
        }
        task.run();
      }
    }.runTaskTimer(this.plugin, initialValue, periodValue);
    this.tasks.add(task);
    return task;
  }

  @Override
  public @NonNull Collection<Task> getTasks() {
    return this.tasks;
  }
}
