package thaumcraft.common.tiles.abstracts;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//oh even more AE2 tricks can be performed like "provide all items needed in a container"
public interface IInfusionComponentStackProvider extends Container, ICubeChunkBasedWeakLookupOwner<IInfusionComponentStackProvider> {
    default @NotNull ItemStack storeReturningStack(@NotNull ItemStack stack){
        for (int i=0;i<getContainerSize();i++){
            if (stack.isEmpty()){
                return ItemStack.EMPTY;
            }
            var inSlot = getItem(i);
            if (inSlot.isEmpty()){
                setItem(i, stack);
                return ItemStack.EMPTY;
            }
            if (ItemStack.isSameItemSameTags(stack, inSlot)){
                int spaceRemaining = inSlot.getMaxStackSize() - inSlot.getCount();
                var toStore = stack.split(spaceRemaining);
                inSlot.setCount(inSlot.getCount() + toStore.getCount());
                setItem(i, inSlot);
            }
        }
        return stack;
    }
}
