package me.googas.starbox.commands;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.annotations.Multiple;
import me.googas.commands.annotations.Required;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.StarboxBukkitCommand;
import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.io.StarboxFile;
import me.googas.starbox.BukkitLanguage;
import me.googas.starbox.Starbox;
import me.googas.starbox.StarboxBukkitFiles;
import me.googas.starbox.utility.items.ItemBuilder;
import me.googas.starbox.utility.items.meta.ItemMetaBuilder;
import me.googas.starbox.utility.items.meta.SkullMetaBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Commands to create items using {@link ItemBuilder}. Javadoc warnings are suppressed as commands
 * already have a description and usage.
 */
@SuppressWarnings("JavaDoc")
public class ItemBuilderCommands {

  @NonNull private final Map<UUID, ItemBuilder> builders = new HashMap<>();

  @Command(aliases = "build", description = "Build the item", permission = "starbox.item-builder")
  public Result build(Player player) {
    player.getInventory().addItem(this.getBuilder(player).build());
    return BukkitLanguage.asResult(player, "item-builder.build");
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
    return BukkitLanguage.asResult(
        player, "item-builder.material", material.toString().toLowerCase());
  }

  @Command(
      aliases = "amount",
      description = "Change the amount of the item",
      permission = "starbox.item-builder")
  public Result amount(
      Player player,
      @Required(name = "amount", description = "The new amount of the item") int amount) {
    this.getBuilder(player).setAmount(amount);
    return BukkitLanguage.asResult(player, "item-builder.amount", amount);
  }

  @Command(
      aliases = "name",
      description = "Set the name of the item",
      permission = "starbox.item-builder")
  public Result name(
      Player player,
      @Required(name = "name", description = "The new name of the item") @Multiple String name) {
    this.getBuilder(player).setName(name);
    return BukkitLanguage.asResult(player, "item-builder.name", BukkitUtils.format(name));
  }

  @Command(
      aliases = "lore",
      description = "Set the lore of the item",
      permission = "starbox.item-builder")
  public Result lore(
      Player player,
      @Required(name = "lore", description = "The new lore of the item") @Multiple String lore) {
    this.getBuilder(player).setLore(lore);
    return BukkitLanguage.asResult(player, "item-builder.lore", BukkitUtils.format(lore));
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
    return BukkitLanguage.asResult(player, "item-builder.unbreakable", unbreakable);
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
      return BukkitLanguage.asResult(player, "item-builder.owner", name);
    }
    return BukkitLanguage.asResult(player, "item-builder.not-skull");
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
      return BukkitLanguage.asResult(player, "item-builder.skin", base64);
    }
    return BukkitLanguage.asResult(player, "item-builder.not-skull");
  }

  @Command(
      aliases = "reset",
      description = "Reset your item builder",
      permission = "starbox.item-builder")
  public Result reset(Player player) {
    this.builders.remove(player.getUniqueId());
    return BukkitLanguage.asResult(player, "item-builder.reset");
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
      return BukkitLanguage.asResult(player, "item-builder.export.success", file);
    } else {
      return BukkitLanguage.asResult(player, "item-builder.export.not");
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
        return BukkitLanguage.asResult(player, "item-builder.import.success", file);
      } else {
        return BukkitLanguage.asResult(player, "item-builder.import.not", file);
      }
    }
    return BukkitLanguage.asResult(player, "item-builder.import.no-file", file);
  }

  public static class Parent extends StarboxBukkitCommand {

    @NonNull @Getter private final List<StarboxBukkitCommand> children = new ArrayList<>();

    public Parent(@NonNull CommandManager manager) {
      super(
          "itemBuilder",
          "Parent command to build items",
          "itemBuilder <subcommand>",
          Collections.singletonList("ib"),
          false,
          manager);
    }

    @Override
    public Result execute(@NonNull CommandContext context) {
      return new Result(BukkitLanguage.getDefault().buildHelp(this, context));
    }

    @Override
    public boolean hasAlias(@NonNull String alias) {
      return alias.equalsIgnoreCase("itemBuilder") || alias.equalsIgnoreCase("ib");
    }
  }

  @NonNull
  private ItemBuilder getBuilder(@NonNull Player player) {
    return this.builders.computeIfAbsent(player.getUniqueId(), uuid -> new ItemBuilder());
  }
}
