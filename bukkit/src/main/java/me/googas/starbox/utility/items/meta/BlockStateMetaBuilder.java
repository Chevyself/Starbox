package me.googas.starbox.utility.items.meta;

import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

/** Builds {@link BlockStateMeta}. */
public class BlockStateMetaBuilder extends ItemMetaBuilder {

  @Getter private BlockState state;

  /**
   * Create the builder.
   *
   * @param itemBuilder the item builder to which this meta will be built
   */
  public BlockStateMetaBuilder(@NonNull ItemBuilder itemBuilder) {
    super(itemBuilder);
  }

  /**
   * Create the builder.
   *
   * @param other another meta builder to copy its values
   */
  public BlockStateMetaBuilder(@NonNull ItemMetaBuilder other) {
    super(other);
  }

  /**
   * Set the state of the meta.
   *
   * @param state the new value to set
   * @return this same instance
   */
  @NonNull
  public BlockStateMetaBuilder setState(BlockState state) {
    this.state = state;
    return this;
  }

  @Override
  public BlockStateMeta build(@NonNull ItemStack stack) {
    ItemMeta itemMeta = super.build(stack);
    if (itemMeta instanceof BlockStateMeta) {
      BlockStateMeta meta = (BlockStateMeta) itemMeta;
      if (this.state != null) meta.setBlockState(this.state);
      return meta;
    }
    return null;
  }
}
