package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;

public interface IAdditionalSuctionProviderBlock {
    default int getAdditionalProvidedSuction(
            Level level,
            IEssentiaTransportInBlockEntity be,
            BlockState beState, BlockPos bePos,
            BlockState attachmentState,//selfState
            BlockPos attachmentPos//selfPos
    ) {
        return 32;
    }
}
