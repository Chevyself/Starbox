package me.googas.starbox;

import com.google.gson.GsonBuilder;
import lombok.NonNull;
import me.googas.adapters.OfflinePlayerAdapter;
import me.googas.io.StarboxFile;
import me.googas.io.context.Json;
import me.googas.io.context.Txt;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.OfflinePlayer;

public class StarboxBukkitFiles {

  public static final StarboxFile DIR = StarboxFile.of(Starbox.getPlugin().getDataFolder());

  public static final StarboxFile EXPORTS = new StarboxFile(StarboxBukkitFiles.DIR, "exports");

  public static class Contexts {

    @NonNull public static final Txt TXT = new Txt();

    @NonNull
    public static final Json JSON =
        new Json(
            new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeHierarchyAdapter(OfflinePlayer.class, new OfflinePlayerAdapter())
                .registerTypeAdapter(ItemBuilder.class, new ItemBuilder.Deserializer())
                .create());
  }
}
