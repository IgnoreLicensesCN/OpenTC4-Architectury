package thaumcraft.common.tiles.eldritch;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import thaumcraft.common.entities.monster.cultists.CultistClericEntity;
import thaumcraft.common.entities.monster.cultists.CultistEntity;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.blocks.worldgenerated.eldritch.EldritchAltarBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.List;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.ENTITY_TEST;

public class EldritchAltarBlockEntity extends TileThaumcraft {
    public EldritchAltarBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EldritchAltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.ELDRITCH_ALTAR(), blockPos, blockState);
    }

    protected int tickCount = System.identityHashCode(this) & 63;

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
    }

    private void spawnClerics() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        int success = 0;
        var altarPos = this.getBlockPos();
        for (int a = 0; a < 4; ++a) {
            int xx = 0;
            int zz = switch (a) {
                case 0 -> {
                    xx = -2;
                    yield -2;
                }
                case 1 -> {
                    xx = -2;
                    yield 2;
                }
                case 2 -> {
                    xx = 2;
                    yield -2;
                }
                case 3 -> {
                    xx = 2;
                    yield 2;
                }
                default -> 0;
            };

            var cleric = new CultistClericEntity(this.level);
            var pickPos = altarPos.offset(xx,-1,zz);
            if (level.getBlockState(pickPos).isFaceSturdy(level,pickPos, Direction.UP)) {
                cleric.setPos(pickPos.above().getCenter());
                var bb = cleric.getBoundingBox();
                if (this.level.getEntities(ENTITY_TEST,bb,_ignored -> true).isEmpty()
                        && !this.level.containsAnyLiquid(bb)) {
                    cleric.setHomeArea(altarPos, 8);
//                    cleric.spawnExplosionParticle();
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
            serverLevel.setBlockAndUpdate(this.getBlockPos(),state);
        }

    }

    private void spawnGuards() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        var checkingAABB = new AABB(getBlockPos()).inflate(24.0F, 16.0F, 24.0F);
        List<? extends CultistEntity> ents = this.level.getEntitiesOfClass(CultistClericEntity.class, checkingAABB);
        if (ents.isEmpty()) {
            BlockState state = serverLevel.getBlockState(this.getBlockPos());
            state.setValue(EldritchAltarBlock.IS_SPAWNER,false);
            serverLevel.setBlock(this.getBlockPos(),state,3);
        } else {
            ents = this.level.getEntitiesOfClass(CultistEntity.class,checkingAABB);
            if (ents.size() < 8) {
                var eg = new EntityCultistKnight(this.level);
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
