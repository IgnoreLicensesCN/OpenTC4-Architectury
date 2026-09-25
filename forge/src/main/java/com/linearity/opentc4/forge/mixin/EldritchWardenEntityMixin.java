package com.linearity.opentc4.forge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import thaumcraft.common.entities.monster.boss.EldritchWardenEntity;

@Mixin(EldritchWardenEntity.class)
public abstract class EldritchWardenEntityMixin {
    @Shadow
    public abstract BlockPos getRestrictCenter();
    @ModifyArgs(
            method = "teleportHome",
            at = @At("HEAD"),
            remap = false
    )
    private Vec3 opentc4$teleportWithEvent(Vec3 teleportToPos) {
        var entity = (EldritchWardenEntity)(Object)this;
        var homePos = getRestrictCenter();
        var event = new EntityTeleportEvent.EnderEntity(entity,homePos.getX(), homePos.getY(), homePos.getZ());
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()){
            return null;
        }
        return event.getTarget();
    }

}
