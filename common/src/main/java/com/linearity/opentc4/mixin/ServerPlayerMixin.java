package com.linearity.opentc4.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.lib.events.RunicShieldHandler;
import thaumcraft.common.runicshield.IRunicShieldProviderItem;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.LinkedHashCentiVisList;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.baubles.ItemAmuletRunic;
import thaumcraft.common.items.baubles.ItemGirdleRunic;
import thaumcraft.common.items.baubles.ItemRingRunic;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.playerdata.PacketRunicChargeS2C;

import static com.linearity.opentc4.simpleutils.bauble.BaubleUtils.forEachBauble;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void opentc4$beforeServerPlayerTick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;

    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void opentc4$afterServerPlayerTick(CallbackInfo ci) {
        opentc4$runicShieldTickForPlayer();
    }

    @Unique
    private void opentc4$runicShieldTickForPlayer(){
        ServerPlayer player = (ServerPlayer)(Object)this;
        RunicShieldHandler.updateRunicShieldForPlayer(player);
    }
}

