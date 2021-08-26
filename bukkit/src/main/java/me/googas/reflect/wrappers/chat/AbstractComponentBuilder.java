package me.googas.reflect.wrappers.chat;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.NonNull;
import me.googas.io.context.Json;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedField;
import me.googas.starbox.Starbox;
import me.googas.starbox.builders.Builder;
import me.googas.starbox.utility.Versions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/** This helps to build {@link BaseComponent} in every 'Bukkit' version. */
public class AbstractComponentBuilder implements Builder<BaseComponent[]> {

  @NonNull
  private static final WrappedClass<ComponentSerializer> COMPONENT_SERIALIZER =
      WrappedClass.of(ComponentSerializer.class);

  @NonNull
  private static final WrappedField<Gson> GSON_FIELD =
      AbstractComponentBuilder.COMPONENT_SERIALIZER.getDeclaredField(Gson.class, "gson");

  @NonNull
  private static final Json JSON =
      new Json(
          AbstractComponentBuilder.GSON_FIELD
              .get(null)
              .handle(Starbox::severe)
              .provide()
              .orElseThrow(NullPointerException::new));

  @NonNull private final List<BaseComponent> components;
  private int cursor;
  private BaseComponent dummy;

  /**
   * Create the builder.
   *
   * @param components some initial components
   */
  public AbstractComponentBuilder(@NonNull Collection<BaseComponent> components) {
    this.components = new ArrayList<>(components);
    this.resetCursor();
  }

  /**
   * Create the builder.
   *
   * @param components some initial components
   */
  public AbstractComponentBuilder(@NonNull BaseComponent... components) {
    this(Arrays.asList(components));
  }

  /** Create the builder. */
  public AbstractComponentBuilder() {
    this(new ArrayList<>());
  }

