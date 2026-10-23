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
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
import thaumcraft.common.items.golem.GolemBellItem;
import thaumcraft.common.items.golem.upgrade.abstracts.*;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.*;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;

import java.util.*;

import static com.linearity.opentc4.Consts.BaseGolemEntityTagAccessors.*;
import static com.linearity.opentc4.utils.compoundtag.accessors.mc.BlockPosAccessor.NULL_POS_TO_WRITE;
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

    public static List<ObjectIntPair<Class<? extends IGolemModifierItem>/*int:priority(lower ones comes first)*/>> defaultGolemUpgradeItemTypes = new AutoSortThreadSafeList<>();

    static {
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemCoreItem.class, 0));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemDecorationItem.class, 100));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemBasicUpgradeItem.class, 200));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemAIProviderItem.class, 300));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemRangedAttackPerformerItem.class, 400));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemAbilityModifierItem.class, 500));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemAdditionalDataProviderItem.class, 600));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemReceivedDamageModifierItem.class,700));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemDamageListenerItem.class, 800));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemCollapseToItemStackListenerItem.class, 900));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemPlayerInteractionListenerItem.class, 1000));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemHomeRangeModifierItem.class, 1100));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemColorfulMarkerProviderItem.class, 1200));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemShootDartAccuracyModifierItem.class,1300));
    }

    public List<ObjectIntPair<Class<? extends IGolemModifierItem>>> getAvailableGolemModifierTypes() {
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
        float range = abilitiesData.homeRange;
        for (var modifierPair:golemUpgradesCacheView.get(IGolemHomeRangeModifierItem.class)){
            range = modifierPair.a().modifyHomeRange(this,modifierPair.b(),range);
        }
        return (int) range;
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

    public static class GolemAbilitiesData {
        public float carryPower;//item carry capacity:*1,fluid carry capacity(mb):*1000
        public float inventorySize = 0;
        public float attackDelay;
        public float regenDelay;
        public float regenAmount;
        public float strength;
        public float attackFireTicks;
        public float speedMultiplier = 1;
        public final GolemGoalSelector goalSelector;
        public final GolemGoalSelector targetSelector;
        public final GolemBasicAbilityProperties properties;
        public float homeRange = 16;

        public GolemAbilitiesData(GolemGoalSelector goalSelector, GolemGoalSelector targetSelector, GolemBasicAbilityProperties properties) {
            this.carryPower = properties.carryPower;
            this.attackDelay = properties.attackDelay;
            this.regenDelay = properties.regenDelay;
            this.regenAmount = properties.regenAmount;
            this.strength = properties.strength;
            this.attackFireTicks = properties.attackFireTicks;
            this.goalSelector = goalSelector;
            this.targetSelector = targetSelector;
            this.properties = properties;
        }

        public void reset() {
            this.carryPower = properties.carryPower;
            this.attackDelay = properties.attackDelay;
            this.regenDelay = properties.regenDelay;
            this.regenAmount = properties.regenAmount;
            this.strength = properties.strength;
            this.attackFireTicks = properties.attackFireTicks;
            this.speedMultiplier = 1;
            this.inventorySize = 0;
            this.homeRange = 16;
        }

        public void safetyCheckAndFix() {
            this.carryPower = Math.max(this.carryPower, 0);
            this.attackDelay = Math.max(this.attackDelay, 1);
            this.regenDelay = Math.max(this.regenDelay, 1);
            this.speedMultiplier = Math.max(this.speedMultiplier, 10E-4F);
            this.inventorySize = Math.max(this.inventorySize, 0);
            this.regenAmount = Math.max(this.regenAmount, 0);
            this.strength = Math.max(this.strength, 0);
            this.attackFireTicks = Math.max(this.attackFireTicks,0);
            this.homeRange = Math.max(this.homeRange, 0);
        }
    }

    private final GolemAbilitiesData abilitiesData = new GolemAbilitiesData(this.wrappedGoalSelector, this.wrappedTargetSelector, getBasicAbilityProperties());

    public int getCarryPower() {
        return (int) this.abilitiesData.carryPower;
    }

    public int getAttackDelay() {
        return (int) this.abilitiesData.attackDelay;
    }

    @NotNull
    public ResourceLocation homeLevelResourceLocation = level().dimension().location();

    private static final EntityDataAccessor<byte[]> DATA_ID_INVENTORY_COLORS = SynchedEntityData.defineId(BaseGolemEntity.class, EntityDataSerializerAdditions.BYTE_ARRAY);

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
            additionalDataProviderPair.a().readAdditional(this,additionalDataProviderPair.b(),tag);
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
            additionalDataProviderPair.a().writeAdditional(this,additionalDataProviderPair.b(),tag);
        }
    }

    @Override
    public ItemStack collapseToItemStackWithoutSneaking() {
        for (var collapseListenerPair:golemUpgradesCacheView.get(IGolemCollapseToItemStackListenerItem.class)) {
            collapseListenerPair.a().onCollapseToItemStackWithoutSneaking(this,collapseListenerPair.b());
        }
        Containers.dropContents(level(),blockPosition(),inventory);
        inventory.clear();
        return super.collapseToItemStackWithoutSneaking();
    }

    @Override
    public ItemStack collapseToItemStackWithSneaking() {
        for (var collapseListenerPair:golemUpgradesCacheView.get(IGolemCollapseToItemStackListenerItem.class)) {
            collapseListenerPair.a().onCollapseToItemStackWithSneaking(this,collapseListenerPair.b());
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
            upgradePair.a().modifyGolemAbility(this, upgradePair.b(), abilitiesData);
        }
        abilitiesData.safetyCheckAndFix();
        reconstructInventory();
    }

    protected void reconstructInventory() {
        var oldInventory = this.inventory;
        int inventorySizePrev = oldInventory.size();
        int inventorySizeNext = (int) this.abilitiesData.inventorySize;
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
                if (clz.isInstance(item)) {
                    golemUpgradesCache.checkAndPut(clz, item, stack);
                }
            }
        }
        refreshAbilities();
        decorationTypes.clear();
        for (var decorationPair:golemUpgradesCacheView.get(IGolemDecorationItem.class)){
            decorationTypes.addAll(decorationPair.a().getOccupiedTypes(this, decorationPair.b()));
        }
    }

    @Override
    protected void onAddedUpgradeStack(IGolemUpgradeItem installedUpgrade, ItemStack usingUpgradeStack) {
        super.onAddedUpgradeStack(installedUpgrade, usingUpgradeStack);
        var item = usingUpgradeStack.getItem();
        if (item instanceof IGolemUpgradeInstalledListenerItem listener) {
            listener.onUpgradeInstalled(this, usingUpgradeStack);
        }
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
            providerPair.a().registerGoalForGolem(this, providerPair.b(), this.wrappedGoalSelector, this.wrappedTargetSelector);
        }
    }

    @Override
    public Class<IGolemUpgradeItem> getUpgradeClass() {
        return IGolemUpgradeItem.class;
    }

    public interface IGolemUpgradeItem extends IAbstractGolemUpgradeItem, IGolemModifierItem {
        @Override
        default boolean golemUpgrade$isApplicableTo(ItemStack stack, AbstractGolemUpgradeApplicableEntity<?> golem) {
            return golem instanceof BaseGolemEntity;
        }
    }

    public boolean canPerformRangedAttack(LivingEntity target, float f) {
        for (var performerPair : golemUpgradesCacheView.get(IGolemRangedAttackPerformerItem.class)) {
            if (performerPair.a().canPerformRangedAttackForGolem(this, performerPair.b(), target, f)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float f) {
        for (var performerPair : golemUpgradesCacheView.get(IGolemRangedAttackPerformerItem.class)) {
            performerPair.a().performRangedAttackForGolemIfCan(this, performerPair.b(), target, f);
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
        for (var damageModifierPair:golemUpgradesCacheView.get(IGolemReceivedDamageModifierItem.class)) {
            f = damageModifierPair.a().modifyReceivedDamage(this,damageModifierPair.b(),damageSource,f);
        }
        if (Float.isNaN(f)) {
            return false;
        }

        boolean result = super.hurt(damageSource, f);
        if (result){
            for (var damageListenerPair:golemUpgradesCacheView.get(IGolemDamageListenerItem.class)) {
                damageListenerPair.a().onDamaged(this,damageListenerPair.b(),damageSource,f);
            }
        }
        return result;
    }

    public static class GolemBasicAbilityProperties {
        public final int basicUpgradeSize;
        public final int carryPower;
        public final int regenDelay;
        public final int regenAmount;
        public final int strength;
        public final int attackDelay;
        public final int attackFireTicks;

        public GolemBasicAbilityProperties(int basicUpgradeSize, int carryPower, int regenDelay, int strength, int attackDelay, int regenAmount, int attackFireTicks) {
            this.basicUpgradeSize = basicUpgradeSize;
            this.carryPower = carryPower;
            this.regenDelay = regenDelay;
            this.regenAmount = regenAmount;
            this.strength = strength;
            this.attackDelay = attackDelay;
            this.attackFireTicks = attackFireTicks;
        }

        public static class Builder {
            public GolemBasicAbilityProperties build() {
                return new GolemBasicAbilityProperties(basicUpgradeSize, carryPower, regenDelay, strength, attackDelay, regenAmount, attackFireTicks);
            }

            public static Builder copy(GolemBasicAbilityProperties properties) {
                return new Builder().strength(properties.strength)
                        .carryPower(properties.carryPower)
                        .basicUpgradeSize(properties.basicUpgradeSize)
                        .regenDelay(properties.regenDelay)
                        .regenAmount(properties.regenAmount)
                        .attackFireTicks(properties.attackFireTicks)
                        .attackDelay(properties.attackDelay);
            }

            protected int basicUpgradeSize = 1;
            protected int carryPower = 1;
            protected int regenDelay = 75;
            protected int strength = 0;
            protected int attackDelay = 20;
            protected int regenAmount = 1;
            protected int attackFireTicks = 0;

            public Builder basicUpgradeSize(int basicUpgradeSize) {
                this.basicUpgradeSize = basicUpgradeSize;
                return this;
            }

            public Builder carryPower(int carryPower) {
                this.carryPower = carryPower;
                return this;
            }

            public Builder regenDelay(int regenDelay) {
                this.regenDelay = regenDelay;
                return this;
            }

            public Builder strength(int strength) {
                this.strength = strength;
                return this;
            }

            public Builder attackDelay(int attackDelay) {
                this.attackDelay = attackDelay;
                return this;
            }

            public Builder regenAmount(int regenAmount) {
                this.regenAmount = regenAmount;
                return this;
            }

            public Builder attackFireTicks(int attackFireTicks){
                this.attackFireTicks = attackFireTicks;
                return this;
            }
        }
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
            ctx.action = 6;

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
        if (usingStack.getItem() instanceof GolemBellItem golemBell){
            golemBell.onInteractGolem(player,interactionHand,usingStack,this);
        }else {
            for (var interactionListenerPair:golemUpgradesCacheView.get(IGolemPlayerInteractionListenerItem.class)) {
                interactionListenerPair.a().listenerOnPlayerInteract(this,interactionListenerPair.b(),player,interactionHand);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
