package thaumcraft.common.tiles.crafted.essentiabe.jars;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class VoidJarBlockEntity extends EssentiaJarBlockEntity {

    public VoidJarBlockEntity(BlockEntityType<? extends VoidJarBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public VoidJarBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.VOID_JAR,blockPos, blockState);
    }

    @Override
    protected int getAspectFilterSuctionAddition() {
        return 16;
    }


    @Override
    public int addEssentia(@NotNull Aspect aspect, int amount, @NotNull Direction fromDirection) {
        if (!canAddEssentia(aspect,amount,fromDirection)) {
            return 0;
        }
        if (capacityFullForAddEssentia()){
            return amount;//what differs
        }
        return doAddEssentia(aspect, amount);
    }
}
