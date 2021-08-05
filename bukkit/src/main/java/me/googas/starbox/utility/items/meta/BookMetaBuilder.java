package me.googas.starbox.utility.items.meta;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.reflect.wrappers.book.WrappedBookMetaGeneration;
import me.googas.starbox.utility.Versions;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

/** Builds {@link BookMeta}. */
public class BookMetaBuilder extends ItemMetaBuilder {

  @NonNull
  private static final WrappedClass BOOK_META =
      WrappedClass.forName("org.bukkit.inventory.meta.BookMeta");

  @NonNull
  private static final WrappedMethod<?> SET_GENERATION =
      BookMetaBuilder.BOOK_META.getMethod(
          "setGeneration", WrappedBookMetaGeneration.GENERATION.getClazz());

  @NonNull @Getter private final List<String> pages = new ArrayList<>();
  @NonNull @Getter private String author = "Unknown";

  @NonNull @Getter
  private WrappedBookMetaGeneration wrappedGeneration = WrappedBookMetaGeneration.ORIGINAL;

  /**
   * Create the builder.
   *
   * @param itemBuilder the item builder to which this meta will be built
   */
  public BookMetaBuilder(@NonNull ItemBuilder itemBuilder) {
    super(itemBuilder);
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
    BookMeta bookMeta = null;
    if (itemMeta instanceof BookMeta) {
      bookMeta = (BookMeta) itemMeta;
      bookMeta.setAuthor(this.author);
      bookMeta.setPages(this.pages);
      if (Versions.BUKKIT >= 12) {
        BookMetaBuilder.SET_GENERATION
            .prepare(bookMeta, this.wrappedGeneration.toGeneration())
            .run();
      }
    }
    return bookMeta;
  }
}
