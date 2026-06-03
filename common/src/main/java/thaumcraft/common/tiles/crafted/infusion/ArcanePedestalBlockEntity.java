package thaumcraft.common.tiles.crafted.infusion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IInfusionCenterItemStackProvider;
import thaumcraft.common.tiles.abstracts.IInfusionComponentStackProvider;
import thaumcraft.common.tiles.crafted.AbstractPedestalBlockEntity;

public class ArcanePedestalBlockEntity
        extends AbstractPedestalBlockEntity
        implements IInfusionComponentStackProvider,
        IInfusionCenterItemStackProvider
{

    public ArcanePedestalBlockEntity(BlockEntityType<? extends ArcanePedestalBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public ArcanePedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ARCANE_PEDESTAL,blockPos,blockState);
    }

    @Override
    public void setLevel(Level level) {
        registerComponentStackProvider(level,this.level);
        super.setLevel(level);
    }

    @Override
    public void setRemoved() {
        unregisterComponentStackProvider(this.level);
        super.setRemoved();
    }
}
