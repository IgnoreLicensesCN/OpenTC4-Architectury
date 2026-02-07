package thaumcraft.api.researchtable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.research.ResearchItem;

public interface IResearchTableAspectEditTool extends IResearchTableEditAspectListener {
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

    //should ensure that player can call #createResearchNote
    ResearchCreateReason canCreateResearchNote(
            Level atLevel,
            Player player,
            ItemStack writeToolStack,
            ResearchItem researchItem
    );
    void createResearchNote(
            Level atLevel,
            ServerPlayer player,
            ItemStack writeToolStack,
            ResearchItem researchItem
    );
}
