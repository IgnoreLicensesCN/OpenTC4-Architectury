package thaumcraft.common.tiles.generated;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableSingleAspectListFromSupplier;
import thaumcraft.api.listeners.manabean.ManaBeanGrowContext;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.blocks.worldgenerated.ManaBeanBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.ManaBeanBlockEntityOrItemStackTagAccessors.OWNING_ASPECT;
import static thaumcraft.api.listeners.manabean.ManaBeanGrowthManager.onGrowStageChanged;

public class ManaBeanBlockEntity extends TileThaumcraft
        implements IAspectDisplayBlockEntity<Aspect>,
        UnmodifiableSingleAspectListFromSupplier.SingleAspectAndAmountSupplier<Aspect> {
    public ManaBeanBlockEntity(BlockEntityType<? extends ManaBeanBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public ManaBeanBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.MANA_BEAN, blockPos, blockState);
    }
    private @NotNull Aspect aspectOwning = Aspects.EMPTY;
    private final AspectList<Aspect> aspectListViewOfSingle = new UnmodifiableSingleAspectListFromSupplier<>(
            this
    );

    @Override
    public Aspect getAspectAsSupplier() {
        return this.aspectOwning;
    }

    @Override
    public int getAmountAsSupplier() {
        return 1;
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        aspectOwning = OWNING_ASPECT.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        OWNING_ASPECT.writeToCompoundTag(compoundTag, aspectOwning);
    }

    public void setAspectOwning(@NotNull Aspect aspectOwning) {
        this.aspectOwning = aspectOwning;
    }

    public @NotNull Aspect getAspectOwning() {
        return aspectOwning;
    }

    @Override
    public @UnmodifiableView @NotNull AspectList<Aspect> getAspectsToDisplay() {
        return aspectListViewOfSingle;
    }

    public void randomTick() {
        if (this.level == null) {
            return;
        }
        var blockState = this.getBlockState();
        int stage = blockState.getValue(ManaBeanBlock.STAGE);
        if (stage < ManaBeanBlock.STAGE_MAX && this.level.random.nextInt(30) == 0) {
            setBlockStateAndUpdate(blockState.setValue(ManaBeanBlock.STAGE, stage+1));
            onGrowStageChanged(
                    new ManaBeanGrowContext(
                            this,
                            stage + 1,
                            ManaBeanBlock.STAGE_MIN,
                            ManaBeanBlock.STAGE_MAX,
                            Direction.UP
                    )
            );
        }
    }

}
