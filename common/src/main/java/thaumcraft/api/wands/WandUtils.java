package thaumcraft.api.wands;

import com.linearity.opentc4.OpenTC4CommonProxy;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.aspects.PrimalAspect;
import thaumcraft.api.listeners.wandconsumption.ConsumptionModifierCalculator;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WandUtils {

    //generate capacity? or whatever you like
    public static CentiVisList<Aspect> getPrimalAspectCentiVisListWithValueCasted(int value) {
        return new CentiVisList<>(Aspects.getPrimalAspects().stream().collect(Collectors.toMap(a -> a, a -> value)));
    }
    public static CentiVisList<PrimalAspect> getPrimalAspectCentiVisListWithValue(int value) {
        return new CentiVisList<>(Aspects.getPrimalAspects().stream().collect(Collectors.toMap(a -> a, a -> value)));
    }
    public static CentiVisList<Aspect> getAspectsCentiVisListWithValue(Collection<Aspect> aspects, int value) {
        return new CentiVisList<>(aspects.stream().collect(Collectors.toMap(a -> a, a -> value)));
    }
    public static CentiVisList<Aspect> getAspectsCentiVisListWithValue(Aspect aspect, int value) {
        return new CentiVisList<>(aspect,value);
    }

    public static final DecimalFormat decimalFormat = new DecimalFormat("#######.##");
    public static void appendWandHoverText(Item wandItem, ItemStack wandStack, @Nullable Level level, List<Component> list, TooltipFlag flag, LivingEntity livingEntity) {
        int pos = list.size();
        String tt2 = "";
        boolean shiftKeyDownFlag = OpenTC4CommonProxy.INSTANCE.isShiftKeyDown();
        if (wandItem instanceof ICentiVisContainer<?> centiVisContainerNotCasted) {
            var tt = Component.empty();
            var centiVisContainer = (ICentiVisContainer<Aspect>) centiVisContainerNotCasted;
            var visOwning = centiVisContainer.getAllCentiVisOwning(wandStack);
            var visCapacity = centiVisContainer.getAllCentiVisCapacity(wandStack);


            for (var entry : visOwning.entrySet()) {
                var aspect = entry.getKey();
                String amountString = String.valueOf(((float)entry.getValue()) / 100.0F);
                String capacityString = String.valueOf(((float)visCapacity.getOrDefault(entry.getKey(),0)) / 100.0F);
                float mod = ConsumptionModifierCalculator.getConsumptionModifier(
                        wandItem,
                        wandStack,
                        livingEntity,
                        aspect,
                        false);
                String consumptionString = decimalFormat.format(mod * 100.0F);
                var focusConsumptionComponent = Component.empty();
                if (wandItem instanceof IWandFocusEngine engine && engine.canApplyFocus()) {
                    var focusStack = engine.getFocusItemStack(wandStack);
                    var focusItem = focusStack.getItem();
                    if (focusItem instanceof IWandFocusItem<?> wandFocusItemNotCasted) {
                        IWandFocusItem<Aspect> wandFocusItem = (IWandFocusItem<Aspect>) wandFocusItemNotCasted;
                        int amt = wandFocusItem.getCentiVisCost(focusStack,wandStack).getAmount(aspect);
                        if (amt > 0) {
                            focusConsumptionComponent =
                                    Component.literal(", "+decimalFormat.format((float) amt * mod / 100.0F) + " ")
                                            .append(Component.translatable(wandFocusItem.isVisCostPerTick() ? "wandItem.Focus.cost2" : "wandItem.Focus.cost1"))
                            ;
                        }
                    }
                }
                if (shiftKeyDownFlag) {
                    list.add(aspect.getName().copy()
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

            if (!shiftKeyDownFlag){
                list.add(Component.literal(tt.toString()));
            }
//            for (Aspect aspect : Aspect.getPrimalAspects()) {
//                if (tag.hasKey(aspect.getTag())) {
//                    String amount = decimalFormat.format((float) tag.getInteger(aspect.getTag()) / 100.0F);
//
//                    String consumption = decimalFormat.format(mod * 100.0F);
//                    ++num;
//                    tot = (int) ((float) tot + mod * 100.0F);
//                    String text = "";
//                    ItemStack focus = this.getFocusItem(wandStack);
//                    if (focus != null) {
//                        int amt = ((ItemFocusBasic) focus.getItem()).getCentiVisCost(focus).getAmount(aspect);
//                        if (amt > 0) {
//                            text = "§r, " + decimalFormat.format((float) amt * mod / 100.0F) + " " + Component.translatable(((ItemFocusBasic) focus.getItem()).isVisCostPerTick(focus) ? "wandItem.Focus.cost2" : "wandItem.Focus.cost1");
//                        }
//                    }
//
//                    if (Thaumcraft.proxy.isShiftKeyDown()) {
//                        list.add(" §" + aspect.getChatcolor() + aspect.getName() + "§r x " + amount + ", §o(" + consumption + "% " + Component.translatable("tc.vis.cost") + ")" + text);
//                    } else {
//                        if (tt.length() > 0) {
//                            tt.append(" | ");
//                        }
//
//                        tt.append("§").append(aspect.getChatcolor()).append(amount).append("§r");
//                    }
//                }
//            }

//            if (!Thaumcraft.proxy.isShiftKeyDown() && num > 0) {
//                list.add(tt.toString());
//                tot /= num;
//                tt2 = " (" + tot + "% " + Component.translatable("tc.vis.costavg") + ")";
//            }
        }

//        list.add(pos, ChatFormatting.GOLD + Component.translatable("wandItem.capacity.text") + " " + this.getMaxVis(wandStack) / 100 + "§r" + tt2);

        if (wandItem instanceof IWandFocusEngine engine && engine.canApplyFocus()) {
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

    public static void addFocusInformation(IWandFocusItem<? extends Aspect> focus,ItemStack focusstack, List<Component> list, TooltipFlag flag) {
		for (var entry:focus.getAppliedWandUpgrades(focusstack).entrySet()) {
            FocusUpgradeType type = entry.getKey();
            var id = type.id();
            var lvl = entry.getValue();
            var lvlComponent = (lvl>1?Component.literal(" ").append(Component.translatable("enchantment.level." + lvl)):Component.empty());
            list.add(
                    type.getLocalizedName().copy()
                            .append(lvlComponent)
                            .withStyle(style -> style.withColor(ChatFormatting.DARK_PURPLE))
            );
		}
	}
}
