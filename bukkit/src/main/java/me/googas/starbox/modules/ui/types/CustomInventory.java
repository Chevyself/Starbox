package me.googas.starbox.modules.ui.types;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.starbox.modules.ui.Button;
import me.googas.starbox.modules.ui.UI;
import me.googas.starbox.modules.ui.buttons.StarboxButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/** A custom inventory is a fixed size UI. */
public class CustomInventory implements UI {

  public static final int EXTRA_SMALL = 9;
  public static final int SMALL = 18;
  public static final int SMALL_MEDIUM = 27;
  public static final int LARGE_MEDIUM = 36;
  public static final int LARGE = 46;
  public static final int EXTRA_LARGE = 54;

  @NonNull @Getter private final Map<Integer, Button> buttons = new HashMap<>();
  @NonNull private final Inventory inventory;
  @NonNull @Getter @Setter private Button empty;

  /**
   * Create the inventory.
   *
   * @param size the size of the inventory
   * @param title the title to be displayed in the inventory
   * @param empty the button for empty slots
   */
  public CustomInventory(int size, String title, @NonNull Button empty) {
    if (!CustomInventory.checkSize(size))
      throw new IllegalArgumentException(
          size + " is not a valid size it must be: 9, 18, 27, 36, 46 or 54");
    if (title != null) {
      this.inventory = Bukkit.createInventory(this, size, title);
    } else {
      this.inventory = Bukkit.createInventory(this, size);
    }
    this.empty = empty;
    this.buttons.forEach((position, button) -> this.inventory.setItem(position, button.getItem()));
  }

  /**
   * Create the inventory.
   *
   * @param size the size of the inventory
   * @param title the title to be displayed in the inventory
   */
  public CustomInventory(int size, String title) {
    this(size, title, StarboxButton.empty());
  }

  /**
   * Checks if the given size can be used in an inventor.
   *
   * <p>Sizes which may be used are: 9, 18, 27, 36, 46, 54
   *
   * @param size the size to check
   * @return true if size can be used
   */
  public static boolean checkSize(int size) {
    return size == CustomInventory.EXTRA_SMALL
        || size == CustomInventory.SMALL
        || size == CustomInventory.SMALL_MEDIUM
        || size == CustomInventory.LARGE_MEDIUM
        || size == CustomInventory.LARGE
        || size == CustomInventory.EXTRA_LARGE;
  }

  @Override
  public @NonNull Button getButton(int position) {
    return this.buttons.get(position);
  }

  @Override
  public CustomInventory set(int position, @NonNull Button button) {
    if (position > this.inventory.getSize()) {
      throw new IllegalArgumentException("Position cannot be greater than the inventory size");
    } else {
      this.buttons.put(position, button);
      this.inventory.setItem(position, button.getItem());
    }
    return this;
  }

  @NonNull
  @Override
  public CustomInventory remove(int position) {
    if (position > this.inventory.getSize()) {
      throw new IllegalArgumentException("Position cannot be greater than the inventory size");
    } else {
      this.buttons.remove(position);
      this.inventory.setItem(position, new ItemStack(Material.AIR));
    }
    return this;
  }

  @Override
  public @NonNull Inventory getInventory() {
    return this.inventory;
  }
}
