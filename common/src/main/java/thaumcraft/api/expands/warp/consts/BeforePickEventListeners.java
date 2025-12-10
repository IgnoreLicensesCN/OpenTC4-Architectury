package thaumcraft.api.expands.warp.consts;

import net.minecraft.entity.player.Player;
import net.minecraft.item.ItemStack;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.listeners.PickWarpEventListenerBefore;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.armor.ItemFortressArmor;

public class BeforePickEventListeners {
    public static final PickWarpEventListenerBefore THAUMIC_FORTRESS_MASK_DISCOUNT = new PickWarpEventListenerBefore(0) {
        @Override
        public void beforePickEvent(PickWarpEventContext e, Player player) {
            ItemStack helm = player.inventory.armorInventory[3];
            if (helm != null
                    && helm.getItem() instanceof ItemFortressArmor
                    && helm.hasTagCompound() && helm.stackTagCompound.hasKey("mask")
                    && helm.stackTagCompound.getInteger("mask") == 0) {
                e.warp -=  2 + player.worldObj.rand.nextInt(4);
            }
        }
    };
    public static final PickWarpEventListenerBefore CALCULATE_WARP_AND_COUNTER = new PickWarpEventListenerBefore(1) {
        @Override
        public void beforePickEvent(PickWarpEventContext e, Player player) {
            e.warp = Math.min(100, (e.warp + e.warp + e.warpCounter) / 3);
            e.warpCounter = (int)((double)e.warpCounter - Math.max(5.0F, Math.sqrt(e.warpCounter) * (double)2.0F));
            Thaumcraft.proxy.getPlayerKnowledge().setWarpCounter(player.getCommandSenderName(), e.warpCounter);
        }
    };
}
