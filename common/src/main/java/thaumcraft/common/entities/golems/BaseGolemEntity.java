package thaumcraft.common.entities.golems;

import com.linearity.opentc4.utils.ClassToItemStackPairMap;
import com.linearity.opentc4.utils.collectionlike.AutoSortThreadSafeList;
import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.crafted.GolemFetterBlock;
import thaumcraft.common.items.golem.upgrade.abstracts.*;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.linearity.opentc4.Consts.BaseGolemEntityTagAccessors.*;
import static com.linearity.opentc4.utils.compoundtag.accessors.mc.BlockPosAccessor.NULL_POS_TO_WRITE;

public abstract class BaseGolemEntity extends AbstractGolemUpgradeApplicableEntity<BaseGolemEntity.IGolemUpgradeItem> implements RangedAttackMob {

    public static class BaseGolemEntityClientEventSignals {
        public static final byte HEAL = 5;
    }

    public BaseGolemEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public abstract GolemBasicAbilityProperties getBasicAbilityProperties();

    public static List<ObjectIntPair<Class<? extends IGolemModifierItem>>> defaultGolemUpgradeItemTypes = new AutoSortThreadSafeList<>();

    static {
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemCoreItem.class, 0));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemDecorationItem.class, 100));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemBasicUpgradeItem.class, 200));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemAIProviderItem.class, 300));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemRangedAttackPerformerItem.class, 400));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemInventoryProviderItem.class, 500));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemCarryItemPowerProviderItem.class, 600));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemAttackDelayProviderItem.class, 700));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemRegenDelayModifierItem.class, 800));
        defaultGolemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemRegenAmountModifierItem.class, 900));
    }

    public List<ObjectIntPair<Class<? extends IGolemModifierItem>>> getAvailableGolemModifierTypes() {
        return defaultGolemUpgradeItemTypes;
    }

    private final ClassToItemStackPairMap golemUpgradesCache = new ClassToItemStackPairMap();
    protected final ClassToItemStackPairMap.View golemUpgradesCacheView = new ClassToItemStackPairMap.View(golemUpgradesCache);

    public int regenTickCounter = 0;

    private boolean isOnFetter = false;
    @Override
    public void tick() {
        super.tick();
        var level = level();
        if (!level.isClientSide) {
            checkRegen(level);
            tryTeleportHome();
        }
        var blockStateBelow = level.getBlockState(blockPosition().below());
        checkFetter(blockStateBelow);
    }

    protected void checkRegen(Level level) {
        if (regenTickCounter > 0) {
            regenTickCounter--;
        } else if (this.getHealth() != this.getMaxHealth()) {
            this.regenTickCounter = abilitiesData.regenDelay;
            level.broadcastEntityEvent(this, BaseGolemEntityClientEventSignals.HEAL);
            this.heal(abilitiesData.regenAmount);
        }
    }

    protected void checkFetter(BlockState blockStateBelow) {
        boolean isOnFetterBefore = this.isOnFetter;
        if (blockStateBelow.getBlock() instanceof GolemFetterBlock fetter) {
            this.isOnFetter = fetter.shouldSetGolemOnFetter(blockStateBelow,this);
        } else {
            this.isOnFetter = false;
        }
        if (isOnFetterBefore != this.isOnFetter){
            if (this.isOnFetter) {
                this.goalSelector.removeAllGoals(_ignored -> true);
                this.targetSelector.removeAllGoals(_ignored -> true);
            }else {
                refreshGoals();
                this.setSpeed(abilitiesData.speedMultiplier * abilitiesData.properties.speed);
            }
        }
        if (this.isOnFetter) {
            this.navigation.stop();
            this.setSpeed(0);
        }
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
                                                            this.teleportTo(serverLevel, teleportPos.x, teleportPos.y, teleportPos.z, Set.of(), this.getXRot(), this.getYRot());
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
                                    , () -> {
                                    }
                            ),
                            () -> {
                            });

        }
    }

    public NonNullList<ItemStack> inventory = NonNullList.withSize(0, ItemStack.EMPTY);
    public ByteArrayList inventoryColors = new ByteArrayList();

    public static class GolemAbilitiesData {
        public int carryPower;//item carry capacity:*1,fluid carry capacity(mb):*1000
        public int inventorySize = 0;
        public int attackDelay;
        public int regenDelay;
        public int regenAmount;
        public int strength;
        public float speedMultiplier = 1;
        public final GoalSelector goalSelector;
        public final GoalSelector targetSelector;
        public final GolemBasicAbilityProperties properties;

        public GolemAbilitiesData(GoalSelector goalSelector, GoalSelector targetSelector, GolemBasicAbilityProperties properties) {
            this.carryPower = properties.carryPower;
            this.attackDelay = properties.attackDelay;
            this.regenDelay = properties.regenDelay;
            this.regenAmount = properties.regenAmount;
            this.strength = properties.strength;
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
            this.speedMultiplier = 1;
            this.inventorySize = 0;
        }

        public void safetyCheckAndFix() {
            this.carryPower = Math.max(this.carryPower, 0);
            this.attackDelay = Math.max(this.attackDelay, 1);
            this.regenDelay = Math.max(this.regenDelay, 1);
            this.speedMultiplier = Math.max(this.speedMultiplier, 10E-4F);
            this.inventorySize = Math.max(this.inventorySize, 0);
            this.regenAmount = Math.max(this.regenAmount, 0);
            this.strength = Math.max(this.strength, 0);
        }
    }

    private final GolemAbilitiesData abilitiesData = new GolemAbilitiesData(this.goalSelector, this.targetSelector, getBasicAbilityProperties());

    public int getCarryPower() {
        return this.abilitiesData.carryPower;
    }

    public int getAttackDelay() {
        return this.abilitiesData.attackDelay;
    }

    protected @NotNull ResourceLocation homeLevelResourceLocation = level().dimension().location();

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.abilitiesData.inventorySize = INVENTORY_SIZE.readIntFromCompoundTag(tag);
        this.inventory = NonNullList.withSize(this.abilitiesData.inventorySize, ItemStack.EMPTY);
        this.inventoryColors = new ByteArrayList(INVENTORY_COLORS.readFromCompoundTag(tag));
        ContainerHelper.loadAllItems(tag, this.inventory);
        this.restrictTo(HOME_POS.readFromCompoundTag(tag), 48);
        var homeLevelResourceLocation = HOME_LEVEL.readFromCompoundTag(tag);
        if (!homeLevelResourceLocation.getNamespace().isEmpty() && !homeLevelResourceLocation.getPath().isEmpty()) {
            this.homeLevelResourceLocation = homeLevelResourceLocation;
        }
        super.readAdditionalSaveData(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        INVENTORY_SIZE.writeIntToCompoundTag(compoundTag, this.abilitiesData.inventorySize);
        INVENTORY_COLORS.writeToCompoundTag(compoundTag, this.inventoryColors.toByteArray());
        ContainerHelper.saveAllItems(compoundTag, this.inventory);
        HOME_POS.writeToCompoundTag(compoundTag, getRestrictCenter());
        HOME_LEVEL.writeToCompoundTag(compoundTag, this.homeLevelResourceLocation);
    }

    protected void refreshAbilities() {
        abilitiesData.reset();
        for (var clz : golemUpgradesCache.getKeys()) {
            if (IGolemAbilityModifierItem.class.isAssignableFrom(clz)) {
                for (var upgradePair : golemUpgradesCacheView.get(clz)) {
                    ((IGolemAbilityModifierItem) upgradePair.a()).modifyGolemAbility(this, upgradePair.b(), abilitiesData);
                }
            }
        }
        abilitiesData.safetyCheckAndFix();
        reconstructInventory();
    }

    protected void reconstructInventory() {
        var oldInventory = this.inventory;
        int inventorySizePrev = oldInventory.size();
        int inventorySizeNext = this.abilitiesData.inventorySize;
        if (inventorySizePrev != inventorySizeNext) {
            var level = level();
            var pos = position();
            var oldInventoryColors = this.inventoryColors;
            var newInventory = NonNullList.withSize(inventorySizeNext, ItemStack.EMPTY);
            var newInventoryColors = new ByteArrayList(inventorySizeNext);
            for (int i = 0; i < inventorySizePrev; i++) {
                var stack = oldInventory.get(i);
                oldInventory.set(i, ItemStack.EMPTY);
                if (i >= inventorySizeNext) {
                    Containers.dropItemStack(level, pos.x, pos.y, pos.z, stack);
                } else {
                    newInventory.add(i, stack);
                    newInventoryColors.set(i, oldInventoryColors.getByte(i));
                }
            }
            for (var i = inventorySizePrev; i < inventorySizeNext; i++) {
                newInventoryColors.set(i, (byte) 0);
            }
            this.inventory = newInventory;
            this.inventoryColors = newInventoryColors;
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
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.6)
                .add(Attributes.FOLLOW_RANGE, 32);
    }

    @Override
    protected void registerGoals() {
        refreshGoals();
    }

    public void refreshGoals() {
        this.goalSelector.removeAllGoals(_ignored -> true);
        this.targetSelector.removeAllGoals(_ignored -> true);
        for (var providerPair : golemUpgradesCacheView.get(IGolemAIProviderItem.class)) {
            providerPair.a().registerGoalForGolem(this, providerPair.b(), this.goalSelector, this.targetSelector);
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
        return !golemCoreFullFor(item) && !basicUpgradeFullFor(item) && !decorationItemFullFor(item);
    }

    protected boolean golemCoreFullFor(Item item) {
        return item instanceof IGolemCoreItem && !golemUpgradesCacheView.get(IGolemCoreItem.class).isEmpty();
    }

    protected boolean basicUpgradeFullFor(Item item) {
        return item instanceof IGolemBasicUpgradeItem && getGolemBasicUpgradeCapacity() <= golemUpgradesCacheView.get(IGolemBasicUpgradeItem.class).size();
    }

    protected boolean decorationItemFullFor(Item item) {
        return item instanceof IGolemDecorationItem decoration && golemUpgradesCacheView.get(IGolemDecorationItem.class).stream().anyMatch(p -> p.a() == decoration);
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


    public static class GolemBasicAbilityProperties {
        public final int basicUpgradeSize;
        public final int carryPower;
        public final float speed;
        public final int regenDelay;
        public final int regenAmount;
        public final int strength;
        public final int attackDelay;

        public GolemBasicAbilityProperties(int basicUpgradeSize, int carryPower, float speed, int regenDelay, int strength, int attackDelay, int regenAmount) {
            this.basicUpgradeSize = basicUpgradeSize;
            this.carryPower = carryPower;
            this.speed = speed;
            this.regenDelay = regenDelay;
            this.regenAmount = regenAmount;
            this.strength = strength;
            this.attackDelay = attackDelay;
        }

        public static class Builder {
            public GolemBasicAbilityProperties build() {
                return new GolemBasicAbilityProperties(basicUpgradeSize, carryPower, speed, regenDelay, strength, attackDelay, regenAmount);
            }

            public int basicUpgradeSize = 1;
            public int carryPower = 1;
            public float speed = 0.38F;
            public int regenDelay = 75;
            public int strength = 0;
            public int attackDelay = 20;
            public int regenAmount = 1;

            public Builder basicUpgradeSize(int basicUpgradeSize) {
                this.basicUpgradeSize = basicUpgradeSize;
                return this;
            }

            public Builder carryPower(int carryPower) {
                this.carryPower = carryPower;
                return this;
            }

            public Builder speed(float speed) {
                this.speed = speed;
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
        }
    }
}
