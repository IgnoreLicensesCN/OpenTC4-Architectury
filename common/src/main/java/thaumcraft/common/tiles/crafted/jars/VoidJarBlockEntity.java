package thaumcraft.common.tiles.crafted.jars;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class VoidJarBlockEntity extends EssentiaJarBlockEntity{

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
    public int addEssentia(Aspect aspect, int amount, Direction fromDirection) {
        if (!canAddEssentia(aspect,amount,fromDirection)) {
            return 0;
        }
        if (capacityFullForAddEssentia()){
            return amount;//what differs
        }
        return doAddEssentia(aspect, amount);
    }
}
