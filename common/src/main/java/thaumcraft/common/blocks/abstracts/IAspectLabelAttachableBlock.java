package thaumcraft.common.blocks.abstracts;

import com.linearity.opentc4.utils.LogicalSide;
import com.linearity.opentc4.utils.RecommendedLogicalSide;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IAspectFilterAccessible;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.tiles.crafted.ArcaneAlembicBlockEntity;

public interface IAspectLabelAttachableBlock {
    //then maybe someone would make a jar-label bag with lots of labels
    //which can be switched by using sneak and mouse-scrolling.
    //so we can stick lots of label in a bag to avoid lots labels in inventory.
    //@return boolean--should consume label(just recommend to consume you can also not to do so)
    @RecommendedLogicalSide(LogicalSide.SERVER)
    default boolean attemptAttachAspectLabel(Level level, BlockPos pos, BlockState state, Aspect labelAspect){
        if (level.getBlockEntity(pos) instanceof IAspectFilterAccessible aspectFilterAccessible) {
            return aspectFilterAccessible.setAspectFilter(labelAspect);
        }
        return false;
    };
    //@return removed existing label
    @RecommendedLogicalSide(LogicalSide.SERVER)
    default boolean attemptRemoveAspectLabel(Level level, BlockPos pos, BlockState state){
        if (level.getBlockEntity(pos) instanceof IAspectFilterAccessible accessible && !accessible.getAspectFilter().isEmpty()) {
            accessible.setAspectFilter(Aspects.EMPTY);
            level.playSound(null,pos, ThaumcraftSounds.PAGE, SoundSource.BLOCKS, 1.0F, 1.1F);
            var spawnEntityPos = pos.getCenter();
            level.addFreshEntity(new ItemEntity(level,spawnEntityPos.x,spawnEntityPos.y,spawnEntityPos.z,new ItemStack(
                    ThaumcraftItems.JAR_LABEL)));
            return true;
        }
        return false;
    };
}
