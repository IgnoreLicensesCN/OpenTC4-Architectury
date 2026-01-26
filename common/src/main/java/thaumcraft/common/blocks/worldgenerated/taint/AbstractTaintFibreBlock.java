package thaumcraft.common.blocks.worldgenerated.taint;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.biomes.BiomeUtils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;

public abstract class AbstractTaintFibreBlock extends AbstractTaintBlock {
    public static final SoundType TAINT_FIBRE_SOUND = new SoundType(
            1.0F,
            1.0F,
            SoundEvents.GRASS_BREAK,
            ThaumcraftSounds.GORE,
            SoundEvents.GRASS_PLACE,
            SoundEvents.GRASS_HIT,
            SoundEvents.GRASS_FALL
    );
    public AbstractTaintFibreBlock(Properties properties) {
        super(
                properties
                        .noOcclusion()
                        .randomTicks()
                        .noCollission()
                        .requiresCorrectToolForDrops()
                        .sound(TAINT_FIBRE_SOUND)
                        .strength(1,5)
        );
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (Platform.getEnvironment() == Env.SERVER) {
            if (entity instanceof LivingEntity living &&
                    (
                            living.getMobType() != MobType.UNDEAD
                                    && !living.getType().is(ThaumcraftEntities.EntityTags.UNDEAD)
                    )
            ) {
                if (living instanceof ServerPlayer && level.random.nextInt(1000) == 0) {
                    living.addEffect(new MobEffectInstance(ThaumcraftEffects.FLUX_TAINT,80,0));
                }else if(!(living instanceof ServerPlayer) && level.random.nextInt(500) == 0) {
                    living.addEffect(new MobEffectInstance(ThaumcraftEffects.FLUX_TAINT,160,0));
                }

            }
        }
        super.entityInside(blockState, level, blockPos, entity);
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        if (i == 1 && Platform.getEnvironment() == Env.CLIENT) {
            level.playSound(
                    null,blockPos,
                    ThaumcraftSounds.ROOTS,
                    SoundSource.BLOCKS,
                    0.1F,
                    0.9F + level.getRandom().nextFloat() * 0.2F
            );
            return true;
        }
        return super.triggerEvent(blockState, level, blockPos, i, j);
    }

    protected boolean cancelRandomTickAfterSpread(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        return false;
    }

    protected void onSpreadFibresFailed(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random){

    }
    public static final VoxelShape SHAPE = Shapes.box(0.2, 0.0, 0.2, 0.8, 0.8, 0.8);

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }


    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (isOnlyAdjacentToTaint(level, blockPos)) {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            BiomeUtils.taintBiomeSpread(world, blockPos, random, this);

            if (cancelRandomTickAfterSpread(blockState, world, blockPos, random)) {
                return;
            }

            var pickPos = blockPos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
            if (world.getBiome(pickPos).is(ThaumcraftBiomeIDs.TAINT_KEY)) {
                var pickState = world.getBlockState(pickPos);
                var pickBlock = pickState.getBlock();
                if (!spreadFibres(world, pickPos)) {
                    int adjacentTaint = BiomeUtils.getAdjacentTaint(world, pickPos);
                    if (adjacentTaint >= 2
                            && (Utils.isWoodLog(world, pickPos)
                            || pickState.is(Blocks.MELON)
                            || pickState.is(Blocks.PUMPKIN)
                            || pickState.is(Blocks.CARVED_PUMPKIN)
                            || pickState.is(Blocks.JACK_O_LANTERN)
                            || pickState.is(Blocks.CACTUS))) {
                        world.setBlock(pickPos, ThaumcraftBlocks.FIBROUS_TAINT.defaultBlockState(), 0, 3);
                        world.blockEvent(pickPos, ThaumcraftBlocks.FIBROUS_TAINT, 1, 0);
                    }

                    if (adjacentTaint >= 3 && !pickState.isAir()
                            && (
                                    pickState.is(BlockTags.SAND)
                                            || pickState.is(Blocks.GRAVEL)
                                            || pickState.is(BlockTags.DIRT)
                                            || pickState.is(Blocks.GRASS_BLOCK)
                                            || pickState.is(Blocks.CLAY)
                    )
                    ) {
                        world.setBlock(pickPos, ThaumcraftBlocks.TAINTED_SOIL.defaultBlockState(), 0, 3);
                        world.blockEvent(pickPos, ThaumcraftBlocks.TAINTED_SOIL, 1, 0);
                    }
                    onSpreadFibresFailed(blockState, world, blockPos, random);
                }
            }
        }

    }

    public static boolean spreadFibres(ServerLevel world, BlockPos blockPos) {
        var blockState = world.getBlockState(blockPos);
        var liquidState = world.getFluidState(blockPos);
        Block block = world.getBlockState(blockPos).getBlock();
        if (BlockUtils.isAdjacentToSolidBlock(world, blockPos)
                && !isOnlyAdjacentToTaint(world, blockPos)
                && !liquidState.isEmpty()
                && (blockState.isAir()
                || blockState.canBeReplaced()
                || blockState.is(BlockTags.FLOWERS)
                || blockState.is(BlockTags.LEAVES)
        )
        ) {
            Block blockForEvent = null;
            if (world.getRandom().nextInt(10) == 0
                    && world.getBlockState(blockPos.above()).isAir()
                    && world.getBlockState(blockPos.below()).isFaceSturdy(world,blockPos.below(), Direction.UP)
            ) {
                if (world.getRandom().nextInt(10) < 9) {
                    blockForEvent = ThaumcraftBlocks.TAINTED_GRASS;
                    world.setBlock(blockPos, blockForEvent.defaultBlockState(), 3);
                } else if (world.getRandom().nextInt(12) < 10) {
                    blockForEvent = ThaumcraftBlocks.TAINTED_PLANT;
                    world.setBlock(blockPos, blockForEvent.defaultBlockState(), 2, 3);
                } else {
                    blockForEvent = ThaumcraftBlocks.SPORE_STALK;
                    world.setBlock(blockPos, blockForEvent.defaultBlockState(), 3, 3);
                }
            } else {
                blockForEvent = ThaumcraftBlocks.FIBROUS_TAINT;
                world.setBlock(blockPos, blockForEvent.defaultBlockState(), 0, 3);
            }
            world.blockEvent(blockPos, blockForEvent, 1, 0);
            return true;
        } else {
            return false;
        }
    }
}
