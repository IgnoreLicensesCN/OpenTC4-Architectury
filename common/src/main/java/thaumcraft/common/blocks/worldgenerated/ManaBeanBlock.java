package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.IManaBeanAspectCombineProviderBlock;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.consumable.aspectowning.ManaBeanItem;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeTags;
import thaumcraft.common.tiles.generated.ManaBeanBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ManaBeanBlock extends SuppressedWarningBlock
        implements
        EntityBlock,
        IManaBeanAspectCombineProviderBlock {
    public static final int STAGE_MIN = 0;
    public static final int STAGE_MAX = 7;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", STAGE_MIN,STAGE_MAX);
//    public static final DirectionProperty ATTACHED_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape[] SHAPES = {
            Block.box(4,12,4,12,16,12),
            Block.box(4,10,4,12,16,12),
            Block.box(4,8,4,12,16,12),
            Block.box(4,6,4,12,16,12),
            Block.box(4,5,4,12,16,12),
            Block.box(4,4,4,12,16,12),
            Block.box(4,3,4,12,16,12),
            Block.box(4,2,4,12,16,12),
    };
    public ManaBeanBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(STAGE, 0)
        );
    }

    public ManaBeanBlock() {
        this(Properties.copy(Blocks.COCOA)
                .lightLevel(state -> state.getValue(STAGE))
                .randomTicks()
        );
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            return new ManaBeanBlockEntity(blockPos, blockState);
        }
        return null;
    }

    @Override
    public @NotNull BlockState updateShape(
            BlockState prevState, Direction changeFromDirection, BlockState neighborState, LevelAccessor levelAccessor, BlockPos selfPos, BlockPos changedPos
    ) {
        return changeFromDirection == Direction.UP && !prevState.canSurvive(levelAccessor, selfPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(prevState, changeFromDirection, neighborState, levelAccessor, selfPos, changedPos);
    }



    @Override
    public @NotNull List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (be instanceof ManaBeanBlockEntity manaBeanBE) {
            var aspect = manaBeanBE.getAspectOwning();
            if (!aspect.isEmpty()) {
                var random = builder.getLevel().getRandom();
                var tool = builder.getParameter(LootContextParams.TOOL);
                int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
                int dropCount = 1;
                if (blockState.getValue(STAGE) == STAGE_MAX ) {
                    if (random.nextInt(3) > 0){
                        dropCount++;
                    }
                    dropCount *= 1 + random.nextInt(fortuneLevel+1);
                }
                int stackSize = ThaumcraftItems.ThaumcraftItemInstances.MANA_BEAN().getMaxStackSize();
                List<ItemStack> drops = new ArrayList<>((dropCount / stackSize)+1);
                while ((dropCount / stackSize) > 0) {
                    dropCount -= stackSize;
                    var addStack = new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.MANA_BEAN(),stackSize);
                    ThaumcraftItems.ThaumcraftItemInstances.MANA_BEAN().writeOwningBonusAspect(addStack,aspect);
                    drops.add(addStack);
                }
                int remaining = dropCount % stackSize;
                if (remaining > 0) {
                    var addStack = new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.MANA_BEAN(),remaining);
                    ThaumcraftItems.ThaumcraftItemInstances.MANA_BEAN().writeOwningBonusAspect(addStack,aspect);
                    drops.add(addStack);
                }
                return drops;
            }
        }

        return super.getDrops(blockState, builder);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return canManaBeanSurvive(Direction.UP, levelReader, blockPos);
    }

    public boolean canManaBeanSurvive(Direction attachedFacing,LevelReader levelReader, BlockPos blockPos) {
        BlockPos attachedPos = blockPos.relative(attachedFacing);
        if (!levelReader.getBiome(blockPos).is(ThaumcraftBiomeTags.MANA_BEAN_SURVIVES)){
            return false;
        }
        return levelReader.getBlockState(attachedPos)
                .is(ThaumcraftBlocks.Tags.MANA_BEAN_SURVIVES);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
        if (serverLevel.getBlockEntity(blockPos) instanceof ManaBeanBlockEntity manaBean) {
            manaBean.randomTick();
        }
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPES[blockState.getValue(STAGE)];
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPES[blockState.getValue(STAGE)];
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(STAGE) < STAGE_MAX;
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (itemStack.getItem() instanceof ManaBeanItem beanItem){
            var owningAspect = beanItem.getContainingAspectFromStack(itemStack);
            if (!owningAspect.isEmpty()){
                if (level.getBlockEntity(blockPos) instanceof ManaBeanBlockEntity manaBean){
                    manaBean.setAspectOwning(owningAspect);
                }
            }
        }
    }

    @Override
    public @NotNull Aspect getAspectCanProvide(Level level, BlockPos selfPos, BlockState selfState) {
        if (level.getBlockEntity(selfPos) instanceof ManaBeanBlockEntity manaBean){
            return manaBean.getAspectOwning();
        }
        return Aspects.EMPTY;
    }
}
