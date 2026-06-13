package thaumcraft.common.items.equipment.masks;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.items.abstracts.armorcomponents.IArmorAttackOthersListenerComponentItem;

public class SippingFiendMaskItem extends Item implements IArmorAttackOthersListenerComponentItem {
    public SippingFiendMaskItem(Properties properties) {
        super(properties);
    }
    public SippingFiendMaskItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void onAttackOtherEntity(
            @Unmodifiable ItemStack selfStack,
            @Unmodifiable ItemStack parentStack,
            Entity user,
            LivingEntity beingAttacked,
            DamageSource damageSource,
            float damageFinalCaused
    ) {
        if (user instanceof LivingEntity living) {
            if (living.level().random.nextFloat() < damageFinalCaused/12.F){
                living.heal(1);
            }
        }
    }
}
