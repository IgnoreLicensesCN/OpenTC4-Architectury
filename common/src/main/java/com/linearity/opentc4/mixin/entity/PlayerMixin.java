package com.linearity.opentc4.mixin.entity;

import com.linearity.annotations.Modifiable;
import com.linearity.opentc4.mixinattachments.*;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.items.abstracts.ISwordLikeItem;
import thaumcraft.common.items.wands.WandCooldownManager;
import thaumcraft.common.runicshield.RunicShieldHandler;
import thaumcraft.common.runicshield.RunicShieldInfo;

import java.util.*;
import java.util.function.Supplier;

import static thaumcraft.common.entities.ThaumcraftEntityEvents.playerTickBefore;
import static thaumcraft.common.entities.ThaumcraftEntityEvents.updateFishingHookForPlayer;

@Mixin(value = Player.class, priority = 900)
public abstract class PlayerMixin
        implements
        PlayerRunicShieldInfoMixinAccessor,
        PlayerWarpInfoMixinAccessor,
        PlayerResearchAndScannedInfoAccessor,
        PlayerWandCooldownManagerMixinAccessor,
        ILuckOwnerEntity,
        FishingHookOwner {

    @Shadow
    @Nullable
    public FishingHook fishing;

    @Override
    public void opentc4$onUpdateFishingHookOwning(FishingHook fishingHook) {
        updateFishingHookForPlayer((Player)(Object) this,fishingHook);
    }

    @Unique
    private final Set<Supplier<@Nullable FishingHook>> opentc4$fishingHooks = new IdentityHashMap<Supplier<@Nullable FishingHook>,Boolean>().keySet();

    @Override
    public @Modifiable @NotNull Set<Supplier<@Nullable FishingHook>> opentc4$getFishingHooks() {
        if (fishing != null) {
            opentc4$fishingHooks.add(((FishingHookWeakRefSupplierOwner)fishing).opentc4$getFishingHookWeakRefSupplier());
        }
        return opentc4$fishingHooks;
    }

    @Override
    public void opentc4$maybeClearFishingHooksOnRemoved() {
        opentc4$fishingHooks.clear();
    }

    @Shadow
    public abstract float getLuck();

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
    private RunicShieldInfo opentc4$playerRunicShieldInfo = new RunicShieldInfo();

    @Override
    public RunicShieldInfo opentc4$getPlayerRunicShieldInfo() {
        return opentc4$playerRunicShieldInfo;
    }

    @Override
    public void opentc4$setPlayerRunicShieldInfo(RunicShieldInfo opentc4$playerRunicShieldInfo) {
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

    @Unique
    private final @NotNull WandCooldownManager openc4$wandCooldownManager = new WandCooldownManager();

    @Override
    public WandCooldownManager opentc4$getWandCooldownManager() {
        return openc4$wandCooldownManager;
    }

    @ModifyVariable(
            method = "actuallyHurt",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float opentc4$playerActuallyHurt$modifyDamageWithRunicShield(float damage, DamageSource damageSource) {
        var player = (Player) (Object) this;
        return RunicShieldHandler.modifyDamageWithRunicShield(damage, damageSource, player);
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
        if (itemStack.getItem() instanceof ISwordLikeItem swordLikeItem) {
            return swordLikeItem.getSwordStackToCheatCheck(itemStack);
        }
        return itemStack;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void opentc4$playerTick(CallbackInfo ci) {
        var player = (Player) (Object) this;
        playerTickBefore(player);
    }
}
