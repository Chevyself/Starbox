package me.googas.starbox.utility.items;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.starbox.builders.Builder;
import me.googas.starbox.builders.SuppliedBuilder;
import me.googas.starbox.modules.ui.Button;
import me.googas.starbox.modules.ui.ButtonListener;
import me.googas.starbox.modules.ui.buttons.StarboxButton;
import me.googas.starbox.modules.ui.item.ItemButton;
import me.googas.starbox.modules.ui.item.ItemButtonListener;
import me.googas.starbox.modules.ui.item.StarboxItemButton;
import me.googas.starbox.utility.Materials;
import me.googas.starbox.utility.items.meta.BannerMetaBuilder;
import me.googas.starbox.utility.items.meta.BookMetaBuilder;
import me.googas.starbox.utility.items.meta.ItemMetaBuilder;
import me.googas.starbox.utility.items.meta.SkullMetaBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder implements Builder<ItemStack>, SuppliedBuilder<ButtonListener, Button> {

  @Getter
  @Delegate(excludes = IgnoredMethods.class)
  private ItemMetaBuilder metaBuilder = new ItemMetaBuilder(this);

  @NonNull @Getter private Material material = Material.GLASS;
  @Getter private int amount = 1;

  public ItemBuilder() {}

  public ItemBuilder(@NonNull Material material, int amount) {
    this.amount = amount;
    this.setMaterial(material);
  }

  public ItemBuilder(@NonNull Material material) {
    this.setMaterial(material);
  }

  public ItemBuilder(int amount) {
    this.amount = amount;
  }

  public @NonNull ItemButton buildAsButton(@NonNull ItemButtonListener listener) {
    return new StarboxItemButton(listener, this.build());
  }

  @NonNull
  public ItemBuilder setMaterial(Material material) {
    this.material = material;
    if (material == Material.BOOK) {
      this.metaBuilder = new BookMetaBuilder(this);
    } else {
      if (Materials.isBanner(material)) {
        this.metaBuilder = new BannerMetaBuilder(this);
        return this;
      }
      if (Materials.isSkull(material)) {
        this.metaBuilder = new SkullMetaBuilder(this);
      }
    }
    return this;
  }

  @NonNull
  public ItemBuilder setAmount(int amount) {
    this.amount = amount;
    return this;
  }

  @Override
  public @NonNull ItemStack build() {
    ItemStack item = new ItemStack(this.material, this.amount);
    if (this.metaBuilder != null) {
      this.metaBuilder.ifBuildPresent(item, item::setItemMeta);
    }
    return item;
  }

  @Override
  @NonNull
  public Button build(@NonNull ButtonListener listener) {
    return new StarboxButton(listener, this.build());
  }

  private interface IgnoredMethods {
    <T extends ItemMeta> T build(@NonNull ItemStack stack);
  }
}
