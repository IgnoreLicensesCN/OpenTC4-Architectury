package thaumcraft.common.entities.golems;

import com.linearity.opentc4.utils.ClassToItemStackPairMap;
import com.linearity.opentc4.utils.collectionlike.SimplePair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.*;
import thaumcraft.common.items.golem.upgrade.abstracts.*;

import java.util.*;

public abstract class BaseGolemEntity extends AbstractGolemUpgradeApplicableEntity<BaseGolemEntity.IGolemUpgradeItem> implements RangedAttackMob {

    public BaseGolemEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }


    public static List<Class<? extends IGolemModifierItem>> golemUpgradeItemTypes = new ArrayList<>();
    static {
//        golemUpgradeItemTypes.add(IGolemCoreItem.class);
//        golemUpgradeItemTypes.add(IGolemDecorationItem.class);
//        golemUpgradeItemTypes.add(IGolemBasicUpgradeItem.class);
        golemUpgradeItemTypes.add(IGolemAIProviderItem.class);
        golemUpgradeItemTypes.add(IGolemRangedAttackPerformerItem.class);
    }
    private final ClassToItemStackPairMap golemUpgradesCache = new ClassToItemStackPairMap();
    protected final ClassToItemStackPairMap.View golemUpgradesCacheView = new ClassToItemStackPairMap.View(golemUpgradesCache);

    @Override
    public void setUpgradeStacks(List<ItemStack> stack) {
        super.setUpgradeStacks(stack);
        golemUpgradesCache.clear();
        for (var upgradeStack:stack){
            var item = upgradeStack.getItem();
            for (var clz:golemUpgradeItemTypes){
                if (clz.isInstance(item)){
                    golemUpgradesCache.checkAndPut(clz,item,upgradeStack);
                }
            }
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
    protected void refreshGoals() {
        this.goalSelector.removeAllGoals(_ignored -> true);
        this.targetSelector.removeAllGoals(_ignored -> true);
        for (var providerPair:golemUpgradesCacheView.get(IGolemAIProviderItem.class)){
            providerPair.a().registerGoalForGolem(providerPair.b(),this,this.goalSelector,this.targetSelector);
        }
    }

    public ItemStack getGolemCoreStack() {
        var cores = golemUpgradesCacheView.get(IGolemCoreItem.class);
        if (cores.isEmpty()){
            return ItemStack.EMPTY;
        }
        return cores.getFirst().b();
    }
    public void setGolemCoreStack(ItemStack stack) {
        if (stack.getItem() instanceof IGolemCoreItem core){
            golemUpgradesCache.put(IGolemCoreItem.class,new SimplePair<>(core,stack));
            golemUpgradesCache.put(IGolemAIProviderItem.class,new SimplePair<>(core,stack));
            onGolemCoreInstalled(stack);
        }
    }

    public void onGolemCoreInstalled(ItemStack stack) {
        refreshGoals();
    }

    @Override
    public Class<IGolemUpgradeItem> getUpgradeClass() {
        return IGolemUpgradeItem.class;
    }

    public interface IGolemUpgradeItem extends IAbstractGolemUpgradeItem<IGolemUpgradeItem>, IGolemModifierItem{
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
        if (item instanceof IGolemCoreItem && !getGolemCoreStack().isEmpty()){
            return false;
        }
        if (item instanceof IGolemBasicUpgradeItem && getGolemBasicUpgradeCapacity() <= golemUpgradesCacheView.get(IGolemBasicUpgradeItem.class).size()){
            return false;
        }
//        if (item instanceof IGolemDecorationItem){}
        return true;
    }
}
