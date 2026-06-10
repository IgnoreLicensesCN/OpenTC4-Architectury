package thaumcraft.api.wands.focus;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.IWandComponentsOwnerItem;
import thaumcraft.api.wands.IWandUpgradeModifier;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;

import java.util.List;

public interface IWandFocusItem<Asp extends Aspect> {

    List<FocusUpgradeType> getAppliedWandUpgradesWithOrder(ItemStack focusStack);
    Object2IntMap<FocusUpgradeType> getAppliedWandUpgrades(ItemStack focusStack);
    default Object2IntMap<FocusUpgradeType> getWandUpgradesWithWandModifiers(ItemStack focusStack,@Nullable ItemStack wandStack) {
        var appliedUpgrades = getAppliedWandUpgrades(focusStack);
        if (wandStack == null) {
            return appliedUpgrades;
        }
        if (wandStack.getItem() instanceof IWandComponentsOwnerItem componentsOwner) {
            for (ItemStack component: componentsOwner.getWandComponents(wandStack)) {
                if (component.getItem() instanceof IWandUpgradeModifier modifier) {
                    appliedUpgrades = modifier.modifyWandUpgrades(component,appliedUpgrades);
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

    default boolean canApplyUpgrade(ItemStack focusstack, Player player, FocusUpgradeType type) {
        return canApplyUpgrade(focusstack, player, type,getRank(focusstack));
    }

    default int getRank(ItemStack focusStack) {
        return getAppliedWandUpgrades(focusStack).size();
    }

    default boolean canApplyUpgrade(ItemStack focusstack, Player player, FocusUpgradeType type, int rank) {
        return getPossibleUpgradesByRank(focusstack).contains(type) && type.canApplyTo(focusstack,this);
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


    /**
     * Just insert two alphanumeric characters before this string in your focus item class
     */
    default String getSortingHelper(ItemStack focusstack) {
        StringBuilder out= new StringBuilder();
        for (FocusUpgradeType id: getAppliedWandUpgrades(focusstack).keySet()) {
            out.append(id.id());
        }
        return out.toString();
    }

    default boolean isCentiVisCostPerTick() {
        return false;
    }

    //you need to cooldown by yourself
    default InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandstack, ItemStack focusStack, Level world, Player player, InteractionHand interactionHand) {return InteractionResultHolder.pass(wandstack);}

    //you need to cooldown by yourself
    default void onUsingFocusTick(ItemStack wandstack,ItemStack focusStack, LivingEntity user, int count) {}

    //you need to cooldown by yourself
    default void onPlayerStoppedUsingFocus(ItemStack wandstack,ItemStack focusStack, Level world, LivingEntity user, int count) {}

    default void onLeftClickBlock(ItemStack wandstack,ItemStack focusStack, Player player, InteractionHand interactionHand) {}
}
