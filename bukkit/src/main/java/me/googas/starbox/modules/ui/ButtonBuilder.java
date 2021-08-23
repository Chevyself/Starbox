package me.googas.starbox.modules.ui;

import java.util.function.Consumer;
import lombok.NonNull;
import me.googas.starbox.builders.Builder;
import me.googas.starbox.modules.ui.buttons.StarboxButton;
import me.googas.starbox.utility.items.ItemBuilder;

/** Builder for {@link Button}. Creates {@link StarboxButton}. */
public class ButtonBuilder implements Builder<Button> {

  @NonNull private ItemBuilder item;
  @NonNull private transient ButtonListener listener;

  ButtonBuilder(@NonNull ItemBuilder item) {
    this.item = item;
    this.listener = event -> event.setCancelled(true);
  }

  ButtonBuilder() {
    this(new ItemBuilder());
  }

  /**
   * Set the button listener.
   *
   * @param listener the new listener
   * @return this same instance
   */
  @NonNull
  public ButtonBuilder listen(@NonNull ButtonListener listener) {
    this.listener = listener;
    return this;
  }

  /**
   * Set the item of the builder.
   *
   * @param item the new item of the builder
   * @return this same instance
   */
  @NonNull
  public ButtonBuilder setItem(ItemBuilder item) {
    this.item = item;
    return this;
  }

  /**
   * Use a {@link Consumer} to edit the {@link ItemBuilder}.
   *
   * @param consumer the consumer to edit the builder
   * @return this same instance
   */
  @NonNull
  public ButtonBuilder withItem(@NonNull Consumer<ItemBuilder> consumer) {
    consumer.accept(this.item);
    return this;
  }

  @Override
  public @NonNull Button build() {
    return new StarboxButton(item.build(), listener);
  }
}
