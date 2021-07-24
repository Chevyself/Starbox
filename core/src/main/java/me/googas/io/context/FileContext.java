package me.googas.io.context;

import java.net.URL;
import java.util.Optional;
import lombok.NonNull;
import me.googas.io.StarboxFile;

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
   * @return a {@link Optional} instance wrapping the read object or null if it could not be read
   *     correctly
   */
  @NonNull
  <T> Optional<T> read(@NonNull StarboxFile file, @NonNull Class<T> type);

  /**
   * Write the object to the given file
   *
   * @param file the file to write the object to
   * @param object the object to write the file to
   * @return true if the object was written correctly false otherwise
   */
  boolean write(@NonNull StarboxFile file, @NonNull Object object);

  /**
   * Read the object from the given input stream
   *
   * @param resource the resource to read the object from
   * @param type the type to return when the file is ready
   * @param <T> the type of the object to return
   * @return a {@link Optional} instance wrapping the read object or null if it could not be read
   *     correctly
   */
  @NonNull
  <T> Optional<T> read(@NonNull URL resource, @NonNull Class<T> type);

  /**
   * Read the default object given by the context from a {@link java.io.File}
   *
   * @param file the file to read the object from
   * @return a {@link Optional} instance wrapping the read object or null if it could not be read
   *     correctly
   */
  @NonNull
  Optional<O> read(@NonNull StarboxFile file);

  /**
   * Read the default object given by the context from a {@link URL}
   *
   * @param resource the resource to read the object from
   * @return a {@link Optional} instance wrapping the read object or null if it could not be read
   *     correctly
   */
  @NonNull
  Optional<O> read(@NonNull URL resource);
}
