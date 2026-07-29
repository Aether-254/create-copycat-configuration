package awa.Aether_254.create_copycat_configuration.client;

import awa.Aether_254.create_copycat_configuration.CopycatModConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public final class CopycatModConfigScreen {
    private CopycatModConfigScreen() {
    }

    public static void register(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (mod, parent) -> create(parent));
    }

    private static Screen create(Screen parent) {
        CopycatModConfig.Data config = CopycatModConfig.get();
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent)
            .setTitle(Component.literal("Create: Copycat Configuration"));
        ConfigCategory category = builder.getOrCreateCategory(Component.literal("Defaults"));
        ConfigEntryBuilder entries = builder.entryBuilder();
        category.addEntry(entries.startBooleanToggle(Component.literal("Enabled"), config.enabled)
            .setDefaultValue(true).setSaveConsumer(value -> config.enabled = value).build());
        category.addEntry(entries.startBooleanToggle(Component.literal("Default collision"),
                config.defaultCollision).setDefaultValue(true)
            .setSaveConsumer(value -> config.defaultCollision = value).build());
        category.addEntry(entries.startBooleanToggle(Component.literal("Default light occlusion"),
                config.defaultLightOcclusion).setDefaultValue(true)
            .setSaveConsumer(value -> config.defaultLightOcclusion = value).build());
        category.addEntry(entries.startIntField(Component.literal("Default brightness"),
                config.defaultBrightness).setDefaultValue(0).setMin(0).setMax(15)
            .setSaveConsumer(value -> config.defaultBrightness = value).build());
        builder.setSavingRunnable(CopycatModConfig::save);
        return builder.build();
    }
}
