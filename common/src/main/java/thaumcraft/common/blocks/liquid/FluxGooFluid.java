package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.monster.tainted.ThaumicSlimeEntity;

import static thaumcraft.common.ThaumcraftSounds.GORE;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.FLUX_GOO;

public class FluxGooFluid extends FiniteFlowingFluid {
    public FluxGooFluid(int maxLevel, Direction gravityDirection) {
        super(maxLevel, gravityDirection);
    }
    public FluxGooFluid() {
        this(8, Direction.DOWN);
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 40;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(FluidState fluidState) {
        return FLUX_GOO().defaultBlockState().setValue(liquidLevel, getAmount(fluidState));
    }

    @Override
    public void tick(Level level, BlockPos pos, FluidState state) {
        super.tick(level, pos, state);
        if (level.isClientSide()) {
            return;
        }
        var rand = level.random;
        int lvl = getAmount(state);

        if (lvl >= 2 && lvl < 6 && level.isEmptyBlock(pos.above())) {
            if (rand.nextInt(25) == 0) {
                spawnSlime(level, pos, 1);
                level.removeBlock(pos, false);
                return;
            }
        }
        if (lvl >= 6 && level.isEmptyBlock(pos.above())) {
            if (rand.nextInt(25) == 0) {
                spawnSlime(level, pos, 2);
                level.removeBlock(pos, false);
                return;
            }
        }

        // 流体蒸发 / 减少
        if (rand.nextInt(30) == 0) {
            decreaseOrRemove(level,pos,state);
        }
    }


    protected void spawnSlime(Level level, BlockPos pos, int size) {
        var slime = new ThaumicSlimeEntity(level);
        slime.setSize(size,true);
        slime.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.addFreshEntity(slime);
        level.playSound(null, pos, GORE,
                net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
    }

}
