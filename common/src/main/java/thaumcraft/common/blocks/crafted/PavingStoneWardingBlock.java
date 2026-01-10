package thaumcraft.common.blocks.crafted;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.WardingStoneBlockEntity;

public class PavingStoneWardingBlock extends Block implements EntityBlock {
    public PavingStoneWardingBlock(Properties properties) {
        super(properties);
    }

    public PavingStoneWardingBlock() {
        super(Properties.copy(Blocks.STONE)
                .strength(2.f, 10.f)
        );
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WardingStoneBlockEntity(blockPos, blockState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType != ThaumcraftBlockEntities.WARDING_STONE){
            return null;
        }
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (level1 instanceof ServerLevel serverLevel && blockEntity instanceof WardingStoneBlockEntity wardingStoneBlockEntity){
                if (wardingStoneBlockEntity.tickCounter.get() == 0){
                    wardingStoneBlockEntity.tickCounter.set(level1.random.nextInt(100));
                }
                boolean charged = level1.hasNeighborSignal(blockPos);

                if (wardingStoneBlockEntity.tickCounter.get() % 5 == 0 && !charged) {

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
                if (wardingStoneBlockEntity.tickCounter.incrementAndGet() % 100 == 0){
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


    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {

        if (!(level instanceof ClientLevel clientLevel)){return;}

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


    //redstone lamp
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        return this.defaultBlockState().setValue(LIT, arg.getLevel().hasNeighborSignal(arg.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    public boolean isCharged(BlockState state) {
        return state.getValue(LIT);
    }


    @Override
    public void neighborChanged(BlockState arg, Level arg2, BlockPos arg3, Block arg4, BlockPos arg5, boolean bl) {
        if (!arg2.isClientSide) {
            boolean bl2 = arg.getValue(LIT);
            if (bl2 != arg2.hasNeighborSignal(arg3)) {
                if (bl2) {
                    arg2.scheduleTick(arg3, this, 4);
                } else {
                    arg2.setBlock(arg3, arg.cycle(LIT), 2);
                }
            }
        }
    }
    @Override
    public void tick(BlockState arg, ServerLevel arg2, BlockPos arg3, RandomSource arg4) {
        if (arg.getValue(LIT) && !arg2.hasNeighborSignal(arg3)) {
            arg2.setBlock(arg3, arg.cycle(LIT), 2);
        }
    }
}
