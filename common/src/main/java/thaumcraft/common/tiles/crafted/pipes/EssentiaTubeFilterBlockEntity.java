package thaumcraft.common.tiles.crafted.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.*;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.EssentiaTubeFilterBlockEntityTagAccessors.ASPECT_FILTER;

public class EssentiaTubeFilterBlockEntity
        extends EssentiaTubeBlockEntity
        implements IAspectFilterAccessible,
        IAspectDisplayBlockEntity<Aspect> {
    private @NotNull("null -> empty") Aspect filter = Aspects.EMPTY;
    public EssentiaTubeFilterBlockEntity(BlockEntityType<? extends EssentiaTubeFilterBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public EssentiaTubeFilterBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_TUBE_FILTER,blockPos, blockState);
    }

    @Override
    protected void calculateSuction(@NotNull("empty -> any") Aspect filter, boolean restrict, @Nullable("null -> any") Direction limitedFacingToAnotherBE) {
        super.calculateSuction(filter.isEmpty()?this.filter:filter, restrict, limitedFacingToAnotherBE);
    }

    @Override
    public @NotNull Aspect getAspectFilter() {
        return filter;
    }

    @Override
    public boolean setAspectFilter(@NotNull Aspect aspectFilter) {
        this.filter = aspectFilter;
        return true;
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        this.filter = ASPECT_FILTER.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ASPECT_FILTER.writeToCompoundTag(compoundTag, this.filter);
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, Direction fromDirection) {
        if (aspect != this.filter && !this.filter.isEmpty()){
            return 0;
        }
        return super.addEssentia(aspect, amount, fromDirection);
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {
        if (aspect != this.filter && !this.filter.isEmpty()){
            return;
        }
        super.setSuction(aspect, amount);
    }

    @Override
    public @NotNull Aspect getEssentiaType(@NotNull Direction face) {
        var defaultResult = super.getEssentiaType(face);
        if (defaultResult.isEmpty()){
            return this.filter;
        }
        return defaultResult;
    }

    @Override
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay() {
        return aspToDisplay;
    }
    private final UnmodifiableSingleAspectListFromSupplier<Aspect> aspToDisplay = new UnmodifiableSingleAspectListFromSupplier<>(
            () -> this.owningAspect.isEmpty()?this.filter:this.owningAspect,() -> this.owningAspect.isEmpty()?0:1
    );

}
