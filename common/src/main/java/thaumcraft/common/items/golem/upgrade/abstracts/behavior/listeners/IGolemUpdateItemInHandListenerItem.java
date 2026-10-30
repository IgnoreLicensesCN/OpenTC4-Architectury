package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public interface IGolemUpdateItemInHandListenerItem extends BaseGolemEntity.IGolemUpgradeItem{
    //maybe you want to place liquid display stack here?
    default void onUpdateItemInHand(BaseGolemEntity golem, ItemStack upgrade){
//        if (golem.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
//            for (var stack:golem.inventory){
//                if (!stack.isEmpty()) {
//                    golem.setItemInHand(InteractionHand.MAIN_HAND, stack);
//                    break;
//                }
//            }
//        }
    }
}
