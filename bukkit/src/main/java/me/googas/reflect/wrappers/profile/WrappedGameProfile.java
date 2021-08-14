package me.googas.reflect.wrappers.profile;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.reflect.wrappers.properties.WrappedPropertyMap;
import me.googas.starbox.Starbox;

/** Wraps the 'com.mojang.authlib.GameProfile' class. */
public class WrappedGameProfile extends StarboxWrapper<Object> {

  @NonNull
  private static final WrappedClass GAME_PROFILE =
      WrappedClass.forName("com.mojang.authlib.GameProfile");

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR =
      WrappedGameProfile.GAME_PROFILE.getConstructor(UUID.class, String.class);

  @NonNull
  private static final WrappedMethod<UUID> GET_ID =
      WrappedGameProfile.GAME_PROFILE.getMethod(UUID.class, "getId");

  @NonNull
  private static final WrappedMethod<String> GET_NAME =
      WrappedGameProfile.GAME_PROFILE.getMethod(String.class, "getName");

  @NonNull
  private static final WrappedMethod<?> GET_PROPERTIES =
      WrappedGameProfile.GAME_PROFILE.getMethod("getProperties");

  /**
   * Create the wrap.
   *
   * @param reference the referenced GameProfile.
   */
  public WrappedGameProfile(@NonNull Object reference) {
    super(reference);
    if (!reference.getClass().equals(WrappedGameProfile.GAME_PROFILE.getClazz())) {
      throw new IllegalArgumentException("Expected a GameProfile received a " + reference);
    }
  }

  /**
   * Create a GameProfile and wrap it.
   *
   * @param uuid the uuid of the profile
   * @param name the name of the profile
   * @return The wrapped profile
   */
  @NonNull
  public static WrappedGameProfile construct(@NonNull UUID uuid, String name) {
    Object object =
        WrappedGameProfile.CONSTRUCTOR
            .invoke(uuid, name)
            .handle(Starbox::severe)
            .provide()
            .orElseThrow(() -> new IllegalStateException("GameProfile could not be created"));
    return new WrappedGameProfile(object);
  }

  /**
   * Get the id of the profile.
   *
   * @return the id
   */
  @NonNull
  public UUID getId() {
    return WrappedGameProfile.GET_ID
        .prepare(this.get())
        .provide()
        .orElseThrow(() -> new IllegalStateException("Could not prepare #getId"));
  }

  /**
   * Get the name of the profile.
   *
   * @return the name
   */
  @NonNull
  public Optional<String> getName() {
    return WrappedGameProfile.GET_NAME.prepare(this.get()).provide();
  }

  /**
   * Get the property map of the profile.
   *
   * @return the property map
   */
  @NonNull
  public WrappedPropertyMap getProperties() {
    Object object =
        WrappedGameProfile.GET_PROPERTIES
            .invoke(this.get().orElseThrow(IllegalStateException::new))
            .handle(Throwable::printStackTrace)
            .provide()
            .orElseThrow(() -> new IllegalStateException("Could not prepare #getProperties"));
    return new WrappedPropertyMap(object);
  }
}
