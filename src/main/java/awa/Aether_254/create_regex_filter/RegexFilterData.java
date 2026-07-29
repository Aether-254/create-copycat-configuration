package awa.Aether_254.create_regex_filter;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public record RegexFilterData(boolean tagMode, String namespacePattern, String pathPattern) {
    public static RegexFilterData read(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return new RegexFilterData(tag.getBoolean("RegexTagMode"),
            tag.contains("RegexNamespace") ? tag.getString("RegexNamespace") : ".*",
            tag.contains("RegexPath") ? tag.getString("RegexPath") : ".*");
    }

    public static void write(ItemStack stack, RegexFilterData data) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putBoolean("RegexTagMode", data.tagMode);
            tag.putString("RegexNamespace", limit(data.namespacePattern));
            tag.putString("RegexPath", limit(data.pathPattern));
        });
    }

    private static String limit(String value) {
        if (value == null || value.isBlank())
            return ".*";
        return value.substring(0, Math.min(128, value.length()));
    }
}
