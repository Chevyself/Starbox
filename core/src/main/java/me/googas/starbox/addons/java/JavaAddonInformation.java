package me.googas.starbox.addons.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.addons.AddonInformation;

/** Each addon has important information which is required to initialize it. */
public class JavaAddonInformation implements AddonInformation {

  @NonNull @Getter private final String name;

  @NonNull @Getter private final String version;

  @NonNull @Getter private final String description;

  @NonNull @Getter private final String main;

  private JavaAddonInformation(
      @NonNull String name,
      @NonNull String version,
      @NonNull String description,
      @NonNull String main) {
    this.name = name;
    this.version = version;
    this.description = description;
    this.main = main;
  }

  /**
   * Get the information of an addon from the stream of the resource.
   *
   * @param stream the stream of the resource
   * @return the addon information
   * @throws IOException if the properties file to load from the stream
   */
  @NonNull
  public static JavaAddonInformation parse(@NonNull InputStream stream) throws IOException {
    Properties properties = new Properties();
    properties.load(stream);
    return new JavaAddonInformation(
        properties.getProperty("name", ""),
        properties.getProperty("version", ""),
        properties.getProperty("description", ""),
        properties.getProperty("main", ""));
  }
}
