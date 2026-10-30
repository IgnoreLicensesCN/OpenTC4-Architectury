package thaumcraft.common.entities.golems;

import com.linearity.opentc4.datautils.EntityDataSerializerAdditions;
import com.linearity.opentc4.mixinaccessors.InteractionOverrideMob;
import com.linearity.opentc4.mixinaccessors.cliententity.BaseGolemEntityClientAccessor;
import com.linearity.opentc4.utils.ClassToItemStackPairMap;
import com.linearity.opentc4.utils.collectionlike.AutoSortThreadSafeList;
import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.crafted.GolemFetterBlock;
import thaumcraft.common.entities.golems.properties.GolemBasicAbilityProperties;
import thaumcraft.common.items.golem.GolemBellItem;
import thaumcraft.common.items.golem.upgrade.abstracts.*;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.*;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.*;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters.IGolemColorfulMarkerProviderItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters.IGolemItemStackMatcherModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters.IGolemTargetControlItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners.*;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack.rangedattack.*;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;

import java.util.*;
import java.util.function.BiPredicate;

import static com.linearity.opentc4.Consts.BaseGolemEntityTagAccessors.*;
import static com.linearity.opentc4.utils.compoundtag.accessors.mc.BlockPosAccessor.NULL_POS_TO_WRITE;
import static net.minecraft.network.syncher.EntityDataSerializers.BYTE;
import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.*;

