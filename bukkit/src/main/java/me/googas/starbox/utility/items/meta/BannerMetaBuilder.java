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

public class BannerMetaBuilder extends ItemMetaBuilder {

  @NonNull @Delegate private final List<Pattern> patterns = new ArrayList<>();

  public BannerMetaBuilder(@NonNull ItemBuilder itemBuilder) {
    super(itemBuilder);
  }

  @Override
  public BannerMeta build(@NonNull ItemStack stack) {
    ItemMeta itemMeta = super.build(stack);
    if (!(itemMeta instanceof BannerMeta)) return null;
    BannerMeta meta = (BannerMeta) itemMeta;
    if (meta == null) return null;
    meta.setPatterns(this.patterns);
    return meta;
  }
}
