package thaumcraft.api.wands;

import com.linearity.opentc4.OpenTC4CommonProxy;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.PrimalAspect;
import thaumcraft.api.aspects.aspectlists.baseimpl.centivis.LinkedHashCentiVisList;
import thaumcraft.api.listeners.wandconsumption.ConsumptionModifierCalculator;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;
import thaumcraft.common.items.abstracts.wandabstraction.wand.IWandFocusEngineItem;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WandUtils {

    private static final Int2ObjectMap<CentiVisList<PrimalAspect>> map_AspectCentiVisListWithValue = new Int2ObjectOpenHashMap<>();

    @Unmodifiable
    public static CentiVisList<Aspect> getPrimalAspectCentiVisListWithValueCastedUnmodifiable(int value) {
        return (CentiVisList<Aspect>)(Object) getPrimalAspectCentiVisListWithValueUnmodifiable(value);
    }
    @Unmodifiable
    public static CentiVisList<PrimalAspect> getPrimalAspectCentiVisListWithValueUnmodifiable(int value) {
        return map_AspectCentiVisListWithValue.computeIfAbsent(
                value,val -> {
                    Object2IntLinkedOpenHashMap<PrimalAspect> map = new Object2IntLinkedOpenHashMap<>();
                    Aspects.getPrimalAspects().forEach(aspect -> {
                        map.put(aspect,val);
                    });
                    return LinkedHashCentiVisList.viewOf(map);
                }
        );
    }

    //generate capacity? or whatever you like
    public static CentiVisList<Aspect> getPrimalAspectCentiVisListWithValueCasted(int value) {
        return new LinkedHashCentiVisList<>(Aspects.getPrimalAspects().stream().collect(Collectors.toMap(a -> a, a -> value)));
    }
    public static CentiVisList<PrimalAspect> getPrimalAspectCentiVisListWithValue(int value) {

        return new LinkedHashCentiVisList<>(Aspects.getPrimalAspects().stream().collect(Collectors.toMap(a -> a, a -> value)));
    }
    public static CentiVisList<Aspect> getAspectsCentiVisListWithValue(Collection<Aspect> aspects, int value) {
        return new LinkedHashCentiVisList<>(aspects.stream().collect(Collectors.toMap(a -> a, a -> value)));
    }
    public static CentiVisList<Aspect> getAspectsCentiVisListWithValue(Aspect aspect, int value) {
        return LinkedHashCentiVisList.of(aspect,value);
    }

    public static final DecimalFormat CENTIVIS_DECIMAL_FORMAT = new DecimalFormat("#######.##");
    public static void appendWandHoverText(Item wandItem, ItemStack wandStack, @Nullable Level level, List<Component> list, TooltipFlag flag, LivingEntity livingEntity) {
        int pos = list.size();
        String tt2 = "";
        boolean shiftKeyDownFlag = OpenTC4CommonProxy.INSTANCE.isShiftKeyDown();
        if (wandItem instanceof ICentiVisContainerItem<?> centiVisContainerNotCasted) {
            var tt = Component.empty();
            var centiVisContainer = (ICentiVisContainerItem<Aspect>) centiVisContainerNotCasted;
            var visOwning = centiVisContainer.getAllCentiVisOwning(wandStack);
            var visCapacity = centiVisContainer.getAllCentiVisCapacity(wandStack);

            visOwning.forEach(
                    (aspect, value) -> addAspectSpecificText(wandItem, wandStack, list, livingEntity, aspect,value, visCapacity, shiftKeyDownFlag, tt)
            );

            if (!shiftKeyDownFlag){
                list.add(Component.literal(tt.toString()));
            }
        }

        if (wandItem instanceof IWandFocusEngineItem engine && engine.canApplyFocus()) {
            var focus = engine.getFocusItemStack(wandStack);
            var focusItem = focus.getItem();
            if (focusItem instanceof IWandFocusItem<? extends Aspect> wandFocusItem) {

                list.add(Component.literal(ChatFormatting.BOLD + "" + ChatFormatting.ITALIC + ChatFormatting.GREEN + focusItem.getName(focus)));
                if (OpenTC4CommonProxy.INSTANCE.isShiftKeyDown()){
                    addFocusInformation(wandFocusItem,focus,list,flag);
                }
            }
        }

    }

    private static void addAspectSpecificText(
            Item wandItem,
            ItemStack wandStack,
            List<Component> list,
            LivingEntity livingEntity,
            Aspect aspect,
            int aspectAmount,
            CentiVisList<Aspect> visCapacity,
            boolean shiftKeyDownFlag,
            MutableComponent tt) {
        String amountString = String.valueOf(((float) aspectAmount) / 100.0F);
        String capacityString = String.valueOf(((float) visCapacity.getOrDefault(aspect,0)) / 100.0F);
        float mod = ConsumptionModifierCalculator.getConsumptionModifier(
                wandItem,
                wandStack,
                livingEntity,
                aspect,
                false);
        String consumptionString = CENTIVIS_DECIMAL_FORMAT.format(mod * 100.0F);
        var focusConsumptionComponent = Component.empty();
        if (wandItem instanceof IWandFocusEngineItem engine && engine.canApplyFocus()) {
            var focusStack = engine.getFocusItemStack(wandStack);
            var focusItem = focusStack.getItem();
            if (focusItem instanceof IWandFocusItem<?> wandFocusItemNotCasted) {
                IWandFocusItem<Aspect> wandFocusItem = (IWandFocusItem<Aspect>) wandFocusItemNotCasted;
                int amt = wandFocusItem.getCentiVisCost(focusStack, wandStack).get(aspect);
                if (amt > 0) {
                    focusConsumptionComponent =
                            Component.literal(", "+ CENTIVIS_DECIMAL_FORMAT.format((float) amt * mod / 100.0F) + " ")
                                    .append(Component.translatable(wandFocusItem.isCentiVisCostPerTick(focusStack,wandStack) ? "wandItem.Focus.cost2" : "wandItem.Focus.cost1"))
                    ;
                }
            }
        }
        if (shiftKeyDownFlag) {
            list.add(
                    aspect.getName().copy()
                    .withStyle(style -> style.withColor(TextColor.fromRgb(aspect.getColor())))
                    .append(Component.literal(" x "+amountString + "/" + capacityString).withStyle(style -> Style.EMPTY))
                    .append(Component.literal(" ,").withStyle(style -> Style.EMPTY))
                    .append(Component.literal("(" + consumptionString + "% ")
                            .append(Component.translatable("tc.vis.cost"))
                            .append( ")")
                            .append(focusConsumptionComponent)
                            .withStyle(style -> Style.EMPTY.withItalic(true))
                            )
            );

        } else {
            if (!tt.copy().getString().isEmpty()) {
                tt.append(" | ");
            }
            tt.append(Component.literal(amountString).withStyle(style -> style.withColor(TextColor.fromRgb(aspect.color))));
        }
    }

    public static void addFocusInformation(IWandFocusItem<? extends Aspect> focus,ItemStack focusstack, List<Component> list, TooltipFlag flag) {
		for (var entry:focus.getAppliedFocusUpgrades(focusstack).object2IntEntrySet()) {
            FocusUpgradeType type = entry.getKey();
            var id = type.id();
            var lvl = entry.getIntValue();
            var lvlComponent = (lvl>1?Component.literal(" ").append(Component.translatable("enchantment.level." + lvl)):Component.empty());
            list.add(
                    type.name().copy()
                            .append(lvlComponent)
                            .withStyle(style -> style.withColor(ChatFormatting.DARK_PURPLE))
            );
		}
	}
}
