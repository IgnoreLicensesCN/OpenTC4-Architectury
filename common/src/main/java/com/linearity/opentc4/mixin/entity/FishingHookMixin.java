package com.linearity.opentc4.mixin.entity;

import com.linearity.opentc4.mixinattachments.FishingHookMixinAccessor;
import com.linearity.opentc4.mixinattachments.FishingHookWeakRefSupplierOwner;
import com.linearity.opentc4.mixinattachments.overrides.entity.fishinghook.*;
import com.linearity.opentc4.mixinattachments.supercallproviders.entity.FishingHookSuperAccessor;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = FishingHook.class,priority = Integer.MIN_VALUE/2)
public abstract class FishingHookMixin extends Entity implements FishingHookSuperAccessor, FishingHookMixinAccessor, FishingHookWeakRefSupplierOwner {

    @Unique
    private final FishingHookWeakRefSupplier opentc4$selfSupplier = new FishingHookWeakRefSupplier((FishingHook)(Object) this);
    @Override
    public FishingHookWeakRefSupplier opentc4$getFishingHookWeakRefSupplier() {
        return opentc4$selfSupplier;
    }

    private FishingHookMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void opentc4$projectileSuper$tick() {
        super.tick();
    }

    @WrapMethod(
            method = "shouldStopFishing"
    )
    private boolean opentc4$shouldStopFishingWrap(Player player, Operation<Boolean> original) {
        if (this instanceof FishingHookShouldStopFishingOverride shouldStopFishingOverride) {
            return shouldStopFishingOverride.opentc4$fishingHook$shouldStopFishing(player,original);
        }
        return original.call(player);
    }

    @WrapMethod(
            method = "retrieve"
    )
    private int opentc4$retrieve(ItemStack itemStack, Operation<Integer> original) {
        if (this instanceof FishingHookRetrieveOverride retrieveOverride) {
            return retrieveOverride.opentc4$fishingHook$retrieve(itemStack,original);
        }
        return original.call(itemStack);
    }

    @WrapMethod(
            method = "tick"
    )
    private void opentc4$tick(Operation<Void> original) {
        if (this instanceof FishingHookTickOverride tickOverride) {
            tickOverride.opentc4$fishingHook$tick(original);
            return;
        }
        original.call();
    }

    @WrapMethod(
            method = "updateOwnerInfo"
    )
    private void opentc4$updateOwnerInfo(FishingHook fishingHook, Operation<Void> original) {
        if (this instanceof FishingHookUpdateOwnerInfoOverride updateOwnerInfoOverride) {
            updateOwnerInfoOverride.opentc4$fishingHook$updateOwnerInfo(original);
            return;
        }
        original.call(fishingHook);
    }

    @WrapMethod(
            method = "recreateFromPacket"
    )
    public void opentc4$recreateFromPacket(ClientboundAddEntityPacket packet, Operation<Void> original){
        if (this instanceof FishingHookRecreateFromPacketOverride recreateFromPacketOverride) {
            recreateFromPacketOverride.opentc4$fishingHook$recreateFromPacket(packet,original);
            return;
        }
        original.call(packet);
    }

    @Override
    public void opentc4$projectileSuper$recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
    }

    @Shadow
    private Enum<?> currentState;
    @Override
    public FishHookState opentc4$getCurrentState() {
        return FishHookState.valueOf(String.valueOf(currentState));
    }

    @Unique
    private static Class<?> opentc4$fishHookStateEnumClass = null;
    @Override
    public void opentc4$setCurrentState(FishHookState currentState)  {
        if (opentc4$fishHookStateEnumClass == null){
            try {
                opentc4$fishHookStateEnumClass = Class.forName("net.minecraft.world.entity.projectile.FishingHook$FishHookState")
                ;
            }catch (Exception e){
                throw new IllegalStateException("Could not find FishingHook$FishHookState");
            }
        }
        this.currentState = Enum.valueOf((Class) opentc4$fishHookStateEnumClass,currentState.name());
    }

}
