package awa.Aether_254.create_copycat_configuration.network;

import awa.Aether_254.create_copycat_configuration.CopycatSettingsData;
import awa.Aether_254.create_copycat_configuration.CopycatSettingsHelper;
import awa.Aether_254.create_copycat_configuration.CreateCopycatConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CopycatSettingsPayload(BlockPos pos, boolean collision, boolean lightOcclusion,
                                     int brightness) implements CustomPacketPayload {
    public static final Type<CopycatSettingsPayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(CreateCopycatConfiguration.MOD_ID, "settings"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CopycatSettingsPayload> STREAM_CODEC = StreamCodec.of(
        (buffer, payload) -> {
            buffer.writeBlockPos(payload.pos);
            buffer.writeBoolean(payload.collision);
            buffer.writeBoolean(payload.lightOcclusion);
            buffer.writeByte(payload.brightness);
        },
        buffer -> new CopycatSettingsPayload(buffer.readBlockPos(), buffer.readBoolean(),
            buffer.readBoolean(), buffer.readUnsignedByte()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CopycatSettingsPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player.distanceToSqr(payload.pos.getX() + 0.5, payload.pos.getY() + 0.5,
                    payload.pos.getZ() + 0.5) > 64.0)
                return;
            if (!BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).toString().equals("create:wrench"))
                return;
            var level = player.level();
            var state = level.getBlockState(payload.pos);
            if (!CopycatSettingsHelper.isCopycat(state.getBlock()))
                return;
            BlockEntity blockEntity = level.getBlockEntity(payload.pos);
            if (!(blockEntity instanceof CopycatSettingsData data))
                return;
            int light = Math.max(0, Math.min(15, payload.brightness));
            data.copycatConfig$set(payload.collision, payload.lightOcclusion, light);
            blockEntity.setChanged();
            var auxiliary = level.getAuxLightManager(new ChunkPos(payload.pos));
            if (auxiliary != null) {
                if (light == 0)
                    auxiliary.removeLightAt(payload.pos);
                else
                    auxiliary.setLightAt(payload.pos, light);
            }
            level.getChunkSource().getLightEngine().checkBlock(payload.pos);
            level.sendBlockUpdated(payload.pos, state, state, 3);
        });
    }
}
