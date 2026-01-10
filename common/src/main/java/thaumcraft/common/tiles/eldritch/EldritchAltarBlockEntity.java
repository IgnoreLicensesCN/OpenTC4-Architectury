package thaumcraft.common.tiles.eldritch;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.blocks.worldgenerated.eldritch.EldritchAltarBlock;
import thaumcraft.common.entities.monster.EntityCultist;
import thaumcraft.common.entities.monster.EntityCultistCleric;
import thaumcraft.common.entities.monster.EntityCultistKnight;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.List;

public class EldritchAltarBlockEntity extends TileThaumcraft {
    public EldritchAltarBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EldritchAltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ELDRITCH_ALTAR, blockPos, blockState);
    }

    public int tickCount = 0;

    public void serverTick() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        tickCount += 1;
        if (tickCount >= 80) {
            if (tickCount % 40 == 0){
                BlockState state = serverLevel.getBlockState(this.getBlockPos());
                if (state.getValue(EldritchAltarBlock.IS_SPAWNER)){
                    int spawnType = state.getValue(EldritchAltarBlock.SPAWNER_TYPE);
                    if (spawnType == 0) {
                        boolean spawnedClerics = state.getValue(EldritchAltarBlock.SPAWNED_CLERICS);
                        if (!spawnedClerics) {
                            spawnClerics();
                        }else {
                            spawnGuards();
                        }
                    }else if (spawnType == 1) {
                        spawnGuardian();
                    }
                }
            }
        }
        //TODO
    }

    private void spawnClerics() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        int success = 0;

        for (int a = 0; a < 4; ++a) {
            int xx = 0;
            int zz = 0;
            switch (a) {
                case 0:
                    xx = -2;
                    zz = -2;
                    break;
                case 1:
                    xx = -2;
                    zz = 2;
                    break;
                case 2:
                    xx = 2;
                    zz = -2;
                    break;
                case 3:
                    xx = 2;
                    zz = 2;
            }

            EntityCultistCleric cleric = new EntityCultistCleric(this.level);
            if (World.doesBlockHaveSolidTopSurface(this.level, this.xCoord + xx, this.yCoord - 1, this.zCoord + zz)) {
                cleric.setPosition((double) this.xCoord + (double) 0.5F + (double) xx, this.yCoord, (double) this.zCoord + (double) 0.5F + (double) zz);
                if (this.level().checkNoEntityCollision(cleric.boundingBox) && this.level().getCollidingBoundingBoxes(cleric, cleric.boundingBox).isEmpty() && !this.level().isAnyLiquid(cleric.boundingBox)) {
                    cleric.setHomeArea(this.xCoord, this.yCoord, this.zCoord, 8);
                    cleric.onSpawnWithEgg(null);
                    cleric.spawnExplosionParticle();
                    if (this.level.addFreshEntity(cleric)) {
                        ++success;
                        cleric.setIsRitualist(true);
                    }
                }
            }
        }

        if (success > 2) {
            BlockState state = serverLevel.getBlockState(this.getBlockPos());
            state.setValue(EldritchAltarBlock.SPAWNED_CLERICS,true);
            serverLevel.setBlock(this.getBlockPos(),state,3);
        }

    }

    private void spawnGuards() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        List ents = this.level.getEntitiesWithinAABB(EntityCultistCleric.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(24.0F, 16.0F, 24.0F));
        if (ents.isEmpty()) {
            BlockState state = serverLevel.getBlockState(this.getBlockPos());
            state.setValue(EldritchAltarBlock.IS_SPAWNER,false);
            serverLevel.setBlock(this.getBlockPos(),state,3);
        } else {
            ents = this.level.getEntitiesWithinAABB(
                    EntityCultist.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(24.0F, 16.0F, 24.0F));
            if (ents.size() < 8) {
                EntityCultistKnight eg = new EntityCultistKnight(this.level());
                int i1 = this.xCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
                int j1 = this.yCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 0, 3) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
                int k1 = this.zCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
                if (World.doesBlockHaveSolidTopSurface(this.level(), i1, j1 - 1, k1)) {
                    eg.setPosition(i1, j1, k1);
                    if (this.level().checkNoEntityCollision(eg.boundingBox) && this.level().getCollidingBoundingBoxes(eg, eg.boundingBox).isEmpty() && !this.level().isAnyLiquid(eg.boundingBox)) {
                        eg.onSpawnWithEgg(null);
                        eg.spawnExplosionParticle();
                        eg.setHomeArea(this.xCoord, this.yCoord, this.zCoord, 16);
                        this.level.addFreshEntity(eg);
                    }
                }
            }

        }
    }

    private void spawnGuardian() {
        EntityEldritchGuardian eg = new EntityEldritchGuardian(this.level());
        int i1 = this.xCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
        int j1 = this.yCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 0, 3) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
        int k1 = this.zCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
        if (World.doesBlockHaveSolidTopSurface(this.level(), i1, j1 - 1, k1)) {
            eg.setPosition(i1, j1, k1);
            if (eg.getCanSpawnHere()) {
                eg.onSpawnWithEgg(null);
                eg.spawnExplosionParticle();
                eg.setHomeArea(this.xCoord, this.yCoord, this.zCoord, 16);
                this.level.addFreshEntity(eg);
            }
        }

    }
}
