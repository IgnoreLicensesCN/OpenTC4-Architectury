package thaumcraft.common.items.wands.foci;

import com.google.gson.JsonObject;
import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.StatCollector;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.IWandFocusItem;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.linearity.opentc4.Consts.FocusUpgradeCompoundTagAccessors.FOCUS_UPGRADE_JSON_ACCESSOR;

public abstract class FocusBasicItem extends Item implements IWandFocusItem {
    public FocusBasicItem(Properties properties) {
        super(properties);
    }

    public void addFocusInformation(ItemStack focusstack, List<Component> list) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Map.Entry<FocusUpgradeType, Integer> entry:this.getWandUpgrades(focusstack).entrySet()) {
            var upgradeType = FocusUpgradeType.getType(entry.getKey().id());
            list.add(Component.literal(ChatFormatting.DARK_PURPLE +upgradeType.getLocalizedName()+
                    (" "+ StatCollector.translateToLocal("enchantment.level." + entry.getValue()))));
        }
    }

    @Override
    public Map<FocusUpgradeType, Integer> getWandUpgrades(ItemStack stack) {
        Map<FocusUpgradeType, Integer> map = new HashMap<>();
        if (!stack.hasTag()) {
            return map;
        }
        var tag = stack.getTag();
        if (tag == null){
            return map;
        }
        var json = FOCUS_UPGRADE_JSON_ACCESSOR.readFromCompoundTag(tag);
        if (json == null){
            return map;
        }

        for (var element:json.entrySet()){
            map.put(FocusUpgradeType.getType(element.getKey()),element.getValue().getAsInt());
        }

        return map;
    }

    @Override
    public void storeWandUpgrades(ItemStack stack, Map<FocusUpgradeType, Integer> wandUpgrades) {
        if (wandUpgrades == null || wandUpgrades.isEmpty()) {
            // 如果没有升级，就删除 tag 中对应的 key
            if (stack.hasTag()) {
                var tag = stack.getTag();
                if (tag == null){return;}
                tag.remove(FOCUS_UPGRADE_JSON_ACCESSOR.tagKey);
            }
            return;
        }

        var tag = stack.getOrCreateTag();
        JsonObject json = new JsonObject();

        // 填充 JsonObject
        for (var entry : wandUpgrades.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                json.addProperty(entry.getKey().id(), entry.getValue());
            }
        }

        // 写入 CompoundTag
        FOCUS_UPGRADE_JSON_ACCESSOR.writeToCompoundTag(tag, json);
    }


    @Override
    public void addWandUpgrade(ItemStack stack, FocusUpgradeType type) {
        var tag = stack.getOrCreateTag();
        var json = FOCUS_UPGRADE_JSON_ACCESSOR.readFromCompoundTag(tag);
        if (json == null){
            json = new JsonObject();
        }
        int level = 0;
        if (json.has(type.id())) { // has() 判断 key 是否存在
            try {
                level = json.get(type.id()).getAsInt();
            } catch (Exception e) {
                OpenTC4.LOGGER.error("Failed to parse level {}",json, e);
            }
        }
        level += 1;

        // 放回 JsonObject
        json.addProperty(type.id(), level);
        FOCUS_UPGRADE_JSON_ACCESSOR.writeToCompoundTag(tag, json);

    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        AspectList al = this.getVisCost(stack);
        if (al!=null && al.size()>0) {
            list.add(Component.literal(StatCollector.translateToLocal(isVisCostPerTick()?"item.Focus.cost2":"item.Focus.cost1")));
            for (Aspect aspect:al.getAspectsSorted()) {
                DecimalFormat myFormatter = new DecimalFormat("#####.##");
                String amount = myFormatter.format(al.getAmount(aspect)/100f);
                list.add(Component.literal(" §"+aspect.getChatcolor()+aspect.getName()+"§r x "+ amount));
            }
        }
        addFocusInformation(stack,list);
    }
}
