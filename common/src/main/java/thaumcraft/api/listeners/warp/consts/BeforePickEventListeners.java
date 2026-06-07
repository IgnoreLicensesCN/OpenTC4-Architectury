package thaumcraft.api.listeners.warp.consts;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.listeners.warp.PickWarpEventContext;
import thaumcraft.api.listeners.warp.listeners.PickWarpEventListenerBefore;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.items.armor.ItemFortressArmor;

public class BeforePickEventListeners {
    public static final PickWarpEventListenerBefore THAUMIC_FORTRESS_MASK_DISCOUNT = new PickWarpEventListenerBefore(0) {
        @Override
        public void beforePickEvent(PickWarpEventContext e, Player player) {
            var helm = player.getItemBySlot(EquipmentSlot.HEAD);
            //TODO:ItemFortressArmor Mask interfaces. "mask" with integer makes chaos.
            if (helm.getItem() instanceof ItemFortressArmor fortressArmor
                    && helm.hasTag()
                    && helm.stackTagCompound.hasKey("mask")
                    && helm.stackTagCompound.getInteger("mask") == 0) {

                e.warp -=  2 + player.getRandom().nextInt(4);
            }
        }
    };
    public static final PickWarpEventListenerBefore CALCULATE_WARP_AND_COUNTER = new PickWarpEventListenerBefore(1) {
        @Override
        public void beforePickEvent(PickWarpEventContext e, Player player) {
            e.warp = Math.min(100, (e.warp + e.warp + e.warpCounter) / 3);
            e.warpCounter = (int)((double)e.warpCounter - Math.max(5.0F, Math.sqrt(e.warpCounter) * (double)2.0F));
            WarpInfo.getFromPlayer(player).setWarpEventCounter(e.warpCounter);
        }
    };
}
