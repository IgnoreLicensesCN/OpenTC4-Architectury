package thaumcraft.api.expands.listeners.warp.consts;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.expands.listeners.warp.PickWarpEventContext;
import thaumcraft.api.expands.listeners.warp.listeners.PickWarpEventListenerBefore;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.armor.ItemFortressArmor;

public class BeforePickEventListeners {
    public static final PickWarpEventListenerBefore THAUMIC_FORTRESS_MASK_DISCOUNT = new PickWarpEventListenerBefore(0) {
        @Override
        public void beforePickEvent(PickWarpEventContext e, Player player) {
            ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
            //TODO:ItemFortressArmor Mask interfaces. "mask" with integer makes chaos.
            if (helm.getItem() instanceof ItemFortressArmor fortressArmor && helm.hasTagCompound() && helm.stackTagCompound.hasKey(
                    "mask") && helm.stackTagCompound.getInteger("mask") == 0) {

                e.warp -=  2 + player.getRandom().nextInt(4);
            }
        }
    };
    public static final PickWarpEventListenerBefore CALCULATE_WARP_AND_COUNTER = new PickWarpEventListenerBefore(1) {
        @Override
        public void beforePickEvent(PickWarpEventContext e, Player player) {
            e.warp = Math.min(100, (e.warp + e.warp + e.warpCounter) / 3);
            e.warpCounter = (int)((double)e.warpCounter - Math.max(5.0F, Math.sqrt(e.warpCounter) * (double)2.0F));
            Thaumcraft.playerKnowledge.setWarpCounter(player.getGameProfile().getName(), e.warpCounter);
        }
    };
}
