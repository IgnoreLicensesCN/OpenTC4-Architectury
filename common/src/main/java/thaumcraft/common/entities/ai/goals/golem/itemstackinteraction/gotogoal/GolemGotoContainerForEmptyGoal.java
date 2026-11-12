package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction.gotogoal;

import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.core.BlockPos;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;

import static thaumcraft.common.config.Config.CONFIG_INSTANCE;
import static thaumcraft.common.entities.golems.GolemHelper.getColorsMatching;

public class GolemGotoContainerForEmptyGoal extends AbstractGolemGotoFurthestContainerGoal {
    public GolemGotoContainerForEmptyGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected final ByteSet colorsTemp = new ByteArraySet(17);
    protected GolemHelper.ContainersWithRoomIterator containersWithRoomIterator = new GolemHelper.ContainersWithRoomIterator(golem);
    @Override
    public boolean canUse() {
        var itemMissing = golem.getMainHandItem();
        if (!itemMissing.isEmpty()
                && this.golem.tickCount % getGolemGoalDelay() <= 0){
            getColorsMatching(colorsTemp, golem, itemMissing);
            containersWithRoomIterator.resetWith(itemMissing);
            BlockPos closestContainerPos = getFurthestContainerPos(containersWithRoomIterator,containersWithRoomIterator,colorsTemp);
            if (closestContainerPos != null) {
                setDest(closestContainerPos);
                return true;
            }
        }
        return false;
    }
}
