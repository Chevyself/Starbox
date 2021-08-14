package me.googas.starbox.utility.items.meta;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

/** Builds {@link BannerMeta}. */
public class BannerMetaBuilder extends ItemMetaBuilder {

  @NonNull @Delegate private final List<Pattern> patterns = new ArrayList<>();

  /**
   * Create the builder.
   *
   * @param itemBuilder the item builder to which this meta will be built
   */
  public BannerMetaBuilder(@NonNull ItemBuilder itemBuilder) {
    super(itemBuilder);
  }

  /** Create the builder. */
  public BannerMetaBuilder() {
    super();
  }

  /**
   * Create the builder.
   *
   * @param other another meta builder to copy its values
   */
  public BannerMetaBuilder(@NonNull ItemMetaBuilder other) {
    super(other);
  }

  @Override
  public BannerMeta build(@NonNull ItemStack stack) {
    ItemMeta itemMeta = super.build(stack);
    if (itemMeta instanceof BannerMeta) {
      BannerMeta banner = (BannerMeta) itemMeta;
      banner.setPatterns(this.patterns);
      return banner;
    }
    return null;
  }
}
