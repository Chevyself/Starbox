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

  @Override
  @NonNull
  public BannerMeta build(@NonNull ItemStack stack) {
    ItemMeta itemMeta = super.build(stack);
    BannerMeta meta = null;
    if (itemMeta instanceof BannerMeta) {
      meta = (BannerMeta) itemMeta;
      meta.setPatterns(this.patterns);
    }
    return meta;
  }
}
