package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.ArcaneWorkbenchBlockEntity;

import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;

public class ArcaneWorkbenchBlock extends Block implements EntityBlock {
    //TODO:BER
    public ArcaneWorkbenchBlock(Properties properties) {
        super(properties);
    }
    public ArcaneWorkbenchBlock(){
        super(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArcaneWorkbenchBlockEntity(blockPos,blockState);
    }

    @Override
    public @NotNull InteractionResult use(
            BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult
    ) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof ArcaneWorkbenchBlockEntity arcaneWorkbench && player instanceof ServerPlayer serverPlayer) {
                openExtendedMenu(serverPlayer,arcaneWorkbench);
            }
            return InteractionResult.CONSUME;
        }
    }
}
