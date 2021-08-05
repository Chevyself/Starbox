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

/**
 * Utility class which helps with the creation of {@link ItemStack}. Create items or buttons in a
 * neat way
 */
public class ItemBuilder implements Builder<ItemStack>, SuppliedBuilder<ButtonListener, Button> {

  @Getter
  @Delegate(excludes = IgnoredMethods.class)
  private ItemMetaBuilder metaBuilder = new ItemMetaBuilder(this);

  @NonNull @Getter private Material material = Material.GLASS;
  @Getter private int amount = 1;

  /** Create the builder. */
  public ItemBuilder() {}

  /**
   * Create the builder with an initial amount and material.
   *
   * @param material the material of the item
   * @param amount the amount of items in the stack
   */
  public ItemBuilder(@NonNull Material material, int amount) {
    this.amount = amount;
    this.setMaterial(material);
  }

  /**
   * Create the builder with an initial material.
   *
   * @param material the material of the item
   */
  public ItemBuilder(@NonNull Material material) {
    this.setMaterial(material);
  }

  /**
   * Create the builder with an initial amount.
   *
   * @param amount the amount of items in the stack
   */
  public ItemBuilder(int amount) {
    this.amount = amount;
  }

  /**
   * Build this as a {@link ItemButton}.
   *
   * @param listener the listener that handles actions of the button
   * @return the created button
   */
  public @NonNull ItemButton buildAsButton(@NonNull ItemButtonListener listener) {
    return new StarboxItemButton(listener, this.build());
  }

  /**
   * Set the material of the item.
   *
   * @param material the new material
   * @return this same instance
   */
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

  /**
   * Set the amount of the stack.
   *
   * @param amount the new amount of items in the stack
   * @return this same instance
   */
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
