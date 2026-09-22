package thaumcraft.common.tiles.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.listeners.worldgen.eldritch.bossgen.ThaumcraftEldritchBossProvider;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkleS2C;
import thaumcraft.common.lib.world.dim.*;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

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
    
    //TODO:below
    private void doBossSpawn() {
        if (this.level == null) {
            throw new RuntimeException("called doBossSpawn but level is null");
        }
        this.level.playSound(null,getBlockPos(), ThaumcraftSounds.ICE, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!this.level.isClientSide) {
            final var pos = this.getBlockPos();
            final var posX = pos.getX();
            final var posY = pos.getY();
            final var posZ = pos.getZ();
            int cx = posX >> 4;
            int cz = posZ >> 4;
            int centerx = posX >> 4;
            int centerz = posZ >> 4;
            int exit = 0;

            for(int a = -2; a <= 2; ++a) {
                for(int b = -2; b <= 2; ++b) {
                    Cell c = MazeHandler.getFromHashMap(new CellLoc(cx + a, cz + b));
                    if (c != null && c.feature == 2) {
                        centerx = cx + a;
                        centerz = cz + b;
                    }

                    if (c != null && c.feature >= 2 && c.feature <= 5 && (c.north || c.south || c.east || c.west)) {
                        exit = c.feature;
                    }
                }
            }

//            MapBossData mbd = (MapBossData)this.level.loadItemData(MapBossData.class, "BossMapData");
//            if (mbd == null) {
//                mbd = new MapBossData("BossMapData");
//                mbd.bossCount = 0;
//                mbd.markDirty();
//                this.level.setItemData("BossMapData", mbd);
//            }
//
//            ++mbd.bossCount;
//            if (this.level.random.nextFloat() < 0.25F) {
//                ++mbd.bossCount;
//            }
//
//            mbd.markDirty();

            ThaumcraftEldritchBossProvider.generateEldritchBossRoom(level,pos,centerx, centerz, exit);

            for(int a = -2; a <= 2; ++a) {
                for(int b = -2; b <= 2; ++b) {
                    for(int c = -2; c <= 2; ++c) {
                        var pickPos = pos.offset(a,b,c);
                        if (this.level.getBlockState(pickPos).is(ThaumcraftBlocks.Tags.ANCIENT_LOCK_ERASES)) {
                            if (level instanceof ServerLevel serverLevel) {
                                new PacketFXBlockSparkleS2C(posX + a, posY + b, posZ + c, 4194368).sendToAllAround(serverLevel,pos,32*32);
                            }
                            this.level.setBlockAndUpdate(pickPos,Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }

            this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }

    }

}
