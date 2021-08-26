package me.googas.starbox.utility.items.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

/** Builds {@link FireworkEffectMeta}. */
public class FireworkMetaBuilder extends ItemMetaBuilder {

  @NonNull @Getter private List<FireworkEffect.Builder> effects = new ArrayList<>();
  private int power = 0;

  /**
   * Create the builder.
   *
   * @param other the item builder to which this meta will be built
   */
  public FireworkMetaBuilder(@NonNull ItemMetaBuilder other) {
    super(other);
  }

  /** Create the builder. */
  public FireworkMetaBuilder() {}

  /**
   * Add an effect to the firework.
   *
   * @param effect the effect to add
   * @return this same instance
   */
  @NonNull
  public FireworkMetaBuilder addEffect(@NonNull FireworkEffect.Builder effect) {
    effects.add(effect);
    return this;
  }

  /**
   * Add many effects to the firework.
   *
   * @param effects the collection of effects to add in the firework
   * @return this same instance
   */
  @NonNull
  public FireworkMetaBuilder addEffects(
      @NonNull Collection<? extends FireworkEffect.Builder> effects) {
    this.effects.addAll(effects);
    return this;
  }

  /**
   * Add many effects to the firework.
   *
   * @param effects the array of effects to add in the firework
   * @return this same instance
   */
  @NonNull
  public FireworkMetaBuilder addEffects(@NonNull FireworkEffect.Builder... effects) {
    return this.addEffects(Arrays.asList(effects));
  }

  /**
   * Set the effects of the firework.
   *
   * @param effects the new effects of the firework
   * @return this same instance
   */
  @NonNull
  public FireworkMetaBuilder setEffects(@NonNull List<FireworkEffect.Builder> effects) {
    this.effects = effects;
    return this;
  }

  /**
   * Set the power of the firework.
   *
   * @param power the new power of the firework
   * @return this same instance
   */
  @NonNull
  public FireworkMetaBuilder setPower(int power) {
    this.power = power;
    return this;
  }

  @Override
  public FireworkMeta build(@NonNull ItemStack stack) {
    ItemMeta itemMeta = super.build(stack);
    if (itemMeta instanceof FireworkMeta) {
      FireworkMeta firework = (FireworkMeta) itemMeta;
      firework.setPower(this.power);
      this.effects.stream().map(FireworkEffect.Builder::build).forEach(firework::addEffect);
      return firework;
    }
    return null;
  }
}
