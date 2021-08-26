package me.googas.starbox.utility.items.meta;

import java.util.function.Consumer;
import lombok.NonNull;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

/** Builds {@link FireworkEffectMeta}. */
public class FireworkEffectMetaBuilder extends ItemMetaBuilder {

  @NonNull private FireworkEffect.Builder effect = FireworkEffect.builder();

  /**
   * Create the builder.
   *
   * @param builder the item builder to which this meta will be built
   */
  public FireworkEffectMetaBuilder(@NonNull ItemMetaBuilder builder) {
    super(builder);
  }

  /** Create the builder. */
  public FireworkEffectMetaBuilder() {}

  /**
   * Edit the {@link FireworkEffect.Builder} with a consumer.
   *
   * @param consumer the consumer to edit the {@link FireworkEffect.Builder}
   * @return this same instance
   */
  @NonNull
  public FireworkEffectMetaBuilder withEffect(@NonNull Consumer<FireworkEffect.Builder> consumer) {
    consumer.accept(effect);
    return this;
  }

  /**
   * Set the effect.
   *
   * @param effect the new effect to set
   * @return this same instance
   */
  @NonNull
  public FireworkEffectMetaBuilder setEffect(@NonNull FireworkEffect.Builder effect) {
    this.effect = effect;
    return this;
  }

  @Override
  public FireworkEffectMeta build(@NonNull ItemStack stack) {
    ItemMeta itemMeta = super.build(stack);
    if (itemMeta instanceof FireworkEffectMeta) {
      FireworkEffectMeta firework = (FireworkEffectMeta) itemMeta;
      firework.setEffect(effect.build());
      return firework;
    }
    return null;
  }
}
