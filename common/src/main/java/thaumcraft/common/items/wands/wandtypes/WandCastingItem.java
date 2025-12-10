package thaumcraft.common.items.wands;

import com.linearity.opentc4.utils.StatCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.wandconsumption.ConsumptionModifierCalculator;
import thaumcraft.api.wands.*;
import thaumcraft.common.Thaumcraft;

import java.text.DecimalFormat;
import java.util.*;

import static com.linearity.opentc4.Consts.WandCastingCompoundTagAccessors.*;
import static thaumcraft.api.wands.WandUtils.appendWandHoverText;

//maybe just an example,you can also make you own one.
//i will use interface instead of (item instanceof WandCastingItem wandCasting)
public class WandCastingItem extends Item
        implements
        WandSpellEventListenable,
        WandCapOwner,
        WandRodOwner,
        EnchantmentRepairVisProvider,
        ArcaneCraftingVisProvider,
        ArcaneCraftingWand,
        WandFocusEngine,
        VisContainer,
        WandComponentsOwner{



    public WandCastingItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public Item getWandCapAsItem(@NotNull ItemStack stack) {
        if (!stack.hasTag()){
            return null;
        }
        var tag = stack.getTag();
        if (tag == null){
            return null;
        }
        if (!WAND_CAP_ACCESSOR.compoundTagHasKey(tag)){
            return null;
        }
        var capString = WAND_CAP_ACCESSOR.readFromCompoundTag(tag);
        ResourceLocation key = new ResourceLocation(capString);
        return BuiltInRegistries.ITEM.get(key);
    }

    @Override
    @Nullable
    public Item getWandRodAsItem(@NotNull ItemStack stack) {
        if (!stack.hasTag()){
            return null;
        }
        var tag = stack.getTag();
        if (tag == null){
            return null;
        }
        if (!WAND_ROD_ACCESSOR.compoundTagHasKey(tag)){
            return null;
        }
        var rodString = WAND_ROD_ACCESSOR.readFromCompoundTag(tag);
        ResourceLocation key = new ResourceLocation(rodString);
        return BuiltInRegistries.ITEM.get(key);
    }

    @Override
    public void onWandSpellEvent(WandSpellEventType event, Player player, ItemStack usingWand, BlockPos atBlockPos, Vec3 atVec3) {

        var cap = getWandCapItem(usingWand);
        var rod = getWandRodItem(usingWand);
        if (cap instanceof WandSpellEventListenable capListenable){
            capListenable.onWandSpellEvent(event, player, usingWand, atBlockPos, atVec3);
        }
        if (rod instanceof WandSpellEventListenable rodListenable){
            rodListenable.onWandSpellEvent(event, player, usingWand, atBlockPos, atVec3);
        }
    }

    @Override
    public void inventoryTick(ItemStack usingWand, Level level, Entity entity, int i, boolean bl) {
        var cap = getWandCapItem(usingWand);
        var rod = getWandRodItem(usingWand);
        if (cap instanceof InventoryTickableComponent tickableComponentCap){
            tickableComponentCap.tickAsComponent(usingWand, level, entity, i, bl);
        }
        if (rod instanceof InventoryTickableComponent tickableComponentRod){
            tickableComponentRod.tickAsComponent(usingWand, level, entity, i, bl);
        }
    }

    @Override
    public float getCraftingVisMultiplier(ItemStack usingWand, Aspect aspect) {
        var cap = getWandCapItem(usingWand);
        var rod = getWandRodItem(usingWand);
        float capMultiplier = 1;
        float rodMultiplier = 1;
        if (cap instanceof ArcaneCraftingVisProvider capProvider){
            capMultiplier = capProvider.getCraftingVisMultiplier(usingWand, aspect);
        }
        if (rod instanceof ArcaneCraftingVisProvider rodProvider){
            rodMultiplier = rodProvider.getCraftingVisMultiplier(usingWand, aspect);
        }

        return capMultiplier * rodMultiplier;
    }

    @Override
    public boolean canProvideVisForRepair() {
        return true;
    }

    @Override
    public ItemStack getFocusItemStack(ItemStack wand) {
        if (!wand.hasTag()){
            return null;
        }
        var tag = wand.getTag();
        if (tag == null){
            return null;
        }
        if (!WAND_FOCUS_ACCESSOR.compoundTagHasKey(tag)){
            return null;
        }
        return WAND_FOCUS_ACCESSOR.readFromCompoundTag(tag);
    }

    @Override
    public ItemStack changeFocusItemStack(ItemStack wand, ItemStack focus) {
        if (!canApplyFocus()){
            throw new IllegalStateException("cannot change focus but called changeFocusItemStack!");
        }
        var tag = wand.getTag();
        if (tag == null){
            return null;
        }
        if (!WAND_FOCUS_ACCESSOR.compoundTagHasKey(tag)){
            WAND_FOCUS_ACCESSOR.writeToCompoundTag(tag, focus);
            return null;
        }
        var result = WAND_FOCUS_ACCESSOR.readFromCompoundTag(tag);
        WAND_FOCUS_ACCESSOR.writeToCompoundTag(tag, focus);
        return result;
    }

    @Override
    public void storeVisOwning(ItemStack itemStack, Map<Aspect, Integer> aspects) {
        CompoundTag tag = itemStack.getOrCreateTag();
        WAND_OWING_VIS_ACCESSOR.writeToCompoundTag(tag, aspects);
    }

    @Override
    public Map<Aspect, Integer> getVisCapacity(ItemStack usingWand) {
        var cap = getWandCapItem(usingWand);
        var rod = getWandRodItem(usingWand);
        Map<Aspect, Integer> result = new HashMap<>();

        if (cap instanceof AspectCapacityOwner capOwner){
            capOwner.getAspectCapacity().forEach(
                    (aspect,integer) -> result.merge(aspect,integer,Integer::sum));
        }

        if (rod != null){
            rod.getAspectCapacity().forEach(
                    (aspect,integer) -> result.merge(aspect,integer,Integer::sum));
        }
        return result;
    }

    @Override
    public Map<Aspect, Integer> getVisOwning(ItemStack usingWand) {
        CompoundTag tag = usingWand.getOrCreateTag();
        return WAND_OWING_VIS_ACCESSOR.readFromCompoundTag(tag);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        appendWandHoverText(this,stack, level, list, tooltipFlag);
    }

    @Override
    public List<Item> getWandComponents(ItemStack stack) {
        List<Item> items = new ArrayList<>(2);
        var cap = getWandCapAsItem(stack);
        if (cap != null) {
            items.add(cap);
        }

        return Collections.unmodifiableList(items);
    }
}
