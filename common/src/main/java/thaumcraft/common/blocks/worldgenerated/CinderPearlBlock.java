package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CinderPearlBlock extends DeadBushBlock {
    public CinderPearlBlock(Properties properties) {
        super(properties);
    }
    
    public CinderPearlBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .replaceable()
                        .noCollission()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .pushReaction(PushReaction.DESTROY)
                        .lightLevel(s -> 8)
        );
    }


    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextBoolean()) {
            float i = blockPos.getX();
            float j = blockPos.getY();
            float k = blockPos.getZ();
            float xr = i + 0.5F + (randomSource.nextFloat() - randomSource.nextFloat()) * 0.1F;
            float yr = j + 0.6F + (randomSource.nextFloat() - randomSource.nextFloat()) * 0.1F;
            float zr = k + 0.5F + (randomSource.nextFloat() - randomSource.nextFloat()) * 0.1F;
            level.addParticle(ParticleTypes.SMOKE, xr, yr, zr, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, xr, yr, zr, 0.0, 0.0, 0.0);
        }
    }
}
