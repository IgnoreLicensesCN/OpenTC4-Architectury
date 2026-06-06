package thaumcraft.common.runicshield.shieldtypes;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;

@UtilityLikeAbstraction
public abstract class CoolingDownAndTriggerOnShieldDownShieldType extends AbstractRunicShieldType<Integer>{
    public CoolingDownAndTriggerOnShieldDownShieldType(RunicShieldTypeResourceLocation shieldTypeResourceLocation, int priority) {
        super(shieldTypeResourceLocation, priority);
    }

    public abstract int getAbilityCooldownTicks();

    @Override
    public void rechargeTickForEntity(Entity entity, EntityRunicShieldInfo shieldInfo) {
        var cooldownTick = shieldInfo.getAdditionalInfo(this);
        if (cooldownTick == null){
            cooldownTick = 0;
            shieldInfo.putAdditionalInfo(this, cooldownTick);
        }
        if (cooldownTick>0){
            shieldInfo.runicShieldAdditionalInfo.put(this,cooldownTick-1);
        }
        super.rechargeTickForEntity(entity, shieldInfo);
    }

    @Override
    public void onShieldRunningOut(Entity victim, DamageSource source, EntityRunicShieldInfo shieldInfo) {
        super.onShieldRunningOut(victim, source, shieldInfo);
        var cooldownTick = shieldInfo.getAdditionalInfo(this);
        if (cooldownTick == null || cooldownTick <= 0){
            cooldownTick = getAbilityCooldownTicks();
            shieldInfo.runicShieldAdditionalInfo.put(this,cooldownTick);
            triggerEventToCooldown(victim, source, shieldInfo);
        }
    }

    public void triggerEventToCooldown(Entity victim, DamageSource source, EntityRunicShieldInfo shieldInfo){
        shieldInfo.rechargeDelay = 80;
    };

    public static final IntTagAccessor COOLDOWN_ACCESSOR = new IntTagAccessor("ability_cooldown");
    @Override
    public @Nullable CompoundTagAccessor<Integer> getOwningTagAccessor() {
        return COOLDOWN_ACCESSOR;
    }
}
