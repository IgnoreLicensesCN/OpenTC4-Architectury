package thaumcraft.common.items.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IAttackBlockListenerItem {
    InteractionResult onLeftClickBlock(Player player, Level level, InteractionHand interactionHand, BlockPos blockPos, Direction direction);
}
