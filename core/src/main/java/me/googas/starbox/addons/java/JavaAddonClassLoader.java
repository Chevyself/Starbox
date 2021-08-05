package me.googas.starbox.addons.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import lombok.NonNull;
import me.googas.io.StarboxFile;

/** The class loader for addons. */
public class JavaAddonClassLoader extends URLClassLoader {

  @NonNull private final StarboxFile addonFile;
  /**
   * Create the class loader for addons.
   *
   * @param addonFile the jar file of the addon
   * @param parent the parent class loader for the addon
   * @throws MalformedURLException in case the jar file is invalid
   */
  public JavaAddonClassLoader(@NonNull StarboxFile addonFile, @NonNull ClassLoader parent)
      throws MalformedURLException {
    super(new URL[] {addonFile.toURI().toURL()}, parent);
    this.addonFile = addonFile;
  }

  /**
   * Get the addon information.
   *
   * @return the addon information
   * @throws IOException if the reader could not be closed or {@link FileNotFoundException} if the
   *     addon does not include the 'addon.json'
   */
  @NonNull
  public JavaAddonInformation getAddonInformation() throws IOException {
    InputStream resource = this.getResourceAsStream("addon.properties");
    if (resource != null) {
      return JavaAddonInformation.parse(resource);
    } else {
      throw new FileNotFoundException(
          "The resource 'addon.json' does not exist in " + this.addonFile.getName());
    }
  }
}
