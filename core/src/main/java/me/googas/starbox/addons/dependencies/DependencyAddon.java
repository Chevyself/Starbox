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

public class DependencyAddon implements Addon {

  @NonNull @Getter private final URL url;
  @NonNull @Getter private final StarboxFile file;
  @NonNull @Getter private final DependencyInformation information;
  private final DownloadTask task;

  public DependencyAddon(
      @NonNull URL url, @NonNull StarboxFile file, @NonNull DependencyInformation information) {
    this.url = url;
    this.file = file;
    this.information = information;
    this.task = new DownloadTask(this.file, this.url);
  }

  @NonNull
  public HandledExpression<Boolean> download() {
    return this.task.download();
  }

  @NonNull
  public static DependencyAddonBuilder of(@NonNull URL url) {
    return new DependencyAddonBuilder(url);
  }

  @NonNull
  public static DependencyAddonBuilder of(@NonNull String url) throws MalformedURLException {
    return new DependencyAddonBuilder(new URL(url));
  }

  public static class DownloadTask {
    @NonNull private final StarboxFile file;
    @NonNull private final URL url;
    // From the task
    private ReadableByteChannel channel;
    private FileOutputStream stream;
    @Getter private boolean completed = false;

    private DownloadTask(@NonNull StarboxFile file, @NonNull URL url) {
      this.file = file; // TODO check that it exists or create
      this.url = url;
    }

    @NonNull
    public HandledExpression<Boolean> download() {
      return HandledExpression.using(
              () -> {
                if (!this.completed) {
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

  public static class DependencyAddonBuilder
      implements SuppliedBuilder<DependencyManager, DependencyAddon>,
          Builder<DependencyInformation> {

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

    @NonNull
    public DependencyAddon build(@NonNull StarboxFile file) {
      return new DependencyAddon(this.url, file, this.build());
    }

    @NonNull
    public DependencyAddon build(@NonNull DependencyManager manager) {
      DependencyInformation information = this.build();
      return new DependencyAddon(this.url, manager.getDependencyFile(information), information);
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
