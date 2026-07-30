package awa.Aether_254.create_copycat_configuration.mixin;

import awa.Aether_254.create_copycat_configuration.CopycatModConfig;
import awa.Aether_254.create_copycat_configuration.CopycatSettingsData;
import awa.Aether_254.create_copycat_configuration.CopycatSettingsHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements CopycatSettingsData {
    @Unique
    private boolean copycatConfig$collision = CopycatModConfig.get().defaultCollision;
    @Unique
    private boolean copycatConfig$occlusion = CopycatModConfig.get().defaultLightOcclusion;
    @Unique
    private int copycatConfig$brightness = CopycatModConfig.get().defaultBrightness;

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void copycatConfig$save(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        tag.putBoolean("CopycatConfigCollision", copycatConfig$collision);
        tag.putBoolean("CopycatConfigOcclusion", copycatConfig$occlusion);
        tag.putByte("CopycatConfigBrightness", (byte) copycatConfig$brightness);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void copycatConfig$load(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        if (tag.contains("CopycatConfigCollision"))
            copycatConfig$collision = tag.getBoolean("CopycatConfigCollision");
        if (tag.contains("CopycatConfigOcclusion"))
            copycatConfig$occlusion = tag.getBoolean("CopycatConfigOcclusion");
        if (tag.contains("CopycatConfigBrightness"))
            copycatConfig$brightness = Math.max(0, Math.min(15, tag.getByte("CopycatConfigBrightness")));
        copycatConfig$applyLight();
    }

    @Inject(method = "setLevel", at = @At("TAIL"))
    private void copycatConfig$setLevel(Level level, CallbackInfo ci) {
        copycatConfig$applyLight();
    }

    @Unique
    private void copycatConfig$applyLight() {
        BlockEntity self = (BlockEntity) (Object) this;
        Level level = self.getLevel();
        if (level == null)
            return;
        if (!CopycatSettingsHelper.isCopycat(self.getBlockState().getBlock()))
            return;
        var auxiliary = level.getAuxLightManager(new ChunkPos(self.getBlockPos()));
        if (auxiliary == null)
            return;
        if (copycatConfig$brightness == 0)
            auxiliary.removeLightAt(self.getBlockPos());
        else
            auxiliary.setLightAt(self.getBlockPos(), copycatConfig$brightness);
    }

    @Override
    public boolean copycatConfig$collision() {
        return copycatConfig$collision;
    }

    @Override
    public boolean copycatConfig$lightOcclusion() {
        return copycatConfig$occlusion;
    }

    @Override
    public int copycatConfig$brightness() {
        return copycatConfig$brightness;
    }

    @Override
    public void copycatConfig$set(boolean collision, boolean lightOcclusion, int brightness) {
        copycatConfig$collision = collision;
        copycatConfig$occlusion = lightOcclusion;
        copycatConfig$brightness = brightness;
    }
}
