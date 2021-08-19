package me.googas.starbox.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.NonNull;
import me.googas.commands.annotations.Multiple;
import me.googas.commands.annotations.Parent;
import me.googas.commands.annotations.Required;
import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.result.Result;
import me.googas.io.StarboxFile;
import me.googas.starbox.Starbox;
import me.googas.starbox.StarboxBukkitFiles;
import me.googas.starbox.utility.items.ItemBuilder;
import me.googas.starbox.utility.items.meta.ItemMetaBuilder;
import me.googas.starbox.utility.items.meta.SkullMetaBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Commands to create items using {@link ItemBuilder}. Javadoc warnings are suppressed as commands
 * already have a description and usage.
 */
@SuppressWarnings("JavaDoc")
public class ItemBuilderCommands {

  @NonNull private final Map<UUID, ItemBuilder> builders = new HashMap<>();

  @Parent
  @Command(
      aliases = {"itemBuilder", "ib"},
      description = "Build an item",
      permission = "starbox.item-builder")
  public Result itemBuilder(Player player) {
    if (builders.containsKey(player.getUniqueId())) {
      return new Result(
          "&cYou already have an active builder. Use &e/itemBuilder build &cto build it");
    } else {
      this.getBuilder(player);
      return new Result("&bBuilder has been initialized");
    }
  }

  @Command(aliases = "build", description = "Build the item", permission = "starbox.item-builder")
  public Result build(Player player) {
    player.getInventory().addItem(this.getBuilder(player).build());
    return new Result("&7The item is now in your inventory!");
  }

  @Command(
      aliases = "material",
      description = "Change the material of the item",
      permission = "starbox.item-builder")
  public Result material(
      Player player,
      @Required(name = "material", description = "The new material of the item")
          Material material) {
    this.getBuilder(player).setMaterial(material);
    return new Result("&7Material has been changed to: &b{0}", material.name().toLowerCase());
  }

  @Command(
      aliases = "amount",
      description = "Change the amount of the item",
      permission = "starbox.item-builder")
  public Result amount(
      Player player,
      @Required(name = "amount", description = "The new amount of the item") int amount) {
    this.getBuilder(player).setAmount(amount);
    return new Result("&7Amount has been changed to: &b{0}", amount);
  }

  @Command(
      aliases = "name",
      description = "Set the name of the item",
      permission = "starbox.item-builder")
  public Result name(
      Player player,
      @Required(name = "name", description = "The new name of the item") @Multiple String name) {
    this.getBuilder(player).setName(name);
    return new Result("&7Name has been changed to: " + name);
  }

  @Command(
      aliases = "lore",
      description = "Set the lore of the item",
      permission = "starbox.item-builder")
  public Result lore(
      Player player,
      @Required(name = "lore", description = "The new lore of the item") @Multiple String lore) {
    this.getBuilder(player).setLore(lore);
    return new Result("&7Lore has been changed to: " + lore);
  }

  @Command(
      aliases = "unbreakable",
      description = "Set whether the item is unbreakable",
      permission = "starbox.item-builder")
  public Result unbreakable(
      Player player,
      @Required(name = "unbreakable", description = "Whether the item has to be unbreakable")
          boolean unbreakable) {
    this.getBuilder(player).setUnbreakable(unbreakable);
    return new Result("&7Unbreakable has been set to: &b{0}", unbreakable);
  }

  @Command(
      aliases = "owner",
      description = "Set the owner of the skull",
      permission = "starbox.item-builder")
  public Result owner(
      Player player,
      @Required(name = "owner", description = "The owner of the skull") OfflinePlayer owner) {
    ItemBuilder builder = this.getBuilder(player);
    ItemMetaBuilder metaBuilder = builder.getMetaBuilder();
    if (metaBuilder instanceof SkullMetaBuilder) {
      ((SkullMetaBuilder) metaBuilder).setOwner(owner);
      String name = owner.getName() == null ? owner.getUniqueId().toString() : owner.getName();
      return new Result("&7Owner of the skull has been set to: &b" + name);
    }
    return new Result("&eThe current material of the item is not a skull");
  }

  @Command(
      aliases = "skin",
      description = "Set the skin of the skull",
      permission = "starbox.item-builder")
  public Result skin(
      Player player,
      @Required(name = "skin", description = "The skin in its Base64") String base64) {
    ItemBuilder builder = this.getBuilder(player);
    ItemMetaBuilder metaBuilder = builder.getMetaBuilder();
    if (metaBuilder instanceof SkullMetaBuilder) {
      ((SkullMetaBuilder) metaBuilder).setSkin(base64);
      return new Result("&7The skin of the skull has been set to: &b" + base64);
    }
    return new Result("&eThe current material of the item is not a skull");
  }

  @Command(
      aliases = "reset",
      description = "Reset your item builder",
      permission = "starbox.item-builder")
  public Result reset(Player player) {
    this.builders.remove(player.getUniqueId());
    return new Result("&7Your builder has been reset");
  }

  @Command(
      aliases = "export",
      description = "Export your current builder",
      permission = "starbox.item-builder")
  public Result export(
      Player player,
      @Required(name = "name", description = "The name of the exported file") String name) {
    StarboxFile file =
        new StarboxFile(StarboxBukkitFiles.EXPORTS, name.endsWith(".json") ? name : name + ".json");
    boolean exported =
        file.write(StarboxBukkitFiles.Contexts.JSON, this.getBuilder(player))
            .provide()
            .orElse(false);
    if (exported) {
      return new Result("&7Successfully exported builder to: &b{0}", file);
    } else {
      return new Result("&7Could not export current builder");
    }
  }

  @Command(
      aliases = "import",
      description = "Import a builder",
      permission = "starbox.item-builder")
  public Result importBuilder(
      Player player,
      @Required(name = "name", description = "The name of the file to import") String name) {
    StarboxFile file =
        new StarboxFile(StarboxBukkitFiles.EXPORTS, name.endsWith(".json") ? name : name + ".json");
    if (file.exists()) {
      AtomicBoolean successful = new AtomicBoolean(true);
      ItemBuilder builder =
          file.read(StarboxBukkitFiles.Contexts.JSON, ItemBuilder.class)
              .handle(Starbox::severe)
              .provide()
              .orElseGet(
                  () -> {
                    successful.set(false);
                    return new ItemBuilder();
                  });
      this.builders.put(player.getUniqueId(), builder);
      if (successful.get()) {
        return new Result("&7Successfully loaded builder from: &b{0}", file);
      } else {
        return new Result("&7Could not load builder from: &d{0}", file);
      }
    }
    return new Result("&cFile {0} does not exist", file);
  }

  @NonNull
  private ItemBuilder getBuilder(@NonNull Player player) {
    return this.builders.computeIfAbsent(player.getUniqueId(), uuid -> new ItemBuilder());
  }
}
