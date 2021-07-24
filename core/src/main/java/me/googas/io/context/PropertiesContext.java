package me.googas.io.context;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import lombok.NonNull;
import me.googas.io.StarboxFile;

/**
 * Reads {@link Properties} from files. {@link #read(StarboxFile, Class)} and {@link
 * #write(StarboxFile, Object)} are not supported yet as there's no way to deserialize or serialize
 * objects for properties
 */
public class PropertiesContext implements FileContext<Properties> {

  /**
   * Write the {@link Properties} to the given file
   *
   * @param file the file to write the properties on
   * @param properties the properties to write on the file
   * @param comment a comment to leave on the file when the properties are written this can be null
   * @return whether the properties were written in the file successfully
   */
  public boolean write(@NonNull StarboxFile file, @NonNull Properties properties, String comment) {
    FileWriter writer = file.getPreparedWriter(false);
    try {
      properties.store(writer, comment);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    try {
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public @NonNull Optional<Properties> read(@NonNull StarboxFile file) {
    Properties properties = new Properties();
    BufferedReader reader = file.getBufferedReader();
    try {
      properties.load(reader);
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not read properties from " + file, e);
    }
    try {
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Optional.of(properties);
  }

  @Override
  @NonNull
  public Optional<Properties> read(@NonNull URL resource) {
    Properties properties = new Properties();
    try {
      properties.load(resource.openStream());
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not read Properties from InputStream", e);
    }
    return Optional.of(properties);
  }

  @Override
  @NonNull
  public <T> Optional<T> read(@NonNull StarboxFile file, @NonNull Class<T> type) {
    throw new UnsupportedOperationException(
        "Read has not been implemented for '.properties' files");
  }

  @Override
  public boolean write(@NonNull StarboxFile file, @NonNull Object object) {
    throw new UnsupportedOperationException(
        "Write has not been implemented for '.properties' files");
  }

  @Override
  @NonNull
  public <T> Optional<T> read(@NonNull URL resource, @NonNull Class<T> type) {
    throw new UnsupportedOperationException(
        "Read has not been implemented for '.properties' files");
  }
}
