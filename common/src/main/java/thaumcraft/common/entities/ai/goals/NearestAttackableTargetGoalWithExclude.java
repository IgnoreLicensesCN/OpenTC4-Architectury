package thaumcraft.common.entities.ai.goals;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class NearestAttackableTargetGoalWithExclude<T extends LivingEntity,ExcludeClass extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    protected final Class<ExcludeClass> excludeType;

    public NearestAttackableTargetGoalWithExclude(Mob mob, Class<T> class_,Class<ExcludeClass> excludeClass, boolean bl) {
        super(mob, class_, bl);
        this.excludeType = excludeClass;
    }

    public NearestAttackableTargetGoalWithExclude(Mob mob, Class<T> class_,Class<ExcludeClass> excludeClass, boolean bl, Predicate<LivingEntity> predicate) {
        super(mob, class_, bl, predicate);
        this.excludeType = excludeClass;
    }

    public NearestAttackableTargetGoalWithExclude(Mob mob, Class<T> class_,Class<ExcludeClass> excludeClass, boolean bl, boolean bl2) {
        super(mob, class_,  bl, bl2);
        this.excludeType = excludeClass;
    }

    public NearestAttackableTargetGoalWithExclude(Mob mob, Class<T> class_,Class<ExcludeClass> excludeClass, int i, boolean bl, boolean bl2, @Nullable Predicate<LivingEntity> predicate) {
        super(mob, class_, i, bl, bl2, predicate);
        this.excludeType = excludeClass;
    }

    protected void findTarget() {
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
            this.target = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(
                    this.targetType,
                    this.getTargetSearchArea(this.getFollowDistance()), (livingEntity) -> !excludeType.isInstance(livingEntity)),
                    this.targetConditions,
                    this.mob,
                    this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            this.target = this.mob.level().getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }

    }
}
