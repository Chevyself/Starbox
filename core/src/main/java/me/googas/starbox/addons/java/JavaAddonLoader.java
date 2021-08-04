package me.googas.starbox.addons.java;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NonNull;
import me.googas.io.StarboxFile;
import me.googas.starbox.addons.Addon;
import me.googas.starbox.addons.AddonInformation;
import me.googas.starbox.addons.AddonLoader;
import me.googas.starbox.addons.exceptions.AddonCouldNotBeLoadedException;
import me.googas.starbox.builders.Builder;

public class JavaAddonLoader implements AddonLoader {

  @NonNull @Getter private final StarboxFile directory;
  @NonNull @Getter private final ClassLoader parentLoader;
  @NonNull @Getter private final List<Addon> loaded;
  @NonNull private final LoggerSupplier loggerSupplier;
  @NonNull private final Consumer<Throwable> throwableHandler;
  @NonNull private final Consumer<AddonCouldNotBeLoadedException> notLoadedConsumer;

  private JavaAddonLoader(
      @NonNull StarboxFile directory,
      @NonNull ClassLoader parentLoader,
      @NonNull List<Addon> loaded,
      @NonNull LoggerSupplier loggerSupplier,
      @NonNull Consumer<Throwable> throwableHandler,
      @NonNull Consumer<AddonCouldNotBeLoadedException> notLoadedConsumer) {
    this.directory = directory;
    this.parentLoader = parentLoader;
    this.loaded = loaded;
    this.loggerSupplier = loggerSupplier;
    this.throwableHandler = throwableHandler;
    this.notLoadedConsumer = notLoadedConsumer;
  }

  /**
   * Create the addon loader
   *
   * @param directory the directory in which it will work
   * @param loggerSupplier supplies the addons with a logger
   * @throws IllegalArgumentException if the directory file is not an actual directory
   * @throws IOException if the directory could not be created
   */
  @Deprecated
  public JavaAddonLoader(@NonNull StarboxFile directory, @NonNull LoggerSupplier loggerSupplier)
      throws IOException {
    this(
        directory,
        JavaAddonLoader.class.getClassLoader(),
        new ArrayList<>(),
        loggerSupplier,
        Throwable::printStackTrace,
        Throwable::printStackTrace);
    if (!directory.exists() && !directory.mkdir())
      throw new IOException(directory.getName() + " could not be created!");
    if (!directory.isDirectory())
      throw new IllegalArgumentException(directory + " is not a directory!");
  }

  /**
   * Initialize the addon that is supposed too be inside of the parameter file
   *
   * @param file the file which must be a jar containing the addon
   * @return the addon if it was initialized null otherwise
   */
  private JavaAddon initializeAddon(@NonNull StarboxFile file)
      throws AddonCouldNotBeLoadedException {
    try {
      JavaAddonClassLoader loader = new JavaAddonClassLoader(file, this.parentLoader);
      JavaAddonInformation info = loader.getAddonInformation();
      Class<?> clazz = Class.forName(info.getMain(), true, loader);
      Object instance = clazz.getDeclaredConstructor().newInstance();
      if (instance instanceof JavaAddon) {
        return ((JavaAddon) instance)
            .init(
                this,
                new StarboxFile(this.directory, info.getName()),
                info,
                loader,
                loggerSupplier.create(info));
      } else {
        throw new AddonCouldNotBeLoadedException(
            "Main class in addon " + file + " does not extend " + JavaAddon.class);
      }
    } catch (MalformedURLException e) {
      throw new AddonCouldNotBeLoadedException(
          "There's been an error while trying to get the url for the file " + file, e);
    } catch (IOException e) {
      throw new AddonCouldNotBeLoadedException(
          "Addon is missing 'addon.properties' or its information could not be parsed");
    } catch (ClassNotFoundException e) {
      throw new AddonCouldNotBeLoadedException("Could not find main class for addon", e);
    } catch (NoSuchMethodException e) {
      throw new AddonCouldNotBeLoadedException(
          "Main addon class does not have no params constructor");
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
      throw new AddonCouldNotBeLoadedException("Could not invoke no params constructor");
    }
  }

  /**
   * Get the jar files inside {@link #directory}
   *
   * @return the jar files inside a list
   */
  @NonNull
  private List<StarboxFile> getJars() {
    List<StarboxFile> jars = new ArrayList<>();
    StarboxFile[] files = this.directory.listFiles();
    if (files != null) {
      for (StarboxFile file : files) {
        if (file.getName().endsWith(".jar")) jars.add(file);
      }
    }
    return jars;
  }

  @NonNull
  public Collection<Addon> load() {
    List<Addon> loaded = new ArrayList<>();
    for (StarboxFile file : this.getJars()) {
      try {
        JavaAddon addon = this.initializeAddon(file);
        if (addon != null) {
          addon.onEnable();
          loaded.add(addon);
        }
      } catch (AddonCouldNotBeLoadedException e) {
        notLoadedConsumer.accept(e);
      } catch (Throwable e) {
        throwableHandler.accept(e);
      }
    }
    return loaded;
  }

  @NonNull
  public List<Addon> unload() {
    List<Addon> unloaded = new ArrayList<>();
    for (Addon addon : this.loaded) {
      unloaded.add(this.unload(addon));
    }
    this.loaded.clear();
    return unloaded;
  }

  public interface LoggerSupplier {
    @NonNull
    Logger create(@NonNull AddonInformation info);
  }

  public static class LoaderBuilder implements Builder<JavaAddonLoader> {

    @NonNull @Getter private final StarboxFile directory;
    @NonNull private LoggerSupplier loggerSupplier;
    @NonNull private Consumer<Throwable> throwableHandler;
    @NonNull private Consumer<AddonCouldNotBeLoadedException> notLoadedConsumer;

    public LoaderBuilder(@NonNull StarboxFile directory) {
      this.directory = directory;
      this.loggerSupplier = info -> Logger.getLogger(info.getName());
      this.throwableHandler = Throwable::printStackTrace;
      this.notLoadedConsumer = Throwable::printStackTrace;
    }

    @NonNull
    public LoaderBuilder supply(@NonNull LoggerSupplier loggerSupplier) {
      this.loggerSupplier = loggerSupplier;
      return this;
    }

    @NonNull
    public LoaderBuilder handle(@NonNull Consumer<Throwable> throwableHandler) {
      this.throwableHandler = throwableHandler;
      return this;
    }

    @NonNull
    public LoaderBuilder handleNotLoaded(
        @NonNull Consumer<AddonCouldNotBeLoadedException> notLoadedConsumer) {
      this.notLoadedConsumer = notLoadedConsumer;
      return this;
    }

    @Override
    public @NonNull JavaAddonLoader build() {
      return null;
    }
  }
}
