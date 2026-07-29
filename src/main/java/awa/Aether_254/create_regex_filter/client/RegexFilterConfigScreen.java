package awa.Aether_254.create_regex_filter.client;

import awa.Aether_254.create_regex_filter.RegexFilterConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public final class RegexFilterConfigScreen {
    private RegexFilterConfigScreen() {
    }

    public static void register(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (mod, parent) -> create(parent));
    }

    private static Screen create(Screen parent) {
        RegexFilterConfig.Data config = RegexFilterConfig.get();
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent)
            .setTitle(Component.literal("Create: Regex Filter"));
        ConfigCategory category = builder.getOrCreateCategory(Component.literal("Matching"));
        ConfigEntryBuilder entries = builder.entryBuilder();
        category.addEntry(entries.startBooleanToggle(Component.literal("Enabled"), config.enabled)
            .setDefaultValue(true).setSaveConsumer(value -> config.enabled = value).build());
        category.addEntry(entries.startBooleanToggle(Component.literal("Case sensitive"), config.caseSensitive)
            .setDefaultValue(true).setSaveConsumer(value -> config.caseSensitive = value).build());
        category.addEntry(entries.startBooleanToggle(Component.literal("Require full match"), config.fullMatch)
            .setDefaultValue(true).setSaveConsumer(value -> config.fullMatch = value).build());
        builder.setSavingRunnable(RegexFilterConfig::save);
        return builder.build();
    }
}
