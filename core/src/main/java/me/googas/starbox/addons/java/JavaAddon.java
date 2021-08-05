package me.googas.starbox.addons.java;

import java.util.Objects;
import java.util.logging.Logger;
import lombok.NonNull;
import lombok.Setter;
import me.googas.io.StarboxFile;
import me.googas.starbox.addons.Addon;

/** This is the class that every addon made in Java can extend. */
public class JavaAddon implements Addon {

  @NonNull private static final String NOT_INIT = "Addon has not been initialized yet!";

  @Setter private JavaAddonLoader addonLoader = null;
  @Setter private StarboxFile dataFile = null;
  @Setter private JavaAddonInformation information = null;
  @Setter private ClassLoader classLoader = null;
  @Setter private Logger logger = null;

  /**
   * Starts the addon. Keeping a no-args constructor to be called in {@link JavaAddonLoader} this
   * method has been created to start all its classes
   *
   * @param addonLoader the addon loader that initialized this addon
   * @param dataFile the directory which this addon may use to store files
   * @param information the information about this addon
   * @param classLoader the class loader of this addon
   * @param logger the logger which this addon may use to print messages
   * @return this addon initialized
   */
  @NonNull
  public JavaAddon init(
      @NonNull JavaAddonLoader addonLoader,
      @NonNull StarboxFile dataFile,
      @NonNull JavaAddonInformation information,
      @NonNull ClassLoader classLoader,
      @NonNull Logger logger) {
    this.addonLoader = addonLoader;
    this.dataFile = dataFile;
    this.information = information;
    this.classLoader = classLoader;
    this.logger = logger;
    return this;
  }

  /**
   * Get the addon loader.
   *
   * @return the addon loader
   */
  @NonNull
  public JavaAddonLoader getAddonLoader() {
    return Objects.requireNonNull(this.addonLoader, JavaAddon.NOT_INIT);
  }

  /**
   * Get the directory for this addon.
   *
   * @return the directory
   */
  @NonNull
  public StarboxFile getDataFile() {
    return Objects.requireNonNull(this.dataFile, JavaAddon.NOT_INIT);
  }

  /**
   * Get the class loader of this addon.
   *
   * @return the class loader
   */
  @NonNull
  public ClassLoader getClassLoader() {
    return Objects.requireNonNull(this.classLoader, JavaAddon.NOT_INIT);
  }

  /**
   * Get the logger of this addon.
   *
   * @return the logger
   */
  @NonNull
  public Logger getLogger() {
    return Objects.requireNonNull(this.logger, JavaAddon.NOT_INIT);
  }

  @Override
  @NonNull
  public JavaAddonInformation getInformation() {
    return Objects.requireNonNull(this.information, JavaAddon.NOT_INIT);
  }

  public void onEnable() throws Throwable {}

  public void onDisable() throws Throwable {}
}
