package awa.Aether_254.create_copycat_configuration.mixin;

import awa.Aether_254.create_copycat_configuration.CopycatSettingsData;
import awa.Aether_254.create_copycat_configuration.CopycatSettingsHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
        at = @At("HEAD"), cancellable = true)
    private void copycatConfig$collision(BlockGetter level, BlockPos pos,
                                         CallbackInfoReturnable<VoxelShape> cir) {
        CopycatSettingsData data = CopycatSettingsHelper.get(level, pos, (BlockState) (Object) this);
        if (data != null && !data.copycatConfig$collision())
            cir.setReturnValue(Shapes.empty());
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
        at = @At("HEAD"), cancellable = true)
    private void copycatConfig$collisionWithContext(BlockGetter level, BlockPos pos, CollisionContext context,
                                                    CallbackInfoReturnable<VoxelShape> cir) {
        copycatConfig$collision(level, pos, cir);
    }

    @Inject(method = "getOcclusionShape", at = @At("HEAD"), cancellable = true)
    private void copycatConfig$occlusionShape(BlockGetter level, BlockPos pos,
                                              CallbackInfoReturnable<VoxelShape> cir) {
        CopycatSettingsData data = CopycatSettingsHelper.get(level, pos, (BlockState) (Object) this);
        if (data != null && !data.copycatConfig$lightOcclusion())
            cir.setReturnValue(Shapes.empty());
    }

    @Inject(method = "getLightBlock", at = @At("HEAD"), cancellable = true)
    private void copycatConfig$lightBlock(BlockGetter level, BlockPos pos,
                                          CallbackInfoReturnable<Integer> cir) {
        CopycatSettingsData data = CopycatSettingsHelper.get(level, pos, (BlockState) (Object) this);
        if (data != null && !data.copycatConfig$lightOcclusion())
            cir.setReturnValue(0);
    }

    @Inject(method = "propagatesSkylightDown", at = @At("HEAD"), cancellable = true)
    private void copycatConfig$skylight(BlockGetter level, BlockPos pos,
                                        CallbackInfoReturnable<Boolean> cir) {
        CopycatSettingsData data = CopycatSettingsHelper.get(level, pos, (BlockState) (Object) this);
        if (data != null && !data.copycatConfig$lightOcclusion())
            cir.setReturnValue(true);
    }
}
