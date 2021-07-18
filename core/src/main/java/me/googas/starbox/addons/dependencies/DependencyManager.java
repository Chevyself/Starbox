package me.googas.starbox.addons.dependencies;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import lombok.NonNull;
import me.googas.io.StarboxFile;
import me.googas.starbox.addons.Addon;
import me.googas.starbox.addons.AddonLoader;
import me.googas.starbox.log.Logging;

public interface DependencyManager extends AddonLoader {

  default boolean initialize(@NonNull DependencyAddon dependency) {
    StarboxFile file = dependency.getFile();
    try {
      Logging.fine(this.getLogger(), "Initializing Dependency from %s", file.getName());
      Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      addURL.setAccessible(true);
      addURL.invoke(URLClassLoader.getSystemClassLoader(), file.toURI().toURL());
      Logging.fine(this.getLogger(), "Accessed %s successfully", file.getName());
      return true;
    } catch (MalformedURLException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      Logging.process(this.getLogger(), e);
    }
    return false;
  }

  @NonNull
  default StarboxFile getDependencyFile(@NonNull DependencyInformation information) {
    return new StarboxFile(
        this.getParent(), information.getName() + "-" + information.getVersion() + ".jar");
  }

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
   * Get the parent to which files may be created
   *
   * @return the file to where the parent must be created
   */
  @NonNull
  StarboxFile getParent();

  @Override
  @NonNull
  default Collection<Addon> load() {
    List<Addon> loaded = new ArrayList<>();
    for (DependencyAddon dependency : this.getDependencies()) {
      DependencyInformation information = dependency.getInformation();
      String name = information.getName();
      String version = information.getVersion();
      if (dependency.download(this.getLogger()) && this.initialize(dependency)) {
        loaded.add(dependency);
        Logging.info(this.getLogger(), "Dependency %s-%s has been loaded", name, version);
      } else {
        Logging.severe(this.getLogger(), "Dependency %s-%s could not be loaded", name, version);
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
