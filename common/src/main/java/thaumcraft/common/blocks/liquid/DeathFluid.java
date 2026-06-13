package thaumcraft.common.blocks.liquid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.migrated.particles.FXSlimyBubble;
import thaumcraft.common.items.ThaumcraftItems;

import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.DEATH_FLUID;

public class DeathFluid extends FiniteFlowingFluid {

    public DeathFluid(int maxLevel, Direction gravityDirection) {
        super(maxLevel, gravityDirection);
    }
    public DeathFluid() {
        this(4, Direction.DOWN);
    }

    public @NotNull Item getBucket() {
        return ThaumcraftItems.ThaumcraftItemInstances.DEATH_FLUID_BUCKET();
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 40;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState fluidState) {
        return DEATH_FLUID().defaultBlockState().setValue(liquidLevel, fluidState.getValue(liquidLevel));
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
        var bubble = new FXSlimyBubble(
                clientLevel,
                x + rand.nextFloat(),
                y + 0.1F + (0.225F*fluidState.getValue(liquidLevel)),
                z + rand.nextFloat(),
                (rand.nextFloat()-0.5F) * 0.075F
        );
        bubble.setAlphaF(0.8F);
        bubble.setRBGColorF(0.3F - rand.nextFloat() * 0.1F, 0.0F, 0.4F + rand.nextFloat() * 0.1F);
        Minecraft.getInstance().particleEngine.add(bubble);
        if (rand.nextInt(50) == 0) {
            double var21 = x + rand.nextFloat();
            double var22 = y + ((double) fluidState.getValue(liquidLevel) /maxLevel);
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

}
