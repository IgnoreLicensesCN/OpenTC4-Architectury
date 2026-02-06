package thaumcraft.api.researchtable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.expands.listeners.researchtable.WriteAspectContext;
import thaumcraft.common.lib.utils.HexCoord;

public interface IResearchTableEditAspectListener {

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
    void afterWriteAspect(WriteAspectContext context);
    void beforeRemoveAspect(RemoveAspectContext context);
    void afterRemoveAspect(RemoveAspectContext context);
}
