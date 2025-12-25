package thaumcraft.common.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.TileThaumcraft;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.Consts.OwnedBlockEntityTagAccessors.OWNERS_ACCESSOR;

public class OwnedBlockEntity extends TileThaumcraft {
    public OwnedBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public OwnedBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.OWNED, blockPos, blockState);
    }
    public Set<String> owners = ConcurrentHashMap.newKeySet();

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        owners.addAll(OWNERS_ACCESSOR.readFromCompoundTag(compoundTag));
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        OWNERS_ACCESSOR.writeToCompoundTag(compoundTag, owners);
    }

}
