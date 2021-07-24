package me.googas.io.context;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.io.StarboxFile;

/**
 * Reads '.json' files to return any type of object that can be deserialized or serialized check
 * {@link Gson} for more information
 */
public class Json implements FileContext<Object> {

  @NonNull @Getter @Setter private Gson gson;

  /**
   * Create the context instance
   *
   * @param gson the instance to deserialize or serialize objects
   */
  public Json(@NonNull Gson gson) {
    this.gson = gson;
  }

  @Override
  @NonNull
  public <T> Optional<T> read(@NonNull StarboxFile file, @NonNull Class<T> type) {
    if (!file.exists()) return Optional.empty();
    BufferedReader reader = file.getBufferedReader();
    Optional<T> optional = Optional.ofNullable(this.gson.fromJson(reader, type));
    try {
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return optional;
  }

  @Override
  public boolean write(@NonNull StarboxFile file, @NonNull Object object) {
    FileWriter writer = file.getPreparedWriter(false);
    this.gson.toJson(object, writer);
    try {
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  @NonNull
  public <T> Optional<T> read(@NonNull URL resource, @NonNull Class<T> type) {
    InputStreamReader reader = null;
    try {
      reader = new InputStreamReader(resource.openStream());
    } catch (IOException e) {
      e.printStackTrace();
      return Optional.empty();
    }
    T t = this.gson.fromJson(reader, type);
    try {
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Optional.ofNullable(t);
  }

  @Override
  @NonNull
  public Optional<Object> read(@NonNull StarboxFile file) {
    return Optional.ofNullable(this.read(file, Object.class));
  }

  @Override
  @NonNull
  public Optional<Object> read(@NonNull URL resource) {
    return Optional.ofNullable(this.read(resource, Object.class));
  }
}
