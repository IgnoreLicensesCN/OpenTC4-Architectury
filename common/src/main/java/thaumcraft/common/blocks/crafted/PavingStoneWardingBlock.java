package thaumcraft.common.blocks.crafted;

import com.linearity.opentc4.clientrenderapi.IClientRandomTickableBlock;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.ThaumcraftBlocks;

public class PavingStoneWardingBlock extends Block implements IClientRandomTickableBlock {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public PavingStoneWardingBlock(Properties properties) {
        super(properties);
    }

    public PavingStoneWardingBlock() {
        super(Properties.copy(Blocks.STONE)
                .strength(2.f, 10.f)
        );
    }

    @Override
    public void onClientRandomTick(BlockState blockState, ClientLevel clientLevel, BlockPos blockPos) {

        var x = blockPos.getX();
        var y = blockPos.getY();
        var z = blockPos.getZ();
        var random = clientLevel.random;
        var posAbove = blockPos.above();
        var posAboveAbove = posAbove.above();
        var blockStateAbove = clientLevel.getBlockState(posAbove);
        var blockStateAboveAbove = clientLevel.getBlockState(posAboveAbove);

        if (clientLevel.hasNeighborSignal(blockPos)) {
            for (int a = 0; a < ClientFXUtils.particleCount(2); ++a) {
                ClientFXUtils.blockRunes(
                        clientLevel,
                        x,
                        (float) y + 0.7F,
                        z,
                        0.2F + random.nextFloat() * 0.4F,
                        random.nextFloat() * 0.3F,
                        0.8F + random.nextFloat() * 0.2F,
                        20,
                        -0.02F
                );
            }
        } else if (
//                    (clientLevel.getBlock(x, y + 1, z) != ConfigBlocks.blockAiry
//                    && clientLevel.getBlock(x, y + 1, z).getBlocksMovement(clientLevel, x, y + 1, z) )
//                    || (clientLevel.getBlock(x, y + 2, z) != ConfigBlocks.blockAiry
//                    && clientLevel.getBlock(x, y + 1, z).getBlocksMovement(clientLevel, x, y + 1, z))
            //yeah render rule changed,
            // but i dont want to make a set called blockAirySet
            // and set some blocks seems no relations into it.
            // that SHOULD be called GARBAGE.
                blockStateAbove.getCollisionShape(clientLevel, posAboveAbove)
                        .isEmpty()
                        || blockStateAboveAbove.getCollisionShape(clientLevel, posAboveAbove)
                        .isEmpty()
        ) {
            for (int a = 0; a < ClientFXUtils.particleCount(3); ++a) {
                ClientFXUtils.blockRunes(
                        clientLevel,
                        x,
                        (float) y + 0.7F,
                        z,
                        0.9F + random.nextFloat() * 0.1F,
                        random.nextFloat() * 0.3F,
                        random.nextFloat() * 0.3F,
                        24,
                        -0.02F
                );
            }
        } else {
            AABB box = new AABB(
                    x, y, z,
                    x + 1, y + 1, z + 1
            ).inflate(1.0D);
            clientLevel.getEntitiesOfClass(
                            LivingEntity.class,
                            box,
                            e -> !(e instanceof Player)
                    )
                    .stream()
                    .findFirst()
                    .ifPresent(entity -> {
                        ClientFXUtils.blockRunes(
                                clientLevel, x,
                                (float) y + 0.6F + random.nextFloat() * Math.max(0.8F, entity.getEyeHeight()), z,
                                0.6F + random.nextFloat() * 0.4F, 0.0F, 0.3F + random.nextFloat() * 0.7F, 20, 0.0F
                        );
                    });
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        boolean hasSignal = blockPlaceContext.getLevel().hasNeighborSignal(blockPlaceContext.getClickedPos());
        return defaultBlockState().setValue(POWERED,hasSignal);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    public boolean isCharged(BlockState state) {
        return state.getValue(POWERED);
    }
}
