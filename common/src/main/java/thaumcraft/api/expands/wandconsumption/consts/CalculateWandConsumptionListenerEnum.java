package thaumcraft.api.expands.wandconsumption.consts;

import com.linearity.opentc4.simpleutils.bauble.BaubleConsumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.effects.VisCostAddEffectWithCategory;
import thaumcraft.api.expands.wandconsumption.listeners.CalculateWandConsumptionListener;
import thaumcraft.api.wands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.linearity.opentc4.simpleutils.bauble.BaubleUtils.forEachBauble;

public enum CalculateWandConsumptionListenerEnum {
    CASTING_MODIFIER(new CalculateWandConsumptionListener(0) {
        @Override
        public float onCalculation(Item casting, ItemStack wandStack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
            if ((casting instanceof IWandComponentsOwner componentsOwner)) {
                var cap = componentsOwner.getWandComponents(wandStack);
                if (cap instanceof IVisCostModifierOwner visCostModifierOwner) {
                    currentConsumption -= (1-visCostModifierOwner.getSpecialCostModifierAspects().getOrDefault(aspect,visCostModifierOwner.getBaseCostModifier()));
                }
            }
            return currentConsumption;
        }
    }),
    DISCOUNT_GEAR(new CalculateWandConsumptionListener(10) {
        @Override
        public float onCalculation(Item casting, ItemStack wandStack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {

            AtomicReference<Float> currentConsumptionAtomic = new AtomicReference<>(currentConsumption);
            if (user instanceof Player player) {
                BaubleConsumer<IVisDiscountGear> visDiscountGearBaubleConsumer = (slot, baubleStack, visDiscountGear) -> {

                    currentConsumptionAtomic.set(currentConsumptionAtomic.get() + visDiscountGear.getVisDiscount(baubleStack, player, aspect));
                    return false;
                };
                forEachBauble(player, IVisDiscountGear.class, visDiscountGearBaubleConsumer);

                for (var equipStack : player.getArmorSlots()) {
                    var item = equipStack.getItem();
                    if (item instanceof IVisDiscountGear visDiscountGear) {
                        currentConsumptionAtomic.set(
                                currentConsumptionAtomic.get() + visDiscountGear.getVisDiscount(equipStack, user, aspect)
                        );
                    }
                }
            }
            return currentConsumptionAtomic.get();
        }
    }),
    VIS_COST_ADD_EFFECT(new CalculateWandConsumptionListener(20) {
        @Override
        public float onCalculation(Item casting, ItemStack wandStack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
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
                    currentConsumption += entry.getValue();
                }
            }
            return currentConsumption;
        }
    }),
    FOCUS_DISCOUNT(new CalculateWandConsumptionListener(30) {
        @Override
        public float onCalculation(Item casting, ItemStack wandStack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
            if (casting instanceof IWandFocusEngine focusEngine) {
                if (focusEngine.canApplyFocus()){
                    var focusStack = focusEngine.getFocusItemStack(wandStack);
                    if (focusStack != null && !crafting && focusStack.getItem() instanceof IWandFocusItem wandFocusItem) {
                        currentConsumption -= (float) wandFocusItem.getWandUpgradesWithWandModifiers(focusStack,wandStack).getOrDefault(FocusUpgradeType.frugal,0) / 10.0F;
                    }
                }
            }
            return currentConsumption;
        }
    }),
    SCEPTRE(new CalculateWandConsumptionListener(40) {
        @Override
        public float onCalculation(Item casting, ItemStack wandStack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
            if (casting instanceof IArcaneCraftingVisDiscountOwner discountOwner) {
                currentConsumption -= discountOwner.getVisDiscount(wandStack);
            }
            return currentConsumption;
        }
    }),
    ENSURE_LOWER_BOUND(new CalculateWandConsumptionListener(10000) {
        @Override
        public float onCalculation(Item casting, ItemStack wandStack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption) {
            return Math.max(currentConsumption, 0.1F);
        }
    });
    public final  CalculateWandConsumptionListener listener;
    CalculateWandConsumptionListenerEnum(CalculateWandConsumptionListener listener) {
        this.listener = listener;
    }
}
