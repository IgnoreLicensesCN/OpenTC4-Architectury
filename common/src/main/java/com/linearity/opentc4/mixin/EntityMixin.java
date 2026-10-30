package com.linearity.opentc4.mixin;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.linearity.opentc4.mixinaccessors.IObjectCacheableEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.blockapi.IEntityInLavaBlock;
import thaumcraft.common.entities.ThaumcraftEntityEvents;

@Mixin(Entity.class)
public class EntityMixin implements IObjectCacheableEntity {
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
            method = "setRemoved",
            at = @At("HEAD")
    )
    private void opentc4$onRemoved(Entity.RemovalReason removalReason, CallbackInfo ci){
        ThaumcraftEntityEvents.convertTaintMobOnRemoved((Entity) (Object) this,removalReason);
    }

    @Unique
    private final ClassToInstanceMap<Object> opentc4$objectCacheMap = MutableClassToInstanceMap.create();

    @Override
    public <T> @Nullable T opentc4$getCachedObjectFromEntity(Class<T> clazz) {
        return opentc4$objectCacheMap.getInstance(clazz);
    }

    @Override
    public <T> void opentc4$putCachedObjectIntoEntity(Class<T> clazz, T object) {
        opentc4$objectCacheMap.putInstance(clazz, object);
    }

    @Override
    public void opentc4$removeCachedObjectFromEntity(Class<?> clazz) {
        opentc4$objectCacheMap.remove(clazz);
    }
}

