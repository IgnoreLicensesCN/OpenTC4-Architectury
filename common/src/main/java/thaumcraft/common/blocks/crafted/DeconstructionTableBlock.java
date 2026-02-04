package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.DeconstructionTableBlockEntity;

import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;

public class DeconstructionTableBlock extends Block implements EntityBlock {
    public DeconstructionTableBlock(Properties properties) {
        super(properties);
    }

    public DeconstructionTableBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE));
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DeconstructionTableBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(
            BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult
    ) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof DeconstructionTableBlockEntity deconstructionTable && player instanceof ServerPlayer serverPlayer) {
                openExtendedMenu(serverPlayer,deconstructionTable);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.DECONSTRUCTION_TABLE) {

            return (level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof DeconstructionTableBlockEntity deconstructionTable) {
                    deconstructionTable.tick();
                }
            };
        }
        return null;
    }
}
