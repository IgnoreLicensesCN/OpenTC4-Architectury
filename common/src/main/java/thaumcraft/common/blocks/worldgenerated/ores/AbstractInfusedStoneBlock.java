package thaumcraft.common.blocks.worldgenerated.ores;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.client.lib.UtilsFXMigrated;

public abstract class AbstractInfusedStoneBlock extends DropExperienceBlock {
    public static final IntProvider INFUSED_STONE_EXP_DROP = UniformInt.of(0,3);
    public final @RGBColor int rgbColor;
    public final int particleColorIndex;

    public AbstractInfusedStoneBlock(Properties properties,@RGBColor int rgbColor, int particleColorIndex) {
        super(properties, INFUSED_STONE_EXP_DROP);
        this.rgbColor = rgbColor;
        this.particleColorIndex = particleColorIndex;
    }

    public AbstractInfusedStoneBlock(@RGBColor int rgbColor,int particleColorIndex) {
        super(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE)
                .sound(SoundType.STONE)
                .strength(1.5f,5.f),
                INFUSED_STONE_EXP_DROP
        );
        this.rgbColor = rgbColor;
        this.particleColorIndex = particleColorIndex;
    }


    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide() && level.random.nextBoolean()) {
            UtilsFXMigrated.infusedStoneSparkle(level, blockPos.getX(),blockPos.getY(),blockPos.getZ(), particleColorIndex);
        }
        super.spawnDestroyParticles(level, player, blockPos, blockState);
    }
}
