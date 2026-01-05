package thaumcraft.common.blocks.worldgenerated;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import thaumcraft.client.fx.migrated.particles.FXWisp;

public class ManaShroomBlock extends BushBlock {
    public ManaShroomBlock(Properties properties) {
        super(properties);
    }
    public ManaShroomBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_MAGENTA)
                        .replaceable()
                        .noCollission()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .pushReaction(PushReaction.DESTROY)
                        .lightLevel(s -> 8)
        );
    }

    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos blockPos, RandomSource random) {
        if (random.nextInt(3) == 0 && world instanceof ClientLevel clientLevel) {
            float i = blockPos.getX();
            float j = blockPos.getY();
            float k = blockPos.getZ();
            float xr = i + 0.5F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.4F;
            float yr = j + 0.3F;
            float zr = k + 0.5F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.4F;
            FXWisp ef = new FXWisp(clientLevel, xr, yr, zr, 0.1F, 0.5F, 0.3F, 0.8F);
            ef.tinkle = false;
            ef.shrink = true;
            ef.setGravity(0.015F);
            Minecraft.getInstance().particleEngine.add(ef);

        }
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        //inspired by cobweb
        if (Platform.getEnvironment() == Env.SERVER && entity instanceof LivingEntity livingEntity){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
        }
    }

    protected static final float AABB_OFFSET = 6.0F;
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }
}
