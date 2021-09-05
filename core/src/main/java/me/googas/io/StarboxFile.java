package me.googas.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.io.context.FileContext;
import me.googas.starbox.expressions.HandledExpression;

/**
 * This is an "extension" of the {@link File} includes methods for reading and writing with the use
 * of {@link FileContext} it delegates every {@link File} methods. Check {@link #read(FileContext,
 * Class)} and {@link #write(FileContext, Object)} to understand further functionality of this
 * class.
 *
 * <p>It is immutable just like {@link File}
 */
public class StarboxFile {

  /** The directory where the program is running. */
  @NonNull public static final String DIR_PATH = System.getProperty("user.dir");
  /** The directory where the program is running as a {@link StarboxFile}. */
  @NonNull public static final StarboxFile DIR = new StarboxFile(StarboxFile.DIR_PATH);

  @NonNull
  @Getter
  @Delegate(excludes = IgnoredMethods.class)
  private final File file;

  private StarboxFile(@NonNull File file) {
    this.file = file;
  }

  /**
   * Create a new StarboxFile instance from the file path. Please note to use {@link File#separator}
   * to correctly find the path
   *
   * @param pathname the path string
   */
  public StarboxFile(@NonNull String pathname) {
    this(new File(pathname));
  }

  /**
   * Create a new StarboxFile instance copying from another instance.
   *
   * @param file the file to be copied.
   */
  public StarboxFile(@NonNull StarboxFile file) {
    this(file.getFile());
  }

  private StarboxFile(@NonNull File parent, @NonNull String child) {
    this(new File(parent, child));
  }

  /**
   * Create a new StarboxFile instance from the path of the parent and child as {@link
   * #StarboxFile(String)} for both.
   *
   * @param parent the path string of the parent
   * @param child the path string of the child
   */
  public StarboxFile(@NonNull String parent, @NonNull String child) {
    this(new File(parent, child));
  }

  /**
   * Create a new StarboxFile instance with an instance of a parent file and the path of the child.
   *
   * @param parent the instance of StarboxFile of the parent
   * @param child the path string of the child
   */
  public StarboxFile(@NonNull StarboxFile parent, @NonNull String child) {
    this(new File(parent.getFile(), child));
  }

  /**
   * Start this wrapper for an actual {@link File}.
   *
   * @param file the file to be wrapped
   * @return a new instance of this class
   */
  @NonNull
  public static StarboxFile of(@NonNull File file) {
    return new StarboxFile(file);
  }

  /**
   * Prepare a {@link FileWriter} this will check that the file exists else it will be created.
   *
   * @param append whether to append the strings appended to the writer or empty the file to write
   * @return the {@link FileWriter}
   * @throws IOException if the file cannot be created
   */
  @NonNull
  public FileWriter getPreparedWriter(boolean append) throws IOException {
    boolean exists = true;
    if (!this.exists()) {
      exists = false;
      this.getParentFile().mkdirs();
      if (this.file.createNewFile()) {
        exists = true;
      }
    }
    if (exists) {
      return new FileWriter(this.getFile(), append);
    }
    throw new IOException(this + " could not be created");
  }

  /**
   * Reads the file using the given {@link FileContext}.
   *
   * <p>// TODO example
   *
   * @param context the context to read the file with
   * @param clazz the clazz of the object that the {@link FileContext} must return upon read
   * @param <T> the type of the object to return
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns the
   *     read object and handles {@link java.io.IOException}
   */
  @NonNull
  public <T> HandledExpression<T> read(@NonNull FileContext<?> context, @NonNull Class<T> clazz) {
    return context.read(this, clazz);
  }

