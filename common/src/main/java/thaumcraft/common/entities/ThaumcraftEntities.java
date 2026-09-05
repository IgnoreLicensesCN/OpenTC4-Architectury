package thaumcraft.common.entities;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.ai.goals.DelayControllableMeleeAttackGoal;
import thaumcraft.common.entities.monster.WispEntity;
import thaumcraft.common.entities.monster.eldritch.EldritchCrabEntity;
import thaumcraft.common.entities.monster.eldritch.EldritchGuardianEntity;
import thaumcraft.common.entities.monster.eldritch.InhabitedZombieEntity;
import thaumcraft.common.entities.monster.pech.*;
import thaumcraft.common.entities.monster.zombies.BrainyZombieEntity;
import thaumcraft.common.entities.monster.zombies.GiantBrainyZombieEntity;
import thaumcraft.common.entities.monster.warp.MindSpiderEntity;
import thaumcraft.common.entities.monster.cultists.CultistClericEntity;
import thaumcraft.common.entities.monster.cultists.CultistEntity;
import thaumcraft.common.entities.monster.cultists.CultistKnightEntity;
import thaumcraft.common.entities.monster.tainted.*;
import thaumcraft.common.entities.monster.tainted.converted.*;
import thaumcraft.common.entities.projectile.AspectArrowEntity;
import thaumcraft.common.entities.projectile.DartEntity;
import thaumcraft.common.entities.projectile.EldritchOrbEntity;
import thaumcraft.common.entities.projectile.golemorb.GolemOrbEntity;
import thaumcraft.common.entities.projectile.golemorb.RedGolemOrbEntity;
import thaumcraft.common.entities.projectile.frostfocus.FrostShardEntity;
import thaumcraft.common.entities.projectile.hellbatfocus.FireBatEntity;
import thaumcraft.common.entities.projectile.pechfocus.PechBlastEntity;
import thaumcraft.common.entities.projectile.primalfocus.PrimalOrbEntity;
import thaumcraft.common.entities.projectile.thrownitem.AlumentumEntity;
import thaumcraft.common.entities.projectile.firefocus.EmberEntity;
import thaumcraft.common.entities.projectile.firefocus.ExplosiveOrbEntity;
import thaumcraft.common.entities.projectile.thrownitem.TaintBottleEntity;
import thaumcraft.common.entities.projectile.shockfocus.ShockOrbEntity;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

import java.util.IdentityHashMap;

import static com.linearity.opentc4.mixin.DefaultAttributesAccessor.opentc4$getSuppliers;
import static com.linearity.opentc4.mixin.DefaultAttributesAccessor.opentc4$setSuppliers;
import static thaumcraft.common.entities.ThaumcraftEntities.Registry.ENTITIES;

public class ThaumcraftEntities {

    public static class ThaumcraftEntityTypeInstances {

