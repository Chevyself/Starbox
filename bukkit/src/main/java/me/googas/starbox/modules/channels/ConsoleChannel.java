package me.googas.starbox.modules.channels;

import lombok.NonNull;
import me.googas.commands.bukkit.utils.BukkitUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;

/**
 * A channel that is used to send data to the console.
 */
public class ConsoleChannel implements Channel {

  @NonNull
  private static final ConsoleChannel instance = new ConsoleChannel();

  private ConsoleChannel() {}

  /**
   * Get the unique instance of the channel.
   *
   * @return the instance
   */
  @NonNull
  public static ConsoleChannel getInstance() {
    return ConsoleChannel.instance;
  }

  @Override
  public @NonNull ConsoleChannel send(@NonNull BaseComponent... components) {
    BukkitUtils.send(Bukkit.getConsoleSender(), components);
    return this;
  }

  @Override
  public @NonNull ConsoleChannel send(@NonNull String text) {
    Bukkit.getConsoleSender().sendMessage(text);
    return this;
  }
}
