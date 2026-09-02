package thaumcraft.common.entities.ai.goals;

import com.linearity.opentc4.mixin.RangedAttackGoalAccessor;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class RangedAndMeleeAttackGoal extends RangedAttackGoal {
    public float meleeAttackRangeSqr = 1;
    public RangedAndMeleeAttackGoal(RangedAttackMob rangedAttackMob, double d, int i, float f) {
        super(rangedAttackMob, d, i, f);
    }

    public RangedAndMeleeAttackGoal(RangedAttackMob rangedAttackMob, double d, int i, int j, float f) {
        super(rangedAttackMob, d, i, j, f);
    }

    @Override
    public void tick() {
        var accessor = ((RangedAttackGoalAccessor)this);
        var mob = accessor.getMob();
        var target = mob.getTarget();
        if (accessor.getAttackTime() == 1 && target != null && mob.distanceToSqr(target) < meleeAttackRangeSqr) {
            accessor.setAttackTime(accessor.getAttackIntervalMax() + 1);
            mob.doHurtTarget(target);
        }
        super.tick();
    }
}
