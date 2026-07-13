package thaumcraft.common.items.abstracts.armorcomponents;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public interface IArmorAttackOthersListenerComponentItem {
    void onAttackOtherEntity(
            @Unmodifiable/*i dont promise this part*/ ItemStack selfStack,
            @Unmodifiable/*i dont promise this part*/ ItemStack parentStack,
            Entity user,
            LivingEntity beingAttacked,
            DamageSource damageSource,
            float damageFinalCaused
    );
}
