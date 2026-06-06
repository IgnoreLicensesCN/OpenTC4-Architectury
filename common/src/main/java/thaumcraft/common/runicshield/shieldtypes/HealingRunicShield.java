package thaumcraft.common.runicshield.shieldtypes;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;

//  "tc.research_page.RUNICHEALING.1": "这是种特别版的护盾指环.<BR>较改造前的指环充能量略少些,但当你你的符文护盾被打破时它能够释放出一波维持数秒的回复能量.<BR>在20s内只能激活一次这种效果.",
public class HealingRunicShield extends CoolingDownAndTriggerOnShieldDownShieldType {
    public HealingRunicShield(RunicShieldTypeResourceLocation shieldTypeResourceLocation,int priority) {
        super(shieldTypeResourceLocation,priority);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 20*20;
    }

    @Override
    public void triggerEventToCooldown(Entity victim, DamageSource source, EntityRunicShieldInfo shieldInfo) {
        if (victim instanceof LivingEntity livingVictim){
            var effectInstance = new MobEffectInstance(MobEffects.REGENERATION,240,shieldInfo.shieldCharged.getOrDefault(this,0));
            livingVictim.addEffect(effectInstance);
            livingVictim.level().playSound(
                    livingVictim,
                    livingVictim.blockPosition(),
                    ThaumcraftSounds.RUNIC_SHIELD_EFFECT,
                    SoundSource.PLAYERS, 1.0F, 1.0F
            );
        }
    }
}
