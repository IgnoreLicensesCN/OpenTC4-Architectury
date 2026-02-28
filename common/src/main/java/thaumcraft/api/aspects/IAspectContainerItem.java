package thaumcraft.api.aspects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

//(dont impl for empty jar)
public interface IAspectContainerItem<Asp extends Aspect> {
    AspectList<Asp> getAspects(ItemStack itemstack);
    int getAspectTypeSize(ItemStack itemstack);//for example 1 for jar and Integer.MAX_VALUE for ESSENTIA_RESERVOIR
    // (but ESSENTIA_RESERVOIR shouldn't impl this since it's designed to work as a block)
    int getAspectMaxVisSize(ItemStack itemstack);//for example 64 for jar and 256 for ESSENTIA_RESERVOIR
    //force set AspectList(not recommended,should throw exception if invalid visSize or typeSize
    void setAspects(ItemStack itemstack, AspectList<Asp> aspects);
    //return:remaining
    //you may want to create new itemstack here
    int storeAspect(@NotNull Level level,@NotNull BlockPos pos,@NotNull ItemStack itemstack, @NotNull("null -> empty") Asp aspect, int amountCanFill);
}
