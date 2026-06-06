package thaumcraft.common.runicshield.shieldtypes;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;

//  "tc.research_page.RUNICEMERGENCY.1": "这是种特别版的护盾护身符.<BR>较改造前的护身符充能量略少些,但当护盾被打破时这项升级能够立即再为护盾增加8点能量.<BR>在1分钟内只能激活一次这种效果.",
public class EmergencyRunicShield extends CoolingDownAndTriggerOnShieldDownShieldType {
    public EmergencyRunicShield(RunicShieldTypeResourceLocation shieldTypeResourceLocation,int priority) {
        super(shieldTypeResourceLocation,priority);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 60*20;
    }

    @Override
    public void triggerEventToCooldown(Entity victim, DamageSource source, EntityRunicShieldInfo shieldInfo) {
        int t = 8 * shieldInfo.shieldCapacity.getInt(this);
        shieldInfo.randomCharge(t,victim.level().random);
        shieldInfo.shouldSyncCharge = true;
        victim.level().playSound(
                victim,victim.blockPosition(), ThaumcraftSounds.RUNIC_SHIELD_CHARGE, SoundSource.PLAYERS,
                1,
                1
        );
    }
}
