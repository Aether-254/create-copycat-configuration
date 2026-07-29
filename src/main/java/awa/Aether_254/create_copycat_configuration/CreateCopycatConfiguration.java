package awa.Aether_254.create_copycat_configuration;

import awa.Aether_254.create_copycat_configuration.client.CopycatConfigClient;
import awa.Aether_254.create_copycat_configuration.client.CopycatModConfigScreen;
import awa.Aether_254.create_copycat_configuration.network.CopycatSettingsPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(CreateCopycatConfiguration.MOD_ID)
public final class CreateCopycatConfiguration {
    public static final String MOD_ID = "create_copycat_configuration";

    public CreateCopycatConfiguration(IEventBus modBus, ModContainer container) {
        CopycatModConfig.load();
        modBus.addListener(this::registerPayloads);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            CopycatModConfigScreen.register(container);
            CopycatConfigClient.register();
        }
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(CopycatSettingsPayload.TYPE, CopycatSettingsPayload.STREAM_CODEC,
            CopycatSettingsPayload::handle);
    }
}
