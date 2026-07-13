package thaumcraft.common.items.abstracts.wandabstraction.focus;

import com.linearity.opentc4.annotations.ModifiableCopy;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.common.items.abstracts.wandabstraction.wand.IWandComponentsOwnerItem;
import thaumcraft.common.items.abstracts.wandabstraction.component.IFocusUpgradeModifierComponentItem;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.items.wands.render.waveanimations.AbstractWandWaveAnimation;
import thaumcraft.common.items.wands.render.waveanimations.ThaumcraftWandWaveAnimations;

import java.util.List;

public interface IWandFocusItem<Asp extends Aspect> {

    //i hate this part
    @Unmodifiable
    @Deprecated(since = "vote in democracy to remove this and use map for faster calc if there's lots of upgrades")
    //not recommended
    List<FocusUpgradeType> getAppliedFocusUpgradesWithOrder(ItemStack focusStack);

    @ApiStatus.Internal
    default @ModifiableCopy Object2IntMap<FocusUpgradeType> getAppliedFocusUpgrades(ItemStack focusStack){
        var upgrades = getAppliedFocusUpgradesWithOrder(focusStack);
        var map = new Object2IntOpenHashMap<FocusUpgradeType>();
        for (var upgrade:upgrades){
            map.merge(upgrade, 1,Integer::sum);
        }
        return map;
    }
    //use this!
    default Object2IntMap<FocusUpgradeType> getFocusUpgradesWithWandModifiers(ItemStack focusStack, @Nullable ItemStack wandStack) {
        var appliedUpgrades = getAppliedFocusUpgrades(focusStack);
        if (wandStack == null) {
            return appliedUpgrades;
        }
        if (wandStack.getItem() instanceof IWandComponentsOwnerItem componentsOwner) {
            for (ItemStack component: componentsOwner.getWandComponents(wandStack)) {
                if (component.getItem() instanceof IFocusUpgradeModifierComponentItem modifier) {
                    appliedUpgrades = modifier.modifyWandUpgrades(component,appliedUpgrades);
                }
            }
        }
        return appliedUpgrades;
    }
    void storeFocusUpgrades(ItemStack stack, List<FocusUpgradeType> wandUpgrades);
    void addFocusUpgrade(ItemStack stack, FocusUpgradeType type);

    default boolean isUpgradedWith(ItemStack focusStack, FocusUpgradeType focusUpgradetype,Object2IntMap<FocusUpgradeType> upgrades) {
        return upgrades.getInt(focusUpgradetype) > 0;
    }

    default int getRank(ItemStack focusStack) {
        return getAppliedFocusUpgrades(focusStack).size();
    }

    default boolean canApplyUpgrade(ItemStack focusStack, LivingEntity upgradeApplier, FocusUpgradeType type) {
        return getPossibleUpgradesByRank(focusStack,getRank(focusStack)).contains(type) && type.canApplyTo(focusStack,this);
    }

    /**
     * How much vis does this focus consume per activation.
     */
    CentiVisList<Asp> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades);

    /**
     * This returns how many milliseconds must pass before the focus can be activated again.
     */
    int getActivationCooldownTicks(ItemStack focusStack, @NotNull ItemStack wandStack);

    /**
     * What upgrades can be applied to this focus <s>for ranks 1 to 5</s>
     */
    @NotNull List<FocusUpgradeType> getPossibleUpgradesByRank(ItemStack focusStack, int rank);


    //only for displaying
    default boolean isCentiVisCostPerTick(ItemStack focusStack,@Nullable ItemStack wandStack) {
        return false;
    }

    //you need to cooldown by yourself
    default InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandStack, ItemStack focusStack, Level level, LivingEntity user, InteractionHand interactionHand) {
        return InteractionResultHolder.pass(wandStack);
    }

    //you need to cooldown by yourself
    //call startUsingItem to get here
    default void onUsingFocusTick(ItemStack wandStack,ItemStack focusStack, LivingEntity user, int count) {}

    //you need to cooldown by yourself
    default void onStoppedUsingFocus(ItemStack wandStack, ItemStack focusStack, Level world, LivingEntity user, int count) {}

    default void onLeftClickBlock(ItemStack wandStack, ItemStack focusStack, LivingEntity user, InteractionHand interactionHand) {}

    //null -> dont handle it
    //"onEntitySwing"
    default @Nullable InteractionResult onFocusUseOn(UseOnContext useOnContext,ItemStack focusStack) {
        return null;
    }
    default AbstractWandWaveAnimation getWaveAnimation(ItemStack focusStack,@Nullable ItemStack wandStack){
        return ThaumcraftWandWaveAnimations.WAVE;
    }
}
