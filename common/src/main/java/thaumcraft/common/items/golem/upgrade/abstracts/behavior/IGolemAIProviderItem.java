package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemAIProviderItem extends IGolemModifierItem, IGolemAbilityModifierItem {
    default void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GoalSelector goalSelector, GoalSelector targetSelector){
        //this.tasks.addTask(0, new AIAvoidCreeperSwell(this));
        //this.tasks.addTask(5, new AIOpenDoor(this, true));
        //         this.tasks.addTask(6, new AIReturnHome(this));
        //         this.tasks.addTask(7, new EntityAIWatchClosest(this, Player.class, 6.0F));
        //         this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        registerGoalForGolem(golem, upgradeStack, abilitiesData.goalSelector,abilitiesData.targetSelector);
    }
}
