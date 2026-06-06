package thaumcraft.common.runicshield.shieldtypes;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;

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
