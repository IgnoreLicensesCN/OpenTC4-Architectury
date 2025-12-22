package thaumcraft.common.tiles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import java.util.concurrent.atomic.AtomicInteger;

import static thaumcraft.common.blocks.crafted.PavingStoneWardingBlock.LIT;

public class WardingStoneBlockEntity extends BlockEntity implements EntityBlock {
    private final AtomicInteger tickCounter = new AtomicInteger();
    public WardingStoneBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }

    public WardingStoneBlockEntity(BlockPos blockPos, BlockState blockState){
        super(ThaumcraftBlockEntities.WARDING_STONE, blockPos, blockState);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WardingStoneBlockEntity(blockPos, blockState);
    }

    @Override
    @NotNull
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (level1 instanceof ServerLevel serverLevel && blockEntity instanceof WardingStoneBlockEntity){
                if (tickCounter.get() == 0){
                    tickCounter.set(level1.random.nextInt(100));
                }
                boolean charged = level1.hasNeighborSignal(blockPos);

                if (tickCounter.get() % 5 == 0 && !charged) {

                    var x = blockPos.getX();
                    var y = blockPos.getY();
                    var z = blockPos.getZ();
                    AABB box = new AABB(
                            x, y, z,
                            x + 1, y + 3, z + 1
                    ).inflate(0.1D);
                    serverLevel.getEntitiesOfClass(
                            LivingEntity.class,
                            box,
                            e -> !(e instanceof Player) && !e.onGround()
                    ).forEach(e -> e.addDeltaMovement(
                            new Vec3(-MathHelper.sin((e.getYRot() + 180.0F) * (float)Math.PI / 180.0F) * 0.2F,
                                    -0.1,
                                    MathHelper.cos((e.getYRot() + 180.0F) * (float)Math.PI / 180.0F) * 0.2F)
                    ));
                }
                if (tickCounter.incrementAndGet() % 100 == 0){
                    var poses = new BlockPos[]{blockPos.above(), blockPos.above().above()};
                    for (var pos:poses){
                        var bState = serverLevel.getBlockState(pos);
                        if (bState.canBeReplaced() && bState.getBlock() != ThaumcraftBlocks.WARDING_AURA){
                            serverLevel.setBlock(pos,ThaumcraftBlocks.WARDING_AURA.defaultBlockState(),3);
                        }
                    }
                }

                if (blockState.getValue(LIT) && !charged) {
                    serverLevel.setBlock(blockPos, blockState.setValue(LIT, false), 3);
                }
                else if (!blockState.getValue(LIT) && charged) {
                    serverLevel.setBlock(blockPos, blockState.setValue(LIT, true), 3);
                }
            }
        };
    }
}
