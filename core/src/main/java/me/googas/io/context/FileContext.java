package me.googas.io.context;

import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import lombok.NonNull;
import me.googas.io.StarboxFile;
import me.googas.starbox.HandledExpression;

/**
 * Implementations of this class are able to read Java objects from files:
 *
 * <ul>
 *   <li>{@link Json} will read Json objects using {@link com.google.gson.Gson} from '.json' files
 *   <li>{@link PropertiesContext} will read Properties objects from '.properties' files
 *   <li>{@link Txt} will read strings from '.txt' files or any type really
 * </ul>
 *
 * @param <O> the type of object that the context returns if no type is given
 */
public interface FileContext<O> {

  /**
   * Read the object from the given file
   *
   * @param file the file to read the object from
   * @param type the type to return when the file is read
   * @param <T> the type of object to return
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns the
   *     read object and handles {@link java.io.IOException}
   */
  @NonNull
  <T> HandledExpression<T> read(@NonNull StarboxFile file, @NonNull Class<T> type);

  /**
   * Read the object from the given reader
   *
   * @param reader the reader to read the object from
   * @param type the type to return when the reader has been finished reading
   * @param <T> the type of object to return
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns the
   *     read object and handles {@link java.io.IOException}
   */
  @NonNull
  <T> HandledExpression<T> read(@NonNull Reader reader, @NonNull Class<T> type);

  /**
   * Write the object to the given file
   *
   * @param file the file to write the object to
   * @param object the object to write the file to
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns
   *     whether the object was written and handles {@link java.io.IOException}
   */
  @NonNull
  HandledExpression<Boolean> write(@NonNull StarboxFile file, @NonNull Object object);

  /**
   * Write the object using the given writer
   *
   * @param writer the write to write the object to
   * @param object the object to be written
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns
   *     whether the object was written and handles {@link java.io.IOException}
   */
  @NonNull
  HandledExpression<Boolean> write(@NonNull Writer writer, @NonNull Object object);

  /**
   * Read the object from the given input stream
   *
   * @param resource the resource to read the object from
   * @param type the type to return when the file is ready
   * @param <T> the type of the object to return
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns the
   *     read object and handles {@link java.io.IOException}
   */
  @NonNull
  <T> HandledExpression<T> read(@NonNull URL resource, @NonNull Class<T> type);

  /**
   * Read the default object given by the context from a {@link java.io.File}
   *
   * @param file the file to read the object from
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns the
   *     read object and handles {@link java.io.IOException}
   */
  @NonNull
  HandledExpression<O> read(@NonNull StarboxFile file);

  /**
   * Read the default object given by the context from a {@link URL}
   *
   * @param resource the resource to read the object from
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns the
   *     read object and handles {@link java.io.IOException}
   */
  @NonNull
  HandledExpression<O> read(@NonNull URL resource);
}
