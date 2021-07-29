package me.googas.starbox.utility.items.meta;

import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockStateMetaBuilder extends ItemMetaBuilder {

  @Getter private BlockState state;

  public BlockStateMetaBuilder(@NonNull ItemBuilder itemBuilder) {
    super(itemBuilder);
  }

  @NonNull
  public BlockStateMetaBuilder setState(BlockState state) {
    this.state = state;
    return this;
  }

  @Override
  public BlockStateMeta build(@NonNull ItemStack stack) {
    ItemMeta itemMeta = super.build(stack);
    BlockStateMeta meta = null;
    if (itemMeta instanceof BlockStateMeta) {
      meta = (BlockStateMeta) itemMeta;
      if (this.state != null) meta.setBlockState(this.state);
    }
    return meta;
  }
}
