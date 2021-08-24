package me.googas.starbox.modules.channels;

import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import java.util.Locale;
import java.util.Optional;

/**
 * This channel send a message to all the server including the console.
 */
public class ServerChannel implements Channel {

    @NonNull @Getter
    public static final ServerChannel instance = new ServerChannel();

    private ServerChannel() {

    }

    @Override
    public @NonNull Channel send(@NonNull BaseComponent... components) {
        Bukkit.getOnlinePlayers().stream().map(Channel::of).forEach(channel -> channel.send(components));
        ConsoleChannel.getInstance().send(components);
        return this;
    }

    @Override
    public @NonNull Channel send(@NonNull String text) {
        Bukkit.getOnlinePlayers().stream().map(Channel::of).forEach(channel -> channel.send(text));
        ConsoleChannel.getInstance().send(text);
        return this;
    }

    @Override
    public Optional<Locale> getLocale() {
        return Optional.empty();
    }
}
