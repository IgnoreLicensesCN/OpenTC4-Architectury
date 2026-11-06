package thaumcraft.common.tiles.crafted.infusion;

import com.linearity.opentc4.mixinaccessors.CoordLookupOwnerLevel;
import com.linearity.opentc4.utils.collectionlike.coordlookups.CoordLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IInfusionCenterItemStackProvider;
import thaumcraft.common.tiles.abstracts.IInfusionComponentStackProvider;
import thaumcraft.common.tiles.abstracts.AbstractPedestalBlockEntity;

public class ArcanePedestalBlockEntity
        extends AbstractPedestalBlockEntity
        implements
        IInfusionComponentStackProvider,
        IInfusionCenterItemStackProvider
{
    public static final CoordLookupOwnerLevel.CoordLookupTypeKey<IInfusionComponentStackProvider> COORD_LOOKUP_KEY_FOR_INFUSION_COMPONENT_PROVIDER = new CoordLookupOwnerLevel.CoordLookupTypeKey<>();
//    public static Map<Level, CoordLookup<IInfusionComponentStackProvider>> INFUSION_COMPONENT_PROVIDERS = new MapMaker().weakKeys().makeMap();

    public ArcanePedestalBlockEntity(BlockEntityType<? extends ArcanePedestalBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public ArcanePedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.ARCANE_PEDESTAL(),blockPos,blockState);
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
    public @NotNull CoordLookup<IInfusionComponentStackProvider> getSelfLookupInLevel(Level level) {
        for (var lookup:((CoordLookupOwnerLevel)level).opentc4$getCoordLookupsForType(COORD_LOOKUP_KEY_FOR_INFUSION_COMPONENT_PROVIDER)){
            return lookup;
        }
        throw new IllegalStateException("Where is my lookup?this should not happen!");
    }

    @Override
    public @NotNull IInfusionComponentStackProvider getStoreItemForLookup() {
        return this;
    }
}
