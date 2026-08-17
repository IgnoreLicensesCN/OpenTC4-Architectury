package thaumcraft.common.entities.ai.goals;

import com.linearity.opentc4.annotations.StoleFrom;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

@StoleFrom("net.minecraft.world.entity.ai.goal.ZombieAttackGoal")
public class ZombieLikeAttackGoal extends MeleeAttackGoal {
    private final PathfinderMob zombie;
    private int raiseArmTicks;

    public ZombieLikeAttackGoal(PathfinderMob zombie, double d, boolean bl) {
        super(zombie, d, bl);
        this.zombie = zombie;
    }

    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    public void stop() {
        super.stop();
        this.zombie.setAggressive(false);
    }

    public void tick() {
        super.tick();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.zombie.setAggressive(true);
        } else {
            this.zombie.setAggressive(false);
        }
    }
}
