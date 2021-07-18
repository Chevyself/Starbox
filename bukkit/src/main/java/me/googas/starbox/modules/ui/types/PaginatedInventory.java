package me.googas.starbox.modules.ui.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.starbox.modules.ui.Button;
import me.googas.starbox.modules.ui.UI;
import me.googas.starbox.modules.ui.buttons.StarboxButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A paginated inventory has different sizes depending on the buttons size and has pages if there is
 * too many buttons
 */
public class PaginatedInventory implements UI {

  @NonNull private final Map<Integer, Button> buttons = new HashMap<>();
  @NonNull private final Map<Integer, Button> toolbar = new HashMap<>();
  private final int defaultSize;
  @NonNull private final String title;
  @Getter private int page = 0;
  @NonNull @Getter @Setter private Button empty;
  private Inventory inventory;

  public PaginatedInventory(int defaultSize, @NonNull String title, @NonNull Button empty) {
    this.defaultSize = defaultSize;
    this.title = title;
    this.empty = empty;
  }

  public PaginatedInventory(int defaultSize, @NonNull String title) {
    this(defaultSize, title, StarboxButton.empty());
  }

  public PaginatedInventory(@NonNull String title, @NonNull Button empty) {
    this(0, title, empty);
  }

  public PaginatedInventory(@NonNull String title) {
    this(0, title);
  }

  /**
   * Adds the default toolbar items. The default toolbar items are both arrows
   *
   * @return this same instance
   */
  @NonNull
  public PaginatedInventory addDefaultToolbar() {
    this.setToolbar(0, StarboxButton.back());
    this.setToolbar(8, StarboxButton.next());
    return this;
  }

  /**
   * Get the size of the inventory, this means, the size of the Minecraft inventory which may be:
   * 18, 27, 36, 46 or 54. It cannot be less than 18 as it needs space for the toolbar
   *
   * @return the size of the Minecraft inventory
   */
  private int size() {
    if (CustomInventory.checkSize(this.defaultSize) && this.defaultSize != 9)
      return this.defaultSize;
    if (this.lastSlot() == 0) return CustomInventory.SMALL;
    if (this.lastSlot() >= 53) return 54;
    int size = 54;
    while (size != 18) {
      size -= 9;
      if (this.lastSlot() > size) {
        break;
      }
    }
    return size;
  }

  /**
   * Get the size which can be used by {@Link #buttons}. This is the size of the Minecraft inventory
   * without the 9 extra slots of the toolbar
   *
   * @return the size which can be used by buttons
   */
  private int buttonSize() {
    return this.size() - 9;
  }

  /**
   * Get the last occupied slot there is
   *
   * @return the last occupied slot 0 if none or just the first one is occupied
   */
  private int lastSlot() {
    int last = 0;
    for (Integer position : this.buttons.keySet()) {
      if (position != null && last < position) {
        last = position;
      }
    }
    return last;
  }

  /**
   * Get the last available slot there is for a button
   *
   * @return the last slot
   */
  private int lastEmptySlot() {
    return this.buttons.isEmpty() ? 0 : this.lastSlot() + 1;
  }

  /**
   * Get the page where an slot is in
   *
   * @param slot the slot to get in which page it is in
   * @return the page that contains the slot
   */
  private int getPage(int slot) {
    return slot / this.buttonSize();
  }

  /**
   * Set the inventory to the previous page.
   *
   * <p>This will refresh the inventory for the viewers to the new page.
   */
  public void previous() {
    this.previousPage();
    this.refresh();
  }

  /**
   * Set the inventory to the next page.
   *
   * <p>This will refresh the inventory for the viewers to the new page.
   */
  public void next() {
    this.nextPage();
    this.refresh();
  }

  /**
   * Refresh the view to all the viewers of the inventory.
   *
   * <p>This is used when a new page needs to be used
   */
  private void refresh() {
    List<HumanEntity> viewers = new ArrayList<>(this.getInventory().getViewers());
    this.inventory = null;
    Inventory inventory = this.getInventory();
    for (HumanEntity viewer : viewers) {
      viewer.closeInventory();
      viewer.openInventory(inventory);
    }
  }

  /**
   * Get the last page of the inventory
   *
   * @return the last page as an integer
   */
  private int lastPage() {
    return ((this.lastEmptySlot() + this.buttonSize() - 1) / this.buttonSize()) - 1;
  }

