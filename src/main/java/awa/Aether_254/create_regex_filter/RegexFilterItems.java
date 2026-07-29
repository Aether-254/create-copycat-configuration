package awa.Aether_254.create_regex_filter;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class RegexFilterItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateRegexFilter.MOD_ID);
    public static final DeferredItem<RegexFilterItem> REGEX_FILTER =
        ITEMS.register("regex_filter", () -> new RegexFilterItem(new Item.Properties().stacksTo(1)));

    private RegexFilterItems() {
    }
}
