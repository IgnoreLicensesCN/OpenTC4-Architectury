package com.linearity.opentc4.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import thaumcraft.common.items.abstracts.IDropFollowingUserTool;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin {
    @WrapOperation(
            method = "dropResources(" +
                    "Lnet/minecraft/world/level/block/state/BlockState;" +
                    "Lnet/minecraft/world/level/Level;" +
                    "Lnet/minecraft/core/BlockPos;" +
                    "Lnet/minecraft/world/level/block/entity/BlockEntity;" +
                    "Lnet/minecraft/world/entity/Entity;" +
                    "Lnet/minecraft/world/item/ItemStack;" +
                    ")V",
            at = @At(value = "INVOKE", target =
                    "Lnet/minecraft/world/level/block/Block;" +
                            "getDrops(" +
                            "Lnet/minecraft/world/level/block/state/BlockState;" +
                            "Lnet/minecraft/server/level/ServerLevel;" +
                            "Lnet/minecraft/core/BlockPos;" +
                            "Lnet/minecraft/world/level/block/entity/BlockEntity;" +
                            "Lnet/minecraft/world/entity/Entity;" +
                            "Lnet/minecraft/world/item/ItemStack;" +
                            ")" +
                            "Ljava/util/List;")
    )
    private static List<ItemStack> opentc4$makeItemStackDropFollowingEntity(
            BlockState droppingState,
            ServerLevel serverLevel,
            BlockPos dropAtPos,
            @Nullable BlockEntity breakingBE,
            @Nullable Entity entityUsingTool,
            ItemStack usingToolStack,
            Operation<List<ItemStack>> original
    ) {
        var originalResult = original.call(droppingState, serverLevel, dropAtPos, breakingBE, entityUsingTool, usingToolStack);
        if (entityUsingTool == null){
            return originalResult;
        }
        if (!(usingToolStack.getItem() instanceof IDropFollowingUserTool dropFollowingUserTool)){
            return originalResult;
        }
        var remaining = new ArrayList<ItemStack>(originalResult.size());
        for (var stackToDrop : originalResult){
            if (!dropFollowingUserTool.canMakeDropFollowPlayer(usingToolStack,droppingState,serverLevel,dropAtPos, entityUsingTool, stackToDrop)){
                remaining.add(stackToDrop);
                continue;
            }
            dropFollowingUserTool.makeDropItemEntityAtPos(usingToolStack,droppingState,serverLevel,dropAtPos, entityUsingTool, stackToDrop);
        }
        return remaining;
    }
}
