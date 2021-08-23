package me.googas.starbox.modules.ui.buttons;

import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.modules.ui.Button;
import me.googas.starbox.modules.ui.ButtonListener;
import me.googas.starbox.modules.ui.types.PaginatedInventory;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/** Implementation for {@link Button}. */
public class StarboxButton implements Button {

  @Getter @NonNull private final ItemStack item;
  @Getter @NonNull private final ButtonListener listener;

  /**
   * Create the button.
   *
   * @param listener the listener of the button
   * @param item the item that represents the button.
   */
  public StarboxButton(@NonNull ItemStack item, @NonNull ButtonListener listener) {
    this.item = item;
    this.listener = listener;
  }

  /**
   * Create a button that represents a page back for a {@link PaginatedInventory}.
   *
   * @return the button
   */
  @NonNull
  public static Button back() {
    return Button.builder()
        .withItem(item -> item.setMaterial(Material.ARROW).withMeta(meta -> meta.setName("Back")))
        .listen(
            event -> {
              InventoryHolder holder = event.getInventory().getHolder();
              if (!(holder instanceof PaginatedInventory)) return;
              ((PaginatedInventory) holder).previous();
            })
        .build();
  }

  /**
   * Create a button that represents a page forward for a {@link PaginatedInventory}.
   *
   * @return the button
   */
  @NonNull
  public static Button next() {
    return Button.builder()
        .withItem(item -> item.setMaterial(Material.ARROW).withMeta(meta -> meta.setName("Next")))
        .listen(
            event -> {
              InventoryHolder holder = event.getInventory().getHolder();
              if (!(holder instanceof PaginatedInventory)) return;
              ((PaginatedInventory) holder).next();
            })
        .build();
  }

  /**
   * Create an empty button.
   *
   * @return an empty button
   */
  @NonNull
  public static Button empty() {
    return Button.builder(new ItemBuilder(Material.AIR)).build();
  }

  /**
   * Create an empty button with a representative item.
   *
   * @param item the item to represent the button
   * @return the button
   */
  @NonNull
  public static Button empty(@NonNull ItemStack item) {
    return new StarboxButton(item, event -> event.setCancelled(true));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    StarboxButton button = (StarboxButton) o;
    return this.listener.equals(button.listener) && this.item.isSimilar(button.item);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", StarboxButton.class.getSimpleName() + "[", "]")
        .add("listener=" + listener)
        .add("item=" + item)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.listener, this.item);
  }
}
