package thaumcraft.common.blocks.crafted.infusion;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.infusion.ArcanePedestalBlockEntity;

public class ArcanePedestalBlock extends SuppressedWarningBlock implements EntityBlock {
    public ArcanePedestalBlock(Properties properties) {
        super(properties);
    }
    public ArcanePedestalBlock() {
        this(
                Properties.of()
                        .strength(3,25)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
        );
    }

    public static final VoxelShape SHAPE = Shapes.box(0.25F, 0.0F, 0.25F, 0.75F, 0.99F, 0.75F);

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    public boolean isOcclusionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof Container container){
            Containers.dropContents(level,pos,container);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArcanePedestalBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(blockPos) instanceof ArcanePedestalBlockEntity pedestal) {
                if (!pedestal.isEmpty()){
                    var centerPos = blockPos.above().getCenter();
                    pedestal.getInventory().forEach(stack -> dropItemStack(level,centerPos.x,centerPos.y,centerPos.z,stack));
                }else if (player != null) {
                    var usingStack = player.getItemInHand(interactionHand);
                    if (!usingStack.isEmpty()){
                        var stackToPut = usingStack.split(1);
                        pedestal.setItem(0, stackToPut);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void dropItemStack(Level level, double d, double e, double f, ItemStack itemStack) {
        double g = EntityType.ITEM.getWidth();
        double h = 1.0 - g;
        double i = g / 2.0;
        double j = Math.floor(d) + level.random.nextDouble() * h + i;
        double k = Math.floor(e) + level.random.nextDouble() * h;
        double l = Math.floor(f) + level.random.nextDouble() * h + i;

        ItemEntity itemEntity = new ItemEntity(level, j, k, l, itemStack.split(itemStack.getCount()));
        level.addFreshEntity(itemEntity);
    }


    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        if (level.getBlockEntity(blockPos) instanceof Container container){
            return AbstractContainerMenu.getRedstoneSignalFromContainer(container);
        }
        return 0;
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos pos, int i, int j) {
        if (level.isClientSide) {
            if (level instanceof ClientLevel clientLevel){
                ClientFXUtils.blockSparkle(
                        clientLevel,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        0xb680ff,
                        2
                );

//                level.levelEvent(
//                        2001,
//                        pos,
//                        Block.getId(Blocks.STONE_BRICKS.defaultBlockState())
//                );
            }
        }
        return super.triggerEvent(blockState, level, pos, i, j);
    }
}
