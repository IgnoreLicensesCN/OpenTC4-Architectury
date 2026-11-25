package thaumcraft.common.entities.goals.golem.miscjobs;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public class GolemMeleeAttackGoal extends MeleeAttackGoal {
    protected final BaseGolemEntity golem;
    protected final boolean followingTargetEvenIfNotSeen;
    public GolemMeleeAttackGoal(BaseGolemEntity golem){
        this(golem, 1, false);
    }
    public GolemMeleeAttackGoal(BaseGolemEntity pathfinderMob, double d, boolean followingTargetEvenIfNotSeen) {
        super(pathfinderMob, d, followingTargetEvenIfNotSeen);
        this.golem = pathfinderMob;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.mob.getNavigation().isDone();
        } else {
            return /*!this.mob.isWithinRestriction(livingentity.blockPosition())
                    ? false
                    :*/ !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
        }
    }
}