public abstract class BaseGolemEntity
        extends AbstractGolemUpgradeApplicableEntity<BaseGolemEntity.IGolemUpgradeItem>
        implements
        RangedAttackMob,
        InteractionOverrideMob {
    public static final UUID SPEED_MODOFIER_UUID = UUID.fromString("6b9a6842-f707-498a-8a58-f0c2fa3d88f4");
    public AttributeModifier getSpeedModifierAttribute() {
        return new AttributeModifier(SPEED_MODOFIER_UUID,
                "golem speed modifier", abilitiesData.speedMultiplier, AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }
    public AttributeModifier getFreezingSpeedModifierAttribute() {
        return new AttributeModifier(SPEED_MODOFIER_UUID,
                "golem speed modifier", 0, AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }
    public void resetSpeedModifier() {
        var attrInstance = getAttribute(Attributes.MOVEMENT_SPEED);
        if (attrInstance != null) {
            attrInstance.removeModifier(SPEED_MODOFIER_UUID);
            attrInstance.addTransientModifier(getSpeedModifierAttribute());
        }
    }
    public void freezeSpeedModifier() {
        var attrInstance = getAttribute(Attributes.MOVEMENT_SPEED);
        if (attrInstance != null) {
            attrInstance.removeModifier(SPEED_MODOFIER_UUID);
            attrInstance.addTransientModifier(getFreezingSpeedModifierAttribute());
        }
    }

    protected final GolemGoalSelector wrappedGoalSelector = new GolemGoalSelector(this.goalSelector);
    protected final GolemGoalSelector wrappedTargetSelector = new GolemGoalSelector(this.targetSelector);

    protected final Set<DecorationOccupiedTypeResourceLocation> decorationTypes = new HashSet<>();

    @Override
    public boolean hasRestriction() {
        if (NULL_POS_TO_WRITE.equals(this.getRestrictCenter())){
            return false;
        }
        return super.hasRestriction();
    }

    public void updateItemInHand() {
        for (var pair:golemUpgradesCacheView.get(IGolemUpdateItemInHandListenerItem.class)) {
            pair.getUpgradeType().onUpdateItemInHand(this,pair.getUpgradeStack());
        }
    }


    public static class BaseGolemEntityClientEventSignals {
        public static final byte START_ACTION_TIMER = 4;
        public static final byte HEAL = 5;
        public static final byte START_LEFT_ARM_TIMER = 6;
        public static final byte BOOT_UP = 7;
        public static final byte START_RIGHT_ARM_TIMER = 8;
    }

    public BaseGolemEntity(EntityType<? extends BaseGolemEntity> entityType, Level level) {
        super(entityType, level);
        var navi = new GroundPathNavigation(this,level);
        this.navigation = navi;
        navi.setCanFloat(true);
        navi.setCanOpenDoors(true);
        navi.setCanPassDoors(true);
        navi.setCanWalkOverFences(true);
    }

    public abstract GolemBasicAbilityProperties getBasicAbilityProperties();

    public static List<ObjectIntPair<Class<? extends IGolemUpgradeItem>/*int:priority(lower ones comes first)*/>> defaultGolemUpgradeItemTypes = new AutoSortThreadSafeList<>(ObjectIntPair::compareTo);

    static {
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemCoreItem.class, 0));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemDecorationItem.class, 100));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemBasicUpgradeItem.class, 200));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemAIProviderItem.class, 300));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemRangedAttackPerformerItem.class, 400));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemAbilityModifierItem.class, 500));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemAdditionalDataProviderItem.class, 600));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemReceivedDamageModifierItem.class,700));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemDamagedListenerItem.class, 800));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemCollapseToItemStackListenerItem.class, 900));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemPlayerInteractionListenerItem.class, 1000));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemHomeRangeModifierItem.class, 1100));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemColorfulMarkerProviderItem.class, 1200));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemShootAccuracyModifierItem.class,1300));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemShootCooldownModifierItem.class,1400));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemTargetControlItem.class,1500));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemUpdateItemInHandListenerItem.class,1600));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemItemStackMatcherModifierItem.class,1700));
    }

    public List<ObjectIntPair<Class<? extends BaseGolemEntity.IGolemUpgradeItem>>> getAvailableGolemModifierTypes() {
        return defaultGolemUpgradeItemTypes;
    }

    private final ClassToItemStackPairMap golemUpgradesCache = new ClassToItemStackPairMap();
    public final ClassToItemStackPairMap.View golemUpgradesCacheView = new ClassToItemStackPairMap.View(golemUpgradesCache);

    public int regenTickCounter = 0;

    private boolean isOnFetter = false;
    public boolean isPaused = false;
    @Override
    public void tick() {
        super.tick();
        var level = level();
        if (!level.isClientSide) {
            checkRegen(level);
            tryTeleportHome();
        }
        var blockStateBelow = level.getBlockState(blockPosition().below());
        checkAIAvailable(blockStateBelow);
    }

    protected void checkRegen(Level level) {
        if (regenTickCounter > 0) {
            regenTickCounter--;
        } else if (this.getHealth() != this.getMaxHealth()) {
            this.regenTickCounter = (int) abilitiesData.regenDelay;
            this.heal(abilitiesData.regenAmount);
            level.broadcastEntityEvent(this, BaseGolemEntityClientEventSignals.HEAL);
        }
    }

    public boolean isAIAvailable() {
        return !this.isOnFetter && !isPaused;
    }

    protected void checkAIAvailable(BlockState blockStateBelow) {
        boolean isAIAvailableBefore = this.isAIAvailable();
        if (blockStateBelow.getBlock() instanceof GolemFetterBlock fetter) {
            this.isOnFetter = fetter.shouldSetGolemOnFetter(blockStateBelow,this);
        } else {
            this.isOnFetter = false;
        }
        if (isAIAvailableBefore != isAIAvailable()){
            if (this.isOnFetter) {
                this.wrappedGoalSelector.removeAllGoals(_ignored -> true);
                this.targetSelector.removeAllGoals(_ignored -> true);
            }else {
                refreshGoals();
                resetSpeedModifier();
            }
        }
        if (!this.isAIAvailable()){
            this.navigation.stop();
        }
        if (this.isOnFetter) {
            freezeSpeedModifier();
        }
    }

    public int getHomeRange(){
        return (int) abilitiesData.homeRange;
    }

    public void tryTeleportHome() {
        var level = this.level();
        var homePos = getRestrictCenter();
        if (this.homeLevelResourceLocation != level.dimension().location()
                || (
                !Objects.equals(homePos, NULL_POS_TO_WRITE)
                        && homePos.distSqr(blockPosition()) > getRestrictRadius() * getRestrictRadius()
        )
        ) {
            level.registryAccess()
                    .registry(Registries.DIMENSION)
                    .ifPresentOrElse(
                            levelAccess -> levelAccess.getOptional(
                                    this.homeLevelResourceLocation
                            ).ifPresentOrElse(
                                    homeLevel -> {
                                        if (homeLevel instanceof ServerLevel serverLevel) {
                                            int x = homePos.getX();
                                            int y = homePos.getY();
                                            int z = homePos.getZ();
                                            var mutablePos = new BlockPos.MutableBlockPos();
                                            for (int yOffset = 1; yOffset >= -1; --yOffset) {
                                                for (int xOffset = -1; xOffset <= 1; ++xOffset) {
                                                    for (int zOffset = -1; zOffset <= 1; ++zOffset) {
                                                        mutablePos.set(
                                                                x + xOffset,
                                                                y - 1 + yOffset,
                                                                z + zOffset
                                                        );
                                                        //                                            World var10000 = this.level();
                                                        if (homeLevel.getBlockState(mutablePos).isFaceSturdy(
                                                                homeLevel,
                                                                mutablePos,
                                                                Direction.UP)
//                                                                && !this.level().isBlockNormalCubeDefault(x + xOffset, y + yOffset, z + zOffset, false)
                                                        ) { 
                                                            var teleportPos = mutablePos.getCenter();
                                                            this.teleportTo(
                                                                    serverLevel,
                                                                    teleportPos.x,
                                                                    teleportPos.y,
                                                                    teleportPos.z,
                                                                    Set.of(),
                                                                    this.getXRot(),
                                                                    this.getYRot()
                                                            );
//                                                                this.setLocationAndAngles((float) (x + xOffset) + 0.5F, (double) y + (double) yOffset, (float) (z + zOffset) + 0.5F, this.rotationYaw, this.rotationPitch);
//                                                                this.getNavigator().clearPathEntity();
                                                            this.navigation.stop();
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    , () -> {}
                            ),
                            () -> {});

        }
    }

    public NonNullList<ItemStack> inventory = NonNullList.withSize(0, ItemStack.EMPTY);
    public final Set<Marker> markers = new HashSet<>();
    public @NotNull Direction homeDirection = Direction.UP;

    private final GolemAbilitiesData abilitiesData = new GolemAbilitiesData(this.wrappedGoalSelector, this.wrappedTargetSelector, getBasicAbilityProperties());

    public int getCarryPower() {
        return (int) this.abilitiesData.carryPower;
    }

    public int getAttackDelay() {
        return (int) this.abilitiesData.attackDelay;
    }

    @NotNull
    public ResourceLocation homeLevelResourceLocation = level().dimension().location();

    public static final EntityDataAccessor<byte[]> DATA_ID_INVENTORY_COLORS = SynchedEntityData.defineId(BaseGolemEntity.class, EntityDataSerializerAdditions.BYTE_ARRAY);
    public static final EntityDataAccessor<Byte> DATA_ID_ATTACK_CONTROL_FLAGS = SynchedEntityData.defineId(BaseGolemEntity.class,BYTE);
    public static final byte FLAG_CAN_ATTACK_HOSTILES = 1;
    public static final byte FLAG_CAN_ATTACK_ANIMALS = 1<<1;
    public static final byte FLAG_CAN_ATTACK_PLAYERS = 1<<2;
    public static final byte FLAG_CAN_ATTACK_CREEPERS = 1<<3;

    public byte getAttackControlFlags(){
        return entityData.get(DATA_ID_ATTACK_CONTROL_FLAGS);
    }
    public void setAttackControlFlags(byte flags){
        entityData.set(DATA_ID_ATTACK_CONTROL_FLAGS,flags);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_INVENTORY_COLORS,new byte[0]);
        this.entityData.define(DATA_ID_ATTACK_CONTROL_FLAGS, (byte) 0);
    }

    public byte[] getInventoryColors() {
        return this.entityData.get(DATA_ID_INVENTORY_COLORS);
    }
    public void setInventoryColors(byte[] colors) {
        this.entityData.set(DATA_ID_INVENTORY_COLORS, colors);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.abilitiesData.inventorySize = INVENTORY_SIZE.readIntFromCompoundTag(tag);
        this.inventory = NonNullList.withSize((int) this.abilitiesData.inventorySize, ItemStack.EMPTY);

        ContainerHelper.loadAllItems(tag, this.inventory);
        this.restrictTo(HOME_POS.readFromCompoundTag(tag), getDefaultRestrictionSize());
        var homeLevelResourceLocation = HOME_LEVEL.readFromCompoundTag(tag);
        if (!homeLevelResourceLocation.getNamespace().isEmpty() && !homeLevelResourceLocation.getPath().isEmpty()) {
            this.homeLevelResourceLocation = homeLevelResourceLocation;
        }
        super.readAdditionalSaveData(tag);
        markers.clear();
        MARKERS.readFromCompoundTagInto(tag,markers);
        homeDirection = HOME_FACE.readFromCompoundTag(tag);
        for (var additionalDataProviderPair:golemUpgradesCacheView.get(IGolemAdditionalDataProviderItem.class)) {
            additionalDataProviderPair.getUpgradeType().readAdditional(this,additionalDataProviderPair.getUpgradeStack(),tag);
        }
    }

    public int getDefaultRestrictionSize(){
        return 48;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        INVENTORY_SIZE.writeIntToCompoundTag(tag, (int) this.abilitiesData.inventorySize);
        ContainerHelper.saveAllItems(tag, this.inventory);
        HOME_POS.writeToCompoundTag(tag, getRestrictCenter());
        HOME_LEVEL.writeToCompoundTag(tag, this.homeLevelResourceLocation);
        MARKERS.writeToCompoundTag(tag, markers);
        HOME_FACE.writeToCompoundTag(tag, homeDirection);
        for (var additionalDataProviderPair:golemUpgradesCacheView.get(IGolemAdditionalDataProviderItem.class)) {
            additionalDataProviderPair.getUpgradeType().writeAdditional(this,additionalDataProviderPair.getUpgradeStack(),tag);
        }
    }

    @Override
    public ItemStack collapseToItemStackWithoutSneaking() {
        for (var collapseListenerPair:golemUpgradesCacheView.get(IGolemCollapseToItemStackListenerItem.class)) {
            collapseListenerPair.getUpgradeType().onCollapseToItemStackWithoutSneaking(this,collapseListenerPair.getUpgradeStack());
        }
        dropInventory();
        return super.collapseToItemStackWithoutSneaking();
    }

    public void dropInventory() {
        Containers.dropContents(level(),blockPosition(),inventory);
        inventory.clear();
        for (var stack:getAllSlots()){
            if (!stack.isEmpty()){
                spawnAtLocation(stack.split(stack.getCount()));
            }
        }
    }

    @Override
    public ItemStack collapseToItemStackWithSneaking() {
        for (var collapseListenerPair:golemUpgradesCacheView.get(IGolemCollapseToItemStackListenerItem.class)) {
            collapseListenerPair.getUpgradeType().onCollapseToItemStackWithSneaking(this,collapseListenerPair.getUpgradeStack());
        }
        var result = super.collapseToItemStackWithSneaking();
        var stackTag = result.getOrCreateTag();
        ContainerHelper.saveAllItems(stackTag, inventory);
        INVENTORY_SIZE.writeIntToCompoundTag(stackTag, inventory.size());
//        HOME_POS.writeToCompoundTag(stackTag, getRestrictCenter());
//        HOME_LEVEL.writeToCompoundTag(stackTag, homeLevelResourceLocation);
        MARKERS.writeToCompoundTag(stackTag, markers);
        return result;
    }

    protected void refreshAbilities() {
        abilitiesData.reset();
        for (var upgradePair : golemUpgradesCacheView.get(IGolemAbilityModifierItem.class)) {
            upgradePair.getUpgradeType().modifyGolemAbility(this, upgradePair.getUpgradeStack(), abilitiesData);
        }
        abilitiesData.safetyCheckAndFix();
        reconstructInventory();
    }

    protected void reconstructInventory() {
        var oldInventory = this.inventory;
        int inventorySizePrev = oldInventory.size();
        int inventorySizeNext = (int) (this.abilitiesData.inventorySize * this.abilitiesData.inventorySizeMultiplier);
        if (inventorySizePrev != inventorySizeNext) {
            var level = level();
            var pos = position();
            byte[] oldInventoryColors = this.getInventoryColors();
            var newInventory = NonNullList.withSize(inventorySizeNext, ItemStack.EMPTY);
            var newInventoryColors = new byte[inventorySizeNext];
            for (int i = 0; i < inventorySizePrev; i++) {
                var stack = oldInventory.get(i);
                oldInventory.set(i, ItemStack.EMPTY);
                if (i >= inventorySizeNext) {
                    Containers.dropItemStack(level, pos.x, pos.y, pos.z, stack);
                } else {
                    newInventory.add(i, stack);
                    newInventoryColors[i] = oldInventoryColors[i];
                }
            }
            this.inventory = newInventory;
            this.setInventoryColors(newInventoryColors);
        }
    }

    @Override
    public void setUpgradeStacks(List<ItemStack> stacks) {
        super.setUpgradeStacks(stacks);
        for (var stack : stacks) {
            var item = stack.getItem();
            for (var clzPair : getAvailableGolemModifierTypes()) {
                var clz = clzPair.first();
                if (clz.isInstance(item) && item instanceof IGolemUpgradeItem modifierItem) {
                    golemUpgradesCache.checkAndPut(clz, item, stack, modifierItem.getUpgradePriority(this,stack,clz));
                }
            }
        }
        refreshAbilities();
        decorationTypes.clear();
        for (var decorationPair:golemUpgradesCacheView.get(IGolemDecorationItem.class)){
            decorationTypes.addAll(decorationPair.getUpgradeType().getOccupiedTypes(this, decorationPair.getUpgradeStack()));
        }
    }

    @Override
    protected void onAddedUpgradeStack(IGolemUpgradeItem installedUpgrade, ItemStack usingUpgradeStack) {
        super.onAddedUpgradeStack(installedUpgrade, usingUpgradeStack);
        var item = usingUpgradeStack.getItem();
        if (item instanceof IGolemUpgradeInstalledListenerItem listener) {
            listener.onUpgradeInstalled(this, usingUpgradeStack);
        }
        this.playSound(ThaumcraftSounds.CAMERA_CLACK,1.0F, 1.0F);
    }

    public static AttributeSupplier.Builder createGolemAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.6)
                .add(Attributes.FOLLOW_RANGE, 32);
    }

    @Override
    protected void registerGoals() {
        refreshGoals();
    }

    public void refreshGoals() {
        this.wrappedGoalSelector.removeAllGoals(_ignored -> true);
        this.targetSelector.removeAllGoals(_ignored -> true);
        for (var providerPair : golemUpgradesCacheView.get(IGolemAIProviderItem.class)) {
            providerPair.getUpgradeType().registerGoalForGolem(this, providerPair.getUpgradeStack(), this.wrappedGoalSelector, this.wrappedTargetSelector);
        }
    }

    @Override
    public Class<IGolemUpgradeItem> getUpgradeClass() {
        return IGolemUpgradeItem.class;
    }

    public interface IGolemUpgradeItem extends IAbstractGolemUpgradeItem {
        @Override
        default boolean golemUpgrade$isApplicableTo(ItemStack stack, AbstractGolemUpgradeApplicableEntity<?> golem) {
            return golem instanceof BaseGolemEntity;
        }
    }

    public boolean canPerformRangedAttack(LivingEntity target, float f) {
        for (var performerPair : golemUpgradesCacheView.get(IGolemRangedAttackPerformerItem.class)) {
            if (performerPair.getUpgradeType().canPerformRangedAttackForGolem(this, performerPair.getUpgradeStack(), target, f)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float f) {
        for (var performerPair : golemUpgradesCacheView.get(IGolemRangedAttackPerformerItem.class)) {
            performerPair.getUpgradeType().performRangedAttackForGolemIfCan(this, performerPair.getUpgradeStack(), target, f);
        }
    }

    @Override
    public int getGolemUpgradeCapacity() {
        return Integer.MAX_VALUE;
    }

    public int getGolemBasicUpgradeCapacity() {
        return 1;
    }

    @Override
    protected boolean canInstallUpgrade(ItemStack stack) {
        if (!super.canInstallUpgrade(stack)) {
            return false;
        }
        var item = stack.getItem();
        return !golemCoreFullFor(item) && !basicUpgradeFullFor(item) && !decorationItemFullFor(stack);
    }

    protected boolean golemCoreFullFor(Item item) {
        return item instanceof IGolemCoreItem && !golemUpgradesCacheView.get(IGolemCoreItem.class).isEmpty();
    }

    protected boolean basicUpgradeFullFor(Item item) {
        return item instanceof IGolemBasicUpgradeItem && getGolemBasicUpgradeCapacity() <= golemUpgradesCacheView.get(IGolemBasicUpgradeItem.class).size();
    }

    protected boolean decorationItemFullFor(ItemStack stack) {
        var item = stack.getItem();
        if (!(item instanceof IGolemDecorationItem decoration)) {
            return false;
        }
        for (var decoType: decoration.getOccupiedTypes(this, stack)) {
            if (decorationTypes.contains(decoType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ThaumcraftSounds.CAMERA_CLACK;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ThaumcraftSounds.CAMERA_CLACK;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(DamageSource damageSource) {
        return ThaumcraftSounds.CAMERA_CLACK;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (damageSource.is(DamageTypes.CACTUS)){
            f = Float.NaN;
        }
        var defaultDamage = f;
        for (var damageModifierPair:golemUpgradesCacheView.get(IGolemReceivedDamageModifierItem.class)) {
            f = damageModifierPair.getUpgradeType().modifyReceivedDamage(this,damageModifierPair.getUpgradeStack(),damageSource,f,defaultDamage);
        }
        if (Float.isNaN(f)) {
            return false;
        }

        boolean result = super.hurt(damageSource, f);
        if (result){
            for (var damageListenerPair:golemUpgradesCacheView.get(IGolemDamagedListenerItem.class)) {
                damageListenerPair.getUpgradeType().onDamaged(this,damageListenerPair.getUpgradeStack(),damageSource,f);
            }
        }
        return result;
    }

    public static class ClientTickContext {
        public int action = 0;
        public int leftArm = 0;
        public int rightArm = 0;
        public int healing = 0;
        public float bootup  = -1.0F;
        public static void clientTick(BaseGolemEntity golem){
            var ctx = ((BaseGolemEntityClientAccessor)golem).opentc4$getClientTickContext();
            if (ctx.action > 0) {
                --ctx.action;
            }

            if (ctx.leftArm > 0) {
                --ctx.leftArm;
            }

            if (ctx.rightArm > 0) {
                --ctx.rightArm;
            }

            if (ctx.healing > 0) {
                --ctx.healing;
            }
            if (ctx.bootup > 0.0F && !golem.golemUpgradesCacheView.get(IGolemCoreItem.class).isEmpty()) {
                ctx.bootup *= ctx.bootup / 33.1F;
                golem.playSound(ThaumcraftSounds.CAMERA_TICKS, ctx.bootup * 0.2F, ctx.bootup);
            }
        }

        public int getActionTimer() {
            return 3 - Math.abs(this.action - 3);
        }

        public static void startActionTimer(BaseGolemEntity golem) {
            var ctx = ((BaseGolemEntityClientAccessor)golem).opentc4$getClientTickContext();
            golem.setNoActionTime(6);
//            ctx.action = 6;

        }

        public static void startLeftArmTimer(BaseGolemEntity golem) {
            var ctx = ((BaseGolemEntityClientAccessor)golem).opentc4$getClientTickContext();
            ctx.leftArm = 5;

        }

        public static void startRightArmTimer(BaseGolemEntity golem) {
            var ctx = ((BaseGolemEntityClientAccessor)golem).opentc4$getClientTickContext();
            ctx.rightArm = 5;
        }
        public static void heal(BaseGolemEntity golem) {
            var ctx = ((BaseGolemEntityClientAccessor)golem).opentc4$getClientTickContext();
            ctx.healing = 5;
        }
        public static void bootup(BaseGolemEntity golem) {
            var ctx = ((BaseGolemEntityClientAccessor)golem).opentc4$getClientTickContext();
            ctx.bootup = 33;
        }
    }

    @Override
    public void handleEntityEvent(byte b) {
        switch (b) {
            case START_ACTION_TIMER -> ClientTickContext.startActionTimer(this);
            case HEAL -> ClientTickContext.clientTick(this);
            case START_RIGHT_ARM_TIMER -> ClientTickContext.startRightArmTimer(this);
            case BOOT_UP -> ClientTickContext.bootup(this);
            case START_LEFT_ARM_TIMER -> ClientTickContext.startLeftArmTimer(this);
            default -> super.handleEntityEvent(b);
        }
    }

    @Override
    public boolean canBeCollapsedBy(LivingEntity living) {
        return isOwnedBy(living.getUUID());
    }

    @Override
    public InteractionResult thaumcraft$interact(Player player, InteractionHand interactionHand) {
        if (!isOwnedBy(player.getUUID())) {
            if (level().isClientSide()) {
                player.sendSystemMessage(Component.translatable("thaumcraft.golem.interact.interact_not_owned_golem"));
            }
            return InteractionResult.PASS;
        }
        var usingStack = player.getItemInHand(interactionHand);
        if (tryFeedStack(player, interactionHand, usingStack)){
            return InteractionResult.SUCCESS;
        }
        if (usingStack.getItem() instanceof GolemBellItem golemBell){
            golemBell.onInteractGolem(player,interactionHand,usingStack,this);
        }else {
            for (var interactionListenerPair:golemUpgradesCacheView.get(IGolemPlayerInteractionListenerItem.class)) {
                interactionListenerPair.getUpgradeType().listenerOnPlayerInteract(this,interactionListenerPair.getUpgradeStack(),player,interactionHand);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        var result = super.doHurtTarget(entity);
        {
            int addFireTicks = (int)abilitiesData.attackFireTicks;
            if (addFireTicks > 0 && !entity.fireImmune()) {
                entity.setRemainingFireTicks(addFireTicks + entity.getRemainingFireTicks());
            }
        }
        for (var listenerPair:golemUpgradesCacheView.get(IGolemDoHurtTargetListenerItem.class)) {
            listenerPair.getUpgradeType().onDoHurtTarget(this,listenerPair.getUpgradeStack(),entity);
        }
        return result;
    }

    @Override
    protected boolean tryFeedStack(
            Player player,
            InteractionHand interactionHand,
            ItemStack usingStack) {
        var result = super.tryFeedStack(player,interactionHand,usingStack);
        if (result){
            for (var upgradePair:golemUpgradesCacheView.get(IGolemOnFeedListener.class)) {
                upgradePair.getUpgradeType().onGolemFed(this,upgradePair.getUpgradeStack(), usingStack, player, interactionHand);
            }
        }
        return result;
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

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
        super.dropCustomDeathLoot(damageSource, i, bl);
        dropInventory();
        setUpgradeStacks(List.of());
    }

    @Override
    protected float getSoundVolume() {
        return 0.1F;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected int decreaseAirSupply(int i) {
        return i;
    }

    @Override
    public int getAirSupply() {
        return getMaxAirSupply();
    }

    private final BiPredicate<ItemStack,ItemStack> itemStackMatcherWhenUpgraded = (stackA,stackB) -> {
        var matcherUpgrades = this.golemUpgradesCacheView.get(IGolemItemStackMatcherModifierItem.class);
        for (var matcherUpgradePair:matcherUpgrades) {
            if (matcherUpgradePair.getUpgradeType().modifyItemStackMatching(this,matcherUpgradePair.getUpgradeStack()).test(stackA,stackB)) {
                return true;
            }
        }
        return false;
    };

    public BiPredicate<ItemStack,ItemStack> getItemStackMatcher(){
        var matcherUpgrades = this.golemUpgradesCacheView.get(IGolemItemStackMatcherModifierItem.class);
        if (matcherUpgrades.isEmpty()) {
            return ItemStack::isSameItemSameTags;
        }
        return itemStackMatcherWhenUpgraded;
    }
}
