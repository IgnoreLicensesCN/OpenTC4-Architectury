package thaumcraft.common.blocks.abstracts;

import com.linearity.colorannotation.annotation.RGBColor;
import com.linearity.opentc4.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import thaumcraft.api.crafting.interfaces.IInfusionStabiliser;
import thaumcraft.client.fx.migrated.particles.FXSpark;
import thaumcraft.common.ThaumcraftSounds;

public abstract class AbstractCrystalBlock extends SuppressedWarningBlock implements IInfusionStabiliser  {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final @RGBColor int[] CRYSTAL_COLORS = {
            0xffffff,
            0xffff7e,
            0xff3c01,
            0x0090ff,
            0x00a000,
            0xeeccff,
            0x555577
    };
    public static final SoundType CRYSTAL_SOUND = new SoundType(
            1.f,1.f,
            ThaumcraftSounds.CRYSTAL,
            ThaumcraftSounds.CRYSTAL,
            ThaumcraftSounds.CRYSTAL,
            ThaumcraftSounds.CRYSTAL,
            ThaumcraftSounds.CRYSTAL
    );//TODO:[sound]are we done?
    public final int[] particleColors;
    public AbstractCrystalBlock(Properties properties,@RGBColor int[] particleColors) {
        super(properties);
        assert particleColors.length > 0;
        this.particleColors = particleColors;
    }
    public AbstractCrystalBlock(@RGBColor int[] particleColors) {
        super(
                BlockBehaviour.Properties.copy(Blocks.GLASS)
                        .lightLevel(s -> 1)//cant set .5f
                        .strength(0.7F,1.f)
                        .sound(CRYSTAL_SOUND)
        );
        assert particleColors.length > 0;
        this.particleColors = particleColors;

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.DOWN)
        );
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (level instanceof ClientLevel world) {
            var random = world.random;
            var i = blockPos.getX();
            var j = blockPos.getY();
            var k = blockPos.getZ();
            if (random.nextInt(17) == 0) {
                FXSpark ef = new FXSpark(world, (double)i + 0.3 + (double)(world.getRandom().nextFloat() * 0.4F), (double)j + 0.3 + (double)(world.getRandom().nextFloat() * 0.4F), (double)k + 0.3 + (double)(world.getRandom().nextFloat() * 0.4F), 0.2F + random.nextFloat() * 0.1F);
                Color c = new Color(particleColors[random.nextInt(particleColors.length)]);
                ef.setRBGColorF((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
                ef.setAlphaF(0.8F);
                Minecraft.getInstance().particleEngine.add(ef);
            }
        }
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

//    TODO:Model
//    public boolean renderAsNormalBlock() {
//        return false;
//    }
//
//    public int getRenderType() {
//        return ConfigBlocks.blockCrystalRI;
//    }

    public boolean canStabaliseInfusion(Level world, BlockPos pos) {
        return true;
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction dir = state.getValue(FACING);
        BlockPos supportPos = pos.relative(dir.getOpposite());
        return world.getBlockState(supportPos).isFaceSturdy(world, supportPos, dir);
    }
    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!state.canSurvive(world, pos)) {
            dropResources(state,world, pos);
            world.removeBlock(pos, false);
        }
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        for (Direction dir : context.getNearestLookingDirections()) {
            BlockState state = this.defaultBlockState().setValue(FACING, dir);
            if (state.canSurvive(world, pos)) {
                return state;
            }
        }
        return null;
    }


}
