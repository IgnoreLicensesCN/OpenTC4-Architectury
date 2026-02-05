package thaumcraft.api.researchtable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.lib.utils.HexCoordUtils;

//for research note and maybe you can do something else?
public interface IResearchNoteDataOwner extends IResearchTableWriteAspectListener{
    default boolean canPlaceIntoResearchTable(
            Level atLevel,
            BlockPos tablePos,
            ItemStack noteStack){
        return true;
    }
    boolean onWriteAspect(
            Level atLevel,
            BlockPos tablePos,
            ItemStack writeToolStack,
            ItemStack noteStack,
            Player player,
            Aspect aspect,
            HexCoordUtils.HexCoord placedAt
    );
    @Nullable ResearchNoteData getResearchNoteData(ItemStack researchNoteStack);
}