  /**
   * Reads the file using the given {@link FileContext} and if the read object is null it will copy
   * the {@link URL} resource and read it to give the object.
   *
   * @param context the context to read the file and the resource stream with
   * @param clazz the clazz of the object that the {@link FileContext} must return upon read
   * @param resource the resource to copy to the file and read if the file could not be read
   * @param <T> the type of the object to return
   * @return the object from the file or the resource if the file could not be read if neither of
   *     those could be read null
   */
  @NonNull
  public <T> T readOr(
      @NonNull FileContext<?> context, @NonNull Class<T> clazz, @NonNull URL resource) {
    return this.read(context, clazz)
        .provide()
        .orElseGet(
            () ->
                context
                    .read(resource, clazz)
                    .provide()
                    .orElseThrow(
                        () ->
                            new IllegalStateException(
                                "Could not provide a non-null object from resource")));
  }

  /**
   * Reads the file using the given {@link FileContext} and if the read object is null it will copy
   * the {@link URL} resource and read it to give the object. This will also write the resource into
   * the file
   *
   * @param context the context to read the file and the resource stream with
   * @param clazz the clazz of the object that the {@link FileContext} must return upon read
   * @param resource the resource to copy to the file and read if the file could not be read
   * @param <T> the type of the object to return
   * @return the object from the file or the resource if the file could not be read if neither of
   *     those could be read null
   */
  @NonNull
  public <T> T readOrWrite(
      @NonNull FileContext<?> context, @NonNull Class<T> clazz, @NonNull URL resource) {
    return this.read(context, clazz)
        .provide()
        .orElseGet(
            () -> {
              T t =
                  context
                      .read(resource, clazz)
                      .provide()
                      .orElseThrow(
                          () ->
                              new IllegalStateException(
                                  "Could not provide a non-null object from resource"));
              this.write(context, t).run();
              return t;
            });
  }

  /**
   * Reads the file using the given {@link FileContext} providing its default type.
   *
   * <p>// TODO example
   *
   * @param context the context to read the file with
   * @return an optional wrapping the object or null if it could not be read
   * @param <O> the type of object that the context reads
   */
  @NonNull
  public <O> HandledExpression<O> read(@NonNull FileContext<O> context) {
    return context.read(this);
  }

  /**
   * Reads the file using the given {@link FileContext} providing its default type. If the object is
   * null the resource will be read and copied to the file
   *
   * @param context the context to read the file with
   * @param resource the resource to copy the default object from
   * @return the object from the file or the copy from the resource
   * @param <O> the type of object that the context reads
   */
  @NonNull
  public <O> O readOr(@NonNull FileContext<O> context, @NonNull URL resource) {
    return this.read(context)
        .provide()
        .orElseGet(
            () ->
                context
                    .read(resource)
                    .provide()
                    .orElseThrow(
                        () ->
                            new IllegalStateException(
                                "Could not provide a non-null object from resource")));
  }

  /**
   * Writes the file using the given {@link FileContext}.
   *
   * <p>// TODO example
   *
   * @param context the context to write the file with
   * @param object the object to write in the object
   * @return a {@link HandledExpression} which on {@link HandledExpression#provide()} returns
   *     whether the object was written and handles {@link java.io.IOException}
   */
  @NonNull
  public HandledExpression<Boolean> write(@NonNull FileContext<?> context, @NonNull Object object) {
    return context.write(this, object);
  }

