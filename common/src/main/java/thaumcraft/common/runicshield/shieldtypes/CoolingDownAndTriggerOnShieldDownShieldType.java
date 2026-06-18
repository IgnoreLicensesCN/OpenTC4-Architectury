package thaumcraft.common.runicshield.shieldtypes;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.RunicShieldInfo;

@UtilityLikeAbstraction
public abstract class CoolingDownAndTriggerOnShieldDownShieldType extends AbstractRunicShieldType<Integer>{
    public CoolingDownAndTriggerOnShieldDownShieldType(RunicShieldTypeResourceLocation shieldTypeResourceLocation, int priority) {
        super(shieldTypeResourceLocation, priority);
    }

    public abstract int getAbilityCooldownTicks();

    @Override
    public void rechargeTickForLiving(LivingEntity living, RunicShieldInfo shieldInfo) {
        var cooldownTick = shieldInfo.getAdditionalInfo(this);
        if (cooldownTick == null){
            cooldownTick = 0;
            shieldInfo.putAdditionalInfo(this, cooldownTick);
        }
        if (cooldownTick>0){
            shieldInfo.runicShieldAdditionalInfo.put(this,cooldownTick-1);
        }
        super.rechargeTickForLiving(living, shieldInfo);
    }

    @Override
    public void onShieldRunningOut(LivingEntity victim, DamageSource source, RunicShieldInfo shieldInfo) {
        super.onShieldRunningOut(victim, source, shieldInfo);
        var cooldownTick = shieldInfo.getAdditionalInfo(this);
        if (cooldownTick == null || cooldownTick <= 0){
            cooldownTick = getAbilityCooldownTicks();
            shieldInfo.runicShieldAdditionalInfo.put(this,cooldownTick);
            triggerEventToCooldown(victim, source, shieldInfo);
        }
    }

    public void triggerEventToCooldown(LivingEntity victim, DamageSource source, RunicShieldInfo shieldInfo){
        shieldInfo.rechargeDelay = 80;
    }

    public static final IntTagAccessor COOLDOWN_ACCESSOR = new IntTagAccessor("ability_cooldown");
    @Override
    public @Nullable CompoundTagAccessor<Integer> getOwningTagAccessor() {
        return COOLDOWN_ACCESSOR;
    }
}
