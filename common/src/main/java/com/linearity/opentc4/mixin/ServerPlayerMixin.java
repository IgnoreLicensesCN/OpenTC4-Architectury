package com.linearity.opentc4.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.IRunicArmor;
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
                            //yeah both damage are 1 i've checked
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
                    EventHandlerRunic.runicInfo.put(player, new int[]{max[0], charged[0], kinetic[0], healing[0], emergency[0]});
                    if (EventHandlerRunic.runicCharge.containsKey(player)) {
                        int charge = EventHandlerRunic.runicCharge.get(player);
                        if (charge > max[0]) {
                            EventHandlerRunic.runicCharge.put(player, max[0]);
                            new PacketRunicChargeS2C((short) max[0],(short) max[0]).sendTo(player);
                        }
                    }
                } else {
                    EventHandlerRunic.runicInfo.remove(player);
                    EventHandlerRunic.runicCharge.put(player, 0);
                    new PacketRunicChargeS2C((short) 0,(short) 0).sendTo(player);
                }
            }

            if (EventHandlerRunic.rechargeDelay > 0) {
                --EventHandlerRunic.rechargeDelay;
            } else if (EventHandlerRunic.runicInfo.containsKey(player)) {
                if (!EventHandlerRunic.lastCharge.containsKey(player)) {
                    EventHandlerRunic.lastCharge.put(player, -1);
                }

                if (!EventHandlerRunic.runicCharge.containsKey(player)) {
                    EventHandlerRunic.runicCharge.put(player, 0);
                }

                if (!EventHandlerRunic.nextCycle.containsKey(player)) {
                    EventHandlerRunic.nextCycle.put(player, 0L);
                }

                long time = System.currentTimeMillis();
                int charge = EventHandlerRunic.runicCharge.get(player);
                if (charge > EventHandlerRunic.runicInfo.get(player)[0]) {
                    charge = EventHandlerRunic.runicInfo.get(player)[0];
                } else if (charge < EventHandlerRunic.runicInfo.get(player)[0] && EventHandlerRunic.nextCycle.get(player) < time 
                        && WandManager.consumeCentiVisFromInventory(player,
                        LinkedHashCentiVisList.of(Aspects.AIR, Config.shieldCost,Aspects.EARTH, Config.shieldCost)//TODO:Configurable
                )
                ) {
                    long interval = Config.shieldRecharge - EventHandlerRunic.runicInfo.get(player)[1] * 500L;
                    EventHandlerRunic.nextCycle.put(player, time + interval);
                    ++charge;
                    EventHandlerRunic.runicCharge.put(player, charge);
                }

                if (EventHandlerRunic.lastCharge.get(player) != charge) {
                    new PacketRunicChargeS2C((short)charge, (short)EventHandlerRunic.runicInfo.get(player)[0]).sendTo(player);
                    EventHandlerRunic.lastCharge.put(player, charge);
                }
            }
        }
    }
}

