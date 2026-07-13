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

//i have a strong feeling that someone would unload this mask and replace other masks (in recipe?)
//at least we have it's item form now.
public class AngryGhostMaskItem extends Item implements IArmorBeingAttackedListenerComponentItem {
    public AngryGhostMaskItem(Properties properties) {
        super(properties);
    }
    public AngryGhostMaskItem() {
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
