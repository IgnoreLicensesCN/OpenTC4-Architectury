package thaumcraft.common.items.golem.upgrade.core;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import com.linearity.opentc4.utils.functionalinterface.ObjInt2BooleanFunction;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.IGolemAdditionalDataProviderItem;

import static com.linearity.opentc4.Consts.EssentiaCoreTagAccessors.*;
import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

public class EssentiaGolemCoreItem extends AbstractGolemCoreItem implements IGolemAdditionalDataProviderItem {

    @NoArgsConstructor
    public static class GolemHoldingEssentiaFakeField implements IObjectCacheableFakeFieldOwner.FakeFieldAttachable<BaseGolemEntity> {
        //i dont want a aspect stack since we dont have value type.
        public Aspect aspect = Aspect.EMPTY;
        public int amount = 0;
        public final ObjInt2BooleanFunction<Aspect> aspectListContainsSelfAspect = (aspect, amount) -> {
            if (this.aspect.equals(aspect)){
                aspectListContainsSelfAspectMetAmount = amount;
                return true;
            }
            return false;
        };
        public int aspectListContainsSelfAspectMetAmount = 0;
        public static GolemHoldingEssentiaFakeField getHolding(BaseGolemEntity golem) {
            return opentc4$getOrCreateCachedObjectFrom(golem, GolemHoldingEssentiaFakeField.class, GolemHoldingEssentiaFakeField::new);
        }
        public static void setHolding(BaseGolemEntity golem,@NotNull Aspect aspect,int amount) {
            var fakeField = opentc4$getOrCreateCachedObjectFrom(golem, GolemHoldingEssentiaFakeField.class, GolemHoldingEssentiaFakeField::new);
            fakeField.aspect = aspect;
            fakeField.amount = amount;
        }
        public void verify(){
            this.amount = Math.max(0,this.amount);
            if (this.amount == 0) {
                this.aspect = Aspect.EMPTY;
            }
        }
    }

    @Override
    public void readAdditional(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem, GolemHoldingEssentiaFakeField.class, GolemHoldingEssentiaFakeField::new);
        fakeField.aspect = HOLDING_ESSENTIA_TYPE.readFromCompoundTag(tag);
        fakeField.amount = HOLDING_ESSENTIA_AMOUNT.readIntFromCompoundTag(tag);
    }

    @Override
    public void writeAdditional(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem, GolemHoldingEssentiaFakeField.class, GolemHoldingEssentiaFakeField::new);
        HOLDING_ESSENTIA_TYPE.writeToCompoundTag(tag, fakeField.aspect);
        HOLDING_ESSENTIA_AMOUNT.writeIntToCompoundTag(tag, fakeField.amount);
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoalIfNotExist(1,GOLEM_FILL_IN_ESSENTIA,GOLEM_FILL_IN_ESSENTIA_GOAL_PROVIDER,golem);
        goalSelector.addGoalIfNotExist(2,GOLEM_GATHER_ESSENTIA,GOLEM_GATHER_ESSENTIA_GOAL_PROVIDER,golem);
        goalSelector.addGoalIfNotExist(2,GOLEM_GOTO_FILL_IN_ESSENTIA,GOLEM_GOTO_FILL_IN_ESSENTIA_GOAL_PROVIDER,golem);
    }
}
