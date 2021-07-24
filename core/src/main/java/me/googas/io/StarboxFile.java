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
import java.util.Optional;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.io.context.FileContext;

/**
 * This is an "extension" of the {@link File} includes methods for reading and writing with the use
 * of {@link FileContext} it delegates every {@link File} methods. Check {@link #read(FileContext,
 * Class)} and {@link #write(FileContext, Object)} to understand further functionality of this
 * class.
 *
 * <p>It is immutable just like {@link File}
 */
public class StarboxFile {

  /** The directory where the program is running */
  @NonNull public static final String DIR_PATH = System.getProperty("user.dir");
  /** The directory where the program is running as a {@link StarboxFile} */
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

  public StarboxFile(@NonNull StarboxFile file) {
    this(file.getFile());
  }

  private StarboxFile(@NonNull File parent, @NonNull String child) {
    this(new File(parent, child));
  }

  /**
   * Create a new StarboxFile instance from the path of the parent and child as {@link
   * #StarboxFile(String)} for both
   *
   * @param parent the path string of the parent
   * @param child the path string of the child
   */
  public StarboxFile(@NonNull String parent, @NonNull String child) {
    this(new File(parent, child));
  }

  /**
   * Create a new StarboxFile instance with an instance of a parent file and the path of the child
   *
   * @param parent the instance of StarboxFile of the parent
   * @param child the path string of the child
   */
  public StarboxFile(@NonNull StarboxFile parent, @NonNull String child) {
    this(new File(parent.getFile(), child));
  }

