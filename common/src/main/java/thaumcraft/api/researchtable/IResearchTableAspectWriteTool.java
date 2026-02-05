package thaumcraft.api.researchtable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.utils.HexCoordUtils;

public interface IResearchTableAspectWriteTool extends IResearchTableWriteAspectListener{
    default boolean canPlaceIntoResearchTable(
            Level atLevel,
            BlockPos tablePos,
            ItemStack writeToolStack){
        return true;
    }

    default boolean canCreateResearchTable(
            Level atLevel,
            BlockPos clickedPos,
            ItemStack writeToolStack
    ) {
        return true;
    }
}
