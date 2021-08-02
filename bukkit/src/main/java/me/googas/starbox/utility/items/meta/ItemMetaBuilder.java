package me.googas.starbox.utility.items.meta;

import com.google.common.collect.Multimap;
import java.util.HashMap;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.reflect.wrappers.attributes.WrappedAttributes;
import me.googas.starbox.Strings;
import me.googas.starbox.builders.MapBuilder;
import me.googas.starbox.builders.SuppliedBuilder;
import me.googas.starbox.modules.ui.Button;
import me.googas.starbox.modules.ui.ButtonListener;
import me.googas.starbox.modules.ui.buttons.StarboxButton;
import me.googas.starbox.utility.Versions;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemMetaBuilder implements SuppliedBuilder<ItemStack, ItemMeta> {

  @NonNull private static final WrappedClass ITEM_META = WrappedClass.of(ItemMeta.class);

  @NonNull
  @APIVersion(value = 8, max = 15)
  private static final WrappedClass ITEM_META_SPIGOT =
      WrappedClass.forName("org.bukkit.inventory.meta.ItemMeta.Spigot");

  @NonNull
  @APIVersion(value = 8, max = 15)
  private static final WrappedMethod<?> ITEM_META_SPIGOT_METHOD =
      ItemMetaBuilder.ITEM_META.getMethod("spigot");

  @NonNull
  @APIVersion(11)
  private static final WrappedMethod<?> SET_UNBREAKABLE =
      ItemMetaBuilder.ITEM_META_SPIGOT.getMethod("setUnbreakable", boolean.class);

  @NonNull
  @APIVersion(value = 8, max = 10)
  private static final WrappedMethod<?> SPIGOT_SET_UNBREAKABLE =
      ItemMetaBuilder.ITEM_META_SPIGOT.getMethod("setUnbreakable", boolean.class);

  @NonNull
  @APIVersion(12)
  private static final WrappedMethod<?> SET_ATTRIBUTE_MODIFIERS =
      ItemMetaBuilder.ITEM_META.getMethod("setAttributeModifiers", Multimap.class);

  @NonNull @Getter private final EnchantmentsBuilder enchantments = new EnchantmentsBuilder(this);
  @NonNull @Getter private final ItemBuilder builder;
  @Getter private String name;
  @Getter private String lore;
  @Getter private WrappedAttributes attributes;
  @Getter private boolean unbreakable;

  public ItemMetaBuilder(@NonNull ItemBuilder builder) {
    this.builder = builder;
    ItemMetaBuilder metaBuilder = this.builder.getMetaBuilder();
    if (metaBuilder != null) {
      if (builder.getName() != null) this.name = builder.getName();
      if (builder.getLore() != null) this.lore = builder.getLore();
      this.unbreakable = builder.isUnbreakable();
    }
  }

  public @NonNull ItemStack buildAll() {
    return this.builder.build();
  }

  public @NonNull Button buildAll(@NonNull ButtonListener listener) {
    return new StarboxButton(listener, this.buildAll());
  }

  @NonNull
  public ItemMetaBuilder setName(String name) {
    this.name = name;
    return this;
  }

  @NonNull
  public ItemMetaBuilder setLore(String lore) {
    this.lore = lore;
    return this;
  }

  @NonNull
  public ItemMetaBuilder setAttributes(WrappedAttributes attributes) {
    this.attributes = attributes;
    return this;
  }

  @NonNull
  public ItemMetaBuilder setUnbreakable(boolean unbreakable) {
    this.unbreakable = unbreakable;
    return this;
  }

  @Override
  public ItemMeta build(@NonNull ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if (meta != null) {
      if (this.name != null) meta.setDisplayName(this.name);
      if (this.lore != null) meta.setLore(Strings.divide(this.lore, 64));
      if (this.attributes != null && Versions.BUKKIT >= 12) {
        ItemMetaBuilder.SET_ATTRIBUTE_MODIFIERS.prepare(meta, this.attributes.build()).run();
      }
      if (Versions.BUKKIT <= 10) {
        ItemMetaBuilder.SPIGOT_SET_UNBREAKABLE
            .prepare(
                ItemMetaBuilder.ITEM_META_SPIGOT_METHOD.prepare(meta).provide().orElse(null),
                this.unbreakable)
            .run();
      } else {
        ItemMetaBuilder.SET_UNBREAKABLE.prepare(meta, this.unbreakable).run();
      }
      this.enchantments
          .build()
          .forEach(((enchantment, integer) -> meta.addEnchant(enchantment, integer, true)));
    }
    return meta;
  }

  public static class EnchantmentsBuilder extends MapBuilder<Enchantment, Integer> {

    @NonNull @Getter private final ItemMetaBuilder meta;

    public EnchantmentsBuilder(@NonNull ItemMetaBuilder meta) {
      super(new HashMap<>());
      this.meta = meta;
    }
  }
}
