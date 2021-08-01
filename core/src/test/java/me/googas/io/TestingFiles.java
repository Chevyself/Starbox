package me.googas.io;

import com.google.gson.GsonBuilder;
import java.io.File;
import java.net.URL;
import java.util.Objects;
import lombok.NonNull;
import me.googas.io.context.Json;
import me.googas.io.context.PropertiesContext;
import me.googas.io.context.Txt;

public class TestingFiles {

  public static final StarboxFile DIR =
      new StarboxFile(StarboxFile.DIR, "testing");
  public static final StarboxFile WRITE = new StarboxFile(TestingFiles.DIR, "write.txt");

  public static class Contexts {

    @NonNull public static Json JSON = new Json(new GsonBuilder().create());
    @NonNull public static PropertiesContext PROPERTIES = new PropertiesContext();
    @NonNull public static Txt TXT = new Txt();
  }

  public static class Resources {

    @NonNull private static final ClassLoader LOADER = TestingFiles.class.getClassLoader();
    @NonNull public static final URL MOCKS = Resources.getResource("mocks.json");

    @NonNull
    public static URL getResource(@NonNull String name) {
      return Objects.requireNonNull(
          Resources.LOADER.getResource(name), name + " was not found in test resources");
    }
  }
}
