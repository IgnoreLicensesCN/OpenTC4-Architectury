package thaumcraft.api.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DamageSourceThaumcraft
{

    public static final ResourceKey<DamageType> TAINT =
            ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryParse("thaumcraft:taint")));

    public static final ResourceKey<DamageType> TENTACLE =
            ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryParse("thaumcraft:tentacle")));

    public static final ResourceKey<DamageType> SWARM =
            ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryParse("thaumcraft:swarm")));

    public static final ResourceKey<DamageType> DISSOLVE =
            ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryParse("thaumcraft:dissolve")));


    private static final Map<ResourceKey<DamageType>, WeakHashMap<Level, Holder<DamageType>>> CACHE = new ConcurrentHashMap<>();

    private static Holder<DamageType> getHolder(Level level, ResourceKey<DamageType> key) {
        // 先获取 ResourceKey 对应的 WeakHashMap，如果没有就创建
        WeakHashMap<Level, Holder<DamageType>> map = CACHE.computeIfAbsent(key, k -> new WeakHashMap<>());

        // 再获取 Level 对应的 Holder，如果没有就从 Registry 里取
        return map.computeIfAbsent(level, l ->
                l.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key)
        );
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(getHolder(level, key));
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> key,@NotNull Entity attacker) {
        return new DamageSource(getHolder(level, key), attacker);
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



//    protected DamageSourceThaumcraft(String par1Str) {
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
