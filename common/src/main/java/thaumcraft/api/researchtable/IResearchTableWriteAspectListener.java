package thaumcraft.api.researchtable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.utils.HexCoord;

public interface IResearchTableWriteAspectListener {

    boolean canWriteAspect(
            Level atLevel,
            BlockPos researchTableBEPos,
            ItemStack writeToolStack,
            ItemStack researchNoteStack,
            Player player,
            Aspect aspect,
            HexCoord placedAt
    );
    void beforeWriteAspect(
            Level atLevel,
            BlockPos researchTableBEPos,
            ItemStack writeToolStack,
            ItemStack researchNoteStack,
            ServerPlayer player,
            Aspect aspect,
            HexCoord placedAt
    );
    void afterWriteAspect(
            Level atLevel,
            BlockPos researchTableBEPos,
            ItemStack writeToolStack,
            ItemStack researchNoteStack,
            ServerPlayer player,
            Aspect aspect,
            HexCoord placedAt
    );
}
