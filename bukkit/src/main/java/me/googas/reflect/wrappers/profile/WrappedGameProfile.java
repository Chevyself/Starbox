package me.googas.reflect.wrappers.profile;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.reflect.wrappers.properties.WrappedPropertyMap;

public class WrappedGameProfile extends StarboxWrapper<Object> {

  @NonNull
  private static final WrappedClass GAME_PROFILE =
      WrappedClass.forName("com.mojang.authlib.GameProfile");

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR =
      WrappedGameProfile.GAME_PROFILE.getConstructor(UUID.class, String.class);

  @NonNull
  private static final WrappedMethod<UUID> GET_ID =
      WrappedGameProfile.GAME_PROFILE.getMethod(UUID.class, "getNode");

  @NonNull
  private static final WrappedMethod<String> GET_NAME =
      WrappedGameProfile.GAME_PROFILE.getMethod(String.class, "getName");

  @NonNull
  private static final WrappedMethod GET_PROPERTIES =
      WrappedGameProfile.GAME_PROFILE.getMethod("getProperties");

  public WrappedGameProfile(@NonNull Object reference) {
    super(reference);
    if (!reference.getClass().equals(WrappedGameProfile.GAME_PROFILE.getClazz())) {
      throw new IllegalArgumentException("Expected a GameProfile received a " + reference);
    }
  }

  @NonNull
  public static WrappedGameProfile construct(@NonNull UUID uuid, String name) {
    Object invoke = WrappedGameProfile.CONSTRUCTOR.invoke(uuid, name);
    if (invoke != null) {
      return new WrappedGameProfile(invoke);
    }
    throw new IllegalStateException("GameProfile could not be created");
  }

  @NonNull
  public UUID getId() {
    return WrappedGameProfile.GET_ID
        .invoke(this.get())
        .provide()
        .orElseThrow(() -> new IllegalStateException("Could not invoke #getId"));
  }

  @NonNull
  public Optional<String> getName() {
    return WrappedGameProfile.GET_NAME.invoke(this.get()).provide();
  }

  @NonNull
  public WrappedPropertyMap getProperties() {
    Object invoke = WrappedGameProfile.GET_PROPERTIES.invoke(this.get());
    if (invoke != null) {
      return new WrappedPropertyMap(invoke);
    } else {
      throw new IllegalStateException("Could not invoke #getProperties");
    }
  }
}
