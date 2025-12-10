package thaumcraft.api.expands.wandconsumption.consts;

import net.minecraft.entity.player.Player;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.wandconsumption.listeners.CalculateWandConsumptionListener;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;

public class CalculateWandConsumptionListeners {
    public static final CalculateWandConsumptionListener CASTING_MODIFIER = new CalculateWandConsumptionListener(0) {
        @Override
        public float onCalculation(ItemWandCasting casting, ItemStack stack, Player player, Aspect aspect, boolean crafting, float currentConsumption) {
            if (casting.getCap(stack).getSpecialCostModifierAspects() != null
                    && casting.getCap(stack).getSpecialCostModifierAspects().contains(aspect)
            ) {
                return casting.getCap(stack).getSpecialCostModifier();
            } else {
                return casting.getCap(stack).getBaseCostModifier();
            }
        }
    };
    public static final CalculateWandConsumptionListener PLAYER_DISCOUNT = new CalculateWandConsumptionListener(10) {
        @Override
        public float onCalculation(ItemWandCasting casting, ItemStack stack, Player player, Aspect aspect, boolean crafting, float currentConsumption) {
            if (player != null) {
                currentConsumption -= WandManager.getTotalVisDiscount(player, aspect);
            }
            return currentConsumption;
        }
    };
    public static final CalculateWandConsumptionListener FOCUS_DISCOUNT = new CalculateWandConsumptionListener(20) {
        @Override
        public float onCalculation(ItemWandCasting casting, ItemStack stack, Player player, Aspect aspect, boolean crafting, float currentConsumption) {
            if (casting.getFocus(stack) != null && !crafting) {
                currentConsumption -= (float) casting.getFocusFrugal(stack) / 10.0F;
            }
            return currentConsumption;
        }
    };
    public static final CalculateWandConsumptionListener SCEPTRE = new CalculateWandConsumptionListener(30) {
        @Override
        public float onCalculation(ItemWandCasting casting, ItemStack stack, Player player, Aspect aspect, boolean crafting, float currentConsumption) {
            if (casting.isSceptre(stack)) {
                currentConsumption -= 0.1F;
            }
            return currentConsumption;
        }
    };
    public static final CalculateWandConsumptionListener SET_MIN = new CalculateWandConsumptionListener(10000) {
        @Override
        public float onCalculation(ItemWandCasting casting, ItemStack stack, Player player, Aspect aspect, boolean crafting, float currentConsumption) {
            return Math.max(currentConsumption, 0.1F);
        }
    };

}
