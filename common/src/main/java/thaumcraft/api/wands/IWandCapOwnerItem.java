package thaumcraft.api.wands;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@UtilityLikeAbstraction(reason = "if someone created component neither cap nor rod i need to keep compatibility")
public interface IWandCapOwnerItem {
    default IWandCapPropertiesOwnerComponent getWandCapItem(ItemStack stack){
        return getWandCapAsItemStack(stack).getItem() instanceof IWandCapPropertiesOwnerComponent owner ? owner : null;
    };
    @NotNull("null -> empty")
    ItemStack getWandCapAsItemStack(ItemStack stack);
}
