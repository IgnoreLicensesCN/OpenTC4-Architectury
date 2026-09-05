package thaumcraft.common.entities.monster.pech;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import com.linearity.opentc4.annotations.StoleFrom;
import com.linearity.opentc4.utils.collectionlike.AutoSortThreadSafeList;
import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.internal.WeightedRandomCollection;
import thaumcraft.api.listeners.aspects.item.bonus.ItemBonusAspectCalculator;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.abstracts.DoorBreakingMonster;
import thaumcraft.common.entities.ai.goals.WrappedChangeableGoal;
import thaumcraft.common.entities.ai.goals.ZombieLikeAttackGoal;
import thaumcraft.common.entities.ai.pech.PechGotoItemEntityGoal;
import thaumcraft.common.entities.ai.pech.PechTradePlayerGoal;
import thaumcraft.common.entities.projectile.pechfocus.PechBlastEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;
import thaumcraft.common.items.abstracts.wandabstraction.wand.IWandFocusEngineItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.linearity.opentc4.Consts.AbstractPechEntityTagAccessors.ANGER;
import static com.linearity.opentc4.Consts.AbstractPechEntityTagAccessors.TAMED;
import static com.linearity.opentc4.utils.consts.EntityTypeTests.PECH_TEST;
import static net.minecraft.world.Containers.dropItemStack;
import static thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter.getBasicAspectsServer;
import static thaumcraft.common.items.ThaumcraftItemInstances.*;

//"entity.Thaumcraft.Pech.name": "岩精强盗",
//"entity.Thaumcraft.Pech.1.name": "岩精法师",
//"entity.Thaumcraft.Pech.2.name": "岩精猎手",
public abstract class AbstractPechEntity extends DoorBreakingMonster
        implements
        RangedAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_TAMED
            = SynchedEntityData.defineId(AbstractPechEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ANGER
            = SynchedEntityData.defineId(AbstractPechEntity.class, EntityDataSerializers.INT);
    public boolean trading = false;
    public boolean updateAINextTick = false;
    public float mumble = 0.0F;
    public NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    public int chargecount;

    public AbstractPechEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        Arrays.fill(this.handDropChances, 0.2F);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ARMOR, 2)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TAMED, false);
        this.entityData.define(DATA_ANGER, 0);
    }

    public boolean isTamed() {
        return this.entityData.get(DATA_TAMED);
    }

    public void setTamed(boolean tamed) {
        this.entityData.set(DATA_TAMED, tamed);
    }

    public int getAnger() {
        return this.entityData.get(DATA_ANGER);
    }

    public void setAnger(int anger) {
        this.entityData.set(DATA_ANGER, anger);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        inventory.clear();
        ContainerHelper.loadAllItems(compoundTag, inventory);
        setTamed(TAMED.readBooleanFromCompoundTag(compoundTag));
        setAnger(ANGER.readIntFromCompoundTag(compoundTag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, inventory);
        TAMED.writeBooleanToCompoundTag(compoundTag, isTamed());
        ANGER.writeIntToCompoundTag(compoundTag, getAnger());
    }

    @Override
    public boolean requiresCustomPersistence() {
        int q = 0;
        for (ItemStack is : this.inventory) {
            if (!is.isEmpty()) {
                ++q;
            }
        }
        return q < 5 || super.requiresCustomPersistence();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(6, new MoveTowardsRestrictionGoal(this, 0.5F));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0F, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));

        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

        this.setCombatTask();
        addBehaviorGoal();
    }

    public void setCombatTask() {
        this.goalSelector.addGoal(2, new PechEntityCombatGoal(this));
        this.goalSelector.addGoal(4, new PechEntityAvoidGoal<>(this, Player.class, 8.0F, 0.5F, 0.6));
        this.targetSelector.addGoal(2, new PechEntityTargetSelector<>(this, Player.class, true));
    }
//    {
//        this.tasks.removeTask(this.aiMeleeAttack);
//        this.tasks.removeTask(this.aiArrowAttack);
//        this.tasks.removeTask(this.aiBlastAttack);

