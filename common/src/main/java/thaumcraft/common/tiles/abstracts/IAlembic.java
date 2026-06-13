package thaumcraft.common.tiles.abstracts;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.aspects.essentiabe.IEssentiaForceOutBlockEntity;


public interface IAlembic extends IEssentiaForceOutBlockEntity<Aspect> {
    @NotNull("null -> empty")
    Aspect getAspect();
    @NotNull("null -> empty")
    Aspect getAspectFilter();

    int getAmount();
    int getMaxAmount();

    //i dont want to impl IAspectInBlockEntity because i'm afraid of others rather than AlchemicalFurnace input this BE
    /**
     * This method is used to add a certain amount of aspect an aspect to the tile entity.
     * @param aspect to add
     * @param amount to add
     * @return the amount of aspect left over that could not be added.
     */
    int addIntoContainer(Aspect aspect, int amount);

    boolean doesContainerAccept(Aspect aspect);
    void clear();

    boolean canFillAspectContainerItem(ItemStack stackToFill,
                                       IEssentiaContainerItem<Aspect> itemToFill,
                                       Aspect aspect);
    boolean fillAspectContainerItem(
            ItemStack stackToFill,
            IEssentiaContainerItem<Aspect> itemToFill,
            int minAmount
    );

}