        public static EntityType<AlumentumEntity> ALUMENTUM() {
            return Registry.SUPPLIER_ALUMENTUM.get();
        }
        public static EntityType<TaintBottleEntity> TAINT_BOTTLE() {
            return Registry.SUPPLIER_TAINT_BOTTLE.get();
        }
        public static EntityType<SpecialItemEntity> SPECIAL_ITEM() {
            return Registry.SUPPLIER_SPECIAL_ITEM.get();
        }
        public static EntityType<ExplosiveOrbEntity> EXPLOSIVE_ORB() {
            return Registry.SUPPLIER_EXPLOSIVE_ORB.get();
        }
        public static EntityType<EmberEntity> EMBER() {
            return Registry.SUPPLIER_EMBER.get();
        }
        public static EntityType<ShockOrbEntity> SHOCK_ORB() {
            return Registry.SUPPLIER_SHOCK_ORB.get();
        }
        public static EntityType<FrostShardEntity> FROST_SHARD() {
            return Registry.SUPPLIER_FROST_SHARD.get();
        }
        public static EntityType<FireBatEntity> FIRE_BAT() {
            return Registry.SUPPLIER_FIRE_BAT.get();
        }
        public static EntityType<PechBlastEntity> PECH_BLAST() {
            return  Registry.SUPPLIER_PECH_BLAST.get();
        }
        public static EntityType<PrimalOrbEntity> PRIMAL_ORB() {
            return Registry.SUPPLIER_PRIMAL_ORB.get();
        }
        public static EntityType<TaintedCreeperEntity> TAINTED_CREEPER() {
            return Registry.SUPPLIER_TAINTED_CREEPER.get();
        }
        public static EntityType<TaintedVillagerEntity> TAINTED_VILLAGER() {
            return Registry.SUPPLIER_TAINTED_VILLAGER.get();
        }
        public static EntityType<TaintedCowEntity> TAINTED_COW() {
            return Registry.SUPPLIER_TAINTED_COW.get();
        }
        public static EntityType<TaintedSheepEntity>  TAINTED_SHEEP() {
            return Registry.SUPPLIER_TAINTED_SHEEP.get();
        }
        public static EntityType<TaintedChickenEntity> TAINTED_CHICKEN() {
            return Registry.SUPPLIER_TAINTED_CHICKEN.get();
        }
        public static EntityType<TaintedPigEntity> TAINTED_PIG() {
            return Registry.SUPPLIER_TAINTED_PIG.get();
        }
        public static EntityType<TaintedSpiderEntity> TAINTED_SPIDER() {
            return Registry.SUPPLIER_TAINTED_SPIDER.get();
        }
        public static EntityType<ThaumicSlimeEntity> THAUMIC_SLIME(){
            return Registry.SUPPLIER_THAUMIC_SLIME.get();
        }
        public static EntityType<TaintedSwarmEntity> TAINTED_SWARM() {
            return Registry.SUPPLIER_TAINTED_SWARM.get();
        }
        public static EntityType<TaintacleEntity> TAINTACLE() {
            return Registry.SUPPLIER_TAINTACLE.get();
        }
        public static EntityType<SmallTaintacleEntity> SMALL_TAINTACLE() {
            return Registry.SUPPLIER_SMALL_TAINTACLE.get();
        }
        public static EntityType<TaintSporeEntity> TAINT_SPORE() {
            return Registry.SUPPLIER_TAINT_SPORE.get();
        }
        public static EntityType<TaintSporeSwarmerEntity> TAINT_SPORE_SWARMER() {
            return Registry.SUPPLIER_TAINT_SPORE_SWARMER.get();
        }
        public static EntityType<BrainyZombieEntity> BRAINY_ZOMBIE() {
            return Registry.SUPPLIER_BRAINY_ZOMBIE.get();
        }
        public static EntityType<GiantBrainyZombieEntity> GIANT_BRAINY_ZOMBIE() {
            return Registry.SUPPLIER_GIANT_BRAINY_ZOMBIE.get();
        }
        public static EntityType<MindSpiderEntity> MIND_SPIDER() {
            return Registry.SUPPLIER_MIND_SPIDER.get();
        }
        public static EntityType<CultistEntity> CULTIST() {
            return Registry.SUPPLIER_CULTIST.get();
        }
        public static EntityType<CultistClericEntity> CULTIST_CLERIC() {
            return Registry.SUPPLIER_CULTIST_CLERIC.get();
        }
        public static EntityType<CultistKnightEntity> CULTIST_KNIGHT() {
            return Registry.SUPPLIER_CULTIST_KNIGHT.get();
        }
        public static EntityType<GolemOrbEntity> GOLEM_ORB() {
            return Registry.SUPPLIER_GOLEM_ORB.get();
        }
        public static EntityType<RedGolemOrbEntity> RED_GOLEM_ORB() {
            return Registry.SUPPLIER_RED_GOLEM_ORB.get();
        }
        public static EntityType<AspectArrowEntity> ASPECT_ARROW() {
            return Registry.SUPPLIER_ASPECT_ARROW.get();
        }
        public static EntityType<EldritchCrabEntity> ELDRITCH_CRAB() {
            return Registry.SUPPLIER_ELDRITCH_CRAB.get();
        }
        public static EntityType<InhabitedZombieEntity> INHABITED_ZOMBIE() {
            return Registry.SUPPLIER_INHABITED_ZOMBIE.get();
        }
        public static EntityType<EldritchGuardianEntity> ELDRITCH_GUARDIAN(){
            return Registry.SUPPLIER_ELDRITCH_GUARDIAN.get();
        }
        public static EntityType<EldritchOrbEntity> ELDRITCH_ORB(){
            return Registry.SUPPLIER_ELDRITCH_ORB.get();
        }
        public static EntityType<PechForagerEntity> PECH_FORAGER() {
            return Registry.SUPPLIER_PECH_FORAGER.get();
        }
        public static EntityType<PechMageEntity> PECH_MAGE() {
            return Registry.SUPPLIER_PECH_MAGE.get();
        }
        public static EntityType<PechStalkerEntity> PECH_STALKER() {
            return Registry.SUPPLIER_PECH_STALKER.get();
        }
        public static EntityType<WispEntity> WISP() {
            return Registry.SUPPLIER_WISP.get();
        }
        public static EntityType<DartEntity> DART() {
            return Registry.SUPPLIER_DART.get();
        }
    }

