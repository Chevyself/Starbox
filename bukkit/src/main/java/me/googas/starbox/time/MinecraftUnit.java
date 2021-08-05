package me.googas.starbox.time;

import java.time.Duration;
import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.time.unit.StarboxUnit;

/** Represents Minecraft time units such as the 'TICK'. */
public enum MinecraftUnit implements StarboxUnit {
  TICK('T', 50, Duration.ofMillis(50));

  @Getter private final char single;
  @Getter private final long millis;
  @NonNull @Getter private final Duration duration;

  MinecraftUnit(char single, long millis, @NonNull Duration duration) {
    this.single = single;
    this.millis = millis;
    this.duration = duration;
  }

  @Override
  public boolean isDurationEstimated() {
    return true;
  }

  @Override
  public boolean isDateBased() {
    return false;
  }

  @Override
  public boolean isTimeBased() {
    return false;
  }
}
