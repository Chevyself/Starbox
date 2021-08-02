package me.googas.io.context;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import lombok.NonNull;
import me.googas.io.StarboxFile;
import me.googas.starbox.expressions.HandledExpression;

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
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns
   *     whether the object was written and handles {@link java.io.IOException}
   */
  public HandledExpression<Boolean> write(
      @NonNull StarboxFile file, @NonNull Properties properties, String comment) {
    AtomicReference<Writer> atomicWriter = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              Writer writer = file.getPreparedWriter(false);
              properties.store(writer, comment);
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
  public @NonNull HandledExpression<Properties> read(@NonNull StarboxFile file) {
    AtomicReference<Reader> atomicReader = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              Properties properties = new Properties();
              Reader reader = file.getBufferedReader();
              properties.load(reader);
              atomicReader.set(reader);
              return properties;
            })
        .next(
            () -> {
              Reader reader = atomicReader.get();
              if (reader != null) reader.close();
            });
  }

  @Override
  @NonNull
  public HandledExpression<Properties> read(@NonNull URL resource) {
    AtomicReference<InputStream> atomicStream = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              Properties properties = new Properties();
              InputStream stream = resource.openStream();
              properties.load(stream);
              atomicStream.set(stream);
              return properties;
            })
        .next(
            () -> {
              InputStream stream = atomicStream.get();
              if (stream != null) stream.close();
            });
  }

  @Override
  @NonNull
  public <T> HandledExpression<T> read(@NonNull StarboxFile file, @NonNull Class<T> type) {
    throw new UnsupportedOperationException(
        "Read has not been implemented for '.properties' files");
  }

  @Override
  public @NonNull <T> HandledExpression<T> read(@NonNull Reader reader, @NonNull Class<T> type) {
    throw new UnsupportedOperationException(
        "Read has not been implemented for '.properties' files");
  }

  @Override
  public HandledExpression<Boolean> write(@NonNull StarboxFile file, @NonNull Object object) {
    throw new UnsupportedOperationException(
        "Write has not been implemented for '.properties' files");
  }

  @Override
  @NonNull
  public HandledExpression<Boolean> write(@NonNull Writer writer, @NonNull Object object) {
    throw new UnsupportedOperationException(
        "Write has not been implemented for '.properties' files");
  }

  @Override
  @NonNull
  public <T> HandledExpression<T> read(@NonNull URL resource, @NonNull Class<T> type) {
    throw new UnsupportedOperationException(
        "Read has not been implemented for '.properties' files");
  }
}
