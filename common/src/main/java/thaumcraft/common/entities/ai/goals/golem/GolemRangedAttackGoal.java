package thaumcraft.common.entities.ai.goals.golem;

import com.linearity.opentc4.annotations.StoleFrom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack.rangedattack.IGolemRangedAttackPerformerItem;

import java.util.EnumSet;

@StoleFrom("net.minecraft.world.entity.ai.goal.RangedAttackGoal")
public class GolemRangedAttackGoal extends Goal {
    protected final BaseGolemEntity golem;
    @Nullable
    protected LivingEntity target;
    protected final double speedModifier;
    protected int seeTime;
    protected final int seeTimeRequired;
    protected final float attackRadiusMin;
    protected final float attackRadiusMinSqr;
//    protected final float attackRadiusMid;
    public GolemRangedAttackGoal(BaseGolemEntity golem){
        this(golem,0);
    }
    public GolemRangedAttackGoal(BaseGolemEntity golem, int seeTimeRequired){
        this(golem, 1,seeTimeRequired);
    }

    public GolemRangedAttackGoal(BaseGolemEntity golem, double speedModifier, int seeTimeRequired){
        this(golem, speedModifier,3,seeTimeRequired);
    }
    public GolemRangedAttackGoal(BaseGolemEntity golem, double speedModifier, float attackRadiusMin/*, float attackRadiusMax*/, int seeTimeRequired) {
        this.golem = golem;
        this.speedModifier = speedModifier;
        this.attackRadiusMin = attackRadiusMin;
        this.attackRadiusMinSqr = this.attackRadiusMin * this.attackRadiusMin;
//        this.attackRadiusMax = attackRadiusMax;
//        this.attackRadiusMaxSqr = this.attackRadiusMax * this.attackRadiusMax;

//        this.attackRadiusMid = (this.attackRadiusMax + this.attackRadiusMin) / 2F;
        this.seeTimeRequired = seeTimeRequired;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    protected float getAttackRadiusMax(){
        return golem.getHomeRange() * 0.8F;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.golem.getTarget();
        if (target == null) {
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }
        float attackRadiusMaxSqr = getAttackRadiusMax();
        attackRadiusMaxSqr *= attackRadiusMaxSqr;
        if (!(target.distanceToSqr(golem) <= attackRadiusMaxSqr && attackRadiusMinSqr <= target.distanceToSqr(golem))){
            return false;
        }

        this.target = target;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.target == null || !this.target.isAlive()) {
            return false;
        }
        return this.canUse() && !this.golem.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }
        double d = this.golem.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean bl = this.golem.getSensing().hasLineOfSight(this.target);
        if (bl) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }

        if (!(d > this.getAttackRadiusMax()) && this.seeTime >= 5) {
            this.golem.getNavigation().stop();
        } else {
            this.golem.getNavigation().moveTo(this.target, this.speedModifier);
        }

        this.golem.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        float distancePercent = (float) Mth.clamp(Math.sqrt(d / this.getAttackRadiusMax()), 0.1, 1.0);
        for (var rangedAttackPerformerPair:golem.golemUpgradesCacheView.get(IGolemRangedAttackPerformerItem.class)){
            rangedAttackPerformerPair.getUpgradeType().performRangedAttackForGolemIfCan(golem,rangedAttackPerformerPair.getUpgradeStack(),this.target,distancePercent);
        }
    }
}
