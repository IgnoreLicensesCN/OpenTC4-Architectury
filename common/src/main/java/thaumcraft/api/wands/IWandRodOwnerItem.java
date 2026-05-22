package thaumcraft.api.wands;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

@UtilityLikeAbstraction(reason = "if someone created component neither cap nor rod i need to keep compatibility")
public interface IWandRodOwnerItem {
    default IWandRodPropertiesOwnerComponent<?> getWandRodItem(ItemStack stack){
        return getWandRodAsItemStack(stack).getItem() instanceof IWandRodPropertiesOwnerComponent<? extends Aspect> owner ? owner : null;
    };
    ItemStack getWandRodAsItemStack(ItemStack stack);
}
