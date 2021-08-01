package me.googas.io.context;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.io.StarboxFile;
import me.googas.starbox.HandledExpression;

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

  @NonNull
  @Override
  public <T> HandledExpression<T> read(@NonNull Reader reader, @NonNull Class<T> type) {
    return HandledExpression.using(
        () -> {
          return this.gson.fromJson(reader, type);
        });
  }

  @Override
  @NonNull
  public <T> HandledExpression<T> read(@NonNull StarboxFile file, @NonNull Class<T> type) {
    AtomicReference<Reader> atomicReader = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              T other = null;
              if (file.exists()) {
                Reader reader = file.getBufferedReader();
                other = this.gson.fromJson(reader, type);
                atomicReader.set(reader);
              }
              return other;
            })
        .next(
            () -> {
              Reader reader = atomicReader.get();
              if (reader != null) reader.close();
            });
  }

  @Override
  @NonNull
  public HandledExpression<Boolean> write(@NonNull StarboxFile file, @NonNull Object object) {
    AtomicReference<Writer> atomicWriter = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              Writer writer = file.getPreparedWriter(false);
              this.gson.toJson(object, writer);
              atomicWriter.set(writer);
              return true;
            })
        .next(
            () -> {
              Writer writer = atomicWriter.get();
              if (writer != null) writer.close();
            });
  }

  @Override
  @NonNull
  public HandledExpression<Boolean> write(@NonNull Writer writer, @NonNull Object object) {
    return HandledExpression.using(
        () -> {
          this.gson.toJson(object, writer);
          return true;
        });
  }

  @Override
  @NonNull
  public <T> HandledExpression<T> read(@NonNull URL resource, @NonNull Class<T> type) {
    AtomicReference<Reader> atomicReader = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              Reader reader = new InputStreamReader(resource.openStream());
              T other = this.gson.fromJson(reader, type);
              atomicReader.set(reader);
              return other;
            })
        .next(
            () -> {
              Reader reader = atomicReader.get();
              if (reader != null) {
                  reader.close();
              }
            });
  }

  @Override
  @NonNull
  public HandledExpression<Object> read(@NonNull StarboxFile file) {
    return this.read(file, Object.class);
  }

  @Override
  @NonNull
  public HandledExpression<Object> read(@NonNull URL resource) {
    return this.read(resource, Object.class);
  }
}
