package thaumcraft.api.listeners.wandconsumption.consts;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.effects.VisCostAddEffectWithCategory;
import thaumcraft.api.listeners.wandconsumption.ConsumptionModifierCalculationContext;
import thaumcraft.api.listeners.wandconsumption.listeners.CalculateWandConsumptionListener;
import thaumcraft.api.wands.*;
import thaumcraft.api.wands.focus.IWandFocusItem;
import thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes;

import java.util.HashMap;
import java.util.Map;

import static com.linearity.opentc4.utils.bauble.BaubleUtils.forEachBauble;

public enum CalculateWandConsumptionListenerEnum {
    CASTING_MODIFIER(new CalculateWandConsumptionListener(0) {
        @Override
        public void onCalculation(ConsumptionModifierCalculationContext context) {
            var casting = context.casting;
            var wandStack = context.wandStack;
            var aspect = context.aspect;
            if ((casting instanceof IVisCostModifierOwnerItem modifierOwner)) {
                context.currentConsumption -= modifierOwner.getCostDiscountForAspect(wandStack, aspect);
            }
        }
    }),
    CASTING_MODIFIER_CRAFTING(new CalculateWandConsumptionListener(5) {
        @Override
        public void onCalculation(ConsumptionModifierCalculationContext context) {
            if (!context.crafting) {
                return;
            }
            var casting = context.casting;
            var wandStack = context.wandStack;
            var aspect = context.aspect;
            if ((casting instanceof IArcaneCraftingVisMultiplierProvider craftingVisMultiplierProvider)) {
                context.currentConsumption -= craftingVisMultiplierProvider.getCraftingVisMultiplier(wandStack, aspect);
            }
        }
    }),
    DISCOUNT_GEAR(new CalculateWandConsumptionListener(10) {
        @Override
        public void onCalculation(ConsumptionModifierCalculationContext context) {
            var casting = context.casting;
            var user = context.user;
            if (user instanceof Player player) {
                forEachBauble(player,(slot, baubleStack, item) -> {
                    var discountGear = IVisDiscountGear.getDiscountGearHandlerForItem(item);
                    if (discountGear != null) {
                        context.currentConsumption -= (discountGear.getVisCostPercentDecrease(baubleStack, player, context.aspect)/100F);
                    }
                    return false;
                });

                for (var equipStack : player.getArmorSlots()) {
                    IVisDiscountGear visDiscountGear = IVisDiscountGear.getDiscountGearHandlerForItem(equipStack.getItem());
                    if (visDiscountGear != null) {
                        context.currentConsumption -= (visDiscountGear.getVisCostPercentDecrease(equipStack, player, context.aspect)/100F);
                    }
                }
            }
        }
    }),
    VIS_COST_ADD_EFFECT(new CalculateWandConsumptionListener(20) {
        @Override
        public void onCalculation(ConsumptionModifierCalculationContext context) {
            var user = context.user;
            if (user != null) {
                Map<String,Integer> percentsWithCategory = new HashMap<>();
                for (var effectInstance:user.getActiveEffects()){
                    var effect = effectInstance.getEffect();
                    if (effect instanceof VisCostAddEffectWithCategory costAdder){
                        var category = costAdder.getVisCostAddCategory();
                        var costPercentage = costAdder.getVisCostAddPercentage(effectInstance.getAmplifier());
                        percentsWithCategory.merge(category, costPercentage,(oldPercentage,newPercentage) -> {
                            if (newPercentage <= 0){
                                return Math.min(oldPercentage,newPercentage);
                            }else {
                                return Math.max(oldPercentage,newPercentage);
                            }
                        });
                    }
                }
                for (var entry : percentsWithCategory.entrySet()){
                    context.currentConsumption += entry.getValue();
                }
            }
        }
    }),
    FOCUS_DISCOUNT(new CalculateWandConsumptionListener(30) {
        @Override
        public void onCalculation(ConsumptionModifierCalculationContext context) {
            if (context.casting instanceof IWandFocusEngineItem focusEngine) {
                if (focusEngine.canApplyFocus()){
                    var focusStack = focusEngine.getFocusItemStack(context.wandStack);
                    if (!focusStack.isEmpty() && !context.crafting && focusStack.getItem() instanceof IWandFocusItem<? extends Aspect> wandFocusItem) {
                        context.currentConsumption -= (float) wandFocusItem
                                .getWandUpgradesWithWandModifiers(focusStack,context.wandStack)
                                .getOrDefault(ThaumcraftFocusUpgradeTypes.FRUGAL,0) / 10.0F;
                    }
                }
            }
        }
    }),
    SCEPTRE(new CalculateWandConsumptionListener(40) {
        @Override
        public void onCalculation(ConsumptionModifierCalculationContext context) {
            if (context.casting instanceof IArcaneCraftingVisDiscountOwnerItem discountOwner) {
                context.currentConsumption -= discountOwner.getVisDiscount(context.wandStack);
            }
        }
    }),
    ENSURE_LOWER_BOUND(new CalculateWandConsumptionListener(10000) {
        @Override
        public void onCalculation(ConsumptionModifierCalculationContext context) {
            context.currentConsumption = Math.max(context.currentConsumption, 0.1F);//TODO:[maybe wont finished]Discuss if we really need this.
        }
    });
    public final  CalculateWandConsumptionListener listener;
    CalculateWandConsumptionListenerEnum(CalculateWandConsumptionListener listener) {
        this.listener = listener;
    }

}
