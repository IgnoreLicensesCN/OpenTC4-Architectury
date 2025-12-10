package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import static thaumcraft.common.blocks.ThaumcraftBlocks.FLUX_GOO;

public class FluxGooFluid extends FlowingFluid {

    public static final ResourceLocation STILL_TEXTURE = new ResourceLocation("modid", "block/flux_goo_still");
    public static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation("modid", "block/flux_goo_flow");

    @Override
    public @NotNull Fluid getSource() {
        return this; // 静态注册的 SourceFluid
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    public Fluid getFlowing() {
        return this;
    }

    @Override
    public Item getBucket() {
        return ItemStack.EMPTY.getItem();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 40;
    }

    @Override
    protected float getExplosionResistance() {
        return 100000.0f;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(FluidState fluidState) {
        return FLUX_GOO.defaultBlockState().setValue(FluxGooBlock.LEVEL, getAmount(fluidState));
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getAmount(FluidState state) {
        return state.getValue(FluxGooFluid.LEVEL);
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
//        Block.dropResources(state, world instanceof Level lvl ? lvl : null, pos);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) {
        return 0;
    }

    @Override
    protected int getDropOff(LevelReader levelReader) {
        return 0;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid instanceof FluxGooFluid;
    }
}
