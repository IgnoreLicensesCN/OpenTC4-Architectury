package thaumcraft.common.blocks.technique;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ClientFXUtils;

import static thaumcraft.common.blocks.crafted.NitorBlock.NITOR_SOUND;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

//blockAiry.3
//arcane bore&lamp
public class GlimmerOfLightBlock extends SuppressedWarningBlock {
    public GlimmerOfLightBlock(Properties properties) {
        super(properties);
    }
    public static final VoxelShape SHAPE = Shapes.empty();
    public GlimmerOfLightBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(0,0)
                .lightLevel(s -> 15)
                .pushReaction(PushReaction.DESTROY)
                .replaceable()
                .noCollission()
                .sound(NITOR_SOUND)
        );
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return true;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid) {
        return true;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (level instanceof ClientLevel clientLevel &&  randomSource.nextInt(500) == 0){
            var fromPos = blockPos.offset(
                    randomSource.nextInt(5) - 2,
                    randomSource.nextInt(5) - 2,
                    randomSource.nextInt(5) - 2
            );
            var toPos = fromPos.offset(
                    randomSource.nextInt(5) - 2,
                    randomSource.nextInt(5) - 2,
                    randomSource.nextInt(5) - 2
            );
            ClientFXUtils.wispFX3(clientLevel,
                    fromPos,
                    toPos,
                    0.1F + randomSource.nextFloat() * 0.1F,
                    7,
                    false,
                    randomSource.nextBoolean() ? -0.033F : 0.033F
            );
        }
    }
}
