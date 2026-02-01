package thaumcraft.common.blocks.worldgenerated.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.monster.EntityTaintSpore;

import java.util.List;

//blocktaintfibres:4
public class MatureSporeStalkBlock extends AbstractTaintFibreBlock{
    public MatureSporeStalkBlock(Properties properties) {
        super(properties);
    }
    public MatureSporeStalkBlock() {
        super(BlockBehaviour.Properties.of()
                .noOcclusion()
                .randomTicks()
                .noCollission()
                .requiresCorrectToolForDrops()
                .sound(TAINT_FIBRE_SOUND)
                .strength(1,5)
                .mapColor(MapColor.COLOR_PURPLE)
                .lightLevel(s -> 10));
    }

    @Override
    protected void onSpreadFibresFailed(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        AABB box = new AABB(
                x,     y + 1, z,
                x + 1, y + 2, z + 1
        );
        List<EntityTaintSpore> sporesNearby = world.getEntitiesOfClass(EntityTaintSpore.class, box);

        if (sporesNearby.isEmpty()) {
            world.setBlockAndUpdate(blockPos, ThaumcraftBlocks.SPORE_STALK.defaultBlockState());
        }
    }
}
