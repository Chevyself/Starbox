package me.googas.starbox.addons.dependencies;

import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.addons.AddonInformation;

/** Contains the information of a {@link DependencyAddon}. */
public class DependencyInformation implements AddonInformation {

  @NonNull @Getter private final String name;
  @NonNull @Getter private final String version;
  @NonNull @Getter private final String description;
  /** {@link AddonInformation} requires a main but we dont got one in this type of information. */
  @NonNull @Getter private final String main = "none";

  /**
   * Create the information.
   *
   * @param name the name of the dependency
   * @param version the version of the dependency
   * @param description the description of the dependency
   */
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
