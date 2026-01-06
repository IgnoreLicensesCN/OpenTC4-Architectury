package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.blockapi.IEntityInLavaBlock;

public class InfernalFurnaceLavaBlock extends AbstractInfernalFurnaceComponent implements IEntityInLavaBlock, EntityBlock {

    public static final VoxelShape STABLE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    public InfernalFurnaceLavaBlock(Properties properties) {
        super(properties);
    }
    public InfernalFurnaceLavaBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.FIRE)
                .replaceable()
                .noCollission()
                .strength(10.0F,500.0F)
                .lightLevel(blockStatex -> 13)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .sound(SoundType.EMPTY)
        );
    }


    public static final BlockPos CENTER_POS_RELATED_FROM_1_1_1 = new BlockPos(1,0,0);
    public static final BlockPos SELF_POS_1_1_1 = new BlockPos(1,1,1);
    @Override
    public BlockPos findMultipartCheckerPosRelatedToSelf(Level level, BlockState state, BlockPos pos) {
        return CENTER_POS_RELATED_FROM_1_1_1;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return STABLE_SHAPE;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    public BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        return SELF_POS_1_1_1;
    }

    @Override
    public void recoverToOriginalBlock(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
        }
    }

    @Override
    public boolean consideredAsLava(BlockState state, Level level, BlockPos pos, @Nullable Entity entityCollideWith) {
        return !considerEntityAsItem(entityCollideWith);
    }

    public static boolean considerEntityAsItem(@Nullable Entity entity){
        return entityAsItemStack(entity) != null;
    }

    public static @Nullable ItemStack entityAsItemStack(Entity entity){
        if (entity instanceof ItemEntity itemEntity){
            return itemEntity.getItem();
        }
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState0, BlockEntityType<T> blockEntityType0) {
        return ((level, blockPos, blockState, blockEntity) ->
        );
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        var itemStack = entityAsItemStack(entity);
        if (itemStack != null) {
            //TODO:store into BlockEntity if can and throw out(yes,new feature) if cant.
            entity.makeStuckInBlock(blockState, new Vec3(0.25, 0.05F, 0.25));
        }
    }

    //===========from lava============
    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return super.getVisualShape(blockState, blockGetter, blockPos, collisionContext);
    }
    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        BlockPos blockpos = blockPos.above();
        if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
            if (randomSource.nextInt(100) == 0) {
                double d0 = blockPos.getX() + randomSource.nextDouble();
                double d1 = blockPos.getY() + 1.0;
                double d2 = blockPos.getZ() + randomSource.nextDouble();
                level.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0, 0.0, 0.0);
                level.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + randomSource.nextFloat() * 0.2F, 0.9F + randomSource.nextFloat() * 0.15F, false);
            }

            if (randomSource.nextInt(200) == 0) {
                level.playLocalSound(
                        blockPos.getX(),
                        blockPos.getY(),
                        blockPos.getZ(),
                        SoundEvents.LAVA_AMBIENT,
                        SoundSource.BLOCKS,
                        0.2F + randomSource.nextFloat() * 0.2F,
                        0.9F + randomSource.nextFloat() * 0.15F,
                        false
                );
            }
        }
    }
}
