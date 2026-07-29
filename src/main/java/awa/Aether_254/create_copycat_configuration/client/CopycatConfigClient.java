package awa.Aether_254.create_copycat_configuration.client;

import awa.Aether_254.create_copycat_configuration.CopycatSettingsData;
import awa.Aether_254.create_copycat_configuration.CopycatSettingsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class CopycatConfigClient {
    private CopycatConfigClient() {
    }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(CopycatConfigClient::onUse);
    }

    private static void onUse(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isUseItem() || event.getHand() != InteractionHand.MAIN_HAND)
            return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null
            || !(minecraft.hitResult instanceof BlockHitResult hit))
            return;
        if (!BuiltInRegistries.ITEM.getKey(minecraft.player.getMainHandItem().getItem())
            .toString().equals("create:wrench"))
            return;
        var state = minecraft.level.getBlockState(hit.getBlockPos());
        if (!CopycatSettingsHelper.isCopycat(state.getBlock()))
            return;
        BlockEntity blockEntity = minecraft.level.getBlockEntity(hit.getBlockPos());
        if (!(blockEntity instanceof CopycatSettingsData data))
            return;
        minecraft.setScreen(new CopycatSettingsScreen(hit.getBlockPos(), data));
        event.setSwingHand(false);
        event.setCanceled(true);
    }
}
