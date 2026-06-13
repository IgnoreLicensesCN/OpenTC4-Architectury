package thaumcraft.common.items.equipment.masks;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.items.abstracts.armorcomponents.IArmorBeingAttackedListenerComponentItem;

public class AngryGhostMask extends Item implements IArmorBeingAttackedListenerComponentItem {
    public AngryGhostMask(Properties properties) {
        super(properties);
    }
    public AngryGhostMask() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void onBeingAttackedByOtherEntity(@Unmodifiable ItemStack selfStack, @Unmodifiable ItemStack parentStack, LivingEntity user, DamageSource damageSource, float damageFinalCaused) {
        var entity = damageSource.getEntity();
        if (entity instanceof LivingEntity living) {
            if (living.level().random.nextFloat() < damageFinalCaused/10.F){
                living.addEffect(new MobEffectInstance(MobEffects.WITHER,80));
            }
        }
    }
}
