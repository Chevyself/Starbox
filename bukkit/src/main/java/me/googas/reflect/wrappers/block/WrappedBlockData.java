package me.googas.reflect.wrappers.block;

import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

@APIVersion(since = 14)
@Deprecated
public class WrappedBlockData {

  @NonNull
  private static final WrappedClass<BlockData> BLOCK_DATA = WrappedClass.of(BlockData.class);

  @NonNull
  private static final WrappedMethod<Material> GET_MATERIAL =
      WrappedBlockData.BLOCK_DATA.getMethod(Material.class, "getMaterial");

  @NonNull
  private static final WrappedMethod<String> GET_STRING =
      WrappedBlockData.BLOCK_DATA.getMethod(String.class, "getAsString");

  @NonNull
  private static final WrappedMethod<String> GET_STRING_BOL =
      WrappedBlockData.BLOCK_DATA.getMethod(String.class, "getAsString", boolean.class);

  @NonNull
  private static final WrappedMethod<?> MERGE =
      WrappedBlockData.BLOCK_DATA.getMethod(
          WrappedBlockData.BLOCK_DATA.getClazz(), "merge", WrappedBlockData.BLOCK_DATA.getClazz());

  @NonNull
  private static final WrappedMethod<Boolean> MATCHES =
      WrappedBlockData.BLOCK_DATA.getMethod(Boolean.class, "matches");

  @NonNull @Getter private final Object blockData;

  public WrappedBlockData(@NonNull Object blockData) {
    this.blockData = blockData;
  }

  @NonNull
  public Optional<String> getAsString(boolean b) {
    return WrappedBlockData.GET_STRING_BOL.prepare(this.blockData, b).provide();
  }

  public Object merge(@NonNull Object blockData) {
    return WrappedBlockData.MERGE.prepare(this.blockData, blockData).provide().orElse(null);
  }

  public boolean matches(Object blockData) {
    if (blockData == null) return false;
    return WrappedBlockData.MATCHES.prepare(this.blockData, blockData).provide().orElse(false);
  }

  @NonNull
  public Optional<Material> getMaterial() {
    return WrappedBlockData.GET_MATERIAL.prepare(this.blockData).provide();
  }

  @NonNull
  public Optional<String> getAsString() {
    return WrappedBlockData.GET_STRING.prepare(this.blockData).provide();
  }
}
