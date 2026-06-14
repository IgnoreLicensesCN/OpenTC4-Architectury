package thaumcraft.common.tiles.technique;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.owned.OwnedBlockEntity;

import static com.linearity.opentc4.Consts.WardedBlockTagAccessors.STORING_BLOCK_STATE;

public class WardedBlockEntity extends OwnedBlockEntity {
    public WardedBlockEntity(BlockEntityType<? extends WardedBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public WardedBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.WARDED_BLOCK(),blockPos, blockState);
    }

    protected @NotNull BlockState storingState = Blocks.AIR.defaultBlockState();
    protected @Nullable CompoundTag tagToRead = null;

    public @NotNull BlockState getStoringState() {
        return storingState;
    }

    public void setStoringState(@NotNull BlockState storingState) {
        this.storingState = storingState;
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        if (level == null) {
            tagToRead = compoundTag;
            return;
        }
        storingState = STORING_BLOCK_STATE.readFromCompoundTag(this.level.holderLookup(Registries.BLOCK),compoundTag);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);

        if (this.level != null && tagToRead != null) {
            storingState = STORING_BLOCK_STATE.readFromCompoundTag(this.level.holderLookup(Registries.BLOCK),tagToRead);
            tagToRead = null;
        }
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        STORING_BLOCK_STATE.writeToCompoundTag(compoundTag,storingState);
    }
}
