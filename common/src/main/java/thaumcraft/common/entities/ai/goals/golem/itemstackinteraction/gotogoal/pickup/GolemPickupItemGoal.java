package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction.gotogoal.pickup;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.ITEM_ENTITY_TEST;
import static thaumcraft.common.entities.golems.GolemHelper.getGolemCarrySpace;

public class GolemPickupItemGoal extends AbstractGolemGoal {
    protected int targetEntityID = 0;
    protected int duration = 0;
    public GolemPickupItemGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public boolean canUse() {
        if (golem.tickCount % getGolemGoalDelay() != 0){
            return false;
        }
        return refreshAndGetTargetEntity() != null;
    }

    protected ItemEntity getCurrentTargetEntity() {
        var level = golem.level();
        var entityTargeting = level.getEntity(targetEntityID);
        if (entityTargeting instanceof ItemEntity itemEntity && !itemEntity.isRemoved()) {
            return itemEntity;
        }
        return null;
    }

    @Nullable
    protected ItemEntity refreshAndGetTargetEntity() {
        var level = golem.level();
        var current = getCurrentTargetEntity();
        if (current != null) {return current;}
        double minRangeSqr = Double.MAX_VALUE;
        double acceptRange = golem.getHomeRange();
        var homePos = golem.getRestrictCenter().getCenter();
        var targets = level.getEntities(
                ITEM_ENTITY_TEST,
                AABB.ofSize(golem.getRestrictCenter().getCenter(), acceptRange,acceptRange,acceptRange),(_ignored) -> true);
        ItemEntity target = null;
        acceptRange *= acceptRange;
        for (var itemEntity : targets) {
            var distanceToGolemSqr = itemEntity.distanceToSqr(golem);
            if (itemEntity.distanceToSqr(homePos) < acceptRange &&
                    distanceToGolemSqr < minRangeSqr && !itemEntity.getItem().isEmpty()
            ) {
                minRangeSqr = distanceToGolemSqr;
                target = itemEntity;
            }
        }
        if (target != null){
            targetEntityID = target.getId();
        }
        return target;
    }

    @Override
    public void tick() {
        super.tick();
        duration -= 1;
        var target = getCurrentTargetEntity();
        if (target != null) {
            golem.lookAt(target, 30.0F, 30.0F);
            double dist = this.golem.distanceToSqr(target);
            if (dist <= (double) 2.0F) {
                this.tryPickUp();
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return duration > 0 && golem.getNavigation().isInProgress() && getCurrentTargetEntity() != null;
    }

    @Override
    public void stop() {
        super.stop();
        targetEntityID = 0;
        duration = 0;
        golem.getNavigation().stop();
    }

    @Override
    public void start() {
        super.start();
        duration = 1000;
        var target = getCurrentTargetEntity();
        if (target != null) {
            this.golem.getNavigation().moveTo(target, golem.getMoveControl().getSpeedModifier());
        }
    }

    protected void tryPickUp() {
        int amount;
        var target = getCurrentTargetEntity();
        if (target != null) {
            ItemStack stack = target.getItem();
            int stackCount = stack.getCount();
            int carry = 0;
            var mainHandStack = golem.getMainHandItem();
            if (mainHandStack.isEmpty() || ItemStack.isSameItemSameTags(stack,mainHandStack)) {
                carry = getGolemCarrySpace(golem, stackCount,stack.getMaxStackSize());
            }
            if (stackCount < carry) {
                amount = stackCount;
            } else {
                amount = carry;
            }
            if (amount > 0) {
                if (mainHandStack.isEmpty()) {
                    stack = stack.split(amount);
                    golem.setItemInHand(InteractionHand.MAIN_HAND, stack);
                } else {
                    stack.shrink(amount);
                    mainHandStack.setCount(mainHandStack.getCount() + amount);
                }
            }

            if (amount > 0) {
                var random = golem.getRandom();
                target.playSound(SoundEvents.ITEM_PICKUP,0.2F,2.8F*random.nextFloat()+0.6F);
            }
        }
    }
}
