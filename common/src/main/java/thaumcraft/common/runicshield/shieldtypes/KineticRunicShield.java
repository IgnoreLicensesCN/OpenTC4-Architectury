package thaumcraft.common.runicshield.shieldtypes;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.UnknownNullability;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.RunicShieldInfo;

public class KineticRunicShield extends CoolingDownAndTriggerOnShieldDownShieldType {
    public KineticRunicShield(RunicShieldTypeResourceLocation shieldTypeResourceLocation,int priority) {
        super(shieldTypeResourceLocation,priority);
    }

    public int getAbilityCooldownTicks(){
        return 20*20;
    }

    @Override
    public void triggerEventToCooldown(@UnknownNullability LivingEntity victim, DamageSource source, RunicShieldInfo shieldInfo) {
        victim.level()
                .explode(victim,
                        victim.getX(),
                        victim.getY() + (victim.getBbHeight() / 2),
                        victim.getZ(),
                        1.5F + shieldInfo.getShieldChargedFor(this)/2.F,
                        Level.ExplosionInteraction.MOB
                );
    }
    @Override
    public Component getShieldName(){
        if (shieldName == null){
            shieldName = Component.translatable("runic_shield.thaumcraft.runic_kinetic").withStyle(ChatFormatting.AQUA);
        }
        return shieldName;
    }
}