    public static class Registry {
        public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
                Thaumcraft.MOD_ID,
                Registries.ENTITY_TYPE
        );
        public static final RegistrySupplier<EntityType<AlumentumEntity>> SUPPLIER_ALUMENTUM = ENTITIES.register("alumentum",
                () -> EntityType.Builder.<AlumentumEntity>of(AlumentumEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("alumentum")
        );
        public static final RegistrySupplier<EntityType<TaintBottleEntity>> SUPPLIER_TAINT_BOTTLE = ENTITIES.register("taint_bottle",
                () -> EntityType.Builder.<TaintBottleEntity>of(TaintBottleEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("taint_bottle")
        );
        public static final RegistrySupplier<EntityType<SpecialItemEntity>> SUPPLIER_SPECIAL_ITEM = ENTITIES.register("special_item",
                () -> EntityType.Builder.<SpecialItemEntity>of(SpecialItemEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("special_item")
        );
        public static final RegistrySupplier<EntityType<ExplosiveOrbEntity>> SUPPLIER_EXPLOSIVE_ORB = ENTITIES.register("explosive_orb",
                () -> EntityType.Builder.<ExplosiveOrbEntity>of(ExplosiveOrbEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("explosive_orb")
        );
        public static final RegistrySupplier<EntityType<EmberEntity>> SUPPLIER_EMBER = ENTITIES.register("ember",
                () -> EntityType.Builder.<EmberEntity>of(EmberEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("ember")
        );
        public static final RegistrySupplier<EntityType<ShockOrbEntity>> SUPPLIER_SHOCK_ORB = ENTITIES.register("shock_orb",
                () -> EntityType.Builder.<ShockOrbEntity>of(ShockOrbEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("shock_orb")
        );
        public static final RegistrySupplier<EntityType<FrostShardEntity>> SUPPLIER_FROST_SHARD = ENTITIES.register("frost_shard",
                () -> EntityType.Builder.<FrostShardEntity>of(FrostShardEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("frost_shard")
        );
        public static final RegistrySupplier<EntityType<PechBlastEntity>> SUPPLIER_PECH_BLAST = ENTITIES.register("pech_blast",
                () -> EntityType.Builder.<PechBlastEntity>of(PechBlastEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("pech_blast")
        );
        public static final RegistrySupplier<EntityType<PrimalOrbEntity>> SUPPLIER_PRIMAL_ORB = ENTITIES.register("primal_orb",
                () -> EntityType.Builder.<PrimalOrbEntity>of(PrimalOrbEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("primal_orb")
        );
        public static final RegistrySupplier<EntityType<FireBatEntity>> SUPPLIER_FIRE_BAT = ENTITIES.register(
                "fire_bat",
                () -> EntityType.Builder.<FireBatEntity>of(FireBatEntity::new, MobCategory.MONSTER)
                        .sized(0.5F, 0.9F)
                        .clientTrackingRange(16)
                        .updateInterval(20)
                        .fireImmune()
                        .build("fire_bat")
        );
        public static final RegistrySupplier<EntityType<TaintedCreeperEntity>> SUPPLIER_TAINTED_CREEPER = ENTITIES.register(
                "tainted_creeper",
                () -> EntityType.Builder.<TaintedCreeperEntity>of(TaintedCreeperEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.7F).clientTrackingRange(8)
                        .build("tainted_creeper")
        );
        public static final RegistrySupplier<EntityType<TaintedVillagerEntity>> SUPPLIER_TAINTED_VILLAGER = ENTITIES.register(
                "tainted_villager",
                () -> EntityType.Builder.<TaintedVillagerEntity>of(TaintedVillagerEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.95F).clientTrackingRange(10)
                        .build("tainted_villager")
        );
        public static final RegistrySupplier<EntityType<TaintedCowEntity>> SUPPLIER_TAINTED_COW = ENTITIES.register(
                "tainted_cow",
                () -> EntityType.Builder.<TaintedCowEntity>of(TaintedCowEntity::new, MobCategory.CREATURE)
                        .sized(0.9F, 1.4F).clientTrackingRange(10)
                        .build("tainted_cow")
        );
        public static final RegistrySupplier<EntityType<TaintedSheepEntity>> SUPPLIER_TAINTED_SHEEP = ENTITIES.register(
                "tainted_sheep",
                () -> EntityType.Builder.<TaintedSheepEntity>of(TaintedSheepEntity::new, MobCategory.CREATURE)
                        .sized(0.9F, 1.3F).clientTrackingRange(10)
                        .build("tainted_sheep")
        );
        public static final RegistrySupplier<EntityType<TaintedChickenEntity>> SUPPLIER_TAINTED_CHICKEN = ENTITIES.register(
                "tainted_chicken",
                () -> EntityType.Builder.<TaintedChickenEntity>of(TaintedChickenEntity::new, MobCategory.CREATURE)
                        .sized(0.4F, 0.7F).clientTrackingRange(10)
                        .build("tainted_chicken")
        );
        public static final RegistrySupplier<EntityType<TaintedPigEntity>> SUPPLIER_TAINTED_PIG = ENTITIES.register(
                "tainted_pig",
                () -> EntityType.Builder.<TaintedPigEntity>of(TaintedPigEntity::new, MobCategory.CREATURE)
                        .sized(0.9F, 0.9F).clientTrackingRange(10)
                        .build("tainted_pig")
        );
        public static final RegistrySupplier<EntityType<TaintedSpiderEntity>> SUPPLIER_TAINTED_SPIDER = ENTITIES.register(
                "tainted_spider",
                () -> EntityType.Builder.<TaintedSpiderEntity>of(TaintedSpiderEntity::new, MobCategory.MONSTER)
                        .sized(0.4F, 0.3F).clientTrackingRange(10)
                        .build("tainted_spider")
        );
        public static final RegistrySupplier<EntityType<ThaumicSlimeEntity>> SUPPLIER_THAUMIC_SLIME = ENTITIES.register(
                "thaumic_slime",
                () -> EntityType.Builder.<ThaumicSlimeEntity>of(ThaumicSlimeEntity::new, MobCategory.MONSTER)
                        .sized(2.04F, 2.04F).clientTrackingRange(10)
                        .build("thaumic_slime")
        );
        public static final RegistrySupplier<EntityType<TaintedSwarmEntity>> SUPPLIER_TAINTED_SWARM = ENTITIES.register(
                "tainted_swarm",
                () -> EntityType.Builder.<TaintedSwarmEntity>of(TaintedSwarmEntity::new, MobCategory.MONSTER)
                        .sized(2, 2)
                        .clientTrackingRange(10)
                        .build("tainted_swarm")
        );
        public static final RegistrySupplier<EntityType<TaintacleEntity>> SUPPLIER_TAINTACLE = ENTITIES.register(
                "taintacle",
                () -> EntityType.Builder.<TaintacleEntity>of(TaintacleEntity::new, MobCategory.MONSTER)
                        .sized(0.66F, 3)
                        .clientTrackingRange(10)
                        .build("taintacle")
        );
        public static final RegistrySupplier<EntityType<SmallTaintacleEntity>> SUPPLIER_SMALL_TAINTACLE = ENTITIES.register(
                "small_taintacle",
                () -> EntityType.Builder.<SmallTaintacleEntity>of(SmallTaintacleEntity::new, MobCategory.MONSTER)
                        .sized(0.22F, 1)
                        .clientTrackingRange(10)
                        .build("small_taintacle")
        );
        public static final RegistrySupplier<EntityType<TaintSporeEntity>> SUPPLIER_TAINT_SPORE = ENTITIES.register(
                "taint_spore",
                () -> EntityType.Builder.<TaintSporeEntity>of(TaintSporeEntity::new, MobCategory.MONSTER)
                        .sized(1, 1)
                        .clientTrackingRange(10)
                        .build("taint_spore")
        );
        public static final RegistrySupplier<EntityType<TaintSporeSwarmerEntity>> SUPPLIER_TAINT_SPORE_SWARMER = ENTITIES.register(
                "taint_spore_swarmer",
                () -> EntityType.Builder.<TaintSporeSwarmerEntity>of(TaintSporeSwarmerEntity::new, MobCategory.MONSTER)
                        .sized(1, 1)
                        .clientTrackingRange(10)
                        .build("taint_spore_swarmer")
        );
        public static final RegistrySupplier<EntityType<BrainyZombieEntity>> SUPPLIER_BRAINY_ZOMBIE = ENTITIES.register(
                "brainy_zombie",
                () -> EntityType.Builder.<BrainyZombieEntity>of(BrainyZombieEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.95F)
                        .clientTrackingRange(10)
                        .build("brainy_zombie")
        );
        public static final RegistrySupplier<EntityType<GiantBrainyZombieEntity>> SUPPLIER_GIANT_BRAINY_ZOMBIE = ENTITIES.register(
                "giant_brainy_zombie",
                () -> EntityType.Builder.<GiantBrainyZombieEntity>of(GiantBrainyZombieEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.95F)
                        .clientTrackingRange(10)
                        .build("giant_brainy_zombie")
        );
        public static final RegistrySupplier<EntityType<MindSpiderEntity>> SUPPLIER_MIND_SPIDER = ENTITIES.register(
                "mind_spider",
                () -> EntityType.Builder.<MindSpiderEntity>of(MindSpiderEntity::new, MobCategory.MONSTER)
                        .sized(0.3F, 0.3F)
                        .clientTrackingRange(10)
                        .build("mind_spider")
        );
        public static final RegistrySupplier<EntityType<CultistEntity>> SUPPLIER_CULTIST = ENTITIES.register(
                "cultist",
                () -> EntityType.Builder.<CultistEntity>of(CultistEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F)
                        .clientTrackingRange(10)
                        .build("cultist")
        );
        public static final RegistrySupplier<EntityType<CultistClericEntity>> SUPPLIER_CULTIST_CLERIC = ENTITIES.register(
                "cultist_cleric",
                () -> EntityType.Builder.<CultistClericEntity>of(CultistClericEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F)
                        .clientTrackingRange(10)
                        .build("cultist_cleric")
        );
        public static final RegistrySupplier<EntityType<CultistKnightEntity>> SUPPLIER_CULTIST_KNIGHT = ENTITIES.register(
                "cultist_knight",
                () -> EntityType.Builder.<CultistKnightEntity>of(CultistKnightEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F)
                        .clientTrackingRange(10)
                        .build("cultist_knight")
        );
        public static final RegistrySupplier<EntityType<GolemOrbEntity>> SUPPLIER_GOLEM_ORB = ENTITIES.register(
                "golem_orb",
                () -> EntityType.Builder.<GolemOrbEntity>of(GolemOrbEntity::new, MobCategory.MISC)
                        .sized(0.1F,0.1F)
                        .clientTrackingRange(10)
                        .build("golem_orb")
        );
        public static final RegistrySupplier<EntityType<RedGolemOrbEntity>> SUPPLIER_RED_GOLEM_ORB = ENTITIES.register(
                "red_golem_orb",
                () -> EntityType.Builder.<RedGolemOrbEntity>of(RedGolemOrbEntity::new, MobCategory.MISC)
                        .sized(0.1F,0.1F)
                        .clientTrackingRange(10)
                        .build("red_golem_orb")
        );
        public static final RegistrySupplier<EntityType<AspectArrowEntity>> SUPPLIER_ASPECT_ARROW = ENTITIES.register("aspect_arrow",
                () -> EntityType.Builder.<AspectArrowEntity>of(AspectArrowEntity::new, MobCategory.MISC)
                        .sized(0.5F, 0.5F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("aspect_arrow")
        );
        public static final RegistrySupplier<EntityType<EldritchCrabEntity>> SUPPLIER_ELDRITCH_CRAB = ENTITIES.register("eldritch_crab",
                () -> EntityType.Builder.<EldritchCrabEntity>of(EldritchCrabEntity::new, MobCategory.MONSTER)
                        .sized(0.8F,0.6F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("eldritch_crab")
        );
        public static final RegistrySupplier<EntityType<InhabitedZombieEntity>> SUPPLIER_INHABITED_ZOMBIE = ENTITIES.register(
                "inhabited_zombie",
                () -> EntityType.Builder.<InhabitedZombieEntity>of(InhabitedZombieEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.95F)
                        .clientTrackingRange(10)
                        .build("inhabited_zombie")
        );
        public static final RegistrySupplier<EntityType<EldritchGuardianEntity>> SUPPLIER_ELDRITCH_GUARDIAN = ENTITIES.register(
                "eldritch_guardian",
                () -> EntityType.Builder.<EldritchGuardianEntity>of(EldritchGuardianEntity::new, MobCategory.MONSTER)
                        .sized(0.8F, 2.25F)
                        .clientTrackingRange(10)
                        .build("eldritch_guardian")
        );
        public static final RegistrySupplier<EntityType<EldritchOrbEntity>> SUPPLIER_ELDRITCH_ORB = ENTITIES.register("eldritch_orb",
                () -> EntityType.Builder.<EldritchOrbEntity>of(EldritchOrbEntity::new, MobCategory.MISC)
                        .sized(0.1F, 0.1F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("eldritch_orb")
        );
        public static final RegistrySupplier<EntityType<PechForagerEntity>> SUPPLIER_PECH_FORAGER = ENTITIES.register(
                "pech_forager",
                () -> EntityType.Builder.<PechForagerEntity>of(PechForagerEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F)
                        .clientTrackingRange(10)
                        .build("pech_forager")
        );
        public static final RegistrySupplier<EntityType<PechMageEntity>> SUPPLIER_PECH_MAGE = ENTITIES.register(
                "pech_mage",
                () -> EntityType.Builder.<PechMageEntity>of(PechMageEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F)
                        .clientTrackingRange(10)
                        .build("pech_mage")
        );
        public static final RegistrySupplier<EntityType<PechStalkerEntity>> SUPPLIER_PECH_STALKER = ENTITIES.register(
                "pech_stalker",
                () -> EntityType.Builder.<PechStalkerEntity>of(PechStalkerEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F)
                        .clientTrackingRange(10)
                        .build("pech_stalker")
        );

        public static final RegistrySupplier<EntityType<WispEntity>> SUPPLIER_WISP = ENTITIES.register(
                "wisp",
                () -> EntityType.Builder.<WispEntity>of(WispEntity::new, MobCategory.MONSTER)
                        .sized(0.9F,0.9F)
                        .clientTrackingRange(10)
                        .build("wisp")
        );
        public static final RegistrySupplier<EntityType<DartEntity>> SUPPLIER_DART = ENTITIES.register("dart",
                () -> EntityType.Builder.<DartEntity>of(DartEntity::new, MobCategory.MISC)
                        .sized(0.5F, 0.5F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("dart")
        );
    }

    public static class EntityTags {
        public static final TagKey<EntityType<?>> UNDEAD = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation("minecraft","undead"));
        public static final TagKey<EntityType<?>> FERTILITY_LAMP_AFFECTIVE = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"fertility_lamp_affective"));
        public static final TagKey<EntityType<?>> FERTILITY_LAMP_NOT_AFFECTIVE = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"fertility_lamp_not_affective"));
        public static final TagKey<EntityType<?>> TAINTED = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"tainted_entity"));
        public static final TagKey<EntityType<?>> ELDRITCH = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"eldritch_entity"));
        public static final TagKey<EntityType<?>> NOT_TAINT_CONVERTABLE = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"not_taint_convertable"));
        public static final TagKey<EntityType<?>> CAN_SPAWN_SMALL_TAINTACLE = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"can_spawn_small_taintacle"));
        public static final TagKey<EntityType<?>> PECH = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"pech"));
    }

    public static void init(){
        ENTITIES.register();
        registerAttributes();
        registerSpawnPlacements();
    }

    private static void registerAttributes() {
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.FIRE_BAT(),FireBatEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_CREEPER(),TaintedCreeperEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_COW(),TaintedCowEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_CHICKEN(),TaintedChickenEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_SHEEP(),TaintedSheepEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_PIG(),TaintedPigEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.THAUMIC_SLIME(), ThaumicSlimeEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_SWARM(), TaintedSwarmEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTACLE(), TaintacleEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.SMALL_TAINTACLE(), SmallTaintacleEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINT_SPORE(), TaintSporeEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINT_SPORE_SWARMER(), TaintSporeSwarmerEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.BRAINY_ZOMBIE(), BrainyZombieEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.GIANT_BRAINY_ZOMBIE(), GiantBrainyZombieEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.MIND_SPIDER(), MindSpiderEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.CULTIST(), CultistEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.CULTIST_CLERIC(), CultistClericEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.CULTIST_KNIGHT(), CultistKnightEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.ELDRITCH_CRAB(), EldritchCrabEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.INHABITED_ZOMBIE(), InhabitedZombieEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.ELDRITCH_GUARDIAN(), EldritchGuardianEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.PECH_FORAGER(),PechForagerEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.PECH_STALKER(),PechStalkerEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.PECH_MAGE(),PechMageEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.WISP(),WispEntity.createAttributes().build());

    }

    private static void registerSpawnPlacements() {
        SpawnPlacementsRegistry.register(
                Registry.SUPPLIER_TAINTACLE,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                TaintacleEntity::checkTaintacleSpawnRules
        );
        SpawnPlacementsRegistry.register(
                Registry.SUPPLIER_BRAINY_ZOMBIE,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules
        );
        SpawnPlacementsRegistry.register(
                Registry.SUPPLIER_FIRE_BAT,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules
        );
        SpawnPlacementsRegistry.register(
                Registry.SUPPLIER_ELDRITCH_GUARDIAN,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EldritchGuardianEntity::checkSpawnRules
        );
        SpawnPlacementsRegistry.register(
                Registry.SUPPLIER_PECH_FORAGER,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                PechForagerEntity::checkSpawnRules
        );
        SpawnPlacementsRegistry.register(
                Registry.SUPPLIER_PECH_MAGE,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                PechMageEntity::checkSpawnRules
        );
        SpawnPlacementsRegistry.register(
                Registry.SUPPLIER_PECH_STALKER,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                PechStalkerEntity::checkSpawnRules
        );
        SpawnPlacementsRegistry.register(
                Registry.SUPPLIER_WISP,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WispEntity::checkSpawnRules
        );
    }


    public static void registerDefaultAttribute(EntityType<? extends LivingEntity> entityType,AttributeSupplier attributeSupplier){
        ensureAttributeSuppliersModifiable();
        opentc4$getSuppliers().put(entityType, attributeSupplier);
    }

    public static void ensureAttributeSuppliersModifiable(){
        var gotMap = opentc4$getSuppliers();
        if (!(gotMap instanceof IdentityHashMap<EntityType<? extends LivingEntity>, AttributeSupplier>)){
            opentc4$setSuppliers(new IdentityHashMap<>(gotMap));
        }
    }

    public static boolean taintedMobWontAttack(LivingEntity entity){
        return entity.getType().is(EntityTags.TAINTED);
    }

    public static void handleTargetSelectorForTaintedMob(PathfinderMob mob, GoalSelector targetSelector){
        targetSelector.addGoal(1, new HurtByTargetGoal(mob));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, Player.class, true, living -> !taintedMobWontAttack(living)));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, IronGolem.class, true, living -> !taintedMobWontAttack(living)));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(mob, Villager.class, true, living -> !taintedMobWontAttack(living)));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(mob, Animal.class, true, living -> !taintedMobWontAttack(living)));
    }
    public static void handleGoalsForTaintedMob(PathfinderMob mob, GoalSelector goalSelector) {
        goalSelector.getAvailableGoals().removeIf(wrapped -> {
            var goal = wrapped.getGoal();
            return goal instanceof PanicGoal
                    || goal instanceof TemptGoal
                    || goal instanceof BreedGoal
                    || goal instanceof FollowParentGoal;
        });
        goalSelector.addGoal(2, new DelayControllableMeleeAttackGoal(mob, 1.0F, false).setAttackInterval(10));
    }

    public static boolean usualCanConvertToTaintedMob(LivingEntity living) {
        return living.hasEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.FLUX_TAINT())
                && !living.getType().is(EntityTags.TAINTED) && !living.getType().is(EntityTags.NOT_TAINT_CONVERTABLE);
    }
    public static <TaintedType extends NotTaintedType,NotTaintedType extends Entity> void
    usualTaintedMobConversion(
            NotTaintedType notTainted,
            EntityType<TaintedType> taintedEntityType
    ){

        var level = notTainted.level();
        var taintedSelf = taintedEntityType.create(level);
        if (taintedSelf != null){
            var tags = new CompoundTag();

            notTainted.saveWithoutId(tags);//TODO:I want to keep some features but idk if it's suitable
            tags.putUUID("UUID", taintedSelf.getUUID());
            taintedSelf.load(tags);
            if (taintedSelf instanceof LivingEntity living && notTainted instanceof LivingEntity notTaintedLiving){
                living.setHealth(living.getMaxHealth() *(notTaintedLiving.getHealth() / notTaintedLiving.getMaxHealth()));
            }

            level.addFreshEntity(taintedSelf);
            notTainted.discard();
        }else {
            OpenTC4.LOGGER.error("failed to convert entity to tainted mob,consider call #usualTaintedMobConversion in server (logical) side");
        }
    }
    public static boolean usualCanRecoverFromTaintedMob(LivingEntity living) {
        return false;//just not impl yet,keep as a mixin point
    }
    public static <TaintedType extends Entity,NotTaintedType extends Entity> void
    usualTaintedMobRecover(
            TaintedType tainted,
            EntityType<NotTaintedType> notTaintedEntityType
    ){

        var level = tainted.level();
        var taintedSelf = notTaintedEntityType.create(level);
        if (taintedSelf != null){
            var tags = new CompoundTag();

            tainted.saveWithoutId(tags);//TODO:I want to keep some features but idk if it's suitable
            tags.putUUID("UUID", taintedSelf.getUUID());
            taintedSelf.load(tags);
            if (taintedSelf instanceof LivingEntity living && tainted instanceof LivingEntity notTaintedLiving){
                living.setHealth(living.getMaxHealth() *(notTaintedLiving.getHealth() / notTaintedLiving.getMaxHealth()));
            }

            level.addFreshEntity(taintedSelf);
            tainted.discard();
        }else {
            OpenTC4.LOGGER.error("failed to convert entity to tainted mob,consider call #usualTaintedMobConversion in server (logical) side");
        }
    }
}
