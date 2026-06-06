package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.PlayerRunicShieldInfoMixinAccessor;
import com.linearity.opentc4.mixinaccessors.PlayerWarpInfoMixinAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = Player.class,priority = 900)
public class PlayerMixin implements PlayerRunicShieldInfoMixinAccessor, PlayerWarpInfoMixinAccessor {
    @Unique
    private final EntityRunicShieldInfo opentc4$playerRunicShieldInfo = new EntityRunicShieldInfo();
    @Override
    public EntityRunicShieldInfo opentc4$getPlayerRunicShieldInfo() {
        return opentc4$playerRunicShieldInfo;
    }

    @Unique
    private final WarpInfo openc4$warpInfo = new WarpInfo();

    @Override
    public WarpInfo opentc4$getWarpInfo() {
        return openc4$warpInfo;
    }

    @ModifyVariable(
            method = "actuallyHurt",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float opentc4$playerActuallyHurt$modifyDamageWithRunicShield(float f, DamageSource damageSource){
        var player = (Player)(Object)this;
        if (!player.level().isClientSide){
            var shieldInfo = EntityRunicShieldInfo.getFromPlayer(player);
            AtomicReference<Float> floatAtomicReference = new AtomicReference<>(f);
            shieldInfo.shieldCapacity.keySet().forEach(
                    shieldType ->
                            floatAtomicReference.updateAndGet(
                                    finalDamage -> shieldType.beforeActuallyHurt(player, damageSource,finalDamage,shieldInfo))
            );
            if (shieldInfo.shouldSyncCharge && player instanceof ServerPlayer serverPlayer){
                shieldInfo.syncChargeSendPacket(serverPlayer);
            }
            shieldInfo.shouldSyncCharge = false;
            return floatAtomicReference.get();
        }
        return f;
    }

}
