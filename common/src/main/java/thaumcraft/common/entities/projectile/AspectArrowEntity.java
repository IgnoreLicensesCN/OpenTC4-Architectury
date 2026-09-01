package thaumcraft.common.entities.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.damagesource.ThaumcraftDamageSources;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.abstracts.IThaumcraftAspectArrowProperties;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.linearity.opentc4.Consts.AspectArrowEntityEntityTagAccessors.OWNING_ASPECT;
import static thaumcraft.common.items.ThaumcraftItemInstances.*;

public class AspectArrowEntity
        extends AbstractArrow {
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(
            AspectArrowEntity.class, EntityDataSerializers.INT);

    public static class AspectArrowManager {

        public static final Map<Aspect, Supplier<ItemStack>> ARROW_ITEM_FOR_ASPECT = new IdentityHashMap<>();

        static {
            ARROW_ITEM_FOR_ASPECT.put(Aspects.EMPTY,() -> ItemStack.EMPTY);
            ARROW_ITEM_FOR_ASPECT.put(Aspects.AIR,() -> AIR_ARROW().getDefaultInstance());
            ARROW_ITEM_FOR_ASPECT.put(Aspects.EARTH,() -> EARTH_ARROW().getDefaultInstance());
            ARROW_ITEM_FOR_ASPECT.put(Aspects.WATER,() -> WATER_ARROW().getDefaultInstance());
            ARROW_ITEM_FOR_ASPECT.put(Aspects.FIRE,() -> FIRE_ARROW().getDefaultInstance());
            ARROW_ITEM_FOR_ASPECT.put(Aspects.ORDER,() -> ORDER_ARROW().getDefaultInstance());
            ARROW_ITEM_FOR_ASPECT.put(Aspects.ENTROPY,() -> ENTROPY_ARROW().getDefaultInstance());
        }
        public static final Map<Aspect, IThaumcraftAspectArrowProperties> ASPECT_ARROW_PROPERTIES = new IdentityHashMap<>();

        static {
            ASPECT_ARROW_PROPERTIES.put(
                    Aspects.EMPTY, IThaumcraftAspectArrowProperties.EMPTY
            );
            ASPECT_ARROW_PROPERTIES.put(Aspects.AIR,AirArrowProperties.INSTANCE);
            ASPECT_ARROW_PROPERTIES.put(Aspects.FIRE,FireArrowProperties.INSTANCE);
            ASPECT_ARROW_PROPERTIES.put(Aspects.WATER,WaterArrowProperties.INSTANCE);
            ASPECT_ARROW_PROPERTIES.put(Aspects.EARTH,EarthArrowProperties.INSTANCE);
            ASPECT_ARROW_PROPERTIES.put(Aspects.ORDER,OrderArrowProperties.INSTANCE);
            ASPECT_ARROW_PROPERTIES.put(Aspects.ENTROPY,EntropyArrowProperties.INSTANCE);
        }

        //mixin point
        public static boolean aspectArrowOnHitEntity(AspectArrowEntity aspectArrow, Entity victim, DamageSource source, float causedDamage, boolean hitSuccess){
            if (hitSuccess) {
                aspectArrow.getAspectArrowProperties().onArrowHitEntity(aspectArrow,victim,source,causedDamage);
            }
            return hitSuccess;
        }
        //mixin point
        public static Entity aspectArrowOnModifyReceiver(AspectArrowEntity aspectArrow, Entity victim, DamageSource source, float causedDamage){
            return victim;
        }
        //mixin point
        public static float aspectArrowModifyDamage(AspectArrowEntity aspectArrow,DamageSource source, float damage){
            return (float) (damage * aspectArrow.getAspectArrowProperties().getDamageMultiplier());
        }
        //mixin point
        public static ResourceKey<DamageType> aspectArrowModifyDamageSource(AspectArrowEntity aspectArrow,
                                                         Entity victim,
                                                         CallbackInfoReturnable<DamageSource> cir){
            var aspectArrowProperties = aspectArrow.getAspectArrowProperties();
            return aspectArrowProperties.getModifiedDamageType(
                    DamageTypes.ARROW
            );
        }
    }

    public static class AirArrowProperties implements IThaumcraftAspectArrowProperties {
        public static final AirArrowProperties INSTANCE = new AirArrowProperties();
        private AirArrowProperties(){}
        @Override
        public ResourceKey<DamageType> getModifiedDamageType(ResourceKey<DamageType> type) {
            return ThaumcraftDamageSources.AIR_ARROW_DAMAGE;
        }
    }
    public static class FireArrowProperties implements IThaumcraftAspectArrowProperties {
        public static final FireArrowProperties INSTANCE = new FireArrowProperties();
        private FireArrowProperties(){}
        @Override
        public ResourceKey<DamageType> getModifiedDamageType(ResourceKey<DamageType> source) {
            return ThaumcraftDamageSources.FIRE_ARROW_DAMAGE;
        }

        @Override
        public void onArrowHitEntity(AspectArrowEntity arrow, Entity victim, DamageSource damageSource, float causedDamage) {
            int ticksOnFire = victim.getRemainingFireTicks();
            victim.setSecondsOnFire(5);
            victim.setRemainingFireTicks(ticksOnFire + victim.getRemainingFireTicks());
        }
    }
    public static class WaterArrowProperties implements IThaumcraftAspectArrowProperties {
        public static final WaterArrowProperties INSTANCE = new WaterArrowProperties();
        private WaterArrowProperties(){}
        @Override
        public ResourceKey<DamageType> getModifiedDamageType(ResourceKey<DamageType> source) {
            return ThaumcraftDamageSources.WATER_ARROW_DAMAGE;
        }

        @Override
        public void onArrowHitEntity(AspectArrowEntity arrow, Entity victim, DamageSource damageSource, float causedDamage) {
            if (victim instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,200,4));
            }
        }
    }
    public static class EarthArrowProperties implements IThaumcraftAspectArrowProperties {
        public static final EarthArrowProperties INSTANCE = new EarthArrowProperties();
        private EarthArrowProperties(){}
        @Override
        public ResourceKey<DamageType> getModifiedDamageType(ResourceKey<DamageType> source) {
            return ThaumcraftDamageSources.EARTH_ARROW_DAMAGE;
        }

        @Override
        public double getDamageMultiplier() {
            return 1.5;
        }
    }
    public static class OrderArrowProperties implements IThaumcraftAspectArrowProperties {
        public static final OrderArrowProperties INSTANCE = new OrderArrowProperties();
        private OrderArrowProperties(){}
        @Override
        public ResourceKey<DamageType> getModifiedDamageType(ResourceKey<DamageType> source) {
            return ThaumcraftDamageSources.ORDER_ARROW_DAMAGE;
        }

        @Override
        public void onArrowHitEntity(AspectArrowEntity arrow, Entity victim, DamageSource damageSource, float causedDamage) {
            if (victim instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,200,4));
            }
        }

        @Override
        public double getDamageMultiplier() {
            return 0.8;
        }
    }
    public static class EntropyArrowProperties implements IThaumcraftAspectArrowProperties {
        public static final EntropyArrowProperties INSTANCE = new EntropyArrowProperties();
        private EntropyArrowProperties(){}
        @Override
        public ResourceKey<DamageType> getModifiedDamageType(ResourceKey<DamageType> source) {
            return ThaumcraftDamageSources.ENTROPY_ARROW_DAMAGE;
        }

        @Override
        public void onArrowHitEntity(AspectArrowEntity arrow, Entity victim, DamageSource damageSource, float causedDamage) {
            if (victim instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.WITHER,100,0));
            }
        }

        @Override
        public double getDamageMultiplier() {
            return 0.8;
        }
    }

    //keep as mixin point
    public static ItemStack getArrowStackForAspect(Aspect aspect) {
        return AspectArrowManager.ARROW_ITEM_FOR_ASPECT.getOrDefault(aspect, () -> ItemStack.EMPTY).get();
    }
    protected Aspect owningAspect = Aspect.EMPTY;
    public AspectArrowEntity(Level level) {
        this(level,Aspect.EMPTY);
    }
    public AspectArrowEntity(Level level,Aspect owningAspect) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ASPECT_ARROW(), level,owningAspect);
    }
    public AspectArrowEntity(EntityType<? extends AspectArrowEntity> entityType, Level level,Aspect owningAspect) {
        super(entityType, level);
        this.owningAspect = owningAspect;
    }


    public AspectArrowEntity(EntityType<? extends AspectArrowEntity> entityType, Level level) {
        this(entityType, level,Aspect.EMPTY);
    }

