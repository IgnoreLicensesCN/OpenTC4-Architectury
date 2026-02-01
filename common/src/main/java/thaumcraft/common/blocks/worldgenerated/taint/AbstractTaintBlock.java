package thaumcraft.common.blocks.worldgenerated.taint;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.liquid.FiniteLiquidBlock;
import thaumcraft.common.entities.EntityFallingTaint;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.biomes.BiomeUtils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;

import static thaumcraft.common.blocks.worldgenerated.taint.AbstractTaintFibreBlock.spreadFibres;

public abstract class AbstractTaintBlock extends Block implements ITaintMaterial{
    public static final SoundType TAINT_BLOCK_SOUND = new SoundType(
            1.0F, 1.0F,
            SoundEvents.GRAVEL_BREAK,
            ThaumcraftSounds.GORE,
            SoundEvents.GRAVEL_PLACE,
            SoundEvents.GRAVEL_HIT,
            SoundEvents.GRAVEL_FALL
    );
    public AbstractTaintBlock(BlockBehaviour.Properties properties) {
        super(properties.randomTicks());
    }
    public AbstractTaintBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .randomTicks()
                        .strength(2,10)
                        .sound(TAINT_BLOCK_SOUND)
        );
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        super.randomTick(blockState, world, blockPos, random);
        if (Platform.getEnvironment() != Env.CLIENT) {
            BiomeUtils.taintBiomeSpread(world, blockPos, random, this);
            beforeSpreadingFibres(blockState, world, blockPos, random);

            var considerSpreadFibresPos = blockPos.offset(random.nextInt(3) - 1,random.nextInt(3) - 1,random.nextInt(3) - 1);
            if (world.getBiome(considerSpreadFibresPos).is(ThaumcraftBiomeIDs.TAINT_ID)) {
                spreadFibres(world, considerSpreadFibresPos);
                afterSpreadFibres(blockState, world, blockPos, random);

            } else {
                onBlockOutOfTaintBiome(blockState, world, blockPos, random);
            }
        }
    }
    public void onBlockOutOfTaintBiome(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random){};
    public void beforeSpreadingFibres(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random){}
    public void afterSpreadFibres(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random){}


    public static boolean isOnlyAdjacentToTaint(Level world, BlockPos pos) {
        for(int a = 0; a < 6; ++a) {
            Direction d = Direction.values()[a];
            var consideringPos = pos.relative(d);
            var bState = world.getBlockState(consideringPos);
            var block = bState.getBlock();
            if (!bState.isAir() && !(block instanceof ITaintMaterial)) {//taintMaterial -> ITaintMaterial
                return false;
            }
        }

        return true;
    }

    public static boolean canFallBelow(Level level, BlockPos pos) {
        var bState = level.getBlockState(pos);
        var fluidState = level.getFluidState(pos);
        var block = bState.getBlock();

        for(int xx = -1; xx <= 1; ++xx) {
            for(int zz = -1; zz <= 1; ++zz) {
                for(int yy = -1; yy <= 1; ++yy) {
                    if (Utils.isWoodLog(level, pos.offset(xx,yy,zz))) {
                        return false;
                    }
                }
            }
        }

        if (bState.isAir()) {
            return true;
        } else if (block == ThaumcraftBlocks.FLUX_GOO
                && (bState.getValue(FiniteLiquidBlock.LEVEL) >= 4)
        ) {
            return false;
        } else if (block != Blocks.FIRE && ! (block instanceof AbstractTaintFibreBlock)) {
            if (bState.canBeReplaced()) {
                return true;
            } else {
                return !fluidState.isEmpty();
//                return fluidState.is(FluidTags.WATER) || fluidState.is(FluidTags.LAVA);
            }
        } else {
            return true;
        }
    }

    protected boolean tryToFall(Level level, BlockPos blockToCopyFrom,BlockPos fallingPos) {
        if (canFallBelow(level, fallingPos.below()) && fallingPos.getY() >= level.getMinBuildHeight()) {
            var savedState = level.getBlockState(blockToCopyFrom);
            byte b0 = 32;
            if (level.hasChunksAt(fallingPos.offset(-b0,-b0,-b0),fallingPos.offset(b0,b0,b0))) {
                if (!(Platform.getEnvironment() == Env.CLIENT)) {
                    EntityFallingTaint entityfalling = new EntityFallingTaint(
                            level,
                            (float)fallingPos.getX() + 0.5F,
                            (float)fallingPos.getY() + 0.5F,
                            (float)fallingPos.getZ() + 0.5F,
                            this,
                            md,
                            blockToCopyFrom.getX(),
                            blockToCopyFrom.getY(),
                            blockToCopyFrom.getZ()
                    );//TODO:entity
                    this.onStartFalling(entityfalling);
                    level.addFreshEntity(entityfalling);
                    return true;
                }
            } else {
                level.setBlockAndUpdate(fallingPos, Blocks.AIR.defaultBlockState());

                while(canFallBelow(level, fallingPos.below()) && fallingPos.getY() >= level.getMinBuildHeight()) {
                    fallingPos = fallingPos.below();
                }

                if (fallingPos.getY() > level.getMinBuildHeight()) {
                    level.setBlockAndUpdate(fallingPos, savedState);
                }
            }
        }

        return false;
    }
    protected void onStartFalling(EntityFallingTaint entityfalling) {
    }
    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (Platform.getEnvironment() == Env.SERVER) {
            if (entity instanceof LivingEntity living &&
                    (
                            living.getMobType() != MobType.UNDEAD
                            && !living.getType().is(ThaumcraftEntities.EntityTags.UNDEAD)
                    )
            ) {
                if (living instanceof ServerPlayer && level.random.nextInt(100) == 0) {
                    living.addEffect(new MobEffectInstance(ThaumcraftEffects.FLUX_TAINT,80,0));
                }else if(!(living instanceof ServerPlayer) && level.random.nextInt(20) == 0) {
                    living.addEffect(new MobEffectInstance(ThaumcraftEffects.FLUX_TAINT,160,0));
                }

            }
        }
        super.stepOn(level, blockPos, blockState, entity);
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        if (i != 1){
            return super.triggerEvent(blockState, level, blockPos, i, j);
        }

        if ((Platform.getEnvironment() == Env.CLIENT)) {
            level.playSound(null,blockPos, ThaumcraftSounds.ROOTS, SoundSource.BLOCKS, 0.1F, 0.9F + level.getRandom().nextFloat() * 0.2F);
        }
        return true;
    }
}
