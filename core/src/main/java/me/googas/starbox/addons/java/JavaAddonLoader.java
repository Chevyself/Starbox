package me.googas.starbox.addons.java;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.io.StarboxFile;
import me.googas.starbox.addons.Addon;
import me.googas.starbox.addons.AddonInformation;
import me.googas.starbox.addons.AddonLoader;

public class JavaAddonLoader implements AddonLoader {

  @NonNull @Getter private final StarboxFile directory;
  @NonNull @Getter private final ClassLoader parentLoader;
  @NonNull @Getter private final List<Addon> loaded;
  @NonNull @Getter @Setter private LoggerSupplier loggerSupplier;

  /**
   * Create the addon loader
   *
   * @param directory the directory in which it will work
   * @param loggerSupplier supplies the addons with a logger
   * @throws IllegalArgumentException if the directory file is not an actual directory
   * @throws IOException if the directory could not be created
   */
  public JavaAddonLoader(@NonNull StarboxFile directory, @NonNull LoggerSupplier loggerSupplier)
      throws IOException {
    if (!directory.exists() && !directory.mkdir())
      throw new IOException(directory.getName() + " could not be created!");
    if (!directory.isDirectory())
      throw new IllegalArgumentException(directory + " is not a directory!");
    this.directory = directory;
    this.parentLoader = this.getClass().getClassLoader();
    this.loaded = new ArrayList<>();
    this.loggerSupplier = loggerSupplier;
  }

  /**
   * Initialize the addon that is supposed too be inside of the parameter file
   *
   * @param file the file which must be a jar containing the addon
   * @return the addon if it was initialized null otherwise
   */
  private JavaAddon initializeAddon(@NonNull StarboxFile file) {
    try {
      JavaAddonClassLoader addonLoader = new JavaAddonClassLoader(file, this.parentLoader);
      JavaAddonInformation info = addonLoader.getAddonInfo();
      Class<?> clazz = Class.forName(info.getMain(), true, addonLoader);
      Object instance = clazz.getDeclaredConstructor().newInstance();
      if (instance instanceof JavaAddon)
        return ((JavaAddon) instance)
            .init(
                this,
                new StarboxFile(this.directory, info.getName()),
                info,
                addonLoader,
                this.loggerSupplier.create(info));
    } catch (IllegalAccessException
        | InstantiationException
        | InvocationTargetException
        | ClassNotFoundException
        | NoSuchMethodException
        | IOException e) {
      e.printStackTrace();
    }
    return null;
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
      JavaAddon addon = this.initializeAddon(file);
      if (addon != null) {
        try {
          addon.onEnable();
          loaded.add(addon);
        } catch (Throwable e) {
          e.printStackTrace();
        }
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
}
