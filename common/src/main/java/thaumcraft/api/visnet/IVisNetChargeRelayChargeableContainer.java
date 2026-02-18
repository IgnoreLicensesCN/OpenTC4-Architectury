package thaumcraft.api.visnet;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//attach to BE
//from TileMagicWorkbenchCharger
// but now it would support more than TileMagicWorkbench
public interface IVisNetChargeRelayChargeableContainer {
    @NotNull("null->empty")
    ItemStack getStackToCharge();
}
