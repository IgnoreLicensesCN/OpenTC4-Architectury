package thaumcraft.common.blocks.crafted.loot;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ThaumcraftSounds;

public class UrnLootBlock extends DefaultAbstractLootBlock {
    public static final SoundType URN_SOUND_TYPE = new SoundType(
            1.0F, 1.0F,
            ThaumcraftSounds.URN_BREAK,
            SoundEvents.STONE_STEP, SoundEvents.STONE_PLACE, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL//TODO:[maybe wont finished]replace other urn sounds from stone
    );
    public UrnLootBlock(Properties properties) {
        super(properties);
    }
    public UrnLootBlock() {
        this(Properties.of().noOcclusion().strength(0.15F,0).sound(URN_SOUND_TYPE));
    }
    public static final VoxelShape SHAPE = Block.box(2,1,2,14,13,14);

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }
}
