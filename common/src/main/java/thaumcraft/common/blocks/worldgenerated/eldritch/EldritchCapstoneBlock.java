package thaumcraft.common.blocks.worldgenerated.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.eldritch.EldritchCapstoneBlockEntity;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

public class EldritchCapstoneBlock extends SuppressedWarningBlock implements EntityBlock {
    public EldritchCapstoneBlock(Properties properties) {
        super(properties);
    }
    public EldritchCapstoneBlock() {
        super(Properties.of()
                .strength(50F,20000F)
                .sound(SoundType.STONE)
                .mapColor(MapColor.COLOR_BLACK)
                .lightLevel(s -> 8)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            return new EldritchCapstoneBlockEntity(blockPos, blockState);
        }
        return null;
    }
}
