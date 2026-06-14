package thaumcraft.common.blocks.abstracts;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IAspectFilterAccessibleBlockEntity;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.ThaumcraftItemInstances;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public interface IAspectLabelAttachableBlock {
    //then maybe someone would make a jar-label bag with lots of labels
    //which can be switched by using sneak and mouse-scrolling.
    //so we can stick lots of label in a bag to avoid lots labels in inventory.
    //@return boolean--should consume label(just recommend to consume you can also not to do so)
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
    default boolean attemptAttachAspectLabel(Level level, BlockPos pos, BlockState state, Aspect labelAspect){
        if (LevelBlockEntityAccessing.getExistingBlockEntity(level, pos) instanceof IAspectFilterAccessibleBlockEntity aspectFilterAccessible) {
            return aspectFilterAccessible.setAspectFilter(labelAspect);
        }
        return false;
    };
    //@return removed existing label
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
    default boolean attemptRemoveAspectLabel(Level level, BlockPos pos, BlockState state){
        if (LevelBlockEntityAccessing.getExistingBlockEntity(level, pos) instanceof IAspectFilterAccessibleBlockEntity accessible && !accessible.getAspectFilter().isEmpty()) {
            if (accessible.setAspectFilter(Aspects.EMPTY)) {
                level.playSound(null, pos, ThaumcraftSounds.PAGE, SoundSource.BLOCKS, 1.0F, 1.1F);
                var spawnEntityPos = pos.getCenter();
                level.addFreshEntity(new ItemEntity(
                        level, spawnEntityPos.x, spawnEntityPos.y, spawnEntityPos.z, new ItemStack(
                        ThaumcraftItemInstances.JAR_LABEL())
                ));//maybe you can just do not drop it since "oh i tore it into pieces it cant be removed in one piece"
                return true;
            }
        }
        return false;
    };
}
