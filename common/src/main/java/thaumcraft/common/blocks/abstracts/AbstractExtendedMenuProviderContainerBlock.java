package thaumcraft.common.blocks.abstracts;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;

public abstract class AbstractExtendedMenuProviderContainerBlock extends AbstractContainerBlock {
    public AbstractExtendedMenuProviderContainerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (level.getBlockEntity(blockPos) instanceof ExtendedMenuProvider menuProvider) {
                openExtendedMenu(serverPlayer,menuProvider);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
