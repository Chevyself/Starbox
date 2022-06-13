package me.googas.io.context;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import lombok.NonNull;
import me.googas.io.StarboxFile;
import me.googas.starbox.expressions.HandledExpression;

/**
 * Reads {@link String} from files. {@link #read(StarboxFile, Class)} and {@link #write(StarboxFile,
 * Object)} are not supported yet as there's no way to deserialize or serialize objects for plaint
 * text files
 */
public class Txt implements FileContext<String> {

  /**
   * Reads an String from a {@link BufferedReader}.
   *
   * @param reader the reader to read the string from
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns the
   *     read object and handles {@link java.io.IOException}
   */
  @NonNull
  public HandledExpression<String> read(@NonNull BufferedReader reader) {
    return HandledExpression.using(
            () -> {
              StringBuilder builder = new StringBuilder();
              String line;
              while ((line = reader.readLine()) != null) {
                builder.append(line);
              }
              return builder.toString();
            })
        .next(reader::close);
  }

  /**
   * Write the {@link String} to the given file.
   *
   * @param file the file to write the string on
   * @param string the string to write on the file
   * @param append whether to append the new content to the previous
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns
   *     whether the object was written and handles {@link java.io.IOException}
   */
  @NonNull
  public HandledExpression<Boolean> write(
      @NonNull StarboxFile file, @NonNull String string, boolean append) {
    AtomicReference<Writer> atomicWriter = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              FileWriter writer = file.getPreparedWriter(append);
              writer.write(string);
              writer.flush();
              return true;
            })
        .next(
            () -> {
              Writer writer = atomicWriter.get();
              if (writer != null) {
                writer.close();
              }
            });
  }

  @Override
  public @NonNull HandledExpression<String> read(@NonNull StarboxFile file) {
    AtomicReference<BufferedReader> atomicReader = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              BufferedReader reader = file.getBufferedReader();
              StringBuilder builder = new StringBuilder();
              String line;
              while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
              }
              builder.deleteCharAt(builder.length() - 1);
              atomicReader.set(reader);
              return builder.toString();
            })
        .next(
            () -> {
              BufferedReader reader = atomicReader.get();
              if (reader != null) reader.close();
            });
  }

  @Override
  @NonNull
  public HandledExpression<String> read(@NonNull URL resource) {
    AtomicReference<BufferedReader> atomicReader = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              BufferedReader reader =
                  new BufferedReader(new InputStreamReader(resource.openStream()));
              StringBuilder builder = new StringBuilder();
              String line;
              while ((line = reader.readLine()) != null) {
                builder.append(line);
              }
              return builder.toString();
            })
        .next(
            () -> {
              BufferedReader reader = atomicReader.get();
              if (reader != null) reader.close();
            });
  }

  @Override
  @NonNull
  public <T> HandledExpression<T> read(@NonNull StarboxFile file, @NonNull Class<T> type) {
    throw new UnsupportedOperationException("Read has not been implemented for '.txt' files");
  }

  @Override
  public @NonNull <T> HandledExpression<T> read(@NonNull Reader reader, @NonNull Class<T> type) {
    throw new UnsupportedOperationException("Read has not been implemented for '.txt' files");
  }

  @Override
  public HandledExpression<Boolean> write(@NonNull StarboxFile file, @NonNull Object object) {
    return this.write(file, object.toString(), false);
  }

  @Override
  public HandledExpression<Boolean> write(@NonNull Writer writer, @NonNull Object object) {
    return HandledExpression.using(
        () -> {
          writer.write(object.toString());
          return true;
        });
  }

  @Override
  @NonNull
  public <T> HandledExpression<T> read(@NonNull URL resource, @NonNull Class<T> type) {
    throw new UnsupportedOperationException("Read has not been implemented for '.txt' files");
  }
}
