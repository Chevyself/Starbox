package me.googas.starbox.modules.ui;

import lombok.NonNull;
import me.googas.reflect.wrappers.attributes.WrappedAttributes;
import me.googas.starbox.builders.Builder;
import me.googas.starbox.modules.ui.buttons.StarboxButton;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.Material;

/**
 * Builder for {@link Button}. Creates {@link StarboxButton}.
 */
public class ButtonBuilder implements Builder<Button> {

  @NonNull
  private ItemBuilder item;
  @NonNull
  private transient ButtonListener listener;

  ButtonBuilder(@NonNull ItemBuilder item) {
    this.item = item;
    this.listener = event -> event.setCancelled(true);
  }

  ButtonBuilder() {
    this(new ItemBuilder());
  }

  /**
   * Set the button listener.
   *
   * @param listener the new listener
   * @return this same instance
   */
  @NonNull
  public ButtonBuilder listen(@NonNull ButtonListener listener) {
    this.listener = listener;
    return this;
  }

  /**
   * Set the item of the builder.
   *
   * @param item the new item of the builder
   * @return this same instance
   */
  @NonNull
  public ButtonBuilder setItem(ItemBuilder item) {
    this.item = item;
    return this;
  }

  /**
   * Set the name of the item.
   *
   * @param name the name of the item
   * @return this same instance
   */
  public @NonNull ButtonBuilder setName(String name) {
    item.setName(name);
    return this;
  }

  /**
   * Set the lore of the item.
   *
   * @param lore the lore of the item
   * @return this same instance
   */
  public @NonNull ButtonBuilder setLore(String lore) {
    item.setLore(lore);
    return this;
  }

  /**
   * Set whether the item is unbreakable.
   *
   * @param unbreakable the new boolean value
   * @return this same instance
   */
  public @NonNull ButtonBuilder setUnbreakable(boolean unbreakable) {
    item.setUnbreakable(unbreakable);
    return this;
  }

  /**
   * Set the attributes of the item.
   *
   * @param attributes the attributes
   * @return this same instance
   */
  public @NonNull ButtonBuilder setAttributes(WrappedAttributes attributes) {
    item.setAttributes(attributes);
    return this;
  }

  /**
   * Set the material of the item.
   *
   * @param material the new material of the item
   * @return this same instance
   */
  @NonNull
  public ButtonBuilder setMaterial(Material material) {
    item.setMaterial(material);
    return this;
  }

  /**
   * Set the amount of the item.
   *
   * @param amount the new amount of the item
   * @return this same instance
   */
  @NonNull
  public ButtonBuilder setAmount(int amount) {
    item.setAmount(amount);
    return this;
  }

  @Override
  public @NonNull Button build() {
    return new StarboxButton(listener, item.build());
  }
}
