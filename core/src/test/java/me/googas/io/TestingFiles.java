package me.googas.io;

import com.google.gson.GsonBuilder;
import java.net.URL;
import java.util.Objects;
import lombok.NonNull;
import me.googas.io.context.Json;
import me.googas.io.context.PropertiesContext;
import me.googas.io.context.Txt;

/** Static access for files used for testing. */
public class TestingFiles {

  public static final StarboxFile DIR = new StarboxFile(StarboxFile.DIR, "testing");
  public static final StarboxFile WRITE = new StarboxFile(TestingFiles.DIR, "write.txt");

  /** Static access for contexts used for testing. */
  public static class Contexts {

    @NonNull public static final Json JSON = new Json(new GsonBuilder().create());
    @NonNull public static PropertiesContext PROPERTIES = new PropertiesContext();
    @NonNull public static final Txt TXT = new Txt();
  }

  /** Static access for resources used for testing. */
  public static class Resources {

    @NonNull private static final ClassLoader LOADER = TestingFiles.class.getClassLoader();
    @NonNull public static final URL MOCKS = Resources.getResource("mocks.json");

    /**
     * Get a resource by its name
     *
     * @param name the name of the resource to match
     * @return the matched url
     */
    @NonNull
    public static URL getResource(@NonNull String name) {
      return Objects.requireNonNull(
          Resources.LOADER.getResource(name), name + " was not found in test resources");
    }
  }
}
