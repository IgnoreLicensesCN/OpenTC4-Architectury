package thaumcraft.common.items.equipment.voidequip;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.items.ThaumcraftItems;

import java.util.List;

public class VoidHoeItem extends HoeItem implements IWarpingGear {
    
    public VoidHoeItem(Tier tier, int i, float f, Properties properties) {
        super(tier, i, f, properties);
    }
    public VoidHoeItem() {
        this(
                ThaumcraftItems.ToolAndArmorMaterial.TOOL_VOID,
                -3, 0.0F,
                new Properties().rarity(Rarity.UNCOMMON).stacksTo(1)
        );
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("enchantment.special.sapless").withStyle(ChatFormatting.GOLD));//added not vanilla feature
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,80));//idk why but azanor kept this(greater than sword)
        }
        return super.hurtEnemy(itemStack, target, attacker);
    }
    @Override
    public int getWarp(ItemStack itemstack, Player player) {
        return 1;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        if (entity != null && !level.isClientSide && entity.tickCount % 20 == 0) {
            itemStack.setDamageValue(itemStack.getDamageValue() - 1);
        }
    }
}