//    protected AspectArrowEntity(EntityType<? extends AspectArrowEntity> entityType, double d, double e, double f, Level level) {
//        super(entityType, d, e, f, level);
//    }
//
//    protected AspectArrowEntity(EntityType<? extends AspectArrowEntity> entityType, LivingEntity livingEntity, Level level) {
//        super(entityType, livingEntity, level);
//    }
    public Aspect getOwningAspect() {
        return owningAspect;
    }
    public void setOwningAspect(Aspect owningAspect) {
        this.owningAspect = owningAspect;
        entityData.set(DATA_COLOR, owningAspect.getColor());
    }
    @Override
    protected @NotNull ItemStack getPickupItem() {
        return getArrowStackForAspect(owningAspect).copy();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_COLOR, Aspects.EMPTY.getColor());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setOwningAspect(OWNING_ASPECT.readFromCompoundTag(compoundTag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OWNING_ASPECT.writeToCompoundTag(compoundTag,getOwningAspect());
    }
    protected int life = 0;
    protected void tickDespawn() {
        ++this.life;
        if (this.life >= 100) {
            this.discard();
        }

    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
    }

    public IThaumcraftAspectArrowProperties getAspectArrowProperties() {
        return AspectArrowManager.ASPECT_ARROW_PROPERTIES.getOrDefault(getOwningAspect(),IThaumcraftAspectArrowProperties.EMPTY);
    }
}
