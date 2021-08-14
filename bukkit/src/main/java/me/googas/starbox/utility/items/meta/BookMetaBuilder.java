package me.googas.starbox.utility.items.meta;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.wrappers.book.WrappedBookMetaGeneration;
import me.googas.starbox.utility.Versions;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

/** Builds {@link BookMeta}. */
public class BookMetaBuilder extends ItemMetaBuilder {

  @NonNull @Getter private final List<String> pages = new ArrayList<>();
  @NonNull @Getter private String author = "Unknown";

  @Getter private WrappedBookMetaGeneration wrappedGeneration = null;

  /**
   * Create the builder.
   *
   * @param itemBuilder the item builder to which this meta will be built
   */
  public BookMetaBuilder(@NonNull ItemBuilder itemBuilder) {
    super(itemBuilder);
  }

  /** Create the builder. */
  public BookMetaBuilder() {
    super();
  }

  /**
   * Create the builder.
   *
   * @param other another meta builder to copy its values
   */
  public BookMetaBuilder(@NonNull ItemMetaBuilder other) {
    super(other);
  }

  /**
   * Set the author.
   *
   * @param author the author of the book
   * @return this same instance
   */
  @NonNull
  public BookMetaBuilder setAuthor(String author) {
    this.author = author;
    return this;
  }

  /**
   * Set the generation of the book.
   *
   * @param wrappedGeneration the generation of the book
   * @return this same instance
   */
  @NonNull
  public BookMetaBuilder setWrappedGeneration(WrappedBookMetaGeneration wrappedGeneration) {
    this.wrappedGeneration = wrappedGeneration;
    return this;
  }

  @Override
  public BookMeta build(@NonNull ItemStack stack) {
    ItemMeta itemMeta = super.build(stack);
    if (itemMeta instanceof BookMeta) {
      BookMeta bookMeta = (BookMeta) itemMeta;
      bookMeta.setAuthor(this.author);
      bookMeta.setPages(this.pages);
      if (Versions.BUKKIT >= 9 && wrappedGeneration != null) {
        bookMeta.setGeneration(wrappedGeneration.getGeneration());
      }
      return bookMeta;
    }
    return null;
  }
}
