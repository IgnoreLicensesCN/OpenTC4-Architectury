package thaumcraft.api.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.world.HolderCache;

import java.util.Objects;

public class ThaumcraftDamageSources
{
    private static final HolderCache<DamageType> holderCache = HolderCache.of(Registries.DAMAGE_TYPE);

    public static final ResourceKey<DamageType> TAINT =
            ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":taint")));

    public static final ResourceKey<DamageType> TENTACLE =
            ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":tentacle")));

    public static final ResourceKey<DamageType> SWARM =
            ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":swarm")));

    public static final ResourceKey<DamageType> DISSOLVE =
            ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":dissolve")));
    public static Holder<DamageType> getHolder(Level level, ResourceKey<DamageType> key) {
        return holderCache.getHolder(level, key);
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(getHolder(level, key));
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> key,@NotNull Entity attacker) {
        return new DamageSource(getHolder(level, key), attacker);
    }
    
    public static final class Tags {
        public static final TagKey<DamageType> BYPASS_RUNIC_SHIELD = 
                TagKey.create(Registries.DAMAGE_TYPE,Objects.requireNonNull(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":bypass_runic_shield")));
    }

//    public static DamageSource tentacle(Level level, Entity attacker) {
//        return new DamageSource(level.registryAccess()
//                .registryOrThrow(Registries.DAMAGE_TYPE)
//                .getHolderOrThrow(TENTACLE),
//                attacker);
//    }
//
//    public static DamageSource swarm(Level level, Entity attacker) {
//        return new DamageSource(level.registryAccess()
//                .registryOrThrow(Registries.DAMAGE_TYPE)
//                .getHolderOrThrow(SWARM),
//                attacker);
//    }
//
//    public static DamageSource dissolve(Level level) {
//        return new DamageSource(level.registryAccess()
//                .registryOrThrow(Registries.DAMAGE_TYPE)
//                .getHolderOrThrow(DISSOLVE));
//    }



//    protected ThaumcraftDamageSources(String par1Str) {
//		super(par1Str);
//	}
//    
//	/** This kind of damage can be blocked or not. */
//    private boolean isUnblockable = false;
//    private boolean isDamageAllowedInCreativeMode = false;
//    private float hungerDamage = 0.3F;
//
//    /** This kind of damage is based on fire or not. */
//    private boolean fireDamage;
//
//    /** This kind of damage is based on a projectile or not. */
//    private boolean projectile;
//
//    /**
//     * Whether this damage source will have its damage amount scaled based on the current difficulty.
//     */
//    private boolean difficultyScaled;
//    private boolean magicDamage = false;
//    private boolean explosion = false;
//    
//    public static DamageSource causeSwarmDamage(LivingEntity par0EntityLiving)
//    {
//        return new EntityDamageSource("swarm", par0EntityLiving);
//    }
//
//    public static DamageSource causeTentacleDamage(LivingEntity par0EntityLiving)
//    {
//        return new EntityDamageSource("tentacle", par0EntityLiving);
//    }
}
