package thaumcraft.common.blocks.abstracts;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.nodes.INodeBlock;
import thaumcraft.api.nodes.INodeBlockEntity;
import thaumcraft.common.ClientFXUtils;

import static thaumcraft.common.items.ThaumcraftItemInstances.WISP_ESSENCE;


@UtilityLikeAbstraction
public abstract class AbstractNodeBlock extends SuppressedWarningBlock implements EntityBlock, INodeBlock {
    public AbstractNodeBlock(Properties properties) {
        super(properties);
    }


    public static void nodeBlockOnRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean isMoving
    ) {
        if (level.isClientSide) {
            if (level instanceof ClientLevel clientLevel && state.getBlock() != newState.getBlock()) {
                var x = pos.getX();
                var y = pos.getY();
                var z = pos.getZ();
                ClientFXUtils.burst(clientLevel, x + 0.5, y + 0.5, z + 0.5, 1.0F);
            }
        } else {
            if (newState.isAir()) {
                if (level.getBlockEntity(pos) instanceof INodeBlockEntity nodeBE) {
                    var aspectsOwning = nodeBE.getAspects();
                    if (!aspectsOwning.isEmpty()) {
                        aspectsOwning.forEach(
                                (aspect, amount) -> {
                                    if (amount > 0) {
                                        int dropCount = Math.min(amount / 10, 1);
                                        var x = pos.getX();
                                        var y = pos.getY();
                                        var z = pos.getZ();
                                        var toDrop = WISP_ESSENCE().ofAspect(aspect);
                                        toDrop.setCount(dropCount);
                                        Containers.dropItemStack(
                                                level,
                                                x + 0.5, y + 0.5, z + 0.5,
                                                toDrop
                                        );
                                    }
                                }
                        );
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean isMoving
    ) {
        nodeBlockOnRemove(state, level, pos, newState, isMoving);
        super.onRemove(state, level, pos, newState, isMoving);
    }

}
