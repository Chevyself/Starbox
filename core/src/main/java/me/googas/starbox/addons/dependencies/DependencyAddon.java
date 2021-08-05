package me.googas.starbox.addons.dependencies;

import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import me.googas.io.StarboxFile;
import me.googas.starbox.addons.Addon;
import me.googas.starbox.builders.Builder;
import me.googas.starbox.builders.SuppliedBuilder;
import me.googas.starbox.expressions.HandledExpression;

/**
 * This represents an addon which contains a dependency for the project. Lets take Starbox as an
 * example: there's some json utilities and adapters which require Gson to work, instead of shading
 * the jar with Gson's jar a {@link DependencyManager} may be created and a {@link DependencyAddon}
 * containing Gson's download may be included and in the runtime it will be added in class path
 */
public class DependencyAddon implements Addon {

  @NonNull @Getter private final URL url;
  @NonNull @Getter private final StarboxFile file;
  @NonNull @Getter private final DependencyInformation information;
  private final transient DownloadTask task;

  /**
   * Create the addon.
   *
   * @param url the url to download the addon
   * @param file the file which will contain the addon
   * @param information the information of the dependency
   */
  public DependencyAddon(
      @NonNull URL url, @NonNull StarboxFile file, @NonNull DependencyInformation information) {
    this.url = url;
    this.file = file;
    this.information = information;
    this.task = new DownloadTask(this.file, this.url);
  }

  /**
   * Create an addon builder.
   *
   * @param url the url where the addon can be downloaded
   * @return a new instance of {@link DependencyAddonBuilder}
   */
  @NonNull
  public static DependencyAddonBuilder of(@NonNull URL url) {
    return new DependencyAddonBuilder(url);
  }

  /**
   * Create an addon builder.
   *
   * @param url the url where the addon can be downloaded as an string
   * @return a new instance of {@link DependencyAddonBuilder}
   * @throws MalformedURLException if the string is a malformed url
   */
  @NonNull
  public static DependencyAddonBuilder of(@NonNull String url) throws MalformedURLException {
    return new DependencyAddonBuilder(new URL(url));
  }

  /**
   * Download the addon. A {@link DownloadTask} will be created and the download will be done
   *
   * @return the {@link HandledExpression} of {@link DownloadTask#download()}
   */
  @NonNull
  public HandledExpression<Boolean> download() {
    return this.task.download();
  }

  /** Represents the task which downloads the addon. */
  public static class DownloadTask {
    @NonNull private final StarboxFile file;
    @NonNull private final URL url;
    // From the task
    private ReadableByteChannel channel;
    private FileOutputStream stream;
    @Getter private boolean completed = false;

    private DownloadTask(@NonNull StarboxFile file, @NonNull URL url) {
      this.file = file;
      this.url = url;
    }

    /**
     * Download the addon. Sets the downloaded bytes in {@link DependencyAddon#getFile()}
     *
     * @return a {@link HandledExpression} which returns whether the addon has been downloaded and
     *     handles {@link java.io.IOException} in case that streams cannot be closed and the file
     *     cannot be created or the bytes cannot be set
     */
    @NonNull
    public HandledExpression<Boolean> download() {
      return HandledExpression.using(
              () -> {
                if (!this.completed) {
                  if (!this.file.exists()) {
                    this.file.getParentFile().mkdirs();
                    this.file.createNewFile();
                  }
                  this.channel = Channels.newChannel(this.url.openStream());
                  this.stream = new FileOutputStream(this.file.getFile());
                  this.stream.getChannel().transferFrom(this.channel, 0, Long.MAX_VALUE);
                  this.completed = true;
                }
                return true;
              })
          .next(
              () -> {
                if (this.channel != null) this.channel.close();
              })
          .next(
              () -> {
                if (this.stream != null) this.stream.close();
              });
    }
  }

  /** Builder for a {@link DependencyAddon}. */
  public static class DependencyAddonBuilder
      implements SuppliedBuilder<StarboxFile, DependencyAddon>, Builder<DependencyInformation> {

    @NonNull private final URL url;
    @NonNull private String name = "No name";
    @NonNull private String version = "No version";
    @NonNull private String description = "No description";

    private DependencyAddonBuilder(@NonNull URL url) {
      this.url = url;
    }

    @NonNull
    public DependencyInformation build() {
      return new DependencyInformation(this.name, this.version, this.description);
    }

    /**
     * Build the addon for a manager. This will use {@link #build(StarboxFile)} using {@link
     * DependencyManager#getDependencyFile(DependencyInformation)}
     *
     * @param manager the manager to build the addon to
     * @return the built dependency addon
     */
    @NonNull
    public DependencyAddon build(@NonNull DependencyManager manager) {
      return this.build(manager.getDependencyFile(this.build()));
    }

    /**
     * Set the name of the addon.
     *
     * @param name the name of the addon
     * @return this same instance
     */
    @NonNull
    public DependencyAddonBuilder setName(String name) {
      this.name = name;
      return this;
    }

    /**
     * Set the version of the addon.
     *
     * @param version the version of the addon
     * @return this same instance
     */
    @NonNull
    public DependencyAddonBuilder setVersion(String version) {
      this.version = version;
      return this;
    }

    /**
     * Set the description of the addon.
     *
     * @param description the description of the addon
     * @return this same instance
     */
    @NonNull
    public DependencyAddonBuilder setDescription(String description) {
      this.description = description;
      return this;
    }

    @NonNull
    @Override
    public DependencyAddon build(@NonNull StarboxFile file) {
      return new DependencyAddon(this.url, file, this.build());
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DependencyAddon.class.getSimpleName() + "[", "]")
        .add("url=" + url)
        .add("file=" + file)
        .add("information=" + information)
        .add("task=" + task)
        .toString();
  }
}
