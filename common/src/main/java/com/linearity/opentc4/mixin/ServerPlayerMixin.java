package com.linearity.opentc4.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.baubles.ItemAmuletRunic;
import thaumcraft.common.items.baubles.ItemGirdleRunic;
import thaumcraft.common.items.baubles.ItemRingRunic;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.PacketHandler;

import static com.linearity.opentc4.simpleutils.bauble.BaubleUtils.forEachBauble;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void opentc4$beforeServerPlayerTick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;


    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void opentc4$afterServerPlayerTick(CallbackInfo ci) {
        runicShieldTickForPlayer();
    }

    @Unique
    private void runicShieldTickForPlayer(){
        ServerPlayer player = (ServerPlayer)(Object)this;
        {
            if (EventHandlerRunic.isDirty || player.tickCount % 40 == 0) {
                final int[] max = {0};
                final int[] charged = {0};
                final int[] kinetic = {0};
                final int[] healing = {0};
                final int[] emergency = {0};
                EventHandlerRunic.isDirty = false;

                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                        ItemStack stack = player.getItemBySlot(slot);
                        if (!stack.isEmpty() && stack.getItem() instanceof IRunicArmor) {
                            int amount = EventHandlerRunic.getFinalCharge(stack);
                            max[0] += amount;
                        }
                    }
                }

                forEachBauble(player,(a,stack,item) -> {
                    if (item instanceof IRunicArmor runic){
                        int amount = EventHandlerRunic.getFinalCharge(stack);
                        if (item instanceof ItemRingRunic) {
                            switch (stack.getDamageValue()) {//TODO:No damage-value using here
                                case 2:
                                    ++charged[0];
                                    break;
                                case 3:
                                    ++healing[0];
                            }
                        } else if (item instanceof ItemAmuletRunic && stack.getDamageValue() == 1) {//TODO:No damage-value using here
                            ++emergency[0];
                        } else if (item instanceof ItemGirdleRunic && stack.getDamageValue() == 1) {//TODO:No damage-value using here
                            ++kinetic[0];
                        }

                        max[0] += amount;
                    }
                    return false;
                });

                if (max[0] > 0) {
                    EventHandlerRunic.runicInfo.put(player.getName().getString(), new Integer[]{max[0], charged[0], kinetic[0], healing[0], emergency[0]});
                    if (EventHandlerRunic.runicCharge.containsKey(player.getName().getString())) {
                        int charge = EventHandlerRunic.runicCharge.get(player.getName().getString());
                        if (charge > max[0]) {
                            EventHandlerRunic.runicCharge.put(player.getName().getString(), max[0]);
                            PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player, (short) max[0], max[0]), player);
                        }
                    }
                } else {
                    EventHandlerRunic.runicInfo.remove(player.getName().getString());
                    EventHandlerRunic.runicCharge.put(player.getName().getString(), 0);
                    PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player, (short) 0, 0), player);
                }
            }

            if (EventHandlerRunic.rechargeDelay > 0) {
                --EventHandlerRunic.rechargeDelay;
            } else if (EventHandlerRunic.runicInfo.containsKey(player.getName().getString())) {
                if (!EventHandlerRunic.lastCharge.containsKey(player.getName().getString())) {
                    EventHandlerRunic.lastCharge.put(player.getName().getString(), -1);
                }

                if (!EventHandlerRunic.runicCharge.containsKey(player.getName().getString())) {
                    EventHandlerRunic.runicCharge.put(player.getName().getString(), 0);
                }

                if (!EventHandlerRunic.nextCycle.containsKey(player.getName().getString())) {
                    EventHandlerRunic.nextCycle.put(player.getName().getString(), 0L);
                }

                long time = System.currentTimeMillis();
                int charge = EventHandlerRunic.runicCharge.get(player.getName().getString());
                if (charge > ((Integer[])EventHandlerRunic.runicInfo.get(player.getName().getString()))[0]) {
                    charge = ((Integer[])EventHandlerRunic.runicInfo.get(player.getName().getString()))[0];
                } else if (charge < ((Integer[])EventHandlerRunic.runicInfo.get(player.getName().getString()))[0] && EventHandlerRunic.nextCycle.get(player.getName().getString()) < time && WandManager.consumeVisFromInventory(player, (new AspectList()).add(
                        Aspect.AIR, Config.shieldCost).add(Aspect.EARTH, Config.shieldCost))) {
                    long interval = Config.shieldRecharge - ((Integer[])EventHandlerRunic.runicInfo.get(player.getName().getString()))[1] * 500;
                    EventHandlerRunic.nextCycle.put(player.getName().getString(), time + interval);
                    ++charge;
                    EventHandlerRunic.runicCharge.put(player.getName().getString(), charge);
                }

                if (EventHandlerRunic.lastCharge.get(player.getName().getString()) != charge) {
                    PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player, (short)charge, ((Integer[])EventHandlerRunic.runicInfo.get(player.getName().getString()))[0]), player);
                    EventHandlerRunic.lastCharge.put(player.getName().getString(), charge);
                }
            }
        }
    }
}

