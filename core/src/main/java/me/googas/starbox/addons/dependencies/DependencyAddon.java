package me.googas.starbox.addons.dependencies;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NonNull;
import me.googas.io.StarboxFile;
import me.googas.starbox.addons.Addon;
import me.googas.starbox.log.Logging;

public class DependencyAddon implements Addon {

  @NonNull @Getter private final StarboxFile file;
  @NonNull @Getter private final DependencyInformation information;
  @Getter private URL url;
  private boolean downloaded;

  public DependencyAddon(
      @NonNull String url, @NonNull StarboxFile file, @NonNull DependencyInformation information) {
    this.file = file;
    this.information = information;
    this.downloaded = file.exists();
    try {
      this.url = new URL(url);
    } catch (MalformedURLException e) {
      this.url = null;
      e.printStackTrace();
    }
  }

  public static DependencyAddonBuilder builder() {
    return new DependencyAddonBuilder();
  }

  public static DependencyAddon create(
      @NonNull String url,
      @NonNull String name,
      @NonNull String version,
      @NonNull String description,
      @NonNull DependencyManager manager) {
    return DependencyAddon.builder()
        .setUrl(url)
        .setName(name)
        .setVersion(version)
        .setDescription(description)
        .build(manager);
  }

  public boolean download(Logger logger) {
    if (this.downloaded) return true;
    try {
      if (new DownloadTask(logger, this.file, this.url).download()) {
        this.downloaded = true;
        return true;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public String toString() {
    return "DependencyAddon{"
        + "url="
        + this.url
        + ", file="
        + this.file
        + ", information="
        + this.information
        + ", downloaded="
        + this.downloaded
        + '}';
  }

  public static class DownloadTask {
    private final Logger logger;
    @NonNull private final StarboxFile file;
    @NonNull private final URL url;
    // From the task
    private ReadableByteChannel channel;
    private FileOutputStream stream;
    @Getter private boolean completed = false;

    public DownloadTask(Logger logger, @NonNull StarboxFile file, @NonNull URL url)
        throws IOException {
      this.logger = logger;
      this.file = file; // TODO check that it exists or create
      this.url = url;
    }

    public boolean download() {
      if (this.completed) return true;
      Logging.info(this.logger, "Starting download for %s", this.file.getName());
      try {
        this.channel = Channels.newChannel(this.url.openStream());
        this.stream = new FileOutputStream(this.file.getFile());
        this.stream.getChannel().transferFrom(this.channel, 0, Long.MAX_VALUE);
      } catch (IOException e) {
        e.printStackTrace();
      }
      Logging.info(this.logger, "%s has been downloaded", this.file.getName());
      this.completed = true;
      if (this.channel != null) {
        try {
          this.channel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (this.stream != null) {
        try {
          this.stream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return true;
    }
  }

  public static class DependencyAddonBuilder {

    @NonNull private String url = "none";
    @NonNull private String name = "No name";
    @NonNull private String version = "No version";
    @NonNull private String description = "No description";

    @NonNull
    public DependencyInformation buildInfo() {
      return new DependencyInformation(this.name, this.version, this.description);
    }

    @NonNull
    public DependencyAddon build(@NonNull StarboxFile file) {
      return new DependencyAddon(this.url, file, this.buildInfo());
    }

    @NonNull
    public DependencyAddon build(@NonNull DependencyManager manager) {
      DependencyInformation info = this.buildInfo();
      return new DependencyAddon(this.url, manager.getDependencyFile(info), info);
    }

    @NonNull
    public DependencyAddonBuilder setUrl(String url) {
      this.url = url;
      return this;
    }

    @NonNull
    public DependencyAddonBuilder setName(String name) {
      this.name = name;
      return this;
    }

    @NonNull
    public DependencyAddonBuilder setVersion(String version) {
      this.version = version;
      return this;
    }

    @NonNull
    public DependencyAddonBuilder setDescription(String description) {
      this.description = description;
      return this;
    }
  }
}
