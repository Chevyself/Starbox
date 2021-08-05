package me.googas.starbox.addons.dependencies;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import lombok.NonNull;
import me.googas.io.StarboxFile;
import me.googas.starbox.addons.Addon;
import me.googas.starbox.addons.AddonLoader;
import me.googas.starbox.addons.exceptions.AddonCouldNotBeLoadedException;

/**
 * Manages {@link DependencyAddon}. This manages stuff as whether they are initialized and
 * downloaded
 */
public interface DependencyManager extends AddonLoader {

  /**
   * Initialize the dependency. This adds all its classes to the current class path
   *
   * @param dependency the dependency to initialize
   * @return the initialized dependency
   * @throws AddonCouldNotBeLoadedException if the dependency cannot be initialized
   */
  @NonNull
  default DependencyAddon initialize(@NonNull DependencyAddon dependency)
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
   * dependency using the parent as {@link #getParent()} and the name of the file will be: {@link
   * DependencyInformation#getName()} + "-" + {@link DependencyInformation#getVersion()} + ".jar"
   *
   * @param information the information of the dependency
   * @return the file for the dependency
   */
  @NonNull
  default StarboxFile getDependencyFile(@NonNull DependencyInformation information) {
    return new StarboxFile(
        this.getParent(), information.getName() + "-" + information.getVersion() + ".jar");
  }

  /**
   * Get the dependencies which this manager is handling.
   *
   * @return the collection of dependencies
   */
  @NonNull
  Collection<DependencyAddon> getDependencies();

  /**
   * Get a logger to use in messages. If no logger is found this will use the System.out or
   * printStackTrace for exceptions
   *
   * @return the logger
   */
  Logger getLogger();

  /**
   * Get the parent to which files may be created.
   *
   * @return the file to where the parent must be created
   */
  @NonNull
  StarboxFile getParent();

  /**
   * This is a handler to use in {@link #load()}. This will handle exceptions thrown in {@link
   * #initialize(DependencyAddon)} and {@link DependencyAddon.DownloadTask#download()}
   *
   * @return the consumer for exceptions
   */
  @NonNull
  Consumer<AddonCouldNotBeLoadedException> getNotLoadedHandler();

  @Override
  @NonNull
  default Collection<Addon> load() {
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
  default Collection<Addon> unload() {
    return new ArrayList<>();
  }
}
