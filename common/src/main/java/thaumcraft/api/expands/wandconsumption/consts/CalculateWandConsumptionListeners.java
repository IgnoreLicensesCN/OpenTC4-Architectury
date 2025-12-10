package thaumcraft.api.expands.wandconsumption.consts;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.wandconsumption.listeners.CalculateWandConsumptionListener;
import thaumcraft.api.wands.*;
import thaumcraft.common.items.wands.WandManager;

public class CalculateWandConsumptionListeners {
    public static final CalculateWandConsumptionListener CASTING_MODIFIER = new CalculateWandConsumptionListener(0) {
        @Override
        public float onCalculation(Item casting, ItemStack stack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
            if ((casting instanceof WandComponentsOwner componentsOwner)) {
                var cap = componentsOwner.getWandComponents(stack);
                if (cap instanceof VisCostModifierOwner visCostModifierOwner) {
                    currentConsumption -= (1-visCostModifierOwner.getSpecialCostModifierAspects().getOrDefault(aspect,visCostModifierOwner.getBaseCostModifier()));
                }
            }
            return currentConsumption;
        }
    };
    public static final CalculateWandConsumptionListener PLAYER_DISCOUNT = new CalculateWandConsumptionListener(10) {
        @Override
        public float onCalculation(Item casting, ItemStack stack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
            if (user != null) {
                currentConsumption -= WandManager.getTotalVisDiscount(user, aspect);
            }
            return currentConsumption;
        }
    };
    public static final CalculateWandConsumptionListener FOCUS_DISCOUNT = new CalculateWandConsumptionListener(20) {
        @Override
        public float onCalculation(Item casting, ItemStack stack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
            if (casting instanceof WandFocusEngine focusEngine) {
                if (focusEngine.canApplyFocus()){
                    var focusStack = focusEngine.getFocusItemStack(stack);
                    if (focusStack != null && !crafting && focusStack.getItem() instanceof IWandFocusItem wandFocusItem) {
                        currentConsumption -= (float) wandFocusItem.getWandUpgrades(stack).getOrDefault(FocusUpgradeType.frugal,0) / 10.0F;
                    }
                }
            }
            return currentConsumption;
        }
    };
    public static final CalculateWandConsumptionListener SCEPTRE = new CalculateWandConsumptionListener(30) {
        @Override
        public float onCalculation(Item casting, ItemStack stack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
            if (casting instanceof ArcaneCraftingVisDiscountOwner discountOwner) {
                currentConsumption -= discountOwner.getVisDiscount(stack);
            }
            return currentConsumption;
        }
    };
    public static final CalculateWandConsumptionListener ENSURE_LOWER_BOUND = new CalculateWandConsumptionListener(10000) {
        @Override
        public float onCalculation(Item casting, ItemStack stack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
            return Math.max(currentConsumption, 0.1F);
        }
    };

}
