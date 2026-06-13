package thaumcraft.common.blocks.liquid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.migrated.particles.FXBubble;
import thaumcraft.common.items.ThaumcraftItemInstances;

import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.PURE_FLUID;

public abstract class PureFluid extends FlowingFluid {

    public @NotNull Item getBucket() {
        return ThaumcraftItemInstances.PURE_FLUID_BUCKET();
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 5;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(FluidState fluidState) {
        return PURE_FLUID().defaultBlockState().setValue(LEVEL, fluidState.getValue(LEVEL));
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
//        Block.dropResources(state, world instanceof Level lvl ? lvl : null, pos);
    }

    @Override
    protected void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource rand) {
        super.animateTick(level, blockPos, fluidState, rand);
        if (!level.isClientSide) {return;}
        if (!(level instanceof ClientLevel clientLevel)){return;}

        var pos = blockPos.getCenter();
        var x = pos.x;
        var y = pos.y;
        var z = pos.z;
        var liquidLevelCurrent = fluidState.getValue(LEVEL);
        FXBubble fb = new FXBubble(
                clientLevel,
                (float)x + rand.nextFloat(),
                (float)y + 0.125F * (float)(8 - liquidLevelCurrent),
                (float)z + rand.nextFloat(), 0.0F, 0.0F, 0.0F, 0);
        fb.setAlphaF(0.25F);
        fb.setRGB(1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().particleEngine.add(fb);

        if (rand.nextInt(25) == 0) {
            double var21 = x + rand.nextFloat();
            double var22 = y + ((double) liquidLevelCurrent / 8);
            double var23 = z + rand.nextFloat();

            level.playSound(null,
                    var21,
                    var22,
                    var23,
                    SoundEvents.LAVA_POP,
                    SoundSource.BLOCKS
                    , 0.1F + rand.nextFloat() * 0.1F, 0.9F + rand.nextFloat() * 0.15F);
        }
    }

    @Override
    public @NotNull Fluid getFlowing() {
        return ThaumcraftFluids.ThaumcraftFluidInstances.PURE_FLUID_FLOWING();
    }

    @Override
    public @NotNull Fluid getSource() {
        return ThaumcraftFluids.ThaumcraftFluidInstances.PURE_FLUID_SOURCE();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader levelReader) {
        return 1;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected float getExplosionResistance() {
        return 100000;
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return false;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return fluidState.getValue(LEVEL);
    }
    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == ThaumcraftFluids.ThaumcraftFluidInstances.PURE_FLUID_SOURCE() || fluid == ThaumcraftFluids.ThaumcraftFluidInstances.PURE_FLUID_FLOWING();
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(LEVEL);
    }

    public static class PureFluidFlowing extends PureFluid {

    }
    public static class PureFluidSource extends PureFluid {

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }
    }
}
