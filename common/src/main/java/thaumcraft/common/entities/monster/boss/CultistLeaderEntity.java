package thaumcraft.common.entities.monster.boss;

import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.ai.goals.CultistHurtByTargetGoal;
import thaumcraft.common.entities.ai.goals.NearestAttackableTargetGoalWithExclude;
import thaumcraft.common.entities.ai.goals.ZombieLikeAttackGoal;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.entity.IChampionModifiedNamingRuleOwner;
import thaumcraft.common.entities.monster.cultists.CultistEntity;
import thaumcraft.common.entities.projectile.golemorb.RedGolemOrbEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.List;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.LIVING_TEST;
import static thaumcraft.common.entities.ThaumcraftEntities.EntityTags.CULTIST;

public class CultistLeaderEntity extends ThaumcraftBossEntity implements IChampionModifiedNamingRuleOwner, RangedAttackMob {
    public static int NAME_COUNT = 15;
    public CultistLeaderEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.CULTIST_LEADER(),level);
    }
    public CultistLeaderEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 40;
    }

    @Override
    protected void registerGoals() {
        addLookingAtGoals();
        addBehaviourGoals();
    }

    protected void addLookingAtGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0F, 30, 40, 24.0F));
        this.goalSelector.addGoal(2, new ZombieLikeAttackGoal(this, 1.1F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        addTargetGoals();
    }

    protected void addTargetGoals() {
        this.targetSelector.addGoal(1, new CultistHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoalWithExclude<>(this, Monster.class, CultistEntity.class, true));
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return ThaumcraftBossEntity.createThaumcraftBossAttributes().add(Attributes.MAX_HEALTH, 125).add(Attributes.ATTACK_DAMAGE,5).add(Attributes.MOVEMENT_SPEED,0.32);
    }


    @Override
    public void setNameWhenChampionModified(LivingEntity living, ChampionModifier modifier) {
        setCustomName(
                Component.translatable(
                        "entity.Thaumcraft.CultistLeader.name",
                        Component.translatable("thaumcraft.boss.name.cultist_leader." + random.nextInt(NAME_COUNT)),
                        modifier.getChampionModifierNameLocalized()
                )
        );
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ThaumcraftItemInstances.CULTIST_LEADER_PLATE_HELMET()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ThaumcraftItemInstances.CULTIST_LEADER_PLATE_CHESTPLATE()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ThaumcraftItemInstances.CULTIST_LEADER_PLATE_LEGGINGS()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ThaumcraftItemInstances.CULTIST_BOOTS()));
        if (difficultyInstance.getDifficulty() == Difficulty.EASY) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ThaumcraftItemInstances.VOID_SWORD()));
        }else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ThaumcraftItemInstances.CRIMSON_SWORD()));
        }
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        float f = difficultyInstance.getEffectiveDifficulty();
        var mainHandStack = getItemBySlot(EquipmentSlot.MAINHAND);
        if (!mainHandStack.isEmpty() && randomSource.nextFloat() < 0.5F * f) {
            EnchantmentHelper.enchantItem(randomSource,mainHandStack,(int)(7 + f * randomSource.nextInt(22)),false);
        }
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || entity.getType().is(CULTIST);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        var x = getX();
        var y = getY();
        var z = getZ();
        final var range = 8;
        var box = new AABB(x - range, y - range, z - range, x + range, y + range, z + range);
        level().getEntities(LIVING_TEST,box, e -> e != this && e.getType().is(CULTIST)).forEach(e -> {
            if (!e.hasEffect(MobEffects.REGENERATION)) {
                e.addEffect(new MobEffectInstance(MobEffects.REGENERATION,60,1));
            }
        });
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float f) {
        if (EntityUtils.canEntityBeSeen(
                this,livingEntity
        )) {
            this.swing(InteractionHand.OFF_HAND);
            this.lookAt(livingEntity,30,30);
            var blast = new RedGolemOrbEntity(this, livingEntity, this.level());
            var blastMotion = blast.getDeltaMovement();
            blast.setPos(blast.getX() + blastMotion.x/2.F, blast.getY(), blast.getZ() + blastMotion.z/2.F);
            var flyVec = livingEntity.getEyePosition().subtract(this.getEyePosition());
            double d0 = flyVec.x;
            double d1 = flyVec.y;
            double d2 = flyVec.z;
            blast.shoot(d0, d1 + (double)2.0F, d2, 0.66F, 3.0F);
            this.playSound(ThaumcraftSounds.EG_ATTACK, 1.0F, 1.0F + this.random.nextFloat() * 0.1F);
            this.level().addFreshEntity(blast);
        }
    }

    @Override
    @Unmodifiable
    public List<ItemStack> generateSpecialDrops() {
        return List.of();
    }
}
