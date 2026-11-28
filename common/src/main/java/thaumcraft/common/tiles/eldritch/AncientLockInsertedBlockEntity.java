package thaumcraft.common.tiles.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.listeners.worldgen.eldritch.bossgen.ThaumcraftEldritchBossProvider;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.worldgenerated.eldritch.AncientLockEmptyBlock;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkleS2C;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static thaumcraft.common.lib.world.dim.cell.features.bossroom.AbstractBossRoom.BOSS_ROOM_INDEX;

public class AncientLockInsertedBlockEntity extends BlockEntity {
    protected int tickCount = System.identityHashCode(this) & 63;

    public AncientLockInsertedBlockEntity(BlockEntityType<? extends AncientLockInsertedBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public AncientLockInsertedBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ThaumcraftBlockEntities.BlockEntityTypeInstances.ANCIENT_LOCK_INSERTED(), blockPos, blockState);
    }
    
    public void tick() {
        if (this.level == null) return;
        tickCount += 1;
        if (this.tickCount % 5 == 0) {
            this.level.playSound(null,getBlockPos(), ThaumcraftSounds.PUMP, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        if (tickCount >= 100 && (!this.level.isClientSide)) {
            doBossSpawn();
        }
    }

    private void doBossSpawn() {
        if (this.level == null) {
            throw new RuntimeException("called doBossSpawn but level is null");
        }
        final var pos = this.getBlockPos();
        this.level.playSound(null,pos, ThaumcraftSounds.ICE, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!this.level.isClientSide && level instanceof ServerLevel serverLevel) {
            final var posX = pos.getX();
            final var posY = pos.getY();
            final var posZ = pos.getZ();
            int centerx = posX >> 4;
            int centerz = posZ >> 4;

            int exitIndex = getBlockState().getValue(BOSS_ROOM_INDEX);
            var dir = getBlockState().getValue(AncientLockEmptyBlock.FACING);
            var dirOp = dir.getOpposite();
            var dirCCWOp = dir.getCounterClockWise().getOpposite();
            var dirCWOp = dir.getClockWise().getOpposite();
            if (exitIndex == 0) {
                if (dir == Direction.NORTH) {
                    centerx += dirOp.getStepX() + dirCCWOp.getStepX();
                    centerz += dirOp.getStepZ() + dirCCWOp.getStepZ();
                }
                if (dir == Direction.WEST) {
                    centerx += dirOp.getStepX() + dirCWOp.getStepX();
                    centerz += dirOp.getStepZ() + dirCWOp.getStepZ();
                }
                if (dir == Direction.SOUTH){
                    centerz += dirOp.getStepZ() + dirCCWOp.getStepZ();
                }
                if (dir == Direction.EAST){
                    centerx += dirOp.getStepX() + dirCWOp.getStepX();
                }
            }
            if (exitIndex == 1) {
                if (dir == Direction.EAST) {
                    centerx += dirOp.getStepX() + dirCCWOp.getStepX();
                    centerz += dirOp.getStepZ() + dirCCWOp.getStepZ();
                }
                if (dir == Direction.NORTH) {
                    centerx += dirOp.getStepX() + dirCWOp.getStepX();
                    centerz += dirOp.getStepZ() + dirCWOp.getStepZ();
                }
                if (dir == Direction.SOUTH){
                    centerz += dirOp.getStepZ() + dirCCWOp.getStepZ();
                }
                if (dir == Direction.WEST){
                    centerx += dirOp.getStepX() + dirCWOp.getStepX();
                }
            }
            if (exitIndex == 2) {
                if (dir == Direction.SOUTH) {
                    centerx += dirOp.getStepX() + dirCCWOp.getStepX();
                    centerz += dirOp.getStepZ() + dirCCWOp.getStepZ();
                }
                if (dir == Direction.EAST) {
                    centerx += dirOp.getStepX() + dirCWOp.getStepX();
                    centerz += dirOp.getStepZ() + dirCWOp.getStepZ();
                }
                if (dir == Direction.NORTH) {
                    centerz += dirOp.getStepZ() + dirCCWOp.getStepZ();
                }
                if (dir == Direction.WEST){
                    centerx += dirOp.getStepX() + dirCWOp.getStepX();
                }
            }
            if (exitIndex == 3) {
                if (dir == Direction.WEST) {
                    centerx += dirOp.getStepX() + dirCCWOp.getStepX();
                    centerz += dirOp.getStepZ() + dirCCWOp.getStepZ();
                }
                if (dir == Direction.SOUTH) {
                    centerx += dirOp.getStepX() + dirCWOp.getStepX();
                    centerz += dirOp.getStepZ() + dirCWOp.getStepZ();
                }
                if (dir == Direction.NORTH) {
                    centerz += dirOp.getStepZ() + dirCCWOp.getStepZ();
                }
                if (dir == Direction.EAST){
                    centerx += dirOp.getStepX() + dirCWOp.getStepX();
                }
            }

            ThaumcraftEldritchBossProvider.generateEldritchBossRoom(level,pos,centerx, centerz, exitIndex);

            for(int a = -2; a <= 2; ++a) {
                for(int b = -2; b <= 2; ++b) {
                    for(int c = -2; c <= 2; ++c) {
                        var pickPos = pos.offset(a,b,c);
                        if (this.level.getBlockState(pickPos).is(ThaumcraftBlocks.Tags.ANCIENT_LOCK_ERASES)) {
                            new PacketFXBlockSparkleS2C(posX + a, posY + b, posZ + c, 4194368).sendToAllAround(serverLevel,pos,32*32);
                            this.level.setBlockAndUpdate(pickPos,Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }

            this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }

    }

}
