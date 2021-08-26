package me.googas.starbox.utility.items.meta;

import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.utility.items.ItemBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/** Builds {@link EnchantmentStorageMeta}. */
public class EnchantmentStorageMetaBuilder extends ItemMetaBuilder {

    @NonNull @Getter
    private final Map<Enchantment, Integer> storage = new HashMap<>();

    /**
     * Create the builder.
     *
     * @param builder the item builder to which this meta will be built
     */
    public EnchantmentStorageMetaBuilder(@NonNull ItemMetaBuilder builder) {
        super(builder);
    }

    /**
     * Create the builder
     */
    public EnchantmentStorageMetaBuilder() {
    }

    @Override
    public @NonNull EnchantmentStorageMetaBuilder addEnchantment(@NonNull Enchantment enchantment, int level) {
        storage.put(enchantment, level);
        return this;
    }

    @Override
    public EnchantmentStorageMeta build(@NonNull ItemStack stack) {
        ItemMeta itemMeta = super.build(stack);
        if (itemMeta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) itemMeta;
            storage.forEach(((enchantment, level) -> storageMeta.addStoredEnchant(enchantment, level, true)));
            return storageMeta;
        }
        return null;
    }

}
