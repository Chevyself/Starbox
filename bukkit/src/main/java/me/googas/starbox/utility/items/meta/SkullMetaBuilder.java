package me.googas.starbox.utility.items.meta;

import java.util.UUID;
import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.reflect.wrappers.profile.WrappedGameProfile;
import me.googas.reflect.wrappers.properties.WrappedProperty;
import me.googas.starbox.Starbox;
import me.googas.starbox.utility.Versions;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/** Builds {@link SkullMeta}. */
public class SkullMetaBuilder extends ItemMetaBuilder {

  @NonNull private static final WrappedClass SKULL_META = WrappedClass.of(SkullMeta.class);

  @NonNull
  @APIVersion(since = 8, max = 11)
  private static final WrappedMethod<?> SET_OWNER =
      SkullMetaBuilder.SKULL_META.getMethod("setOwner", String.class);

  private OfflinePlayer owner;
  private String skin;

  /**
   * Create the builder.
   *
   * @param itemBuilder the item to which the meta will be built
   */
  public SkullMetaBuilder(@NonNull ItemBuilder itemBuilder) {
    super(itemBuilder);
  }

  /** Create the builder. */
  public SkullMetaBuilder() {
    super();
  }

  /**
   * Create the builder.
   *
   * @param other another meta builder to copy its values
   */
  public SkullMetaBuilder(@NonNull ItemMetaBuilder other) {
    super(other);
  }

  private void appendSkin(@NonNull ItemStack stack, @NonNull SkullMeta meta) {
    if (this.skin != null) {
      if (Versions.BUKKIT <= 11) {
        ItemBuilder.SET_DURABILITY.invoke(stack, (short) 3).run();
      }
      WrappedGameProfile gameProfile = WrappedGameProfile.construct(UUID.randomUUID(), null);
      gameProfile.getProperties().put("textures", WrappedProperty.construct("textures", this.skin));
      WrappedClass.of(meta.getClass())
          .getDeclaredField("profile")
          .set(meta, gameProfile.get().orElseThrow(NullPointerException::new))
          .handle(Starbox::severe)
          .run();
    }
  }

  /**
   * Set the owner of the skull.
   *
   * @param owner the new owner of the skull
   * @return this same instance
   */
  @NonNull
  public SkullMetaBuilder setOwner(OfflinePlayer owner) {
    this.owner = owner;
    return this;
  }

  /**
   * Set the skin to use in this skull.
   *
   * @param skin the skin to use in the skull
   * @return this same instance
   */
  @NonNull
  public SkullMetaBuilder setSkin(String skin) {
    this.skin = skin;
    return this;
  }

  @Override
  public SkullMeta build(@NonNull ItemStack stack) {
    ItemMeta itemMeta = super.build(stack);
    if (itemMeta instanceof SkullMeta) {
      SkullMeta meta = (SkullMeta) itemMeta;
      if (this.owner != null) {
        if (Versions.BUKKIT > 11) {
          meta.setOwningPlayer(owner);
        } else {
          SkullMetaBuilder.SET_OWNER.invoke(meta, this.owner.getName()).run();
        }
      }
      this.appendSkin(stack, meta);
      return meta;
    }
    return null;
  }
}
