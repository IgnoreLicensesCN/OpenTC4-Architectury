package thaumcraft.common.items.abstracts;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

//should we impl this on Staff?
public interface ISwordLikeItem {
    default ItemStack getSwordStackToCheatCheck(ItemStack originalStack){
        return Items.DIAMOND_SWORD.getDefaultInstance();
    }
}
