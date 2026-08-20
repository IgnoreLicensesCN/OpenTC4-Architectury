package thaumcraft.common.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import thaumcraft.common.entities.monster.cultists.CultistClericEntity;

public class CultistHurtByTargetGoal extends HurtByTargetGoal {
    public CultistHurtByTargetGoal(PathfinderMob pathfinderMob, Class<?>... classs) {
        super(pathfinderMob, classs);
    }

    @Override
    protected void alertOther(Mob mob, LivingEntity livingEntity) {
        if (livingEntity instanceof CultistClericEntity cultistCleric
                && cultistCleric.getIsRitualist()
        ) {
            if (this.mob.getRandom().nextInt(3) == 0) {
                cultistCleric.setIsRitualist(false);
                cultistCleric.setTarget(this.mob.getTarget());
            }
        } else {
            super.alertOther(mob, livingEntity);
        }
    }
}
