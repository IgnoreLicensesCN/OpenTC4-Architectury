package thaumcraft.api.visnet;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//attach to BE
public interface IVisNetChargeRelayChargeableContainer {
    @NotNull("null->empty")
    ItemStack getStackToCharge();
}
