package thaumcraft.common.items.abstracts.wandabstraction.wand;

import com.linearity.opentc4.annotations.Modifiable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//which can use wand focus
//you should drive focus yourself
public interface IWandFocusEngineItem {
    default boolean canApplyFocus(){
        return true;
    };

    @NotNull("null -> empty")
    ItemStack getFocusItemStack(ItemStack wand);
    //returns focus it owns before
    @Modifiable @NotNull ItemStack changeFocusItemStack(ItemStack wand, ItemStack focus);
}
