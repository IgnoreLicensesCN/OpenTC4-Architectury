package thaumcraft.common.tiles.eldritch;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import thaumcraft.common.entities.monster.cultists.CultistClericEntity;
import thaumcraft.common.entities.monster.cultists.CultistEntity;
import thaumcraft.common.entities.monster.cultists.CultistKnightEntity;
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
                            spawnCultistGuards();
                        }
                    }else if (spawnType == 1) {
                        spawnEldritchGuardian();
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
                    cleric.restrictTo(altarPos, 8);
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

    private void spawnCultistGuards() {
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
                var selfPos = getBlockPos();
                var rand = level.random;
                var knight = new CultistKnightEntity(this.level);
                var pickPos = selfPos.offset(
                        MathHelper.getRandomIntegerInRange(rand, 4, 10) * (rand.nextBoolean()?0:1),
                        MathHelper.getRandomIntegerInRange(rand, 0, 3) * (rand.nextBoolean()?0:1),
                        MathHelper.getRandomIntegerInRange(rand, 4, 10) * (rand.nextBoolean()?0:1)
                );
                if (level.getBlockState(pickPos.below()).isFaceSturdy(level,pickPos,Direction.UP)) {
                    var knightBb = knight.getBoundingBox();
                    var knightWidth = knight.getBbWidth();
                    var knightHeight = knight.getBbHeight();
                    knight.setPos(pickPos.getX(),pickPos.getY(),pickPos.getZ());
                    if (level.getEntities(knight, knightBb,_ignored -> true).isEmpty()
                                    && !level.containsAnyLiquid(knightBb)
                    ) {
                        for (int i = 0; i < 20; ++i)
                        {
                            double d0 = rand.nextGaussian() * 0.02D;
                            double d1 = rand.nextGaussian() * 0.02D;
                            double d2 = rand.nextGaussian() * 0.02D;
                            double d3 = 10.0D;
                            level.addParticle(
                                    ParticleTypes.EXPLOSION,
                                    selfPos.getX() + (rand.nextFloat() * knightWidth * 2.0F) - knightWidth - d0 * d3,
                                    selfPos.getY() + (rand.nextFloat() * knightHeight) - d1 * d3,
                                    selfPos.getZ() + (rand.nextFloat() * knightWidth * 2.0F) - knightWidth - d2 * d3,
                                    d0, d1, d2
                            );
                        }
                        knight.restrictTo(selfPos, 16);
                        this.level.addFreshEntity(knight);
                    }
                }
            }
        }
    }

    private void spawnEldritchGuardian() {
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
