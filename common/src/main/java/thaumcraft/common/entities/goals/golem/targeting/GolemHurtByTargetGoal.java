package thaumcraft.common.entities.goals.golem.targeting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import java.lang.ref.WeakReference;
import java.util.function.Predicate;

public class GolemHurtByTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
    public static boolean golemCanTarget(@Nullable BaseGolemEntity golem,LivingEntity living) {
        if (golem == null){
            return false;
        }
        if (living instanceof OwnableEntity ownableEntity) {
            var owner = ownableEntity.getOwner();
            if (owner != null){
                if (golem.isOwnedBy(owner.getUUID())){
                    return false;
                }
                return golem != owner && !golem.isAlliedTo(owner);
            }
        }
        return true;
    }
    public static Predicate<LivingEntity> golemCanTargetAsPredicate(BaseGolemEntity golem) {
        var golemWeakRef = new WeakReference<>(golem);
        return living -> golemCanTarget(golemWeakRef.get(), living);
    }

    protected final BaseGolemEntity golem;

    public GolemHurtByTargetGoal(BaseGolemEntity mob){
        this(mob,true);
    }
    public GolemHurtByTargetGoal(BaseGolemEntity mob, boolean bl) {
        this(mob, 10, bl, false, null);
    }

    public GolemHurtByTargetGoal(BaseGolemEntity mob, boolean bl, Predicate<LivingEntity> predicate) {
        this(mob, 10, bl, false, predicate);
    }

    public GolemHurtByTargetGoal(BaseGolemEntity mob, boolean bl, boolean bl2) {
        this(mob, 10, bl, bl2, null);
    }

    public GolemHurtByTargetGoal(BaseGolemEntity mob, int i, boolean bl, boolean bl2, @Nullable Predicate<LivingEntity> predicate) {
        super(mob,LivingEntity.class,i, bl, bl2,predicate == null ? golemCanTargetAsPredicate(mob) : predicate);
        this.golem = mob;
    }
}
