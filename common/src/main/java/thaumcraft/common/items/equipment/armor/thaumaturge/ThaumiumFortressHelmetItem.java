package thaumcraft.common.items.equipment.armor.thaumaturge;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IVisDiscountGearItem;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.ThaumcraftToolAndArmorMaterial;
import thaumcraft.common.items.abstracts.armorcomponents.*;
import thaumcraft.common.runicshield.IRunicShieldProviderItem;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.linearity.opentc4.Consts.ThaumiumFortressHelmetItemTagAccessors.GOGGLES;
import static com.linearity.opentc4.Consts.ThaumiumFortressHelmetItemTagAccessors.MASK;

public class ThaumiumFortressHelmetItem extends ThaumiumFortressArmorItem
        implements
        IArmorComponentsOwnerItem,
        IVisDiscountGearItem ,
        IGoggles,
        IAttackOthersListenerArmor,
        IBeingAttackedListenerArmor
{

    public ThaumiumFortressHelmetItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public ThaumiumFortressHelmetItem() {
        this(ThaumcraftToolAndArmorMaterial.THAUMIUM_FORTRESS, Type.HELMET, new Properties().stacksTo(1).rarity(Rarity.RARE));
    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        for (var stack:getArmorComponents(itemStack)) {
            list.add(stack.getDisplayName().copy().withStyle(style ->  style.withColor(stack.getRarity().color)));
            stack.getItem().appendHoverText(stack, level, list, tooltipFlag);
        }
    }

    @Override
    public int getVisCostPercentDecrease(ItemStack helmetStack, @Nullable LivingEntity living, @Nullable Aspect aspect) {
        int decreases = 0;
        for (var stack:getArmorComponents(helmetStack)){
            if (stack.getItem() instanceof IVisDiscountGearItem visDiscountGear) {
                decreases += visDiscountGear.getVisCostPercentDecrease(stack, living, aspect);//changed:added vis discount
            }
        }
        return decreases;
    }

    @Override
    public @Unmodifiable Object2IntMap<AbstractRunicShieldType<?>> getRunicCharge(ItemStack helmetStack) {
        Object2IntMap<AbstractRunicShieldType<?>> finalMap = super.getRunicCharge(helmetStack);
        for (var stack:getArmorComponents(helmetStack)){
            if (stack.getItem() instanceof IRunicShieldProviderItem shieldProviderItem) {
                finalMap = IRunicShieldProviderItem.calculateShieldSumAndCache(finalMap,shieldProviderItem.getRunicCharge(stack));
            }
        }
        return finalMap;
    }

    @Override
    public List<ItemStack> getArmorComponents(ItemStack helmetStack) {
        var tag = helmetStack.getTag();
        if (tag == null) {
            return Collections.emptyList();
        }
        var result = new ArrayList<ItemStack>(2);
        var gogglesStack = GOGGLES.readFromCompoundTag(tag);
        if (!gogglesStack.isEmpty()) {
            result.add(gogglesStack);
        }
        var maskStack = MASK.readFromCompoundTag(tag);
        if (!maskStack.isEmpty()) {
            result.add(maskStack);
        }
        return result;
    }

    @Override
    public boolean showAsWearingGogglesOfRevealing(ItemStack itemstack, LivingEntity player) {
        for (var stack:getArmorComponents(itemstack)) {
            if (stack.getItem() instanceof IGoggles gogglesItem) {
                if (gogglesItem.showAsWearingGogglesOfRevealing(stack, player)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        if (i != (36 + type.getSlot().getIndex()) || !(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        for (var stack:getArmorComponents(itemStack)) {
            if (stack.getItem() instanceof IArmorTickableComponentItem tickableComponentItem) {
                tickableComponentItem.tickAsComponent(stack,itemStack,livingEntity);
            }
        }
    }

    @Override
    public void onAttackOtherEntity(ItemStack selfStack, Entity user, LivingEntity beingAttacked, DamageSource damageSource, float damageFinalCaused) {
        for (var stack:getArmorComponents(selfStack)) {
            if (stack.getItem() instanceof IArmorAttackOthersListenerComponentItem componentItem){
                componentItem.onAttackOtherEntity(stack,selfStack,user,beingAttacked,damageSource,damageFinalCaused);
            }
        }
    }

    @Override
    public void onBeingAttackedByOtherEntity(@Unmodifiable ItemStack selfStack, LivingEntity user, DamageSource damageSource, float damageFinalCaused) {
        for (var stack:getArmorComponents(selfStack)) {
            if (stack.getItem() instanceof IArmorBeingAttackedListenerComponentItem componentItem){
                componentItem.onBeingAttackedByOtherEntity(stack,selfStack,user,damageSource,damageFinalCaused);
            }
        }
    }
}
