package me.googas.starbox;

import lombok.NonNull;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a {@link Language} file to have as a resource and get objects based on a desired language. Even if
 * utilities such as the {@link java.util.ResourceBundle} exists, this class is something more appropriate for the scope
 * of Starbox.
 */
public interface Language {

    /**
     * Get a locale from a string such as 'en_US'.
     *
     * @param locale the string to get the locale from
     * @return the locale
     */
    @NonNull
    static Locale getLocale(@NonNull String locale) {
        String[] split = locale.split("_", 3);
        switch (split.length) {
            case 1:
                return new Locale(split[0]);
            case 2:
                return new Locale(split[0], split[1]);
            case 3:
                return new Locale(split[0], split[1], split[2]);
            default:
                throw new IllegalArgumentException(locale + " does not match a locale");
        }
    }

    /**
     * Get a raw object for the provided key. A raw object can be null but it must be handled by the get methods.
     *
     * @param key the key to get the raw object
     * @return the raw object
     */
    @NonNull
    Optional<?> getRaw(@NonNull String key);

    /**
     * Get an object for the provided key.
     *
     * @param key the key to get the object
     * @return the object
     */
    @NonNull
    Object get(@NonNull String key);

    /**
     * Get the object and have a map for formatting.
     *
     * @param key the key to get the object
     * @param map the map to format the object
     * @return the object
     */
    @NonNull
    Object get(@NonNull String key, @NonNull Map<String, String> map);

    /**
     * Get the object and have other objects to format it.
     *
     * @param key the key to get the object
     * @param objects the map to format the object
     * @return the object
     */
    @NonNull
    Object get(@NonNull String key, Object... objects);

    /**
     * Get the locale to which objects will be based to.
     *
     * @return the locale
     */
    @NonNull
    Locale getLocale();
}
