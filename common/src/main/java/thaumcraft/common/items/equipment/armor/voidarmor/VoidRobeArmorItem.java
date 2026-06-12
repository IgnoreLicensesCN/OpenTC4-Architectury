package thaumcraft.common.items.equipment.armor.voidarmor;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;

public class VoidRobeArmorItem extends DyeableArmorItem implements IWarpingGear, IAugmentationRunicShieldProviderItem, IVisDiscountGear {
    public VoidRobeArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public VoidRobeArmorItem(Type type) {
        super(ThaumcraftItems.ToolAndArmorMaterial.ARMOR_VOID, type, new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public int getWarp(ItemStack itemstack, @Nullable Entity entityEquipped) {
        return 2;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        if (entity != null && !level.isClientSide && entity.tickCount % 20 == 0) {
            itemStack.setDamageValue(itemStack.getDamageValue() - 1);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addShieldToolTip(itemStack, level, list, tooltipFlag);
        addVisDiscountToolTip(itemStack,level,list,tooltipFlag,null,null);
        addWarpTooltip(itemStack, level, list, tooltipFlag);
    }

    @Override
    public int getVisCostPercentDecrease(ItemStack stack, @Nullable LivingEntity living, @Nullable Aspect aspect) {
        return 5;
    }

//    TODO:[maybe wont finished]re-add this void robe armor damage calc part(wtf is this)
    //   public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
    //      int priority = 0;
    //      double ratio = (double)this.damageReduceAmount / (double)25.0F;
    //      if (source.isMagicDamage()) {
    //         priority = 1;
    //         ratio = (double)this.damageReduceAmount / (double)35.0F;
    //      } else if (source.isUnblockable()) {
    //         priority = 0;
    //         ratio = 0.0F;
    //      }
    //
    //      return new ISpecialArmor.ArmorProperties(priority, ratio, armor.getMaxDamage() + 1 - armor.getItemDamage());
    //   }
}