  /**
   * Append a component.
   *
   * @param component the component to append
   * @param retention the retention of format to copy from previous component
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder append(
      @NonNull BaseComponent component, @NonNull ComponentBuilder.FormatRetention retention) {
    BaseComponent previous = components.isEmpty() ? null : components.get(components.size() - 1);
    if (previous == null) {
      previous = this.dummy;
      this.dummy = null;
    }
    if (Versions.BUKKIT > 11) {
      if (previous != null) {
        component.copyFormatting(previous, retention, false);
      }
    } else {
      List<BaseComponent> extra = component.getExtra();
      if (previous instanceof TextComponent
          && component instanceof TextComponent
          && (retention == ComponentBuilder.FormatRetention.FORMATTING
              || retention == ComponentBuilder.FormatRetention.ALL)) {
        String text = ((TextComponent) component).getText();
        component = new TextComponent((TextComponent) previous);
        ((TextComponent) component).setText(text);
      }
      switch (retention) {
        case EVENTS:
        case ALL:
          if (previous != null) {
            component.setInsertion(previous.getInsertion());
            component.setClickEvent(previous.getClickEvent());
            component.setHoverEvent(previous.getHoverEvent());
          }
          break;
        case FORMATTING:
          component.setClickEvent(null);
          component.setHoverEvent(null);
          break;
      }
      component.setExtra(extra);
    }
    this.components.add(component);
    this.resetCursor();
    return this;
  }

  /**
   * Add a hover event to the current component.
   *
   * @param wrapper the wrapper of the hover event to add
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder event(@NonNull WrappedHoverEvent wrapper) {
    this.getCurrent().setHoverEvent(wrapper.getEvent());
    return this;
  }

  private AbstractComponentBuilder resetCursor() {
    this.cursor = components.isEmpty() ? -1 : components.size() - 1;
    return this;
  }

  /**
   * Set the color of the current component.
   *
   * @param color the color to set the section
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder color(@NonNull ChatColor color) {
    this.getCurrent().setColor(color);
    return this;
  }

  /**
   * Add a click event to the current component.
   *
   * @param event the event to add
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder event(@NonNull ClickEvent event) {
    this.getCurrent().setClickEvent(event);
    return this;
  }

  /**
   * Append text to the builder. This will create a {@link TextComponent} and append it
   *
   * @param text the text to append
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder append(@NonNull String text) {
    return this.append(text, ComponentBuilder.FormatRetention.ALL);
  }

  /**
   * Append text to the builder and adjust it to previous components using {@link
   * net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention}
   *
   * @param text the text to append
   * @param retention how the text should retain previous format
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder append(
      @NonNull String text, @NonNull ComponentBuilder.FormatRetention retention) {
    return this.append(new TextComponent(text), retention);
  }

  /**
   * Append many components with adjusting them to a retention.
   *
   * @param retention how the components should retain to previous formats
   * @param components the components to add
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder appendAll(
      @NonNull ComponentBuilder.FormatRetention retention, Collection<BaseComponent> components) {
    components.forEach(component -> this.append(component, retention));
    return this;
  }

  /**
   * Append many components with adjusting them to a retention.
   *
   * @param retention how the components should retain to previous formats
   * @param components the components to add
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder appendAll(
      @NonNull ComponentBuilder.FormatRetention retention, @NonNull BaseComponent... components) {
    return this.appendAll(retention, Arrays.asList(components));
  }

  /**
   * Set whether to obfuscate the current section.
   *
   * @param obfuscated whether to obfuscate
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder obfuscated(boolean obfuscated) {
    this.getCurrent().setObfuscated(obfuscated);
    return this;
  }

  /**
   * Set whether to strikethrough the current section.
   *
   * @param strikethrough whether to strikethrough
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder strikethrough(boolean strikethrough) {
    this.getCurrent().setStrikethrough(strikethrough);
    return this;
  }

  /**
   * Set whether to italic the current section.
   *
   * @param italic whether to italic
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder italic(boolean italic) {
    this.getCurrent().setItalic(italic);
    return this;
  }

  /**
   * Set whether to bold the current section.
   *
   * @param bold whether to bold
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder bold(boolean bold) {
    this.getCurrent().setBold(bold);
    return this;
  }

  /**
   * Set whether to underline the current section.
   *
   * @param underline whether to underline
   * @return this same instance
   */
  @NonNull
  public AbstractComponentBuilder underline(boolean underline) {
    this.getCurrent().setUnderlined(underline);
    return this;
  }

  /**
   * Set whether to reset the current section.
   *
   * @return this same instance
   */
  @NonNull
  @APIVersion(since = 12)
  public AbstractComponentBuilder reset() {
    this.retain(ComponentBuilder.FormatRetention.NONE);
    return this;
  }

  @NonNull
  @APIVersion(since = 12)
  private AbstractComponentBuilder retain(@NonNull ComponentBuilder.FormatRetention retention) {
    this.getCurrent().retain(retention);
    return this;
  }

  @NonNull
  private BaseComponent getCurrent() {
    return cursor < 0 ? this.getDummy() : this.components.get(cursor);
  }

  @NonNull
  private BaseComponent getDummy() {
    if (this.dummy == null) {
      this.dummy = new TextComponent();
    }
    return this.dummy;
  }

  @Override
  public BaseComponent @NonNull [] build() {
    BaseComponent[] arr = new BaseComponent[components.size()];
    for (int i = 0; i < components.size(); i++) {
      arr[i] = components.get(i);
    }
    return arr;
  }

  /**
   * This adapter deserializes and serializes {@link BaseComponent} using bungees gson.
   */
    public static class Adapter
        implements JsonDeserializer<BaseComponent>, JsonSerializer<BaseComponent> {
      @Override
      public JsonElement serialize(
              BaseComponent src, Type typeOfSrc, JsonSerializationContext context) {
        return AbstractComponentBuilder.JSON.getGson().toJsonTree(src, typeOfSrc);
      }

      @Override
      public BaseComponent deserialize(
          JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        return AbstractComponentBuilder.JSON.getGson().fromJson(json, typeOfT);
      }
    }
}
