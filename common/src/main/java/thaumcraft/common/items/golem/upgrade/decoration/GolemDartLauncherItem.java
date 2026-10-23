package thaumcraft.common.items.golem.upgrade.decoration;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.entities.projectile.DartEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.IGolemRangedAttackPerformerItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.IGolemShootDartAccuracyModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;

import java.util.*;

import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.START_LEFT_ARM_TIMER;
import static thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem.DefaultDecorationTypes.*;

//ItemGolemDecoration:0
public class GolemDartLauncherItem extends Item implements IGolemDecorationItem, IGolemRangedAttackPerformerItem, IGolemAIProviderItem {
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
        return true;
    }

    @Override
    public void performRangedAttackForGolem(LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f) {
        float divergence = 7;
        if (shooter instanceof BaseGolemEntity baseGolem){
            for (var modifierPair:baseGolem.golemUpgradesCacheView.get(IGolemShootDartAccuracyModifierItem.class)){
                divergence = modifierPair.a().modifyShootAccuracy(baseGolem, modifierPair.b(), divergence);
            }
        }

        var dart = new DartEntity(shooter, target,shooter.level(), 1.6F, divergence/* - (float)this.getUpgradeAmount(3) * 1.75F TODO:this*/);
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
        //TODO
    }
}
