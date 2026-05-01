package thaumcraft.api.researchtable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.listeners.researchtable.WriteAspectContext;
import thaumcraft.common.lib.utils.HexCoord;

public interface IResearchTableEditAspectListenerItem {

    boolean canWriteAspect(
            Level atLevel,
            BlockPos researchTableBEPos,
            ItemStack writeToolStack,
            ItemStack researchNoteStack,
            Player player,
            Aspect aspect,
            HexCoord placedAt
    );
    void beforeWriteAspect(WriteAspectContext context);
    //you can give random primal aspect after writing
    // instead of give them all at a time
    // after it has no duration and right-clicked?
    void afterWriteAspect(WriteAspectContext context);
    void beforeRemoveAspect(RemoveAspectContext context);
    //also,higher chance to turn aspect back or whatever.
    // maybe you can add warp here?(wtf)
    void afterRemoveAspect(RemoveAspectContext context);
}
