package com.linearity.opentc4.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.blockapi.IEntityInLavaBlock;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;

@Mixin(Entity.class)
public class EntityMixin {

    @WrapOperation(
            method = "updateFluidHeightAndDoFluidPushing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"
            )
    )
    private FluidState opentc4$insideLavaBlockCheck(Level level, BlockPos pos, Operation<FluidState> original){
        //maybe better than another foreach?
        var blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof IEntityInLavaBlock entityInLavaBlock) {
            if (entityInLavaBlock.consideredAsLava(blockState,level,pos,(Entity)(Object)this)) {
                return Fluids.LAVA.defaultFluidState();
            }
        }
        return original.call(level, pos);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void opentc4$entityTick(CallbackInfo ci){
        var self = (Entity)(Object)this;
        if (self instanceof ITaintConvertableEntity taintConvertable && taintConvertable.canConvertToTaintedMob()) {
            taintConvertable.convertToTaintedMob();
        }
    }

}

