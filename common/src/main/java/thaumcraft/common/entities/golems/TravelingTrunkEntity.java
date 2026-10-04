package thaumcraft.common.entities.golems;

import com.linearity.opentc4.annotations.StoleFrom;
import com.linearity.opentc4.mixinaccessors.InteractionOverridenMob;
import com.linearity.opentc4.utils.collectionlike.SimplePair;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.abstracts.StayableOwnableEntity;
import thaumcraft.common.entities.ai.goals.CrossDimensionFollowingOwnerGoal;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

import static com.linearity.opentc4.Consts.TravelingTrunkEntityTagAccessors.*;
import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;
import static thaumcraft.common.entities.golems.TravelingTrunkEntity.ITravelingTrunkUpgradeItem.DEFAULT_PAIR;

public class TravelingTrunkEntity extends Mob implements StayableOwnableEntity, InteractionOverridenMob {

    public TravelingTrunkEntity(Level worldIn) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TRAVELING_TRUNK(), worldIn);
    }
    public TravelingTrunkEntity(EntityType<TravelingTrunkEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new TravelingTrunkMoveControl(this);
        this.setPersistenceRequired();
    }

    protected int getJumpDelay() {
        return this.random.nextInt(5) + 10;
    }
    protected SoundEvent getJumpSound() {
        return SoundEvents.CHEST_CLOSE;
    }


    public static final int BASIC_SLOT_COUNT = 27;
    public @NotNull NonNullList<ItemStack> inventory = NonNullList.withSize(BASIC_SLOT_COUNT, ItemStack.EMPTY);
    protected int eatDelay = 0;

    public float lidrot = 0;
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 75)
                .add(Attributes.ATTACK_DAMAGE,4);
    }
    private static final EntityDataAccessor<Boolean> DATA_ID_OPENED = SynchedEntityData.defineId(TravelingTrunkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_STAY = SynchedEntityData.defineId(TravelingTrunkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> DATA_ID_OWNER = SynchedEntityData.defineId(TravelingTrunkEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<ItemStack> DATA_ID_UPGRADE = SynchedEntityData.defineId(TravelingTrunkEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> DATA_ID_SLOT_COUNT = SynchedEntityData.defineId(TravelingTrunkEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_ANGER = SynchedEntityData.defineId(TravelingTrunkEntity.class, EntityDataSerializers.INT);


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_OPENED, false);
        this.entityData.define(DATA_ID_STAY, false);
        this.entityData.define(DATA_ID_OWNER, Optional.empty());
        this.entityData.define(DATA_ID_UPGRADE, ItemStack.EMPTY);
        this.entityData.define(DATA_ID_SLOT_COUNT, BASIC_SLOT_COUNT);
        this.entityData.define(DATA_ID_ANGER, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        thaumcraft$setStay(STAY.readBooleanFromCompoundTag(compoundTag));
        setOwnerUUID(OWNER.readFromCompoundTag(compoundTag));
        setUpgradeStack(UPGRADE.readFromCompoundTag(compoundTag));
        var upgrade = getTravelingTrunkUpgrade();
        setSlotCount(upgrade.a().travelingTrunkUpgrade$inventorySize(this,upgrade.b()));
        ContainerHelper.loadAllItems(compoundTag, this.inventory);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        var ownerUUID = getOwnerUUID();
        STAY.writeBooleanToCompoundTag(compoundTag,thaumcraft$getStay());
        OWNER.writeToCompoundTag(compoundTag,ownerUUID == null ? this.uuid : ownerUUID);
        UPGRADE.writeToCompoundTag(compoundTag,getUpgradeStack());
        ContainerHelper.saveAllItems(compoundTag, this.inventory);
    }

    public boolean getOpened(){
        return this.entityData.get(DATA_ID_OPENED);
    }
    public void setOpened(boolean opened) {
        this.entityData.set(DATA_ID_OPENED, opened);
    }
    @Override
    public boolean thaumcraft$getStay(){
        return this.entityData.get(DATA_ID_STAY);
    }
    @Override
    public void thaumcraft$setStay(boolean stay) {
        this.entityData.set(DATA_ID_STAY, stay);
    }
    @Override
    public @Nullable UUID getOwnerUUID(){
        return this.entityData.get(DATA_ID_OWNER).orElse(null);
    }
    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_ID_OWNER, Optional.ofNullable(uuid));
    }
    public ItemStack getUpgradeStack(){
        return this.entityData.get(DATA_ID_UPGRADE);
    }
    public void setUpgradeStack(ItemStack stack) {
        this.entityData.set(DATA_ID_UPGRADE, stack);
    }
    public int getSlotCount() {
        return entityData.get(DATA_ID_SLOT_COUNT);
    }
    public void setSlotCount(int newSlotCount) {
        int slotCount = getSlotCount();
        var newInventory = NonNullList.withSize(newSlotCount, ItemStack.EMPTY);
        for (int i = 0; i < slotCount; i++) {
            if (i >= newSlotCount) {
                this.spawnAtLocation(inventory.get(i));
            } else {
                newInventory.set(i, inventory.get(i));
            }
        }
        this.inventory = newInventory;
        this.entityData.set(DATA_ID_SLOT_COUNT,newSlotCount);
    }
    public int getAnger(){
        return this.entityData.get(DATA_ID_ANGER);
    }
    public void setAnger(int anger) {
        this.entityData.set(DATA_ID_ANGER, anger);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        var upgradePair = getTravelingTrunkUpgrade();
        var modifiedDamage = upgradePair.a().travelingTrunkUpgrade$modifyHurtDamage(this,upgradePair.b(),damageSource,f);
        if (!Float.isNaN(f) && Float.isNaN(modifiedDamage)){
            return false;
        }
        return super.hurt(damageSource, modifiedDamage);
    }

    protected @NotNull SimplePair<ITravelingTrunkUpgradeItem,ItemStack> getTravelingTrunkUpgrade() {
        var upgrade = DEFAULT_PAIR;
        var upgradeStack = getUpgradeStack();
        if (upgradeStack.getItem() instanceof ITravelingTrunkUpgradeItem installedUpgrade){
            upgrade = new SimplePair<>(installedUpgrade,upgradeStack);
        }
        return upgrade;
    }

    public static final Vec3 VELOCITY_ADDITION_IN_WATER = new Vec3(0.0D, 0.033, 0.0D);

    @Override
    public void tick() {
        super.tick();
        var upgrade = getTravelingTrunkUpgrade();
        if (this.moveControl instanceof TravelingTrunkMoveControl travelingTrunkMoveControl) {
            travelingTrunkMoveControl.setWantedMovement(upgrade.a().travelingTrunkUpgrade$wantedMovement(this,upgrade.b()));
        }
        upgrade.a().travelingTrunkUpgrade$tick(this,upgrade.b());
        if (this.isInWater()) {
            this.addDeltaMovement(VELOCITY_ADDITION_IN_WATER);
        }

        var level = level();

        if (level.isClientSide) {
            if (!this.onGround() && this.getDeltaMovement().y < 0 && !this.isInWater()) {
                this.lidrot += 0.015F;
            }

            if ((this.onGround() || this.isInWater()) && !this.getOpened()) {
                this.lidrot -= 0.1F;
                if (this.lidrot < 0.0F) {
                    this.lidrot = 0.0F;
                }
            }

            if (this.getOpened()) {
                this.lidrot += 0.035F;
            }

            if (this.lidrot > (this.getOpened() ? 0.5F : 0.2F)) {
                this.lidrot = this.getOpened() ? 0.5F : 0.2F;
            }
        }

        this.fallDistance = 0.0F;
        if (this.getAnger() > 0) {
            this.setAnger(this.getAnger() - 1);
        }

        if (this.eatDelay > 0) {
            --this.eatDelay;
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        refreshGoals();
    }

    public void refreshGoals() {
        this.goalSelector.removeAllGoals(_ignored -> true);
        this.targetSelector.removeAllGoals(_ignored -> true);
        var upgrade = this.getTravelingTrunkUpgrade();
        upgrade.a().travelingTrunkUpgrade$registerGoals(this,upgrade.b(),this.goalSelector,this.targetSelector);
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {
        if (this.getTarget() != null && livingEntity == null){
            this.setAnger(5);
        }
        if (this.getTarget() == null && livingEntity != null){
            this.setAnger(600);
        }
        super.setTarget(livingEntity);
    }

    @Override
    public InteractionResult thaumcraft$interact(Player player, InteractionHand interactionHand) {
        var upgrade = getTravelingTrunkUpgrade();
        var interactionResult = upgrade.a().travelingTrunkUpgrade$modifyInteraction(this,upgrade.b(),player,interactionHand);
        if (interactionResult == InteractionResult.SUCCESS) {
            var usingStack = player.getItemInHand(interactionHand);
            if (usingStack.getItem() instanceof ITravelingTrunkUpgradeItem installedUpgrade && upgrade.b().isEmpty()){
                installUpgrade(installedUpgrade, usingStack);
                this.playSound(ThaumcraftSounds.UPGRADE,0.5F, 1.0F);
                player.swing(interactionHand);
                return InteractionResult.SUCCESS;
            }

            var foodProperties = usingStack.getItem().getFoodProperties();
            if (foodProperties != null){
                upgrade.a().travelingTrunkUpgrade$onFeed(this,upgrade.b(),usingStack,player,interactionHand);
            }

            if (player instanceof ServerPlayer serverPlayer) {
                openInventoryForPlayer(serverPlayer);
            }
        }
        return interactionResult;
    }

    protected void installUpgrade(ITravelingTrunkUpgradeItem installedUpgrade, ItemStack usingStack) {
        var usingUpgradeStack = usingStack.split(1);
        setUpgradeStack(usingUpgradeStack);
        installedUpgrade.travelingTrunkUpgrade$onInstalled(this,usingUpgradeStack);
    }

    protected void openInventoryForPlayer(ServerPlayer player){
        openExtendedMenu(player,menuProvider);
//        player.openGui(Thaumcraft.instance, 2, this.level(), this.getEntityId(), 0, 0);
    }

    @Override
    public @NotNull ItemStack eat(Level level, ItemStack itemStack) {
        var foodProps = itemStack.getItem().getFoodProperties();
        if (foodProps == null){
            return super.eat(level, itemStack);
        }
        int itemCountPre = itemStack.getCount();
        int healAmountPerConsumption = foodProps.getNutrition();
        var result = super.eat(level, itemStack);
        int itemCountConsumed = itemCountPre - result.getCount();
        if (itemCountConsumed > 0){
            this.heal(healAmountPerConsumption * itemCountConsumed);
            if (this.getHealth() == this.getMaxHealth()){
                this.playSound(SoundEvents.PLAYER_BURP,0.5F, this.random.nextFloat()*0.5F + 0.5F);
            }else {
                this.playSound(SoundEvents.GENERIC_EAT,0.5F, this.random.nextFloat()*0.5F + 0.5F);
            }
        }
        return result;
    }

    public interface ITravelingTrunkUpgradeItem {
        ITravelingTrunkUpgradeItem DEFAULT = new ITravelingTrunkUpgradeItem() {};
        SimplePair<ITravelingTrunkUpgradeItem,ItemStack> DEFAULT_PAIR = new SimplePair<>(DEFAULT, ItemStack.EMPTY);

        //NaN if cancel
        default float travelingTrunkUpgrade$modifyHurtDamage(TravelingTrunkEntity trunk,ItemStack upgradeStack,DamageSource source, float amount) {
            if (source.is(DamageTypes.CACTUS)) {
                return Float.NaN;
            }
            return amount;
        }

        default void travelingTrunkUpgrade$tick(TravelingTrunkEntity trunk,ItemStack upgradeStack){

        }

        //maybe something like "aqua&ordo" upgrade for a team's access?
        default InteractionResult travelingTrunkUpgrade$modifyInteraction(TravelingTrunkEntity trunk,ItemStack upgradeStack,Player player, InteractionHand interactionHand) {
            return InteractionResult.SUCCESS;
        }

        default void travelingTrunkUpgrade$onInstalled(TravelingTrunkEntity trunk,ItemStack upgradeStack){

        }

        default void travelingTrunkUpgrade$onFeed(TravelingTrunkEntity trunk,ItemStack upgradeStack,ItemStack usingStack,Player player,InteractionHand interactionHand) {
            var foodProperties = usingStack.getItem().getFoodProperties();
            if (foodProperties != null){
                //may works fine for eternal beef(or some bad food?whatever)
                //Trunk-And-Eternal-beef army?You and what army?
                usingStack.getItem().finishUsingItem(usingStack,trunk.level(),trunk);

                //TODO:[maybe wont finished]vote in democracy(crazy)
                // to decide if add cooldown
                // for "player using stack"
                // or "all living entity"
                // or "this living entity",
                // and add item cooldown
                // (in most cases we may have to add this cooldown manually!unless we make this entity a player(which may leads to lots of bugs).
                // I can also make a api for item cooldowns but we have to make it applicable for other mods ONE BY ONE
//                player.getCooldowns().addCooldown(usingStack.getItem(),);
            }
        }

        default double travelingTrunkUpgrade$wantedMovement(TravelingTrunkEntity trunk,ItemStack upgradeStack){
            return 1;
        }

        default void travelingTrunkUpgrade$registerGoals(TravelingTrunkEntity trunk, ItemStack upgradeStack, GoalSelector goalSelector,GoalSelector targetSelector) {
            goalSelector.addGoal(5, new TravelingTrunkAttackGoal(trunk));
            goalSelector.addGoal(6, new CrossDimensionFollowingOwnerGoal(trunk, trunk,1.0, 10.0F, 2.0F, false));
            travelingTrunkUpgrade$registerTargetGoals(trunk,upgradeStack,targetSelector);
        }
        default void travelingTrunkUpgrade$registerTargetGoals(TravelingTrunkEntity trunk, ItemStack upgradeStack,GoalSelector targetSelector){
        }
        default int travelingTrunkUpgrade$inventorySize(TravelingTrunkEntity trunk,ItemStack upgradeStack){
            return BASIC_SLOT_COUNT;
        }
        default ItemStack travelingTrunkUpgrade$getTravelingTrunkStack(TravelingTrunkEntity trunk,ItemStack upgradeStack){
            //TODO:Order upgrade keeps inv
        }
    }

    @StoleFrom("net.minecraft.world.entity.monster.Slime")
    public static class TravelingTrunkMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final TravelingTrunkEntity entity;
        private boolean isAggressive;

        public TravelingTrunkMoveControl(TravelingTrunkEntity entity) {
            super(entity);
            this.entity = entity;
            this.yRot = 180.0F * entity.getYRot() / (float) Math.PI;
        }

        public void setDirection(float f, boolean bl) {
            this.yRot = f;
            this.isAggressive = bl;
        }

        public void setWantedMovement(double d) {
            this.speedModifier = d;
            this.operation = MoveControl.Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();
            if (this.operation != MoveControl.Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = MoveControl.Operation.WAIT;
                if (this.mob.onGround()) {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.entity.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }

                        this.entity.getJumpControl().jump();

                        this.entity.playSound(this.entity.getJumpSound(), this.entity.getSoundVolume(), this.entity.getVoicePitch());
                    } else {
                        this.entity.xxa = 0.0F;
                        this.entity.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }
            }
        }
    }

    @StoleFrom("net.minecraft.world.entity.monster.Slime$SlimeAttackGoal")
    public static class TravelingTrunkAttackGoal  extends Goal {
        private final TravelingTrunkEntity slime;
        private int growTiredTimer;

        public TravelingTrunkAttackGoal(TravelingTrunkEntity arg) {
            this.slime = arg;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                return this.slime.canAttack(livingentity) && this.slime.getMoveControl() instanceof TravelingTrunkMoveControl;
            }
        }

        @Override
        public void start() {
            this.growTiredTimer = reducedTickDelay(300);
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            if (this.slime.getAnger() <= 0){
                return false;
            }
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                return this.slime.canAttack(livingentity) && --this.growTiredTimer > 0;
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        protected int attackTime = 0;
        @Override
        public void tick() {
            LivingEntity target = this.slime.getTarget();
            if (target != null) {
                var level = this.slime.level();
                this.slime.lookAt(target, 10.0F, 20.0F);
                if (this.attackTime <= 0
                        && this.slime.distanceToSqr(target) < 2.25
                        && target.getBoundingBox().maxY > this.slime.getBoundingBox().minY
                        && target.getBoundingBox().minY < this.slime.getBoundingBox().maxY) {
                    this.attackTime = 10 + this.slime.random.nextInt(5);
                    target.hurt(level.damageSources().mobAttack(this.slime), 4.0F);
                    level.broadcastEntityEvent(this.slime, (byte)17);
                    this.slime.playSound(SoundEvents.BLAZE_HURT, 0.5F, this.slime.random.nextFloat() * 0.1F + 0.9F);
                }
            }

            if (this.slime.getMoveControl() instanceof TravelingTrunkMoveControl slime$slimemovecontrol) {
                slime$slimemovecontrol.setDirection(this.slime.getYRot(), this.slime.isEffectiveAi());
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
        super.dropCustomDeathLoot(damageSource, i, bl);
        Containers.dropContents(level(),blockPosition(),inventory);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        var block = ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_PLANKS();
        return block.getSoundType(block.defaultBlockState()).getStepSound();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.ITEM_BREAK;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return super.shouldDespawnInPeaceful();
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 17) {
            this.lidrot = 0.15F;
            return;
        }
        if (b == 18) {
            this.lidrot = 0.15F;
            this.showHeartsOrSmokeFX(true);
            return;
        }
        super.handleEntityEvent(b);
    }

    protected void showHeartsOrSmokeFX(boolean flag) {
        var s = flag?ParticleTypes.HEART:ParticleTypes.SMOKE;
        int amount = flag?1:7;
        var pos = this.position();

        for(int i = 0; i < amount; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double d1 = this.random.nextGaussian() * 0.02;
            double d2 = this.random.nextGaussian() * 0.02;
            this.level().addParticle(
                    s,
                    pos.x + (this.getBbWidth() * (this.random.nextFloat()*2.0F - 1)),
                    pos.y + 0.5F + (this.random.nextFloat() * this.getBbHeight()),
                    pos.z + (this.getBbWidth() * (this.random.nextFloat()*2.0F - 1)),
                    d,
                    d1,
                    d2
            );
        }

    }
    public boolean wantsToAttack(LivingEntity livingEntity, LivingEntity livingEntity2) {
        if (livingEntity instanceof Creeper || livingEntity instanceof Ghast) {
            return false;
        } else if (livingEntity instanceof Wolf wolf) {
            return !wolf.isTame() || wolf.getOwner() != livingEntity2;
        } else if (livingEntity instanceof Player && livingEntity2 instanceof Player && !((Player)livingEntity2).canHarmPlayer((Player)livingEntity)) {
            return false;
        } else {
            return livingEntity instanceof AbstractHorse && ((AbstractHorse)livingEntity).isTamed()
                    ? false
                    : !(livingEntity instanceof TamableAnimal) || !((TamableAnimal)livingEntity).isTame();
        }
    }

    public boolean isOwner(@Nullable UUID uuidToCheck) {
        var ownerUUID = getOwnerUUID();
        if (getOwnerUUID() == this.uuid || getOwnerUUID() == null){
            return true;
        }
        return uuidToCheck == ownerUUID;
    }
}
