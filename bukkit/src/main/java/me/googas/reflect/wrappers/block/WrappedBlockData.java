package me.googas.reflect.wrappers.block;

import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.reflect.wrappers.WrappedReturnMethod;
import org.bukkit.Material;

@APIVersion(14)
public class WrappedBlockData {

  @NonNull
  private static final WrappedClass BLOCK_DATA =
      WrappedClass.forName("org.bukkit.block.data.BlockData");

  @NonNull
  private static final WrappedReturnMethod<Material> GET_MATERIAL =
      WrappedBlockData.BLOCK_DATA.getMethod(Material.class, "getMaterial");

  @NonNull
  private static final WrappedReturnMethod<String> GET_STRING =
      WrappedBlockData.BLOCK_DATA.getMethod(String.class, "getAsString");

  @NonNull
  private static final WrappedReturnMethod<String> GET_STRING_BOL =
      WrappedBlockData.BLOCK_DATA.getMethod(String.class, "getAsString", boolean.class);

  @NonNull
  private static final WrappedMethod MERGE =
      WrappedBlockData.BLOCK_DATA.getMethod(
          WrappedBlockData.BLOCK_DATA.getClazz(), "merge", WrappedBlockData.BLOCK_DATA.getClazz());

  @NonNull
  private static final WrappedMethod MATCHES = WrappedBlockData.BLOCK_DATA.getMethod("matches");

  @NonNull @Getter private final Object blockData;

  public WrappedBlockData(@NonNull Object blockData) {
    this.blockData = blockData;
  }

  @NonNull
  public Optional<String> getAsString(boolean b) {
    return WrappedBlockData.GET_STRING_BOL.invoke(this.blockData, b);
  }

  public Object merge(@NonNull Object blockData) {
    return WrappedBlockData.MERGE.invoke(this.blockData, blockData);
  }

  public boolean matches(Object blockData) {
    if (blockData == null) return false;
    Object invoke = WrappedBlockData.MATCHES.invoke(this.blockData, blockData);
    if (invoke instanceof Boolean) {
      return (boolean) invoke;
    }
    return false;
  }

  @NonNull
  public Optional<Material> getMaterial() {
    return WrappedBlockData.GET_MATERIAL.invoke(this.blockData);
  }

  @NonNull
  public Optional<String> getAsString() {
    return WrappedBlockData.GET_STRING.invoke(this.blockData);
  }
}