  /**
   * Copies the parameter {@link URL} to the file.
   *
   * @param resource the resource to copy into the file
   * @return whether it was copied successfully false otherwise
   */
  public HandledExpression<Boolean> copy(@NonNull URL resource) {
    return HandledExpression.using(
        () -> {
          if (!this.file.exists()) {
            this.getParentFile().mkdirs();
            this.file.createNewFile();
            Files.copy(
                resource.openStream(), this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
          }
          return true;
        });
  }

  private StarboxFile[] listFiles(File[] files) {
    if (files == null) return null;
    StarboxFile[] starboxFiles = new StarboxFile[files.length];
    for (int i = 0; i < files.length; i++) {
      starboxFiles[i] = new StarboxFile(files[i]);
    }
    return starboxFiles;
  }

  /**
   * List the files inside this if it is a directory.
   *
   * @return the files inside this directory or null if this is not a directory
   */
  public StarboxFile[] listFiles() {
    return this.listFiles(this.file.listFiles());
  }

  /**
   * List the files inside this if it is a directory.
   *
   * @param filter a filter to filter out files
   * @return the files inside this directory or null if this is not a directory
   */
  public StarboxFile[] listFiles(FileFilter filter) {
    return this.listFiles(this.file.listFiles(filter));
  }

  /**
   * List the files inside this if it is a directory.
   *
   * @param filter a name filter to filter out files
   * @return the files inside this directory or null if this is not a directory
   */
  public StarboxFile[] listFiles(FilenameFilter filter) {
    return this.listFiles(this.file.listFiles(filter));
  }

  /**
   * Copies the bytes of another file to this one.
   *
   * @param source the source file to copy the bytes from
   * @return a {@link HandledExpression} that returns whether the source has been copied and handles
   *     {@link IOException} for creating missing directories and closing streams
   */
  @NonNull
  public HandledExpression<Boolean> copy(@NonNull StarboxFile source) {
    AtomicReference<InputStream> atomicInput = new AtomicReference<>();
    AtomicReference<OutputStream> atomicOutput = new AtomicReference<>();
    return HandledExpression.using(
            () -> {
              if (source.exists()
                  && (!this.getParentFile().exists() && !this.getParentFile().mkdirs())
                  && (!this.exists() && !this.createNewFile())) return false;
              InputStream input = new FileInputStream(source.getFile());
              OutputStream output = new FileOutputStream(this.getFile());
              byte[] buffer = new byte[1024];
              int length;
              while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
              }
              atomicInput.set(input);
              atomicOutput.set(output);
              return true;
            })
        .next(
            () -> {
              InputStream input = atomicInput.get();
              if (input != null) input.close();
            })
        .next(
            () -> {
              OutputStream output = atomicOutput.get();
              if (output != null) output.close();
            });
  }

  /**
   * Copies a complete directory to this one and each file. Using {@link #copy(StarboxFile)} this
   * method is recursive and copies every directory inside the source directory.
   *
   * @param source the source directory to copy from
   * @return this same instance
   * @throws IOException if the directories could not be created or bytes of the files could not be
   *     copied
   */
  @NonNull
  public StarboxFile copyDirectory(@NonNull StarboxFile source) throws IOException {
    String[] list = source.list();
    if (source.isDirectory() && list != null) {
      if (!this.exists() && !this.mkdir()) throw new IOException(this + " could not be created");
      for (String string : list) {
        StarboxFile sourceFile = new StarboxFile(source, string);
        StarboxFile destinationFile = new StarboxFile(this, string);
        destinationFile.copyDirectory(sourceFile);
      }
    } else {
      this.copy(source).run();
    }
    return this;
  }

  /**
   * Deletes this file and if it is a directory it will delete everything inside of it.
   *
   * @return whether the file or directory has been deleted
   */
  public boolean deleteAll() {
    StarboxFile[] files = this.listFiles();
    boolean deleted = false;
    if (files != null) {
      for (StarboxFile file : files) {
        if (file.deleteAll()) {
          deleted = true;
        }
      }
    }
    if (this.exists()) {
      this.getFile().delete();
      deleted = true;
    }
    return deleted;
  }

  /**
   * Get a {@link BufferedReader} for the file this will check that the file exists else {@link
   * IllegalStateException} will be thrown.
   *
   * @return the {@link BufferedReader} for the file
   * @throws FileNotFoundException if the file does not exists, or it is a directory or for any
   *     other reason
   */
  @NonNull
  public BufferedReader getBufferedReader() throws FileNotFoundException {
    return new BufferedReader(new FileReader(this.file));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    StarboxFile that = (StarboxFile) o;
    return Objects.equals(this.file, that.file);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.file);
  }

  @Override
  public String toString() {
    return this.file.toString();
  }

  private interface IgnoredMethods {

    File[] listFiles();

    File[] listFiles(FileFilter filter);

    File[] listFiles(FilenameFilter filter);
  }
}
