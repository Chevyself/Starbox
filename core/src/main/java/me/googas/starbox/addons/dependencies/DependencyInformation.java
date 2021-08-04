package me.googas.starbox.addons.dependencies;

import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.addons.AddonInformation;

public class DependencyInformation implements AddonInformation {

  @NonNull @Getter private final String name;
  @NonNull @Getter private final String version;
  @NonNull @Getter private final String description;
  @NonNull @Getter private final String main = "None";

  public DependencyInformation(
      @NonNull String name, @NonNull String version, @NonNull String description) {
    this.name = name;
    this.version = version;
    this.description = description;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DependencyInformation.class.getSimpleName() + "[", "]")
        .add("name='" + name + "'")
        .add("version='" + version + "'")
        .add("description='" + description + "'")
        .add("main='" + main + "'")
        .toString();
  }
}
