package me.googas.starbox.addons.dependencies;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import me.googas.io.StarboxFile;
import me.googas.starbox.addons.Addon;
import me.googas.starbox.addons.AddonLoader;
import me.googas.starbox.addons.exceptions.AddonCouldNotBeLoadedException;
import me.googas.starbox.builders.Builder;

/**
 * Manages {@link DependencyAddon}. This manages stuff as whether they are initialized and
 * downloaded
 */
public class DependencyManager implements AddonLoader {

  @NonNull @Getter private final List<DependencyAddon> dependencies;
  @NonNull @Getter private final StarboxFile parent;
  @NonNull @Getter private final Consumer<AddonCouldNotBeLoadedException> notLoadedHandler;

  /**
   * Create the manager.
   *
   * @param dependencies the list of dependencies to handle
   * @param parent the file where the dependencies will be listed
   * @param notLoadedHandler the handler for dependencies which could not be loaded
   */
  protected DependencyManager(
      @NonNull List<DependencyAddon> dependencies,
      @NonNull StarboxFile parent,
      @NonNull Consumer<AddonCouldNotBeLoadedException> notLoadedHandler) {
    this.dependencies = dependencies;
    this.parent = parent;
    this.notLoadedHandler = notLoadedHandler;
  }

  /**
   * Create a builder instance.
   *
   * @param parent the directory where all the dependencies will be listed
   * @return the new builder instance
   */
  @NonNull
  public static DependencyManagerBuilder using(@NonNull StarboxFile parent) {
    return new DependencyManagerBuilder(parent);
  }

  /**
   * Initialize the dependency. This adds all its classes to the current class path
   *
   * @param dependency the dependency to initialize
   * @return the initialized dependency
   * @throws AddonCouldNotBeLoadedException if the dependency cannot be initialized
   */
  @NonNull
  private DependencyAddon initialize(@NonNull DependencyAddon dependency)
      throws AddonCouldNotBeLoadedException {
    StarboxFile file = dependency.getFile();
    try {
      Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      addURL.setAccessible(true);
      addURL.invoke(URLClassLoader.getSystemClassLoader(), file.toURI().toURL());
      return dependency;
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new AddonCouldNotBeLoadedException(
          "Could not load dependency addon into current classpath");
    } catch (MalformedURLException e) {
      throw new AddonCouldNotBeLoadedException(
          "There's been an error while trying to get the url for the file " + file, e);
    }
  }

  /**
   * Get a file to use for a {@link DependencyAddon}. This will create the jar file of the
   * dependency using the parent as {@link DependencyManager#parent} and the name of the file will
   * be: {@link DependencyInformation#getName()} + "-" + {@link DependencyInformation#getVersion()}
   * + ".jar"
   *
   * @param information the information of the dependency
   * @return the file for the dependency
   */
  @NonNull
  public StarboxFile getDependencyFile(@NonNull DependencyInformation information) {
    return new StarboxFile(
        this.getParent(), information.getName() + "-" + information.getVersion() + ".jar");
  }

  @Override
  @NonNull
  public List<Addon> load() {
    List<Addon> loaded = new ArrayList<>();
    for (DependencyAddon dependency : this.getDependencies()) {
      boolean downloaded = dependency.download().provide().orElse(false);
      if (downloaded) {
        try {
          this.initialize(dependency);
        } catch (AddonCouldNotBeLoadedException e) {
          this.getNotLoadedHandler().accept(e);
        }
      } else {
        this.getNotLoadedHandler()
            .accept(new AddonCouldNotBeLoadedException(dependency + " could not be downloaded"));
      }
    }
    this.getLoaded().addAll(loaded);
    return loaded;
  }

  @Override
  @NonNull
  public List<Addon> unload() {
    return new ArrayList<>();
  }

  @Override
  public @NonNull List<Addon> getLoaded() {
    return new ArrayList<>(this.dependencies);
  }

  /** Helps to build the manager in a neat way. */
  public static class DependencyManagerBuilder implements Builder<DependencyManager> {

    @NonNull @Getter private final StarboxFile parent;

    @NonNull @Getter
    private final List<DependencyAddon.DependencyAddonBuilder> dependencies = new ArrayList<>();

    @NonNull @Getter
    private Consumer<AddonCouldNotBeLoadedException> handler = Throwable::printStackTrace;

    /**
     * Create the builder.
     *
     * @param parent The directory where all the dependencies will be listed
     */
    private DependencyManagerBuilder(@NonNull StarboxFile parent) {
      this.parent = parent;
    }

    /**
     * Handles the exception when addons cannot be loaded.
     *
     * @param handler the handler for the exceptions
     * @return this same instance
     */
    @NonNull
    public DependencyManagerBuilder handle(
        @NonNull Consumer<AddonCouldNotBeLoadedException> handler) {
      this.handler = handler;
      return this;
    }

    /**
     * Add a dependency to the builder.
     *
     * @param addonBuilder the dependency builder to add.
     * @return this same instance
     */
    @NonNull
    public DependencyManagerBuilder add(
        @NonNull DependencyAddon.DependencyAddonBuilder addonBuilder) {
      this.dependencies.add(addonBuilder);
      return this;
    }

    @Override
    public @NonNull DependencyManager build() {
      DependencyManager manager = new DependencyManager(new ArrayList<>(), parent, handler);
      this.dependencies.forEach(builder -> manager.getDependencies().add(builder.build(manager)));
      return manager;
    }
  }
}
