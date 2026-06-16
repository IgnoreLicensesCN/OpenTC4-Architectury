package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.PlayerResearchAndScannedInfoAccessor;
import com.linearity.opentc4.mixinaccessors.PlayerRunicShieldInfoMixinAccessor;
import com.linearity.opentc4.mixinaccessors.PlayerWarpInfoMixinAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.items.abstracts.IFlyingAbilityProviderWearing;
import thaumcraft.common.items.abstracts.ISwordLikeItem;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;

import java.util.concurrent.atomic.AtomicReference;

import static thaumcraft.common.lib.events.EventHandlerEntity.updateSpeedForHasteEnchantment;

@Mixin(value = Player.class,priority = 900)
public class PlayerMixin
        implements
        PlayerRunicShieldInfoMixinAccessor,
        PlayerWarpInfoMixinAccessor,
        PlayerResearchAndScannedInfoAccessor
{
    @Unique
    private ResearchAndScannedInfo opentc4$ResearchAndScannedInfo = new ResearchAndScannedInfo();


    @Override
    public ResearchAndScannedInfo opentc4$getResearchAndScannedInfo() {
        return opentc4$ResearchAndScannedInfo;
    }

    @Override
    public void opentc4$setResearchAndScannedInfo(ResearchAndScannedInfo completedResearches) {
        opentc4$ResearchAndScannedInfo = completedResearches;
    }

    @Unique
    private EntityRunicShieldInfo opentc4$playerRunicShieldInfo = new EntityRunicShieldInfo();
    @Override
    public EntityRunicShieldInfo opentc4$getPlayerRunicShieldInfo() {
        return opentc4$playerRunicShieldInfo;
    }

    @Override
    public void opentc4$setPlayerRunicShieldInfo(EntityRunicShieldInfo opentc4$playerRunicShieldInfo) {
        this.opentc4$playerRunicShieldInfo = opentc4$playerRunicShieldInfo;
    }

    @Unique
    private @NotNull WarpInfo openc4$warpInfo = new WarpInfo();

    @Override
    public WarpInfo opentc4$getWarpInfo() {
        return openc4$warpInfo;
    }

    @Override
    public void opentc4$setWarpInfo(WarpInfo warpInfo) {
        this.openc4$warpInfo = warpInfo;
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

    //hope wont crash
    @WrapOperation(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack opentc4$bypassSweepCheck(Player player, InteractionHand hand, Operation<ItemStack> original) {
        ItemStack itemStack = original.call(player, hand);
        if (itemStack.getItem() instanceof ISwordLikeItem) {
            return Items.DIAMOND_SWORD.getDefaultInstance();
        }
        return itemStack;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void opentc4$playerTick(CallbackInfo ci){
        var player = (Player)(Object)this;
        IFlyingAbilityProviderWearing.FlyingAbilityProviderCheck.checkFlyingProviderForPlayer(player);
        updateSpeedForHasteEnchantment(player);
    }
}
