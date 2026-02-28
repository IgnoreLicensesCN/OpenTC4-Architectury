package thaumcraft.common.blocks.crafted.jars;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

//TODO:Model or what to render
public abstract class JarBlock extends SuppressedWarningBlock {
    public JarBlock(Properties properties) {
        super(properties);
    }
    //TODO:Check all SoundType(Got to know that CustomStepSound is SoundType not XXX_STEP)
    public static final SoundType JAR_SOUND = new SoundType(
            1.0F,
            1.0F,
            ThaumcraftSounds.JAR,
            ThaumcraftSounds.JAR,
            ThaumcraftSounds.JAR,
            ThaumcraftSounds.JAR,
            ThaumcraftSounds.JAR
    );
    public static final Properties JAR_PROPERTIES = Properties.copy(Blocks.GLASS)
            .sound(JAR_SOUND)
            .lightLevel(s -> 1);

    public static final VoxelShape JAR_SHAPE = Shapes.box(
            0.1875F, 0.0F, 0.1875F, 0.8125F, 0.75F, 0.8125F
    );

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return JAR_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return JAR_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return JAR_SHAPE;
    }
}
