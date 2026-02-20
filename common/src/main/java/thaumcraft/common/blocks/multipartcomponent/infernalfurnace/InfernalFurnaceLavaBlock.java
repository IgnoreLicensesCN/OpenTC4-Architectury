package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import com.linearity.opentc4.recipeclean.itemmatch.ItemAndDamageMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.ItemMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.TagItemMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.blockapi.IEntityInLavaBlock;
import thaumcraft.common.tiles.crafted.InfernalFurnaceBlockEntity;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InfernalFurnaceLavaBlock extends AbstractInfernalFurnaceComponent implements IEntityInLavaBlock, EntityBlock {

    public static final VoxelShape STABLE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    public static final Map<RecipeItemMatcher, ItemStack> infernalFurnaceSmeltingBonus = new ConcurrentHashMap<>();

    public InfernalFurnaceLavaBlock(Properties properties) {
        super(properties);
    }
    public InfernalFurnaceLavaBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.FIRE)
                .replaceable()
                .noCollission()
                .strength(10.0F,500.0F)
                .lightLevel(blockStatex -> 13)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .sound(SoundType.EMPTY)
        );
    }


    public static final BlockPos SELF_POS_1_1_1 = new BlockPos(1,1,1);

    /**
     * Returns the bonus item produced from a smelting operation in the infernal furnace
     *
     * @param in The input ofAspectVisList the smelting operation. e.g. new ItemStack(oreGold)
     * @return the The bonus item that can be produced
     */
    public static ItemStack getSmeltingBonus(ItemStack in) {
        ItemStack out = infernalFurnaceSmeltingBonus.get(ItemAndDamageMatcher.of(in.getItem(), in.getDamageValue()));
        if (out == null) {
            out = infernalFurnaceSmeltingBonus.get(ItemMatcher.of(in.getItem()));
        }
        if (out == null) {
            for (Map.Entry<RecipeItemMatcher, ItemStack> entry : infernalFurnaceSmeltingBonus.entrySet()) {
                if (entry.getKey().matches(in)) {
                    return entry.getValue();
                }
            }
        }
        return out == null?ItemStack.EMPTY:out.copy();
    }

    /**
     * This method is used to determine what bonus items are generated when the infernal furnace smelts items
     *
     * @param in  The input ofAspectVisList the smelting operation. e.g. new ItemStack(Block.oreGold)
     * @param out The bonus item that can be produced from the smelting operation e.g. new ItemStack(nuggetGold,0,0).
     *            Stacksize should be 0 unless you want to guarantee that at least 1 item is always produced.
     */
    public static void addInfernalFurnaceSmeltingBonus(ItemStack in, ItemStack out) {
        var setIn = new ItemStack(out.getItem(), 0);
        setIn.setDamageValue(out.getDamageValue());
        infernalFurnaceSmeltingBonus.put(
                ItemAndDamageMatcher.of(in.getItem(), in.getDamageValue()),
                setIn);
    }

    /**
     * This method is used to determine what bonus items are generated when the infernal furnace smelts items
     *
     * @param in  The tag string input ofAspectVisList the smelting operation. e.g. "oreGold"
     * @param out The bonus item that can be produced from the smelting operation e.g. new ItemStack(nuggetGold,0,0).
     *            Stacksize should be 0 unless you want to guarantee that at least 1 item is always produced.
     */
    public static void addInfernalFurnaceSmeltingBonus(String in, ItemStack out) {
        var setIn = new ItemStack(out.getItem(), 0);
        setIn.setDamageValue(out.getDamageValue());
        infernalFurnaceSmeltingBonus.put(TagItemMatcher.of(in), setIn);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return STABLE_SHAPE;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        return SELF_POS_1_1_1;
    }

    @Override
    public void onMultipartDestroyed(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            Blaze blaze = new Blaze(EntityType.BLAZE,serverLevel);
            blaze.setYRot(0);
            blaze.setXRot(0);
            blaze.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            blaze.addEffect(new MobEffectInstance(MobEffects.REGENERATION,6000,2));
            blaze.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,12000,0));
            serverLevel.addFreshEntity(blaze);
        }
    }

    @Override
    public boolean consideredAsLava(BlockState state, Level level, BlockPos pos, @Nullable Entity entityCollideWith) {
        return !considerEntityAsItem(entityCollideWith);
    }

    public static boolean considerEntityAsItem(@Nullable Entity entity){
        return entityAsItemStack(entity) != null;
    }

    public static @Nullable ItemStack entityAsItemStack(Entity entity){
        if (entity instanceof ItemEntity itemEntity){
            return itemEntity.getItem();
        }
        return null;
    }
    public static void putItemStackIntoEntity(Entity entity,ItemStack stack){
        if (entity instanceof ItemEntity itemEntity){
            itemEntity.setItem(stack);
            return;
        }
        throw new IllegalArgumentException("entity not supported to set itemStack");
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfernalFurnaceBlockEntity(blockPos, blockState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState0, BlockEntityType<T> blockEntityType) {
        if (blockEntityType != ThaumcraftBlockEntities.INFERNAL_FURNACE) {
            return null;
        }
        return ((level, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof InfernalFurnaceBlockEntity infernalFurnaceBlockEntity){
                infernalFurnaceBlockEntity.blockEntityTick();
            }
        }
        );
    }

    public static final Vec3 IN_LAVA_SPEED_MULTIPLIER = new Vec3(0.5, 0.025F, 0.5);

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        var itemStack = entityAsItemStack(entity);
        if (itemStack != null) {
            if (entity.onGround() && !level.isClientSide){

                var bEntity = level.getBlockEntity(blockPos);
                if (bEntity instanceof InfernalFurnaceBlockEntity infernalFurnace){
                    itemStack = infernalFurnace.insertItemStack(itemStack);
                    if (itemStack.isEmpty()) {
                        entity.discard();
                        return;
                    }
                    putItemStackIntoEntity(entity, itemStack);
                }

                //maybe some ofAspectVisList you can play some redstone tricks?
                //throw too high leads to it's so easy to push item to next furnace? idk
                //but i must tell to others that THIS FURNACE HAS LIMITED CAPACITY.
                entity.addDeltaMovement(new Vec3(0, 0.9, 0));
                return;
            }
            entity.makeStuckInBlock(blockState, IN_LAVA_SPEED_MULTIPLIER);
        }else {
            entity.makeStuckInBlock(blockState, IN_LAVA_SPEED_MULTIPLIER);
        }
    }

    //===========from lava============
    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        BlockPos blockpos = blockPos.above();
        if (level.getBlockState(blockpos).isAir()) {
            if (randomSource.nextInt(100) == 0) {
                double d0 = blockPos.getX() + randomSource.nextDouble();
                double d1 = blockPos.getY() + 1.0;
                double d2 = blockPos.getZ() + randomSource.nextDouble();
                level.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0, 0.0, 0.0);
                level.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + randomSource.nextFloat() * 0.2F, 0.9F + randomSource.nextFloat() * 0.15F, false);
            }

            if (randomSource.nextInt(200) == 0) {
                level.playLocalSound(
                        blockPos.getX(),
                        blockPos.getY(),
                        blockPos.getZ(),
                        SoundEvents.LAVA_AMBIENT,
                        SoundSource.BLOCKS,
                        0.2F + randomSource.nextFloat() * 0.2F,
                        0.9F + randomSource.nextFloat() * 0.15F,
                        false
                );
            }
            for (int i=0;i<3;i++){
                double d0 = blockPos.getX() + randomSource.nextDouble();
                double d1 = blockPos.getY() + 1.0 + randomSource.nextDouble()*0.5;
                double d2 = blockPos.getZ() + randomSource.nextDouble();
                level.addParticle(ParticleTypes.LARGE_SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
            }
        }
    }
}