  /**
   * Get the slot in which a button is on
   *
   * @param button the button to get the slot of
   * @return the slot of the button if it is in the UI else -1
   */
  public int getSlot(@NonNull Button button) {
    for (Integer integer : this.buttons.keySet()) {
      if (integer != null) {
        if (this.buttons.get(integer).equals(button)) return integer;
      }
    }
    return -1;
  }

  /**
   * Go to the previous page
   *
   * <p>If there's no previous page it will got to the last page
   */
  private void previousPage() {
    if (this.page > 0) {
      this.page--;
    } else {
      this.page = this.lastPage();
    }
  }

  /**
   * Go to the previous page
   *
   * <p>If there's no next page it will got to the first
   */
  private void nextPage() {
    if (this.page < this.lastPage()) {
      this.page++;
    } else {
      this.page = 0;
    }
  }

  /**
   * Add a button to the last empty spot
   *
   * @param button the button to add
   */
  public void add(@NonNull Button button) {
    this.set(this.lastEmptySlot(), button);
  }

  /**
   * Add an item to the toolbar
   *
   * @param position the position to add the item in the toolbar
   * @param button the button to add in the toolbar
   */
  public void setToolbar(int position, Button button) {
    if (position < 0 || position > 8)
      throw new IllegalArgumentException(
          position + " is out of bounds. Position must be between 0 and 8");
    if (button != null) {
      this.toolbar.put(position, button);
      if (this.inventory != null)
        this.inventory.setItem(position + this.buttonSize(), button.getItem());
    } else {
      this.toolbar.remove(position);
      if (this.inventory != null) this.inventory.setItem(position, new ItemStack(Material.AIR));
    }
  }

  /** Sets the buttons in the toolbar */
  private void addToolbar() {
    if (this.inventory == null) return;
    this.toolbar.forEach(
        (position, button) ->
            this.inventory.setItem(position + this.buttonSize(), button.getItem()));
  }

  /** Sets the rest of the buttons */
  private void addItems() {
    if (this.inventory == null) return;
    this.buttons.forEach(
        (position, button) -> {
          int realPosition = position - (this.page * this.buttonSize());
          if (realPosition >= 0 && realPosition < this.buttonSize()) {
            this.inventory.setItem(realPosition, button.getItem());
          }
        });
  }

  public void remove(@NonNull Button button, boolean updateSlots) {
    int slot = this.getSlot(button);
    this.remove(slot);
    if (updateSlots) {
      this.updateSlots(slot);
    }
  }

  private void updateSlots(int last) {
    Map<Integer, Button> toUpdate = new TreeMap<>();
    for (Map.Entry<Integer, Button> entry : new TreeMap<>(this.buttons).entrySet()) {
      if (entry.getKey() >= last) {
        toUpdate.put(entry.getKey(), entry.getValue());
      }
    }
    for (Map.Entry<Integer, Button> entry : toUpdate.entrySet()) {
      this.remove(entry.getKey());
      this.add(entry.getValue());
    }
  }

  public int getInterfacePosition(int position) {
    if (position < this.buttonSize()) {
      return (this.page * this.buttonSize()) + position;
    } else {
      return position - 45;
    }
  }

  @Override
  public @NonNull Button getButton(int position) {
    Button button;
    if (position < this.buttonSize()) {
      button = this.buttons.get((this.page * this.buttonSize()) + position);
    } else {
      button = this.toolbar.get(position - 45);
    }
    return button == null ? this.empty : button;
  }

  @Override
  public PaginatedInventory set(int position, @NonNull Button button) {
    this.buttons.put(position, button);
    if (this.getPage(position) != this.page) return this;
    int realPosition = position - (this.page * this.buttonSize());
    if (this.inventory != null) {
      if (this.inventory.getSize() < this.size()) {
        this.refresh();
      }
      this.getInventory().setItem(realPosition, button.getItem());
    }
    return this;
  }

  @Override
  public PaginatedInventory remove(int position) {
    this.buttons.remove(position);
    if (this.getPage(position) == this.page) {
      int realPosition = position - (this.page * this.buttonSize());
      this.getInventory().setItem(realPosition, new ItemStack(Material.AIR));
    }
    return this;
  }

  @Override
  public @NonNull Inventory getInventory() {
    if (this.inventory == null) {
      this.inventory =
          Bukkit.createInventory(
              this,
              this.size(),
              this.title
                  .replace("%page%", String.valueOf(this.page + 1))
                  .replace(
                      "%max%", String.valueOf(this.lastPage() <= 0 ? 1 : this.lastPage() + 1)));
      this.addToolbar();
      this.addItems();
    }
    return this.inventory;
  }
}
