package thaumcraft.api.wands;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;

import java.util.List;
import java.util.Map;

public interface IWandFocusItem<Asp extends Aspect> {

    List<FocusUpgradeType> getAppliedWandUpgradesWithOrder(ItemStack focusStack);
    Map<FocusUpgradeType,Integer> getAppliedWandUpgrades(ItemStack focusStack);
    default Map<FocusUpgradeType,Integer> getWandUpgradesWithWandModifiers(ItemStack focusStack,@Nullable ItemStack wandStack) {
        var appliedUpgrades = getAppliedWandUpgrades(focusStack);
        if (wandStack == null) {
            return appliedUpgrades;
        }
        if (wandStack.getItem() instanceof IWandComponentsOwner componentsOwner) {
            for (ItemStack component: componentsOwner.getWandComponents(wandStack)) {
                if (component.getItem() instanceof IWandUpgradeModifier modifier) {
                    appliedUpgrades = modifier.modifyWandUpgrades(appliedUpgrades);
                }
            }
        }
        return appliedUpgrades;
    };
    void storeWandUpgrades(ItemStack stack, List<FocusUpgradeType> wandUpgrades);
    void addWandUpgrade(ItemStack stack, FocusUpgradeType type);

    default boolean isUpgradedWith(ItemStack focusstack, FocusUpgradeType focusUpgradetype) {
        return getAppliedWandUpgrades(focusstack).containsKey(focusUpgradetype);
    }

    default boolean canApplyUpgrade(ItemStack focusstack, Player player, FocusUpgradeType type, int rank) {
        return true;
    }

    /**
     * How much vis does this focus consume per activation.
     */
    CentiVisList<Asp> getCentiVisCost(ItemStack focusstack, @Nullable ItemStack wandStack);

    /**
     * This returns how many milliseconds must pass before the focus can be activated again.
     */
    int getActivationCooldown(ItemStack focusstack);

    /**
     * Used by foci like equal trade to determine their area in artchitect mode
     */
    int getMaxAreaSize(ItemStack focusstack);

    /**
     * What upgrades can be applied to this focus for ranks 1 to 5
     */
    List<FocusUpgradeType> getPossibleUpgradesByRank(ItemStack focusstack);


    default
    /**
     * Just insert two alphanumeric characters before this string in your focus item class
     */
    String getSortingHelper(ItemStack focusstack) {
        StringBuilder out= new StringBuilder();
        for (FocusUpgradeType id: getAppliedWandUpgrades(focusstack).keySet()) {
            out.append(id.id());
        }
        return out.toString();
    }

    default boolean isVisCostPerTick() {
        return false;
    }

    //you need to cooldown by yourself
    default InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandstack, ItemStack focusStack, Level world, Player player, InteractionHand interactionHand) {
		// TODO Auto-generated method stub
		return null;
	}

    //you need to cooldown by yourself
    default void onUsingFocusTick(ItemStack wandstack,ItemStack focusStack, LivingEntity user, int count) {
		// TODO Auto-generated method stub
	}

    //you need to cooldown by yourself
    default void onPlayerStoppedUsingFocus(ItemStack wandstack,ItemStack focusStack, Level world, LivingEntity user, int count) {
		// TODO Auto-generated method stub

	}

    //you need to cooldown by yourself
//    default boolean onFocusBlockStartBreak(ItemStack wandstack, int x, int y,int z, Player player) {
//		// TODO Auto-generated method stub
//		return false;
//	}
    default void onLeftClickBlock(ItemStack wandstack,ItemStack focusStack, Player player, InteractionHand interactionHand) {

    }
}
