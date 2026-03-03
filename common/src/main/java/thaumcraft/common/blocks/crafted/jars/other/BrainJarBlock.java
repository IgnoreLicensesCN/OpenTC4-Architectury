package thaumcraft.common.blocks.crafted.jars.other;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.common.blocks.crafted.jars.JarBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.jars.BrainJarBlockEntity;

public class BrainJarBlock extends JarBlock implements EntityBlock {
    public BrainJarBlock(Properties properties) {
        super(properties);
    }
    public BrainJarBlock(){
        this(JAR_PROPERTIES);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this) {
            return new BrainJarBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.BRAIN_JAR) {
            if (level.isClientSide()) {
                return ((level1, blockPos, blockState1, blockEntity) -> {
                    if (blockEntity instanceof BrainJarBlockEntity brainJar) {
                        brainJar.clientTick();
                    }
                });
            }
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof BrainJarBlockEntity brainJar) {
                    brainJar.serverTick();
                }
            });
        }
        return null;
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        var be = level.getBlockEntity(blockPos);
        if (be instanceof BrainJarBlockEntity brainJar) {
            if (level.isClientSide()) {
                playJarSound(level,blockPos,.2f);
                return InteractionResult.SUCCESS;
            }
            brainJar.eatDelay = 40;


            int var6 = level.random.nextInt(Math.min(brainJar.xp + 1, 64));
            if (var6 > 0) {
                brainJar.xp -= var6;
                int xp = var6;

                while (xp > 0) {
                    int var2 = ExperienceOrb.getExperienceValue(xp);
                    xp -= var2;
                    var addToPos = blockPos.getCenter();
                    level.addFreshEntity(new ExperienceOrb(level, addToPos.x,addToPos.y,addToPos.z, var2));
                }
                brainJar.markDirtyAndUpdateSelf();
            }
            return InteractionResult.CONSUME;
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState arg) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return !(level.getBlockEntity(pos) instanceof IValueContainerBasedComparatorSignalProviderBlockEntity signalProvider)
                ? 0 : signalProvider.getComparatorSignal();
    }
}
