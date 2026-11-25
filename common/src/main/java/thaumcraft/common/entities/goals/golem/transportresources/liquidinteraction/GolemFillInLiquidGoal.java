package thaumcraft.common.entities.goals.golem.transportresources.liquidinteraction;

import dev.architectury.fluid.FluidStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.core.LiquidGolemCoreItem;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class GolemFillInLiquidGoal extends AbstractGolemFluidGoal {
    public GolemFillInLiquidGoal(BaseGolemEntity golem) {
        super(golem);
    }


    @Override
    public boolean canUse() {
        var holdingFluid = LiquidGolemCoreItem.GolemHoldingFluidFakeField.getHoldingFluid(golem);
        if (golem.getNavigation().isDone()
                && !holdingFluid.isEmpty()
                && golem.getRestrictCenter().distToCenterSqr(golem.position()) < reachRangeSqr()
        ) {
            return insertFluidHome(holdingFluid,false) > 0;
        }
        return false;
    }

    protected long insertFluidHome(FluidStack holdingFluid,boolean doit) {
        var dir = golem.homeDirection;
        var homePos = golem.getRestrictCenter();
        Level level = golem.level();
        return platformUtils.getFluidInteractionManager().insertFluidToPos(level, homePos, dir, holdingFluid, doit);
    }

    @Override
    public void start() {
        super.start();
        var holdingFluid = LiquidGolemCoreItem.GolemHoldingFluidFakeField.getHoldingFluid(golem);
        if (!holdingFluid.isEmpty()){
            var inserted = insertFluidHome(holdingFluid,true);
            if (inserted > 0){
                holdingFluid.setAmount(Math.max(0,holdingFluid.getAmount()-inserted));
                if (inserted > 200){
                    golem.playSound(SoundEvents.GENERIC_SWIM,
                            Math.min(0.2F, 0.2F * (inserted / (float)golem.getCarryPower())),
                            this.golem.getRandom().nextFloat()*0.6F +0.7F
                    );
                }
            }
        }
    }
}
