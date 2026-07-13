package thaumcraft.common.items.equipment.specialtool;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.items.ThaumcraftToolAndArmorMaterial;

import java.util.List;

public class CrimsonSwordItem extends SwordItem implements IWarpingGear {
    public CrimsonSwordItem(Tier tier, int i, float f, Properties properties) {
        super(tier, i, f, properties);
    }
    public CrimsonSwordItem() {
        this(ThaumcraftToolAndArmorMaterial.CRIMSON_VOID,
                3,
                -2.4F,
                new Properties().stacksTo(1)
                );
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
        list.add(Component.translatable("enchantment.special.sapgreat").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,60));
            target.addEffect(new MobEffectInstance(MobEffects.HUNGER,120));
        }
        return super.hurtEnemy(itemStack, target, attacker);
    }
}
