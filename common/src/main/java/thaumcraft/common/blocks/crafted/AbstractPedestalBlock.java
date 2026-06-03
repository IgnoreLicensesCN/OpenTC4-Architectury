package thaumcraft.common.blocks.crafted;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.AbstractPedestalBlockEntity;

@UtilityLikeAbstraction(reason = "lazy to write #use everywhere")
public abstract class AbstractPedestalBlock extends SuppressedWarningBlock implements EntityBlock {
    public AbstractPedestalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }


    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof Container container){
            Containers.dropContents(level,pos,container);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(blockPos) instanceof AbstractPedestalBlockEntity pedestal) {
                if (!pedestal.isEmpty()){
                    var centerPos = blockPos.above().getCenter();
                    pedestal.getInventory().forEach(stack -> dropItemStack(level,centerPos.x,centerPos.y,centerPos.z,stack));
                    playPickupSound(level,blockPos);
                } else if (player != null) {
                    var usingStack = player.getItemInHand(interactionHand);
                    if (!usingStack.isEmpty()){
                        var stackToPut = usingStack.split(1);
                        pedestal.setItem(0, stackToPut);
                    }
                    playPickupSound(level,blockPos);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void playPickupSound(Level level,BlockPos blockPos){
        level.playSound(
                null,
                blockPos,
                SoundEvents.ITEM_PICKUP,
                SoundSource.BLOCKS,
                0.2F,
                ((level.getRandom().nextFloat() - level.getRandom().nextFloat())
                        * 0.7F + 1.0F) * 1.5F);

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
}