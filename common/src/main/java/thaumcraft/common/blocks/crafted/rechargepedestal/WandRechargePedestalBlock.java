package thaumcraft.common.blocks.crafted.rechargepedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.AbstractPedestalBlock;
import thaumcraft.common.tiles.crafted.WandRechargePedestalBlockBlockEntity;

public class WandRechargePedestalBlock extends AbstractPedestalBlock {
    public WandRechargePedestalBlock(Properties properties) {
        super(properties);
    }
    public WandRechargePedestalBlock() {
        this(
                Properties.of()
                        .strength(3,25)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
        );
    }
    public static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F),
            Shapes.or(
                    Shapes.box(0.25F, 0.5F, 0.25F, 0.75F, 1.0F, 0.75F),
                    Shapes.box(0.125F, 0.25F, 0.125F, 0.875F, 0.5F, 0.875F)
            )
    );

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WandRechargePedestalBlockBlockEntity(blockPos,blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide){
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof WandRechargePedestalBlockBlockEntity rechargePedestal) {
                    rechargePedestal.serverTick();
                }
            });
        }
        return null;//TODO:Client side logic
    }
}
