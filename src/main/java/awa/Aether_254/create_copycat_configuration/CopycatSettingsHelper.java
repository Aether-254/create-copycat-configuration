package awa.Aether_254.create_copycat_configuration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class CopycatSettingsHelper {
    private CopycatSettingsHelper() {
    }

    public static boolean isCopycat(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        String className = block.getClass().getName();
        return className.startsWith("com.simibubi.create.content.decoration.copycat.")
            || className.startsWith("com.copycatsplus.copycats.")
            || id.getNamespace().equals("copycats");
    }

    public static CopycatSettingsData get(BlockGetter level, BlockPos pos, BlockState state) {
        if (!CopycatModConfig.get().enabled || !isCopycat(state.getBlock()))
            return null;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof CopycatSettingsData data ? data : null;
    }
}
