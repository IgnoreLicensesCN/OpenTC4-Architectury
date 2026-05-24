package thaumcraft.common.blocks.crafted.loot;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CrateLootBlock extends DefaultAbstractLootBlock {
    public CrateLootBlock(Properties properties) {
        super(properties);
    }
    public CrateLootBlock() {
        this(Properties.of().noOcclusion().strength(0.15F,0).sound(SoundType.WOOD));
    }
    public static final VoxelShape SHAPE = Block.box(1,0,1,15,14,15);

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }
}
