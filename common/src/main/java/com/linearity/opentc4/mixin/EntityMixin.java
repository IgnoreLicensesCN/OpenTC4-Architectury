package com.linearity.opentc4.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.blockapi.IEntityInLavaBlock;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(
            method = "isInLava",
            at = @At("HEAD"),
            cancellable = true
    )
    private void opentc4$isInLava(CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity)(Object)this;

        Level level = self.level();
        if (level.isClientSide) return; // 可选，通常不必客户端算

        AABB box = self.getBoundingBox();

        // 扫描实体 AABB 覆盖的方块
        int minX = Mth.floor(box.minX);
        int maxX = Mth.floor(box.maxX);
        int minY = Mth.floor(box.minY);
        int maxY = Mth.floor(box.maxY);
        int minZ = Mth.floor(box.minZ);
        int maxZ = Mth.floor(box.maxZ);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    pos.set(x, y, z);

                    BlockState state = level.getBlockState(pos);
                    Block block = state.getBlock();

                    if (block instanceof IEntityInLavaBlock lavaLike) {
                        if (lavaLike.consideredAsLava(state, level, pos, self)) {
                            cir.setReturnValue(true);
                            return;
                        }
                    }
                }
            }
        }
    }

}

