package thaumcraft.common.entities.golems;

import com.linearity.opentc4.utils.ClassToItemStackPairMap;
import com.linearity.opentc4.utils.collectionlike.AutoSortThreadSafeList;
import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.*;
import thaumcraft.common.items.golem.upgrade.abstracts.*;

import java.util.*;

public abstract class BaseGolemEntity extends AbstractGolemUpgradeApplicableEntity<BaseGolemEntity.IGolemUpgradeItem> implements RangedAttackMob {

    public BaseGolemEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }


    public static List<ObjectIntPair<Class<? extends IGolemModifierItem>>> golemUpgradeItemTypes = new AutoSortThreadSafeList<>();
    static {
        golemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemCoreItem.class,0));
        golemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemDecorationItem.class,100));
        golemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemBasicUpgradeItem.class,200));
        golemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemAIProviderItem.class, 300));
        golemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemRangedAttackPerformerItem.class, 400));
        golemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemInventoryProviderItem.class, 500));
        golemUpgradeItemTypes.add(new ObjectIntPair<>(IGolemCarryItemPowerProviderItem.class, 600));
    }
    private final ClassToItemStackPairMap golemUpgradesCache = new ClassToItemStackPairMap();
    protected final ClassToItemStackPairMap.View golemUpgradesCacheView = new ClassToItemStackPairMap.View(golemUpgradesCache);

    public static class GolemAbibitiesData {
        public int carryPower = 0;
        public int inventorySize = 0;//TODO:Serialization
    }
    private final GolemAbibitiesData abilitiesData = new GolemAbibitiesData();
    public int getCarryPower(){
        return this.abilitiesData.carryPower;
    }
    public NonNullList<ItemStack> inventory = NonNullList.withSize(0,ItemStack.EMPTY);//TODO:Serialization
    public ByteArrayList inventoryColors = new ByteArrayList();//TODO:Serialization

    protected void refreshAbilities(){
        refreshGoals();
        reconstructInventory();
        recalculateCarryPower();
    }
    protected void reconstructInventory(){
        int inventorySizePrev = this.abilitiesData.inventorySize;
        this.abilitiesData.inventorySize = 0;
        for (var provider:golemUpgradesCacheView.get(IGolemInventoryProviderItem.class)){
            this.abilitiesData.inventorySize += provider.a().getInventorySizeCanProvide(this,provider.b());
        }
        if (inventorySizePrev != this.abilitiesData.inventorySize){
            var level = level();
            var pos = position();
            var oldInventory = this.inventory;
            var oldInventoryColors = this.inventoryColors;
            var newInventory = NonNullList.withSize(this.abilitiesData.inventorySize,ItemStack.EMPTY);
            var newInventoryColors = new ByteArrayList(this.abilitiesData.inventorySize);
            for (int i=0;i<oldInventory.size();i++){
                var stack = oldInventory.get(i);
                oldInventory.set(i,ItemStack.EMPTY);
                if (i > newInventory.size()){
                    Containers.dropItemStack(level,pos.x,pos.y,pos.z,stack);
                }else {
                    newInventory.add(i,stack);
                    newInventoryColors.set(i,oldInventoryColors.getByte(i));
                }
            }
            for (var i=oldInventory.size();i<newInventory.size();i++){
                newInventoryColors.set(i,(byte)0);
            }
            this.inventory = newInventory;
            this.inventoryColors = newInventoryColors;
        }
    }
    protected void recalculateCarryPower(){
        abilitiesData.carryPower = 0;
        for (var upgradePair:golemUpgradesCacheView.get(IGolemCarryItemPowerProviderItem.class)){
            upgradePair.a().getCarryCountCanProvide(this,upgradePair.b());
        }
    }

    @Override
    public void setUpgradeStacks(List<ItemStack> stacks) {
        super.setUpgradeStacks(stacks);
        for (var stack:stacks){
            var item = stack.getItem();
            for (var clzPair:golemUpgradeItemTypes){
                var clz = clzPair.first();
                if (clz.isInstance(item)){
                    golemUpgradesCache.checkAndPut(clz,item,stack);
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
            listener.onUpgradeInstalled(this,usingUpgradeStack);
        }
    }

    public static AttributeSupplier.Builder createGolemAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE,1)
                .add(Attributes.MOVEMENT_SPEED,0.6)
                .add(Attributes.FOLLOW_RANGE,32);
    }

    @Override
    protected void registerGoals() {
        refreshGoals();
    }
    public void refreshGoals() {
        this.goalSelector.removeAllGoals(_ignored -> true);
        this.targetSelector.removeAllGoals(_ignored -> true);
        for (var providerPair:golemUpgradesCacheView.get(IGolemAIProviderItem.class)){
            providerPair.a().registerGoalForGolem(providerPair.b(),this,this.goalSelector,this.targetSelector);
        }
    }

    @Override
    public Class<IGolemUpgradeItem> getUpgradeClass() {
        return IGolemUpgradeItem.class;
    }

    public interface IGolemUpgradeItem extends IAbstractGolemUpgradeItem, IGolemModifierItem{
        @Override
        default boolean golemUpgrade$isApplicableTo(ItemStack stack, AbstractGolemUpgradeApplicableEntity<?> golem){
            return golem instanceof BaseGolemEntity;
        }
    }


    @Override
    public void performRangedAttack(LivingEntity target, float f) {
        for (var performerPair:golemUpgradesCacheView.get(IGolemRangedAttackPerformerItem.class)){
            performerPair.a().performRangedAttackForGolem(this,performerPair.b(), target, f);
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
        if (!super.canInstallUpgrade(stack)){
            return false;
        }
        var item = stack.getItem();
        if (golemCoreFullFor(item) || basicUpgradeFullFor(item) || decorationItemFullFor(item)){
            return false;
        }
        return true;
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
}
