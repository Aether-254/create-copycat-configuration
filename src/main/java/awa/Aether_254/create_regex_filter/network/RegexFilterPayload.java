package awa.Aether_254.create_regex_filter.network;

import awa.Aether_254.create_regex_filter.CreateRegexFilter;
import awa.Aether_254.create_regex_filter.RegexFilterData;
import awa.Aether_254.create_regex_filter.RegexFilterItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RegexFilterPayload(boolean tagMode, String namespacePattern,
                                 String pathPattern) implements CustomPacketPayload {
    public static final Type<RegexFilterPayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(CreateRegexFilter.MOD_ID, "configure"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RegexFilterPayload> STREAM_CODEC = StreamCodec.of(
        (buffer, payload) -> {
            buffer.writeBoolean(payload.tagMode);
            buffer.writeUtf(payload.namespacePattern, 128);
            buffer.writeUtf(payload.pathPattern, 128);
        },
        buffer -> new RegexFilterPayload(buffer.readBoolean(), buffer.readUtf(128), buffer.readUtf(128)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RegexFilterPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ItemStack stack = context.player().getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.getItem() instanceof RegexFilterItem)
                RegexFilterData.write(stack, new RegexFilterData(payload.tagMode,
                    payload.namespacePattern, payload.pathPattern));
        });
    }
}
