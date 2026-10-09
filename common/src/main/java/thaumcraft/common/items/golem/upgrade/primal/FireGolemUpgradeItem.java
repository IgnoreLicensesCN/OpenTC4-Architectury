package thaumcraft.common.items.golem.upgrade.primal;

import com.linearity.opentc4.annotations.StoleFrom;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;

import java.util.EnumSet;
import java.util.List;

//GolemUpgrade:2
public class FireGolemUpgradeItem extends Item implements TravelingTrunkEntity.ITravelingTrunkUpgradeItem {
    public FireGolemUpgradeItem(Properties properties) {
        super(properties);
    }
    public FireGolemUpgradeItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("golem_upgrade.fire.desc"));
    }

    @Override
    public void travelingTrunkUpgrade$registerTargetGoals(TravelingTrunkEntity trunk, ItemStack upgradeStack, GoalSelector targetSelector) {
        targetSelector.addGoal(1, new TravelingTrunkEntityOwnerHurtByTargetGoal(trunk));
        targetSelector.addGoal(2, new TravelingTrunkOwnerHurtTargetGoal(trunk));
    }

    @StoleFrom("net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal")
    public static class TravelingTrunkOwnerHurtTargetGoal extends TargetGoal {
        private final TravelingTrunkEntity tameAnimal;
        private LivingEntity ownerLastHurt;
        private int timestamp;

        public TravelingTrunkOwnerHurtTargetGoal(TravelingTrunkEntity tamableAnimal) {
            super(tamableAnimal, false);
            this.tameAnimal = tamableAnimal;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (!this.tameAnimal.thaumcraft$getStay()) {
                LivingEntity livingEntity = this.tameAnimal.getOwner();
                if (livingEntity == null) {
                    return false;
                } else {
                    this.ownerLastHurt = livingEntity.getLastHurtMob();
                    int i = livingEntity.getLastHurtMobTimestamp();
                    return i != this.timestamp
                            && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT)
                            && this.tameAnimal.wantsToAttack(this.ownerLastHurt, livingEntity);
                }
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.mob.setTarget(this.ownerLastHurt);
            LivingEntity livingEntity = this.tameAnimal.getOwner();
            if (livingEntity != null) {
                this.timestamp = livingEntity.getLastHurtMobTimestamp();
            }

            super.start();
        }
    }
    @StoleFrom("net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal")
    public static class TravelingTrunkEntityOwnerHurtByTargetGoal extends TargetGoal {
        private final TravelingTrunkEntity tameAnimal;
        private LivingEntity ownerLastHurtBy;
        private int timestamp;

	public TravelingTrunkEntityOwnerHurtByTargetGoal(TravelingTrunkEntity tamableAnimal) {
            super(tamableAnimal, false);
            this.tameAnimal = tamableAnimal;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (!this.tameAnimal.thaumcraft$getStay()) {
                LivingEntity livingEntity = this.tameAnimal.getOwner();
                if (livingEntity == null) {
                    return false;
                } else {
                    this.ownerLastHurtBy = livingEntity.getLastHurtByMob();
                    int i = livingEntity.getLastHurtByMobTimestamp();
                    return i != this.timestamp
                            && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT)
                            && this.tameAnimal.wantsToAttack(this.ownerLastHurtBy, livingEntity);
                }
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.mob.setTarget(this.ownerLastHurtBy);
            LivingEntity livingEntity = this.tameAnimal.getOwner();
            if (livingEntity != null) {
                this.timestamp = livingEntity.getLastHurtByMobTimestamp();
            }

            super.start();
        }
    }
}
