package awa.Aether_254.create_regex_filter;

import awa.Aether_254.create_regex_filter.client.RegexFilterClient;
import awa.Aether_254.create_regex_filter.client.RegexFilterConfigScreen;
import awa.Aether_254.create_regex_filter.network.RegexFilterPayload;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(CreateRegexFilter.MOD_ID)
public final class CreateRegexFilter {
    public static final String MOD_ID = "create_regex_filter";

    public CreateRegexFilter(IEventBus modBus, ModContainer container) {
        RegexFilterConfig.load();
        RegexFilterItems.ITEMS.register(modBus);
        modBus.addListener(this::creativeTabs);
        modBus.addListener(this::registerPayloads);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            RegexFilterConfigScreen.register(container);
            RegexFilterClient.register();
        }
    }

    private void creativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
            event.accept(RegexFilterItems.REGEX_FILTER.get());
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(RegexFilterPayload.TYPE, RegexFilterPayload.STREAM_CODEC,
            RegexFilterPayload::handle);
    }
}
