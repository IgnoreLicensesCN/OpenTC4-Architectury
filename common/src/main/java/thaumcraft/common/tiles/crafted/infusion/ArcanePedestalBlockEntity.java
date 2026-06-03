package thaumcraft.common.tiles.crafted.infusion;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.CubeChunkedWeakLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.ICubeChunkBasedWeakLookupOwner;
import thaumcraft.common.tiles.abstracts.IInfusionCenterItemStackProvider;
import thaumcraft.common.tiles.abstracts.IInfusionComponentStackProvider;
import thaumcraft.common.tiles.crafted.AbstractPedestalBlockEntity;

import java.util.Map;

public class ArcanePedestalBlockEntity
        extends AbstractPedestalBlockEntity
        implements
        IInfusionComponentStackProvider,
        IInfusionCenterItemStackProvider
{
    public static Map<Level, CubeChunkedWeakLookups<IInfusionComponentStackProvider>> INFUSION_COMPONENT_PROVIDERS = new MapMaker().weakKeys().makeMap();

    public ArcanePedestalBlockEntity(BlockEntityType<? extends ArcanePedestalBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public ArcanePedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ARCANE_PEDESTAL,blockPos,blockState);
    }

    @Override
    public void setLevel(Level level) {
        registerToCubeLookup(level,this.level);
        super.setLevel(level);
    }

    @Override
    public void setRemoved() {
        unregisterFromWeakLookup(this.level);
        super.setRemoved();
    }

    @Override
    public Map<Level, CubeChunkedWeakLookups<IInfusionComponentStackProvider>> getSelfLookupMap() {
        return INFUSION_COMPONENT_PROVIDERS;
    }

    @Override
    public @NotNull IInfusionComponentStackProvider getStoreItemForLookup() {
        return this;
    }
}
