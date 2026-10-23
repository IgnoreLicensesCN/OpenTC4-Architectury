package thaumcraft.common.items.golem.upgrade.decoration;

import com.linearity.opentc4.mixinaccessors.ItemCooldownOwnerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ai.goals.GolemRangedAttackGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.entities.projectile.DartEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.rangedattack.IGolemRangedAttackPerformerItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.rangedattack.IGolemShootCooldownModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.rangedattack.IGolemShootAccuracyModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;
import thaumcraft.common.lib.resourcelocations.GoalResourceLocation;

import java.util.*;
import java.util.function.Function;

import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.START_LEFT_ARM_TIMER;
import static thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem.DefaultDecorationTypes.*;

//ItemGolemDecoration:0
public class GolemDartLauncherItem extends Item
        implements
        IGolemDecorationItem,
        IGolemRangedAttackPerformerItem,
        IGolemAIProviderItem,
        ItemCooldownOwnerEntity.IItemCooldownProviderItem
{
    public static final GoalResourceLocation DART_ATTACK = GoalResourceLocation.of(Thaumcraft.MOD_ID,"dart_attack");
    public static final Function<BaseGolemEntity, Goal> DART_ATTACK_GOAL = GolemRangedAttackGoal::new;

    public GolemDartLauncherItem() {
        this(new Properties());
    }
    public GolemDartLauncherItem(Properties properties) {
        super(properties);
    }

    public static final Set<DecorationOccupiedTypeResourceLocation> OCCUPIED_TYPES = Set.of(ARM_RIGHT,HAND_RIGHT);
    @Override
    public @Unmodifiable Set<DecorationOccupiedTypeResourceLocation> getOccupiedTypes(BaseGolemEntity golem, ItemStack stack) {
        return OCCUPIED_TYPES;
    }

    @Override
    public boolean canPerformRangedAttackForGolem(LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f) {
        if (shooter instanceof ItemCooldownOwnerEntity cooldownOwner){
            return !cooldownOwner.opentc4$getItemCooldowns().isOnCooldown(upgradeStack.getItem());
        }
        return true;
    }

    public static final float DEFAULT_DIVERGENCE = 7;
    public static final float DEFAULT_COOLDOWN = 30;

    @Override
    public void performRangedAttackForGolem(LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f) {
        float divergence = DEFAULT_DIVERGENCE;
        if (shooter instanceof BaseGolemEntity baseGolem){
            for (var modifierPair:baseGolem.golemUpgradesCacheView.get(IGolemShootAccuracyModifierItem.class)){
                divergence = modifierPair.a().modifyShootAccuracy(baseGolem, modifierPair.b(), divergence, DEFAULT_DIVERGENCE);
            }
        }
        divergence = Math.max(0,divergence);

        var dart = new DartEntity(shooter, target,shooter.level(), 1.6F, divergence/* - (float)this.getUpgradeAmount(3) * 1.75F TODO:impl this*/);
        float damage = (float) shooter.getAttributeValue(Attributes.ATTACK_DAMAGE);
        dart.setBaseDamage(damage * 0.4F);
        shooter.playSound(ThaumcraftSounds.GOLEM_IRON_SHOOT, 0.5F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.6F));
        shooter.level().addFreshEntity(dart);
        if (shooter instanceof BaseGolemEntity golem) {
            golem.level().broadcastEntityEvent(golem, START_LEFT_ARM_TIMER);
        }
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        IGolemAIProviderItem.super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoalIfNotExist(2,DART_ATTACK,DART_ATTACK_GOAL,golem);
    }

    @Override
    public int getItemCooldownForItemStackAndEntity(ItemStack itemStack, LivingEntity livingEntity) {
        float cooldown = DEFAULT_COOLDOWN;
        if (livingEntity instanceof BaseGolemEntity baseGolem){
            for (var modifierPair:baseGolem.golemUpgradesCacheView.get(IGolemShootCooldownModifierItem.class)){
                cooldown = modifierPair.a().modifyDartCooldown(baseGolem, modifierPair.b(), cooldown, DEFAULT_COOLDOWN);
            }
        }
        cooldown = Math.max(0,cooldown);
        return (int) cooldown /* - this.theGolem.getUpgradeAmount(0) * 8 todo upgrade decrease cooldown and impl this*/;
    }
}
