package me.googas.starbox;

import com.google.gson.GsonBuilder;
import lombok.NonNull;
import me.googas.adapters.OfflinePlayerAdapter;
import me.googas.io.StarboxFile;
import me.googas.io.context.Json;
import me.googas.io.context.Txt;
import me.googas.reflect.wrappers.chat.AbstractComponentBuilder;
import me.googas.reflect.wrappers.inventory.WrappedBookMetaGeneration;
import me.googas.starbox.utility.items.ItemBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;

/** Static access for files used by the plugin. */
public class StarboxBukkitFiles {

  public static final StarboxFile DIR = StarboxFile.of(Starbox.getPlugin().getDataFolder());

  public static final StarboxFile EXPORTS = new StarboxFile(StarboxBukkitFiles.DIR, "exports");

  /** Static access for file contexts. */
  public static class Contexts {

    @NonNull public static final Txt TXT = new Txt();

    @NonNull
    public static final Json JSON =
        new Json(
            new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeHierarchyAdapter(OfflinePlayer.class, new OfflinePlayerAdapter())
                .registerTypeAdapter(
                    WrappedBookMetaGeneration.class, new WrappedBookMetaGeneration.Adapter())
                .registerTypeAdapter(ItemBuilder.class, new ItemBuilder.Deserializer())
                .registerTypeHierarchyAdapter(
                    BaseComponent.class, new AbstractComponentBuilder.Adapter())
                .create());
  }
}
