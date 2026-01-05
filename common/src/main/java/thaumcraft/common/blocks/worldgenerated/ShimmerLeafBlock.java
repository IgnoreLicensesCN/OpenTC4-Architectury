package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import thaumcraft.client.fx.migrated.particles.FXWisp;

public class ShimmerLeafBlock extends BushBlock {
    public ShimmerLeafBlock(Properties properties) {
        super(properties);
    }
    public ShimmerLeafBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .pushReaction(PushReaction.DESTROY)
                        .lightLevel(s -> 8)
        );
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (!(level instanceof ClientLevel clientLevel)) {return;}
        if (randomSource.nextInt(3) == 0) {
            float i = blockPos.getX();
            float j = blockPos.getY();
            float k = blockPos.getZ();
            float cr = 0.3F + randomSource.nextFloat() * 0.3F;
            float cg = 0.7F + randomSource.nextFloat() * 0.3F;
            float cb = 0.7F + randomSource.nextFloat() * 0.3F;
            float xr = i + 0.5F + (randomSource.nextFloat() - randomSource.nextFloat()) * 0.1F;
            float yr = j + 0.5F + (randomSource.nextFloat() - randomSource.nextFloat()) * 0.15F;
            float zr = k + 0.5F + (randomSource.nextFloat() - randomSource.nextFloat()) * 0.1F;
            FXWisp ef = new FXWisp(clientLevel, xr, yr, zr, 0.2F, cr, cg, cb);
            ef.tinkle = false;
            Minecraft.getInstance().particleEngine.add(ef);
        }
    }

    protected static final float AABB_OFFSET = 6.0F;
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }
}
