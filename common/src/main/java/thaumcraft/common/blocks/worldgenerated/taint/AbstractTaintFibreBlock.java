package thaumcraft.common.blocks.worldgenerated.taint;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.internal.WeightedRandomCollection;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.world.biomes.BiomeUtils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;

//TODO:impl getOverlayBlockTexture
public abstract class AbstractTaintFibreBlock extends AbstractTaintBlock {
    public static final SoundType TAINT_FIBRE_SOUND = new SoundType(
            1.0F,
            1.0F,
            ThaumcraftSounds.GORE,//SoundEvents.GRASS_BREAK,
            ThaumcraftSounds.GORE,
            ThaumcraftSounds.GORE,//SoundEvents.GRASS_PLACE,
            ThaumcraftSounds.GORE,//SoundEvents.GRASS_HIT,
            ThaumcraftSounds.GORE//SoundEvents.GRASS_FALL
    );

    public AbstractTaintFibreBlock(Properties properties) {
        super(properties);
    }

    public AbstractTaintFibreBlock() {
        super(BlockBehaviour.Properties.of()
                .noOcclusion()
                .randomTicks()
                .noCollission()
                .sound(TAINT_FIBRE_SOUND)
                .strength(1, 5)
                .mapColor(MapColor.COLOR_PURPLE));
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
                if (level.random.nextInt(living instanceof Player ? 1000 : 500) == 0) {
                    living.addEffect(new MobEffectInstance(ThaumcraftEffects.ThaumcraftEffectTypeInstances.FLUX_TAINT(), 80, 0));
                }

            }
        }
        super.entityInside(blockState, level, blockPos, entity);
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        if (i == 1 && Platform.getEnvironment() == Env.CLIENT) {
            level.playSound(
                    null, blockPos,
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

    protected void onSpreadFibresFailed(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {

    }

    public static final VoxelShape SHAPE = Shapes.box(0.2, 0.0, 0.2, 0.8, 0.8, 0.8);

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
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
            BiomeUtils.taintBiomeSpread(world, blockPos, random);

            if (cancelRandomTickAfterSpread(blockState, world, blockPos, random)) {
                return;
            }

            var pickPos = blockPos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
            if (world.getBiome(pickPos).is(ThaumcraftBiomeIDs.TAINT_KEY)) {
                var pickState = world.getBlockState(pickPos);
                if (!spreadFibres(world, pickPos)) {
                    spreadSoil(world, pickPos, pickState);
                    onSpreadFibresFailed(blockState, world, blockPos, random);
                }
            }
        }
    }

    protected void spreadSoil(ServerLevel world, BlockPos pickPos, BlockState pickState) {
        int adjacentTaint = BiomeUtils.getTaintedBlocksNear(world, pickPos);
        BlockState stateToSpread = getBlockStateToSpreadSoil(pickState, adjacentTaint);
        if (stateToSpread == null) {
            return;
        }
        world.setBlockAndUpdate(pickPos, stateToSpread);
        world.blockEvent(pickPos, stateToSpread.getBlock(), 1, 0);
    }

    protected @Nullable BlockState getBlockStateToSpreadSoil(BlockState pickState, int adjacentTaint) {
        if (adjacentTaint >= 2 && (pickState.is(ThaumcraftBlocks.Tags.CAN_BE_CONVERTED_TO_FIBROUS_TAINT))) {
            return ThaumcraftBlocks.ThaumcraftBlockInstances.FIBROUS_TAINT().defaultBlockState();
        }

        if (adjacentTaint >= 3 && pickState.is(ThaumcraftBlocks.Tags.CAN_BE_CONVERTED_TO_TAINTED_SOIL)) {
            return ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_SOIL().defaultBlockState();
        }
        return null;
    }

    public static final WeightedRandomCollection<BlockState> TAINTED_DECORATIONS_TO_SPREAD = new WeightedRandomCollection<>();
    public static void initDecorations() {
        TAINTED_DECORATIONS_TO_SPREAD.add(ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_GRASS().defaultBlockState(),99);
        TAINTED_DECORATIONS_TO_SPREAD.add(ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_PLANT().defaultBlockState(),10);
        TAINTED_DECORATIONS_TO_SPREAD.add(ThaumcraftBlocks.ThaumcraftBlockInstances.SPORE_STALK().defaultBlockState(),1);
    }

    public static boolean spreadFibres(ServerLevel world, BlockPos blockPos) {
        if (TAINTED_DECORATIONS_TO_SPREAD.getInternalContainerView().isEmpty()){
            initDecorations();
        }
        var random = world.random;
        if (canSpreadFibresTo(world,blockPos)) {
            BlockState blockStateForEvent;
            if (random.nextInt(10) == 0
                    && world.getBlockState(blockPos.above()).isAir()
                    && world.getBlockState(blockPos.below()).isFaceSturdy(world, blockPos.below(), Direction.UP)
            ) {
                blockStateForEvent = TAINTED_DECORATIONS_TO_SPREAD.getRandom(random);
                world.setBlockAndUpdate(blockPos, blockStateForEvent);
            } else {
                blockStateForEvent = ThaumcraftBlocks.ThaumcraftBlockInstances.FIBROUS_TAINT().defaultBlockState();
                world.setBlockAndUpdate(blockPos, blockStateForEvent);
            }
            world.blockEvent(blockPos, blockStateForEvent.getBlock(), 1, 0);
            return true;
        } else {
            return false;
        }
    }

    public static boolean canSpreadFibresTo(ServerLevel level,BlockPos blockPos) {
        var blockState = level.getBlockState(blockPos);
        var liquidState = level.getFluidState(blockPos);
        return BlockUtils.isAdjacentToSolidBlock(level, blockPos)
                && !isOnlyAdjacentToTaint(level, blockPos)
                && !liquidState.isEmpty()
                && (blockState.isAir()
                || blockState.canBeReplaced()
                || blockState.is(BlockTags.FLOWERS)
                || blockState.is(BlockTags.LEAVES));
    }
}
