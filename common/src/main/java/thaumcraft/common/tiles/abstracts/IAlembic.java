package thaumcraft.common.tiles.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainerBlockEntity;
import thaumcraft.api.aspects.IAspectContainerItem;
import thaumcraft.api.aspects.IAspectOutBlockEntity;

//TODO:for TileAlembic
public interface IAlembic extends IAspectOutBlockEntity<Aspect> {
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
                                       IAspectContainerItem<Aspect> itemToFill,
                                       Aspect aspect);
    void fillAspectContainerItem(
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill
    );

}
