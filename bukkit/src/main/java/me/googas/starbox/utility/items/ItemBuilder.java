package me.googas.starbox.utility.items;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.builders.Builder;
import me.googas.starbox.builders.SuppliedBuilder;
import me.googas.starbox.modules.ui.Button;
import me.googas.starbox.modules.ui.ButtonListener;
import me.googas.starbox.modules.ui.buttons.StarboxButton;
import me.googas.starbox.modules.ui.item.ItemButton;
import me.googas.starbox.modules.ui.item.ItemButtonListener;
import me.googas.starbox.modules.ui.item.StarboxItemButton;
import me.googas.starbox.utility.items.meta.ItemMetaBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utility class which helps with the creation of {@link ItemStack}. Create items or buttons in a
 * neat way
 */
public class ItemBuilder implements Builder<ItemStack>, SuppliedBuilder<ButtonListener, Button> {

  @NonNull
  private static final WrappedClass<ItemStack> ITEM_STACK = WrappedClass.of(ItemStack.class);

  @NonNull
  public static final WrappedMethod<?> SET_DURABILITY =
      ItemBuilder.ITEM_STACK.getMethod("setDurability", short.class);

  @NonNull
  @Getter
  @SerializedName(value = "meta", alternate = {"meta-builder", "metaBuilder"})
  private ItemMetaBuilder metaBuilder;

  @NonNull @Getter private Material material;
  @Getter private int amount;

  /**
   * Create the item builder.
   *
   * @param metaBuilder the meta builder for the item {@link ItemMeta}
   * @param material the material of the item
   * @param amount the amount of items in the stack
   */
  protected ItemBuilder(
      @NonNull ItemMetaBuilder metaBuilder, @NonNull Material material, int amount) {
    this.metaBuilder = metaBuilder;
    this.material = material;
    this.amount = amount;
  }

  /** Create the builder. */
  public ItemBuilder() {
    this(new ItemMetaBuilder(), Material.GLASS, 1);
  }

  /**
   * Create the builder with an initial amount and material.
   *
   * @param material the material of the item
   * @param amount the amount of items in the stack
   */
  public ItemBuilder(@NonNull Material material, int amount) {
    this(ItemMetaBuilder.getMeta(material), material, amount);
  }

  /**
   * Create the builder with an initial material.
   *
   * @param material the material of the item
   */
  public ItemBuilder(@NonNull Material material) {
    this(material, 1);
  }

  /**
   * Create the builder with an initial amount.
   *
   * @param amount the amount of items in the stack
   */
  public ItemBuilder(int amount) {
    this(Material.GLASS, amount);
  }

  /**
   * Use a {@link Consumer} to edit the {@link ItemMetaBuilder}.
   *
   * @param consumer the consumer to edit the builder
   * @return this same instance
   */
  @NonNull
  public ItemBuilder withMeta(@NonNull Consumer<ItemMetaBuilder> consumer) {
    consumer.accept(this.metaBuilder);
    return this;
  }

  /**
   * Build this as a {@link ItemButton}.
   *
   * @param listener the listener that handles actions of the button
   * @return the created button
   */
  @Deprecated
  public @NonNull ItemButton buildAsButton(@NonNull ItemButtonListener listener) {
    return new StarboxItemButton(listener, this.build());
  }

  /**
   * Build this as a {@link Button}.
   *
   * @param listener the listener that handles actions of the button
   * @return the created button
   */
  @Deprecated
  public @NonNull StarboxButton buildForUI(@NonNull ButtonListener listener) {
    return new StarboxButton(this.build(), listener);
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
    this.metaBuilder = ItemMetaBuilder.getMeta(material, this.metaBuilder);
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
    this.metaBuilder.ifBuildPresent(item, item::setItemMeta);
    return item;
  }

  @Override
  @NonNull
  public Button build(@NonNull ButtonListener listener) {
    return new StarboxButton(this.build(), listener);
  }

  private interface IgnoredMethods {
    <T extends ItemMeta> T build(@NonNull ItemStack stack);
  }

  /**
   * {@link com.google.gson.Gson} deserializer for {@link ItemBuilder}. This takes into
   * consideration the 'material' of the object to get a valid {@link ItemMetaBuilder}
   */
  public static class Deserializer implements JsonDeserializer<ItemBuilder> {
    @Override
    public ItemBuilder deserialize(
        JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      if (json instanceof JsonObject) {
        JsonObject object = json.getAsJsonObject();
        JsonElement materialElement = object.get("material");
        JsonElement amountElement = object.get("amount");
        Material material =
            Material.valueOf(materialElement == null ? "glass" : materialElement.getAsString());
        int amount = amountElement == null ? 1 : amountElement.getAsInt();
        return new ItemBuilder(
            context.deserialize(
                object.get("meta-builder"), ItemMetaBuilder.getMeta(material).getClass()),
            material,
            amount);
      }
      throw new JsonParseException("Expected an object");
    }
  }
}
