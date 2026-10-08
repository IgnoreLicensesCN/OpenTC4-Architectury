package thaumcraft.common.entities.golems;

import com.linearity.opentc4.datautils.EntityDataSerializerAdditions;
import com.linearity.opentc4.utils.collectionlike.SimplePair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.linearity.opentc4.Consts.AbstractGolemUpgradeApplicableEntityTagAccessors.OWNER;
import static com.linearity.opentc4.Consts.AbstractGolemUpgradeApplicableEntityTagAccessors.UPGRADES;

public abstract class AbstractGolemUpgradeApplicableEntity<
        UpgradeItem extends AbstractGolemUpgradeApplicableEntity.IAbstractGolemUpgradeItem<UpgradeItem,UpgradeGolem>,
        UpgradeGolem extends AbstractGolemUpgradeApplicableEntity<UpgradeItem,UpgradeGolem>
        > extends Mob implements OwnableEntity {
    private static final EntityDataAccessor<Optional<UUID>> DATA_ID_OWNER = SynchedEntityData.defineId(AbstractGolemUpgradeApplicableEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<List<ItemStack>> DATA_ID_UPGRADES = SynchedEntityData.defineId(AbstractGolemUpgradeApplicableEntity.class, EntityDataSerializerAdditions.ITEM_STACK_NON_NULL_LIST);
    protected AbstractGolemUpgradeApplicableEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_OWNER, Optional.empty());
        this.entityData.define(DATA_ID_UPGRADES, List.of());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setUpgradeStacks(UPGRADES.readFromCompoundTag(compoundTag));
        setOwnerUUID(OWNER.readFromCompoundTag(compoundTag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        UPGRADES.writeToCompoundTag(compoundTag,getUpgradeStacks());
        var ownerUUID = getOwnerUUID();
        OWNER.writeToCompoundTag(compoundTag,ownerUUID == null ? this.uuid : ownerUUID);

    }
    @Override
    public @Nullable UUID getOwnerUUID(){
        return this.entityData.get(DATA_ID_OWNER).orElse(null);
    }
    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_ID_OWNER, Optional.ofNullable(uuid));
    }

    public int getGolemUpgradeCapacity(){
        return 1;
    }
    public List<ItemStack> getUpgradeStacks(){
        return this.entityData.get(DATA_ID_UPGRADES);
    }

    public void setUpgradeStacks(List<ItemStack> stack) {
        this.entityData.set(DATA_ID_UPGRADES, stack);
        List<SimplePair<UpgradeItem,ItemStack>> cache = new ArrayList<>(stack.size());
        var upgradeClass = getUpgradeClass();
        for (var upgradeStack:stack) {
            var item = upgradeStack.getItem();
            if (upgradeClass.isInstance(item)) {
                var upgrade = upgradeClass.cast(item);
                cache.add(new SimplePair<>(upgradeClass.cast(item),upgradeStack));
                var attrMap = upgrade.getAttributes(upgradeStack, (UpgradeGolem) this);
                for (var attributeToApplyPair:attrMap.entrySet()){
                    var attributeToApply = attributeToApplyPair.getKey();
                    for (var attributeModifier:attributeToApplyPair.getValue()){
                        var attributeInstance = getAttribute(attributeToApply);
                        if (attributeInstance != null){
                            attributeInstance.addPermanentModifier(attributeModifier);
                        }
                    }
                }
            }
        }

        upgradesCache = cache;
        upgradesCacheView = Collections.unmodifiableList(upgradesCache);
    }

    private List<SimplePair<UpgradeItem,ItemStack>> upgradesCache = List.of();
    protected List<SimplePair<UpgradeItem,ItemStack>> upgradesCacheView = Collections.unmodifiableList(upgradesCache);

    public interface IAbstractGolemUpgradeItem<
            ItemClass extends IAbstractGolemUpgradeItem<ItemClass,GolemClass>,
            GolemClass extends AbstractGolemUpgradeApplicableEntity<ItemClass,GolemClass>
            > {

        boolean golemUpgrade$isApplicableTo(ItemStack stack,AbstractGolemUpgradeApplicableEntity<?,?> golem);

        default Map<Attribute,Collection<AttributeModifier>> getAttributes(ItemStack upgradeStack, GolemClass golem) {
            return Collections.emptyMap();
        }
    }

    public abstract Class<UpgradeItem> getUpgradeClass();

    public boolean isOwner(@Nullable UUID uuidToCheck) {
        var ownerUUID = getOwnerUUID();
        if (getOwnerUUID() == this.uuid || getOwnerUUID() == null){
            return true;
        }
        return uuidToCheck == ownerUUID;
    }

    protected boolean tryInstallUpgradeFromStack(ItemStack stack) {
        if (stack.isEmpty()){return false;}
        var item = stack.getItem();
        var upgradeClass = getUpgradeClass();
        if (canInstallUpgrade(stack) && upgradeClass.isInstance(item)) {
            stack = stack.split(1);
            installUpgrade(upgradeClass.cast(item),stack);
            return true;
        }
        return false;
    }

    protected boolean canInstallUpgrade(ItemStack stack) {
        if (getGolemUpgradeCapacity() <= getUpgradeStacks().size()){
            return false;
        }
        if (stack.isEmpty()){return false;}
        var item = stack.getItem();
        var upgradeClass = getUpgradeClass();
        return upgradeClass.isInstance(item);
    }

    protected void installUpgrade(UpgradeItem installedUpgrade, ItemStack usingStack) {
        var usingUpgradeStack = usingStack.split(1);
        onAddedUpgradeStack(installedUpgrade,usingUpgradeStack);
    }

    protected void onAddedUpgradeStack(UpgradeItem installedUpgrade, ItemStack usingUpgradeStack) {
        var stacks = getUpgradeStacks();
        stacks.add(usingUpgradeStack);
        setUpgradeStacks(stacks);
    }


}
