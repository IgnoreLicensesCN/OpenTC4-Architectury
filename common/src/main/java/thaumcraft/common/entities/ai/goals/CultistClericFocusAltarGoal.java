package thaumcraft.common.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import thaumcraft.common.entities.monster.cultists.CultistClericEntity;
import thaumcraft.common.tiles.eldritch.EldritchAltarBlockEntity;

public class CultistClericFocusAltarGoal extends Goal {
    protected CultistClericEntity cleric;
    public CultistClericFocusAltarGoal(CultistClericEntity c) {
        this.cleric = c;
    }
    @Override
    public boolean canUse() {
        return cleric.getIsRitualist() && cleric.hasRestriction();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        var homePos = this.cleric.getRestrictCenter();
        var level = this.cleric.level();
        if (this.cleric.hasRestriction()
                && this.cleric.tickCount % 40 == 0
                && (homePos.distSqr(cleric.blockPosition()) > 16.0F
                || !(level.getBlockEntity(homePos) instanceof EldritchAltarBlockEntity))
        ) {
            this.cleric.setIsRitualist(false);
        }
    }
}
