package thaumcraft.common.items.golem.upgrade.core;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import dev.architectury.fluid.FluidStack;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.IGolemAdditionalDataProviderItem;

import static com.linearity.opentc4.Consts.LiquidCoreTagAccessors.HOLDING_FLUID;
import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

public class LiquidGolemCoreItem
        extends AbstractGolemCoreItemWithGui
    implements IGolemAdditionalDataProviderItem
{

    public LiquidGolemCoreItem() {
        super();
    }

    public LiquidGolemCoreItem(Properties properties) {
        super(properties);
    }
    @NoArgsConstructor
    public static class GolemHoldingFluidFakeField implements IObjectCacheableFakeFieldOwner.FakeFieldAttachable<BaseGolemEntity> {
        public FluidStack fluid = FluidStack.empty();
        public static FluidStack getHoldingFluid(BaseGolemEntity golem) {
            return opentc4$getOrCreateCachedObjectFrom(golem,GolemHoldingFluidFakeField.class,GolemHoldingFluidFakeField::new).fluid;
        }
        public static void setHoldingFluid(BaseGolemEntity golem, FluidStack fluid) {
            opentc4$getOrCreateCachedObjectFrom(golem,GolemHoldingFluidFakeField.class,GolemHoldingFluidFakeField::new).fluid = fluid;
        }
    }

    @Override
    public void readAdditional(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag) {
        opentc4$getOrCreateCachedObjectFrom(golem,GolemHoldingFluidFakeField.class,GolemHoldingFluidFakeField::new).fluid
                = HOLDING_FLUID.readFromCompoundTag(tag);
    }

    @Override
    public void writeAdditional(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag) {
        HOLDING_FLUID.writeToCompoundTag(
                tag,
                opentc4$getOrCreateCachedObjectFrom(golem,GolemHoldingFluidFakeField.class,GolemHoldingFluidFakeField::new).fluid
        );
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoalIfNotExist(1,GOLEM_FILL_IN_LIQUID,GOLEM_FILL_IN_LIQUID_GOAL_PROVIDER,golem);
        goalSelector.addGoalIfNotExist(2,GOLEM_GET_LIQUID,GOLEM_GET_LIQUID_GOAL_PROVIDER,golem);
    }
}
