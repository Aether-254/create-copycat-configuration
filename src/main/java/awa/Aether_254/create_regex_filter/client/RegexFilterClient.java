package awa.Aether_254.create_regex_filter.client;

import awa.Aether_254.create_regex_filter.RegexFilterItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class RegexFilterClient {
    private RegexFilterClient() {
    }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(RegexFilterClient::onUse);
    }

    private static void onUse(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isUseItem() || event.getHand() != InteractionHand.MAIN_HAND)
            return;
        Minecraft minecraft = Minecraft.getInstance();
        ItemStack stack = minecraft.player == null ? ItemStack.EMPTY : minecraft.player.getMainHandItem();
        if (!(stack.getItem() instanceof RegexFilterItem))
            return;
        minecraft.setScreen(new RegexFilterScreen(stack));
        event.setSwingHand(false);
        event.setCanceled(true);
    }
}
