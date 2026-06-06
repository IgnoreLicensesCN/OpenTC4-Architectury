package thaumcraft.common.runicshield.shieldtypes;

import com.linearity.opentc4.utils.compoundtag.accessors.ITagAccessorOwner;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableCentiVisList;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.network.fx.PacketFXShieldS2C;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static thaumcraft.api.damagesource.ThaumcraftDamageSources.Tags.BYPASS_RUNIC_SHIELD;
import static thaumcraft.common.runicshield.ThaumcraftRunicShieldTypes.CHARGED;

public abstract class AbstractRunicShieldType<AdditionalInfoClass>
        implements Comparable<AbstractRunicShieldType<AdditionalInfoClass>>,
        ITagAccessorOwner<AdditionalInfoClass>
{
    private static final Map<RunicShieldTypeResourceLocation, AbstractRunicShieldType<?>> RUNIC_SHIELD_TYPES = new ConcurrentHashMap<>();
    public static final @UnmodifiableView Map<RunicShieldTypeResourceLocation, AbstractRunicShieldType<?>> RUNIC_SHIELD_TYPES_VIEW
            = Collections.unmodifiableMap(RUNIC_SHIELD_TYPES);
    public final RunicShieldTypeResourceLocation id;
    public final int priority;

    public AbstractRunicShieldType(RunicShieldTypeResourceLocation shieldTypeResourceLocation, int priority) {
        if (!RUNIC_SHIELD_TYPES.containsKey(shieldTypeResourceLocation)) {
            RUNIC_SHIELD_TYPES.put(shieldTypeResourceLocation, this);
        } else {
            throw new IllegalArgumentException("RunicShieldType ID already exists:" + shieldTypeResourceLocation);
        }
        this.id = shieldTypeResourceLocation;
        this.priority = priority;
    }

    private static final @Unmodifiable CentiVisList<Aspect> rechargeCost = UnmodifiableCentiVisList.of(
            Aspects.AIR, 50, Aspects.EARTH, 50
    );

    public @Unmodifiable CentiVisList<Aspect> getRechargeCost() {
        return rechargeCost;
    }

    public int getTickCooldownAfterRegen(Entity entity, EntityRunicShieldInfo shieldInfo) {
        //TODO:[maybe wont finished]also "Vote in democracy" to decide if change to chargedShield's own feature
        int chargedTypeCapacity = Math.clamp(shieldInfo.shieldCapacity.getInt(CHARGED), 0, 4);
        return 40 - 10 * chargedTypeCapacity;
    }

    //called every tick
    public void rechargeTickForEntity(Entity entity, EntityRunicShieldInfo shieldInfo) {
        if (shieldInfo.rechargeDelay > 0) {
            return;
        }
        var shieldCapacityMap = shieldInfo.shieldCapacity;
        var shieldMap = shieldInfo.shieldCharged;
        int capacity = shieldCapacityMap.getInt(this);
        if (capacity > shieldMap.getInt(this)) {
            if (WandManager.consumeCentiVisFromInventory(entity, getRechargeCost())) {
                shieldMap.mergeInt(this, 1, Integer::sum);
                shieldInfo.rechargeDelay += getTickCooldownAfterRegen(entity, shieldInfo);
                shieldInfo.shouldSyncCharge = true;
            }
        } else if (capacity < shieldMap.getInt(this)) {
            shieldMap.put(this, capacity);
            shieldInfo.shouldSyncCharge = true;
        }
    }

    /**
     *
     * @param victim     being called #actuallyHurt (in case someone wants something more than LivingEntity this class is not "LivingEntity")
     * @param source     of damage
     * @param damage     received
     * @param shieldInfo of entity
     * @return damage wants to change to.
     */
    public float beforeActuallyHurt(
            Entity victim /*only for player in vanilla TC4*/,
            DamageSource source,
            float damage, /*may decreased by shields before*/
            EntityRunicShieldInfo shieldInfo) {
        if (damage < 0) {
            return damage;
        }
        if (victim.isInvulnerableTo(source)) {
            return damage;
        }
        if (shouldIgnoreDamageForSource(source)){
            return damage;
        }
        return onDamagingShield(victim, source, damage, shieldInfo);
    }
    public float onDamagingShield(
            Entity victim /*only for player in vanilla TC4*/,
            DamageSource source,
            float damage, /*may decreased by shields before*/
            EntityRunicShieldInfo shieldInfo
    ){
        int owningShield = shieldInfo.shieldCharged.getInt(this);
        if (owningShield <= 0) {
            return damage;
        }
        int damageCeil = (int) Math.ceil(damage);
        int damageToReduce = Math.min(damageCeil, owningShield);
        int remaining = owningShield - damageToReduce;
        shieldInfo.shieldCharged.put(this, remaining);
        if (damageToReduce > 0){
            onDamagingShield(victim, source, damageToReduce, shieldInfo);
        }
        if (damageToReduce > 0 && remaining <= 0){
            onShieldRunningOut(victim, source, shieldInfo);
        }
        return damage - damageToReduce;
    }
    public void onDamagedShield(
            Entity victim /*only for player in vanilla TC4*/,
            DamageSource source,
            float damage, /*may decreased by shields before*/
            float damaged, /* shield value damaged */
            EntityRunicShieldInfo shieldInfo
    ){
        if (victim.level() instanceof ServerLevel serverLevel) {
            var entityCausedDamage = source.getEntity();
            int damageArgForEffect = entityCausedDamage == null ? -1 : entityCausedDamage.getId();
            if (source.is(DamageTypes.FALL)) {
                damageArgForEffect = -2;
            }else if (source.is(DamageTypes.FALLING_BLOCK)){
                damageArgForEffect = -3;
            }
            new PacketFXShieldS2C(victim.getId(), damageArgForEffect).sendToAllAround(
                    serverLevel,
                    victim.position(),
                    64*64
            );
        }
    }

    public boolean shouldIgnoreDamageForSource(DamageSource source){
        if (source.is(BYPASS_RUNIC_SHIELD)){
            return true;
        }
        return source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || source.is(DamageTypeTags.BYPASSES_RESISTANCE)
                || source.is(DamageTypeTags.BYPASSES_EFFECTS)
                || source.is(DamageTypeTags.IS_DROWNING);
    }

    //oh this is a kind of feature change--called when this type of shield is running out not all running out
    public void onShieldRunningOut(Entity victim,DamageSource source,EntityRunicShieldInfo shieldInfo) {

    }

    @Override
    public int compareTo(@NotNull AbstractRunicShieldType o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract @Nullable CompoundTagAccessor<AdditionalInfoClass> getOwningTagAccessor();
}