//        ItemStack itemstack = this.getHeldItem();
//        if (itemstack != null && itemstack.getItem() == Items.bow) {
//            this.tasks.addTask(2, this.aiArrowAttack);
//        } else if (itemstack != null && itemstack.getItem() == ConfigItems.WandCastingItem) {
//            this.tasks.addTask(2, this.aiBlastAttack);
//        } else {
//            this.tasks.addTask(2, this.aiMeleeAttack);
//        }
//
//        if (this.isTamed()) {
//            this.tasks.removeTask(this.aiAvoidPlayer);
//        } else {
//            this.tasks.addTask(4, this.aiAvoidPlayer);
//        }
//    }

    protected void addBehaviorGoal() {
        this.goalSelector.addGoal(2, new PechTradePlayerGoal(this));
        this.goalSelector.addGoal(3, new PechGotoItemEntityGoal(this));
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
        super.dropCustomDeathLoot(damageSource, i, bl);
        var level = level();
        var pos = position();
        for (ItemStack stack : inventory) {
            if (random.nextFloat() < 0.88F) {
                dropItemStack(level, pos.x, pos.y, pos.z, stack);
            }
        }

        var primalAspectArray = Aspects.getPrimalAspects()
                .toArray(new Aspect[0]);
        for (int tries = 0; tries < 1 + i; tries++) {
            if (random.nextBoolean()) {
                ItemStack is = MANA_BEAN().ofAspect(primalAspectArray[random.nextInt(primalAspectArray.length)]);
                this.spawnAtLocation(is, 1.5F);
            }
        }
        if (random.nextInt(10) < 1 + i) {
            this.spawnAtLocation(new ItemStack(ThaumcraftItemInstances.GOLD_COIN()), 1.5F);
        }
        if (random.nextInt(400) < (1 + i)) {
            this.spawnAtLocation(new ItemStack(ThaumcraftItemInstances.KNOWLEDGE_FRAGMENT()), 1.5F);
        }

    }

    @Override
    public void aiStep() {
        super.aiStep();
//        if (this.updateAINextTick) {
//            this.updateAINextTick = false;
//            this.setCombatTask();
//        }
        if (this.tickCount % 40 == 0 && !this.level().isClientSide) {
            this.heal(1);
        }
    }

    protected void becomeAngryAt(LivingEntity par1Entity) {
        if (this.getAnger() <= 0) {
            this.level()
                    .broadcastEntityEvent(this, (byte) 19);
            this.playSound(ThaumcraftSounds.PECH_CHARGE, this.getSoundVolume(), this.getVoicePitch());
        }

        this.setTarget(par1Entity);
        this.setAnger(400 + this.random.nextInt(400));
        this.setTamed(false);
        this.updateAINextTick = true;
    }

    @Override
    public boolean hurt(DamageSource damSource, float par2) {
        var result = super.hurt(damSource, par2);

        Entity entity = damSource.getEntity();
        if (entity instanceof LivingEntity living) {
            this.level()
                    .getEntities(
                            PECH_TEST, this.getBoundingBox()
                                    .inflate(32.0F, 16.0F, 32.0F),
                            _ignored -> true
                    )
                    .forEach(
                            e -> e.becomeAngryAt(living)
                    );
        }

        return result;
    }

    @Override
    public void tick() {

        if (this.mumble > 0.0F) {
            this.mumble *= 0.75F;
        }

        if (this.getAnger() > 0) {
            this.setAnger(this.getAnger() - 1);
        }

        if (level().isClientSide) {
            if (this.random.nextInt(15) == 0 && this.getAnger() > 0) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.ANGRY_VILLAGER,
                                this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
                                this.getY() + (double) 0.5F + (double) (this.random.nextFloat() * this.getBbHeight()),
                                this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
                                d0, d1, d2
                        );
            }

            if (this.random.nextInt(25) == 0 && this.isTamed()) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.HAPPY_VILLAGER,
                                this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
                                this.getY() + (double) 0.5F + (double) (this.random.nextFloat() * this.getBbHeight()),
                                this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
                                d0, d1, d2
                        );
            }
        }

        super.tick();
    }

    public static class PechEntityAvoidGoal<L extends LivingEntity> extends AvoidEntityGoal<L> {
        protected final AbstractPechEntity pech;

        public PechEntityAvoidGoal(AbstractPechEntity pathfinderMob, Class<L> class_, float f, double d, double e) {
            super(pathfinderMob, class_, f, d, e);
            this.pech = pathfinderMob;
        }

        public PechEntityAvoidGoal(AbstractPechEntity pathfinderMob, Class<L> class_, Predicate<LivingEntity> predicate, float f, double d, double e, Predicate<LivingEntity> predicate2) {
            super(pathfinderMob, class_, predicate, f, d, e, predicate2);
            this.pech = pathfinderMob;
        }

        public PechEntityAvoidGoal(AbstractPechEntity pathfinderMob, Class<L> class_, float f, double d, double e, Predicate<LivingEntity> predicate) {
            super(pathfinderMob, class_, f, d, e, predicate);
            this.pech = pathfinderMob;
        }

        @Override
        public boolean canUse() {
            return !pech.isTamed() && super.canUse();
        }
    }

    public static class PechEntityTargetSelector<P extends Player> extends NearestAttackableTargetGoal<P> {
        protected final AbstractPechEntity pech;

        public PechEntityTargetSelector(AbstractPechEntity mob, Class<P> class_, boolean bl) {
            super(mob, class_, bl);
            this.pech = mob;
        }

        public PechEntityTargetSelector(AbstractPechEntity mob, Class<P> class_, boolean bl, Predicate<LivingEntity> predicate) {
            super(mob, class_, bl, predicate);
            this.pech = mob;
        }

        public PechEntityTargetSelector(AbstractPechEntity mob, Class<P> class_, boolean bl, boolean bl2) {
            super(mob, class_, bl, bl2);
            this.pech = mob;
        }

        public PechEntityTargetSelector(AbstractPechEntity mob, Class<P> class_, int i, boolean bl, boolean bl2, @Nullable Predicate<LivingEntity> predicate) {
            super(mob, class_, i, bl, bl2, predicate);
            this.pech = mob;
        }

        @Override
        public boolean canUse() {
            return pech.getAnger() > 0 && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            var result = super.canContinueToUse();
            if (result && pech.getAnger() > 0) {

                if (pech.chargecount > 0) {
                    --pech.chargecount;
                }

                if (pech.chargecount == 0) {
                    pech.chargecount = 100;
                    pech.playSound(ThaumcraftSounds.PECH_CHARGE, pech.getSoundVolume(), pech.getVoicePitch());
                }

                pech.level()
                        .broadcastEntityEvent(pech, (byte) 17);
            }
            return result;
        }
    }

    public static class PechEntityCombatGoal extends WrappedChangeableGoal {
        protected final AbstractPechEntity pech;
        protected @NotNull RangedAttackGoal rangedAttackGoal;
        protected @NotNull MeleeAttackGoal meleeAttackGoal;

        public PechEntityCombatGoal(AbstractPechEntity pech) {
            super();
            this.pech = pech;
            rangedAttackGoal = new RangedAttackGoal(pech, 0.6, 20, 50, 15.0F);
            meleeAttackGoal = new ZombieLikeAttackGoal(pech/*, LivingEntity.class*/, 0.6, false);
        }

        protected boolean projectileWeaponItemCanUse(ItemStack stack, ProjectileWeaponItem projectileWeaponItem) {
            if (!projectileWeaponItemTypeSupported(stack, projectileWeaponItem)) {
                return false;
            }
            if (projectileWeaponItem instanceof CrossbowItem && CrossbowItem.isCharged(stack)) {
                return true;
            }
            var predicate = projectileWeaponItem.getAllSupportedProjectiles();
            return predicate.test(pech.getMainHandItem()) || predicate.test(pech.getOffhandItem());
        }

        protected boolean projectileWeaponItemTypeSupported(ItemStack stack, ProjectileWeaponItem projectileWeaponItem) {
            return projectileWeaponItem instanceof BowItem || projectileWeaponItem instanceof CrossbowItem;
        }

        protected @Nullable Goal goalByFocus(ItemStack wandStack, IWandFocusEngineItem focusEngineItem) {
            var focusStack = focusEngineItem.getFocusItemStack(wandStack);
            if (focusStack.getItem() == PECH_FOCUS()) {
                return rangedAttackGoal;
            }
//            if (focusStack.is(RANGED_FOCUS_FOR_AI)){
//                if (focusStack.getItem() instanceof IWandFocusItem<?> focusItem){
//                    var upgrades = focusItem.getAppliedFocusUpgrades(focusStack);
//                    var cost = (CentiVisList<Aspect>)focusItem.getCentiVisCost(focusStack,upgrades);
//                    if (cost.isEmpty()){
//                        return rangedAttackGoal;
//                    }
//                    if (!(focusEngineItem instanceof ICentiVisContainerItem<?> centiVisContainerItem)){
//                        return null;
//                    }
//                    ((ICentiVisContainerItem<Aspect>)centiVisContainerItem).consumeAllCentiVis(
//                            wandStack,pech,cost,false, CONSUMPTION_FOCUS, true
//                    );
//                }
//                return rangedAttackGoal;
//            }
            return null;
        }

        protected Goal pickGoal() {
            var mainHandItemStack = pech.getMainHandItem();
            var mainHandItem = mainHandItemStack.getItem();
            var offhandItemStack = pech.getOffhandItem();
            var offhandItem = offhandItemStack.getItem();

            if (mainHandItem instanceof IWandFocusEngineItem focusEngineItem) {
                var goalByFocus = goalByFocus(mainHandItemStack, focusEngineItem);
                if (goalByFocus != null) {
                    return goalByFocus;
                }
            }
            if (offhandItem instanceof IWandFocusEngineItem focusEngineItem) {
                var goalByFocus = goalByFocus(offhandItemStack, focusEngineItem);
                if (goalByFocus != null) {
                    return goalByFocus;
                }
            }
            if (mainHandItem instanceof ProjectileWeaponItem projectileWeaponItem
                    && projectileWeaponItemCanUse(mainHandItemStack, projectileWeaponItem)) {
                return rangedAttackGoal;
            }
            if (offhandItem instanceof ProjectileWeaponItem projectileWeaponItem
                    && mainHandItemStack.isEmpty()
                    && projectileWeaponItemCanUse(offhandItemStack, projectileWeaponItem)) {
                return rangedAttackGoal;
            }
            return meleeAttackGoal;
        }
    }

    public boolean canPickUpItemEntity(ItemEntity itemEntity) {
        if (itemEntity == null) {
            return false;
        } else {
            var stack = itemEntity.getItem();
            if (!this.isTamed()
                    && PechTradeManager.getItemStackValuePerCount(stack) != 0
            ) {
                return true;
            } else {
                for (var inventoryStack : this.inventory) {
                    if (inventoryStack.isEmpty()) {
                        return true;
                    }
                    if (ItemStack.isSameItemSameTags(stack, inventoryStack)
                            && stack.getCount() + inventoryStack.getCount() <= inventoryStack.getMaxStackSize()) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public abstract PechTradeManager.PechTradeOptionProvider getTradeOptions();

    public void pickUpItemEntity(ItemEntity itemEntity) {
        var stack = itemEntity.getItem();
        int am = stack.getCount();
        int value = PechTradeManager.getItemStackValuePerCount(stack) * stack.getCount();
        ItemStack is = this.pickupItemStack(stack);
        if (isTamed()) {
            getTradeOptions().generateOutputForValue(this, value)
                    .forEach(
                            outStack -> {
                                var newItemEntity = this.spawnAtLocation(outStack);
                                if (newItemEntity != null) {
                                    newItemEntity.setThrower(AbstractPechEntity.this.getUUID());
                                }
                            }
                    );
        }
        itemEntity.setItem(is);
        if (is.isEmpty()) {
            itemEntity.discard();
        }

        if (is.getCount() != am) {
            this.playSound(
                    SoundEvents.ITEM_PICKUP,
                    0.2F,
                    random.nextFloat() * 2.8F + 0.6F
            );
        }
    }

    //return remaining
    public @NotNull ItemStack pickupItemStack(@NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return stack;
        } else {
            int stackValuePerCount = PechTradeManager.getItemStackValuePerCount(stack);
            if (!this.isTamed() && stackValuePerCount > 0) {
                if (this.random.nextInt(10) < stackValuePerCount) {
                    this.setTamed(true);
                    this.updateAINextTick = true;
                    this.level()
                            .broadcastEntityEvent(this, (byte) 18);
                }

                stack.shrink(1);
                return stack.getCount() <= 0 ? ItemStack.EMPTY : stack;
            } else {
                //add to inventory
                //add on stack
                for (ItemStack stackInSlot : this.inventory) {
                    if (!stack.isEmpty()
                            && stack.getCount() > 0
                            && !stackInSlot.isEmpty()
                            && stackInSlot.getCount() < stackInSlot.getMaxStackSize()
                            && ItemStack.isSameItemSameTags(stack, stackInSlot)) {
                        if (stack.getCount() + stackInSlot.getCount() <= stackInSlot.getMaxStackSize()) {
                            stackInSlot.setCount(stackInSlot.getCount() + stack.getCount());
                            return ItemStack.EMPTY;
                        }

                        int canAdd = Math.min(stack.getCount(), stackInSlot.getMaxStackSize() - stackInSlot.getCount());
                        stackInSlot.setCount(canAdd + stackInSlot.getCount());
                        stack.shrink(canAdd);
                    }
                }
                for (int a = 0; a < this.inventory.size(); ++a) {
                    var stackInSlot = this.inventory.get(a);
                    if (!stack.isEmpty() && stackInSlot.isEmpty()) {
                        this.inventory.set(a, stack);
                        return ItemStack.EMPTY;
                    }
                }

                return stack;
            }
        }
    }

    public static class PechTradeManager {
        //ignores item count
        public static final Object2IntMap<Item> SIMPLE_ITEM_VALUE_MAP = new Object2IntOpenHashMap<>();

        static {
            init();
        }

        //mixin point
        private static void init() {
            SIMPLE_ITEM_VALUE_MAP.put(MANA_BEAN(), 1);
            SIMPLE_ITEM_VALUE_MAP.put(Items.GOLD_INGOT, 2);
            SIMPLE_ITEM_VALUE_MAP.put(Items.GOLDEN_APPLE, 2);
            SIMPLE_ITEM_VALUE_MAP.put(Items.ENDER_PEARL, 3);
            SIMPLE_ITEM_VALUE_MAP.put(Items.DIAMOND, 4);
            SIMPLE_ITEM_VALUE_MAP.put(Items.EMERALD, 5);
            SIMPLE_ITEM_VALUE_MAP.put(Items.ENCHANTED_GOLDEN_APPLE, 10);
        }

        @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
        public static int getItemStackValuePerCount(ItemStack stack) {
            int mapResult = SIMPLE_ITEM_VALUE_MAP.getInt(stack.getItem());
            if (mapResult != 0) {
                return mapResult;
            }
            var aspects = ItemBonusAspectCalculator.getBonusAspects(stack, getBasicAspectsServer(stack.getItem()));
            return Math.min(32, aspects.getOrDefault(Aspects.GREED, 0));
        }

        public abstract static class PechTradeOptionProvider {
            public abstract List<ItemStack> generateOutputForValue(AbstractPechEntity pech, int value);
        }

        public static class PechTradeOptionProviderExample extends PechTradeOptionProvider {
            public final AutoSortThreadSafeList<ObjectIntPair<List<Supplier<ItemStack>>>> TRADE_OPTIONS_BY_VALUE
                    = new AutoSortThreadSafeList<>();

            @Override
            public List<ItemStack> generateOutputForValue(AbstractPechEntity pech, int value) {
                List<ItemStack> result = new ArrayList<>();
                var random = pech.random;
                while (value > 0) {
                    int currentUsedAmount = Math.min(
                            TRADE_OPTIONS_BY_VALUE.getLast()
                                    .rightInt(), Math.max((value + 1) / 2, random.nextInt(value) + 1)
                    );
                    if (currentUsedAmount <= 0) {
                        break;
                    }
                    if (currentUsedAmount == 1 && pickFromInventory(pech, result)) {
                        continue;
                    }
                    if (currentUsedAmount >= TRADE_OPTIONS_BY_VALUE.getLast()
                            .rightInt() - 1 && pech.random.nextBoolean()) {
                        if (!pickFromChestLoot(pech, result)) {
                            value += currentUsedAmount;
                        }
                        continue;
                    }
                    pickFromTradeTable(pech, result, currentUsedAmount);

                }
                return result;
            }

            protected boolean pickFromInventory(AbstractPechEntity pech, List<ItemStack> result) {
                var random = pech.random;
                if (random.nextBoolean() && pech.hasStuffInPack()) {
                    IntArrayList lootSlots = new IntArrayList(pech.inventory.size());
                    for (int slotIndex = 0; slotIndex < pech.inventory.size(); slotIndex++) {
                        var stackInSlot = pech.inventory.get(slotIndex);
                        if (!stackInSlot.isEmpty()) {
                            lootSlots.add(slotIndex);
                        }
                    }

                    int pickedSlot = lootSlots.getInt(pech.random.nextInt(lootSlots.size()));
                    var pickedStack = pech.inventory.get(pickedSlot);
                    pickedStack = pickedStack.split(pickedStack.getCount());
                    result.add(pickedStack);
                    return true;
                }
                return false;
            }

            protected LootTable getLootTable(Level level) {
                return level.getServer()
                        .getLootData()
                        .getLootTable(ResourceLocation.tryBuild("minecraft", "simple_dungeon"));
            }

            protected boolean pickFromChestLoot(AbstractPechEntity pech, List<ItemStack> result) {
                LootTable lootTable = getLootTable(pech.level());
                var builder = (new LootParams.Builder((ServerLevel) pech.level()))
                        .withParameter(LootContextParams.ORIGIN, pech.position());
                var toPick = lootTable.getRandomItems(builder.create(LootContextParamSets.CHEST));
                if (!toPick.isEmpty()) {
                    for (var pickedStack : toPick) {
                        if (!pickedStack.isEmpty()) {
                            result.add(pickedStack);
                            return true;
                        }
                    }
                }
                return false;
            }

            protected void pickFromTradeTable(AbstractPechEntity pech, List<ItemStack> result, int currentUsedAmount) {
                while (currentUsedAmount > 0) {
                    ObjectIntPair<List<Supplier<ItemStack>>> tradeOptionsAndValueCurrent = null;
                    for (int tradeOptionsReversedIndex = TRADE_OPTIONS_BY_VALUE.size() - 1; tradeOptionsReversedIndex >= 0; tradeOptionsReversedIndex--) {
                        tradeOptionsAndValueCurrent = TRADE_OPTIONS_BY_VALUE.get(tradeOptionsReversedIndex);
                        if (currentUsedAmount < tradeOptionsAndValueCurrent.rightInt()) {
                            continue;
                        }
                    }
                    if (tradeOptionsAndValueCurrent == null) {
                        break;
                    }
                    var options = tradeOptionsAndValueCurrent.left();
                    if (options.size() > 1) {
                        result.add(options.get(pech.random.nextInt(options.size())).get());
                    } else if (!options.isEmpty()) {
                        result.add(options.getFirst().get());
                    }
                    currentUsedAmount -= tradeOptionsAndValueCurrent.rightInt();
                }
            }
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    protected boolean hasStuffInPack() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ThaumcraftSounds.PECH_IDLE;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(DamageSource damageSource) {
        return ThaumcraftSounds.PECH_HIT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ThaumcraftSounds.PECH_DEATH;
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return this.getAnger() <= 0 ? null : super.getTarget();
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float f) {
        //TODO:depends on item in hand

        for (var hand : InteractionHand.values()) {
            if (performRangedAttackWithHand(hand, livingEntity, f)) {
                this.swing(hand);
                return;
            }
        }
    }

    protected boolean performRangedAttackWithHand(InteractionHand interactionHand, LivingEntity victim, float f) {
        var stack = getItemInHand(interactionHand);
        var item = stack.getItem();
        if (item instanceof BowItem bowItem) {
            {
                @StoleFrom("net.minecraft.world.entity.monster.AbstractSkeleton#performRangedAttack()")
                ItemStack itemStack = this.getProjectile(stack);
                AbstractArrow abstractArrow = ProjectileUtil.getMobArrow(this, itemStack, f);
                double d = victim.getX() - this.getX();
                double e = victim.getY(0.3333333333333333) - abstractArrow.getY();
                double g = victim.getZ() - this.getZ();
                double h = Math.sqrt(d * d + g * g);
                abstractArrow.shoot(
                        d, e + h * 0.2F, g, 1.6F, 14 - this.level()
                                .getDifficulty()
                                .getId() * 4
                );
                this.playSound(
                        SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom()
                                .nextFloat() * 0.4F + 0.8F)
                );
                this.level()
                        .addFreshEntity(abstractArrow);
            }
            return true;
        } else if (item instanceof IWandFocusEngineItem focusEngineItem
                && focusEngineItem.getFocusItemStack(stack)
                .getItem() == PECH_FOCUS()) {
            var blast = new PechBlastEntity(this, this.level());
            var victimMovement = victim.getDeltaMovement();
            double d0 = victim.getX() + victimMovement.x - this.getX();
            double d1 = victim.getY() + (double) victim.getEyeHeight() - 1.500000023841858 - this.getY();
            double d2 = victim.getZ() + victimMovement.z - this.getZ();
            float f1 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
            blast.shoot(d0, d1 + (double) (f1 * 0.1F), d2, 1.5F, 4.0F);
            this.playSound(ThaumcraftSounds.ICE, 0.4F, 1.0F + this.random.nextFloat() * 0.1F);
            this.level()
                    .addFreshEntity(blast);
            return true;
        }

        return false;
    }

    @Override
    public void playAmbientSound() {
        if (!level().isClientSide()) {
            if (this.random.nextInt(3) == 0) {
                this.level()
                        .getEntities(
                                PECH_TEST, this.getBoundingBox()
                                        .inflate(4.0F, 2.0F, 4.0F), p -> p != this
                        )
                        .forEach(
                                p -> {
                                    this.level()
                                            .broadcastEntityEvent(p, (byte) 17);
                                    this.playSound(
                                            ThaumcraftSounds.PECH_TRADE, this.getSoundVolume(), this.getVoicePitch());
                                }
                        );
            }

            this.level()
                    .broadcastEntityEvent(this, (byte) 16);
        }

        super.playAmbientSound();
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 16) {
            this.mumble = (float) Math.PI;
        } else if (b == 17) {
            this.mumble = ((float) Math.PI * 2F);
        } else if (b == 18) {
            for (int i = 0; i < 5; ++i) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.HAPPY_VILLAGER,
                                this.getX() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
                                this.getY() + 0.5F + (this.random.nextFloat() * this.getBbHeight()),
                                this.getZ() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
                                d0, d1, d2
                        );
            }
        }

        if (b == 19) {
            for (int i = 0; i < 5; ++i) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.ANGRY_VILLAGER,
                                this.getX() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
                                this.getY() + 0.5F + (this.random.nextFloat() * this.getBbHeight()),
                                this.getZ() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
                                d0, d1, d2
                        );
            }

            this.mumble = ((float) Math.PI * 2F);
        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return getBbHeight() * 0.66F;
    }

    @Override
    public boolean mayInteract(Level level, BlockPos blockPos) {
        return super.mayInteract(level, blockPos);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        var usingStack = player.getItemInHand(interactionHand);
        //TODO:[maybe wont finished]:item tag to bypass this interaction
        if (!player.isShiftKeyDown() && (usingStack.isEmpty() || !(usingStack.getItem() instanceof NameTagItem nameTagItem))) {
            if (this.isTamed()) {
                if (!level().isClientSide) {
                    player.openGui(Thaumcraft.instance, 1, this.level(), this.getEntityId(), 0, 0);
                }
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
        }
        return super.interact(player, interactionHand);
    }

    //TODO:Types of peches and their trade tabs


    protected static WeightedRandomCollection<TriConsumer<AbstractPechEntity,RandomSource,DifficultyInstance>>
    EQUIPMENT_POSSIBLE = new WeightedRandomCollection<>();
    static {
        initEquipments();
    }
    private static void initEquipments() {
//        EQUIPMENT_POSSIBLE.add(
//                ((pech, random, difficultyInstance) -> {
//                    var wandItem = WAND_CASTING();
//                    ItemStack wand = WAND_CASTING().getDefaultInstance();
//                    ItemStack focus = PECH_FOCUS().getDefaultInstance();
//                    wandItem.changeFocusItemStack(wand, focus);
//                    wandItem.addCentiVis(wand, Aspects.EARTH, 2 + random.nextInt(6), true);
//                    wandItem.addCentiVis(wand, Aspects.ENTROPY, 2 + random.nextInt(6), true);
//                    wandItem.addCentiVis(wand, Aspects.WATER, 2 + random.nextInt(6), true);
//                    wandItem.addCentiVis(wand, Aspects.AIR, random.nextInt(4), true);
//                    wandItem.addCentiVis(wand, Aspects.FIRE, random.nextInt(4), true);
//                    wandItem.addCentiVis(wand, Aspects.ORDER, random.nextInt(4), true);
//                    pech.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, wand);
//                }),2
//        );

//        EQUIPMENT_POSSIBLE.add(
//                ((pech, randomSource, difficultyInstance) -> {
//                    pech.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
//                }),5
//        );
        EQUIPMENT_POSSIBLE.add(
                ((pech, randomSource, difficultyInstance) -> {
                    pech.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }),1
        );
        EQUIPMENT_POSSIBLE.add(
                ((pech, randomSource, difficultyInstance) -> {
                    pech.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
                }),1
        );
        EQUIPMENT_POSSIBLE.add(
                ((pech, randomSource, difficultyInstance) -> {
                    pech.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
                }),1
        );
        EQUIPMENT_POSSIBLE.add(
                ((pech, randomSource, difficultyInstance) -> {
                    pech.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                }),1
        );
        EQUIPMENT_POSSIBLE.add(
                ((pech, randomSource, difficultyInstance) -> {
                    pech.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
                }),1
        );
        EQUIPMENT_POSSIBLE.add(
                ((pech, randomSource, difficultyInstance) -> {
                    pech.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
                }),1
        );
        EQUIPMENT_POSSIBLE.add(
                ((pech, randomSource, difficultyInstance) -> {
                    pech.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
                }),1
        );
    }
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
        populatePechSpecificEquip(randomSource, difficultyInstance);
        this.setCanPickUpLoot(
                this.random.nextFloat()
                        < 0.75F * level().getCurrentDifficultyAt(blockPosition()).getSpecialMultiplier()
                /*this.level().func_147462_b(this.posX, this.posY, this.posZ)*/
        );
    }

    protected void populatePechSpecificEquip(RandomSource randomSource, DifficultyInstance difficultyInstance){
        EQUIPMENT_POSSIBLE.getRandom(randomSource).accept(this,randomSource,difficultyInstance);
    }
}