  /**
   * Prepare a {@link FileWriter} this will check that the file exists else it will be created
   *
   * @param append whether to append the strings appended to the writer or empty the file to write
   * @return the {@link FileWriter}
   * @throws IllegalStateException in case that the file could not be created
   */
  @NonNull
  public FileWriter getPreparedWriter(boolean append) {
    boolean exists = true;
    if (!this.exists()) {
      exists = false;
      try {
        this.getParentFile().mkdirs();
        if (this.file.createNewFile()) {
          exists = true;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (exists) {
      try {
        return new FileWriter(this.getFile(), append);
      } catch (IOException e) {
        throw new IllegalStateException("Failed to open writer for " + this, e);
      }
    }
    throw new IllegalStateException("Could not create file for " + this);
  }

  /**
   * Reads the file using the given {@link FileContext}
   *
   * <p>// TODO example
   *
   * @param context the context to read the file with
   * @param clazz the clazz of the object that the {@link FileContext} must return upon read
   * @param <T> the type of the object to return
   * @return an optional wrapping the object or null if it could not be read
   */
  @NonNull
  public <T> Optional<T> read(@NonNull FileContext<?> context, @NonNull Class<T> clazz) {
    return context.read(this, clazz);
  }

  /**
   * Reads the file using the given {@link FileContext} and if the read object is null return the
   * default object
   *
   * @see #read(FileContext, Class)
   * @param context the context to read the file with
   * @param clazz the clazz of the object that the {@link FileContext} must return upon read
   * @param def the default object in case the read object is null
   * @param <T> the type of the object to return
   * @return the object from the file or the default object if null
   */
  @NonNull
  @Deprecated
  public <T> T readOr(@NonNull FileContext<?> context, @NonNull Class<T> clazz, T def) {
    return this.read(context, clazz).orElse(def);
  }

  /**
   * Reads the file using the given {@link FileContext} and if the read object is null it will copy
   * the {@link URL} resource and read it to give the object
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
        .orElseGet(
            () ->
                context
                    .read(resource, clazz)
                    .orElseThrow(
                        () ->
                            new IllegalStateException(
                                "Could not provide a non-null object from resource")));
  }

  /**
   * Read the file using the given {@link FileContext} and if the read object is null a default
   * object will be given by the {@link Supplier}
   *
   * @param context the context to read the file with
   * @param clazz the clazz of the object that the {@link FileContext} must return upon read
   * @param supplier the default object in case the read object is null
   * @param <T> the type of the object to return
   * @return the object from the file or the default supplied if null
   */
  @Deprecated
  public <T> T readOrGet(
      @NonNull FileContext<?> context, @NonNull Class<T> clazz, @NonNull Supplier<T> supplier) {
    return this.read(context, clazz).orElseGet(supplier);
  }

  /**
   * Reads the file using the given {@link FileContext} providing its default type
   *
   * <p>// TODO example
   *
   * @param context the context to read the file with
   * @return an optional wrapping the object or null if it could not be read
   */
  @NonNull
  public <O> Optional<O> read(@NonNull FileContext<O> context) {
    return context.read(this);
  }

  /**
   * Reads the file using the given {@link FileContext} providing its default type. If the object is
   * null the default parameter will be given
   *
   * @param context the context to read the file with
   * @param def the default object to provide if the read from the file is null
   * @return the object from the file or the default object if the object read from the file is null
   */
  @NonNull
  @Deprecated
  public <O> O readOr(@NonNull FileContext<O> context, @NonNull O def) {
    return context.read(this).orElse(def);
  }

  /**
   * Reads the file using the given {@link FileContext} providing its default type. If the object is
   * null the resource will be read and copied to the file
   *
   * @param context the context to read the file with
   * @param resource the resource to copy the default object from
   * @return the object from the file or the copy from the resource
   */
  @NonNull
  public <O> O readOr(@NonNull FileContext<O> context, @NonNull URL resource) {
    return this.read(context)
        .orElseGet(
            () ->
                context
                    .read(resource)
                    .orElseThrow(
                        () ->
                            new IllegalStateException(
                                "Could not provide a non-null object from resource")));
  }

  /**
   * Reads the file using the given {@link FileContext} providing its default type. If the object
   * will be returned using the {@link Supplier}
   *
   * @param context the context to read the file with
   * @param supplier the supplier to get the object from
   * @return the object from the file or the default object from the supplier
   */
  @Deprecated
  public <O> O readOrGet(@NonNull FileContext<O> context, @NonNull Supplier<O> supplier) {
    return context.read(this).orElseGet(supplier);
  }

  /**
   * Writes the file using the given {@link FileContext}
   *
   * <p>// TODO example
   *
   * @param context the context to write the file with
   * @param object the object to write in the object
   * @return true if the object was written successfully false otherwise
   */
  public boolean write(@NonNull FileContext<?> context, @NonNull Object object) {
    return context.write(this, object);
  }

  /**
   * Copies the parameter {@link URL} to the file
   *
   * @param resource the resource to copy into the file
   * @return whether it was copied successfully false otherwise
   */
  public boolean copy(@NonNull URL resource) {
    if (!this.file.exists()) {
      this.file.getParentFile().mkdirs();
      try {
        this.file.createNewFile();
      } catch (IOException e) {
        throw new IllegalArgumentException(this.file + " could not be created", e);
      }
    }
    try {
      Files.copy(resource.openStream(), this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private StarboxFile[] listFiles(File[] files) {
    if (files == null) return null;
    StarboxFile[] starboxFiles = new StarboxFile[files.length];
    for (int i = 0; i < files.length; i++) {
      starboxFiles[i] = new StarboxFile(files[i]);
    }
    return starboxFiles;
  }

  public StarboxFile[] listFiles() {
    return this.listFiles(this.file.listFiles());
  }

  public StarboxFile[] listFiles(FileFilter filter) {
    return this.listFiles(this.file.listFiles(filter));
  }

  public StarboxFile[] listFiles(FilenameFilter filter) {
    return this.listFiles(this.file.listFiles(filter));
  }

  /**
   * Get a {@link FileWriter} of the file
   *
   * @return the {@link FileWriter} for the file
   * @throws IOException in case the file does not exist
   */
  @NonNull
  public FileWriter getWriter() throws IOException {
    return new FileWriter(this.file);
  }

  /**
   * Get a {@link FileReader} of the file
   *
   * @return the {@link FileReader} for the file
   * @throws FileNotFoundException in case that the file does not exist
   */
  @NonNull
  public FileReader getReader() throws FileNotFoundException {
    return new FileReader(this.file);
  }

  /**
   * Get a {@link BufferedReader} for the file this will check that the file exists else {@link
   * IllegalStateException} will be thrown
   *
   * @return the {@link BufferedReader} for the file
   * @throws IllegalStateException in case the file does not exist
   */
  @NonNull
  public BufferedReader getBufferedReader() {
    if (this.file.exists()) {
      try {
        return new BufferedReader(new FileReader(this.file));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    throw new IllegalStateException(this + " does not exist");
  }

  /**
   * Deletes this file and if it is a directory it will delete everything inside of it
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
    if (this.exists() && this.delete()) {
      deleted = true;
    }
    return deleted;
  }

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
      this.copy(source);
    }
    return this;
  }

  @NonNull
  public StarboxFile copy(@NonNull StarboxFile source) throws IOException {
    try (InputStream input = new FileInputStream(source.getFile());
        OutputStream output = new FileOutputStream(this.getFile())) {
      byte[] buffer = new byte[1024];
      int length;
      while ((length = input.read(buffer)) > 0) {
        output.write(buffer, 0, length);
      }
    }
    return this;
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
