package me.googas.starbox.utility.items.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.utility.Versions;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

/** Builds {@link BannerMeta}. */
public class BannerMetaBuilder extends ItemMetaBuilder {

  @NonNull
  private static final WrappedClass<BannerMeta> BANNER_META = WrappedClass.of(BannerMeta.class);

  @NonNull
  private static final WrappedMethod<?> SET_BASE_COLOR =
      BannerMetaBuilder.BANNER_META.getMethod("setBaseColor", DyeColor.class);

  @NonNull
  @Getter
  @APIVersion(since = 8, max = 11)
  private final List<Pattern> patterns = new ArrayList<>();

  @NonNull @Getter private DyeColor baseColor = DyeColor.WHITE;

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
   * Add a {@link Pattern} to the banner.
   *
   * @param pattern the pattern to add
   * @return this same instance
   */
  @NonNull
  public BannerMetaBuilder addPattern(@NonNull Pattern pattern) {
    this.patterns.add(pattern);
    return this;
  }

  /**
   * Add many {@link Pattern} to the banner.
   *
   * @param patterns the collection of patterns to add
   * @return this same instance
   */
  @NonNull
  public BannerMetaBuilder addPatterns(@NonNull Collection<? extends Pattern> patterns) {
    this.patterns.addAll(patterns);
    return this;
  }

  /**
   * Add many {@link Pattern} to the banner.
   *
   * @param patterns the collection array of patterns to add
   * @return this same instance
   */
  @NonNull
  public BannerMetaBuilder addPatterns(@NonNull Pattern... patterns) {
    return this.addPatterns(Arrays.asList(patterns));
  }

  /**
   * Set the base color of the banner.
   *
   * @param baseColor the new base color of the banner
   * @return this same instance
   */
  @APIVersion(since = 8, max = 11)
  @NonNull
  public BannerMetaBuilder setBaseColor(@NonNull DyeColor baseColor) {
    this.baseColor = baseColor;
    return this;
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
      if (Versions.BUKKIT <= 11) {
        BannerMetaBuilder.SET_BASE_COLOR.prepare(banner, this.baseColor).run();
      }
      return banner;
    }
    return null;
  }
}
