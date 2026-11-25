package thaumcraft.common.entities.goals.golem.miscjobs;

import com.linearity.opentc4.mixin.accessors.FishingHookAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import thaumcraft.common.entities.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.ThaumcraftFishingHook;

import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.START_RIGHT_ARM_TIMER;

public class GolemFishingGoal extends AbstractGolemGoal {

    public GolemFishingGoal(BaseGolemEntity golem) {
        super(golem);
    }

    public int getSearchTimes() {
        return golem.getHomeRange();
    }
    public int getRange() {
        return golem.getHomeRange() / 2;
    }

    protected int tickRemaining = 0;

    protected BlockPos.MutableBlockPos waterPos = new BlockPos.MutableBlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    @Override
    public boolean canUse() {
        if (golem.tickCount % getGolemGoalDelay() != 0){
            return false;
        }
        if (golem.getNavigation().isInProgress()){
            return false;
        }
        for (var hookSupplier:golem.opentc4$getFishingHooks()){
            var hook = hookSupplier.get();
            if (hook != null){
                hook.discard();
            }
        }
        findWater();
        if (
                waterPos.getX() == Integer.MAX_VALUE
                && waterPos.getY() == Integer.MAX_VALUE
                && waterPos.getZ() == Integer.MAX_VALUE
        ){
            return false;
        };
        golem.playSound(
                SoundEvents.FISHING_BOBBER_THROW,
                0.5F,
                0.4F / (golem.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        var level = golem.level();
        var hook = new ThaumcraftFishingHook(golem,level,0,0);
        hook.moveTo(waterPos.getX() + 0.5F, waterPos.getY() + 0.5F, waterPos.getZ() + 0.5F);
        return level.addFreshEntity(hook);
    }

    @Override
    public boolean canContinueToUse() {
        var hooks = golem.opentc4$getFishingHooks();
        hooks.removeIf(s -> s.get() == null);
        if (hooks.isEmpty()){
            return false;
        }
        if (
                waterPos.getX() == Integer.MAX_VALUE
                        && waterPos.getY() == Integer.MAX_VALUE
                        && waterPos.getZ() == Integer.MAX_VALUE
        ){
            return false;
        }
        return tickRemaining > 0;
    }

    protected void findWater() {
        var random = golem.getRandom();
        var searchTimes = getSearchTimes();
        var range = getRange();
        var randomXZRange = range*2+1;
        var randomYRange = range+1;
        for (int _time = 0;_time < searchTimes;_time++){
            waterPos.setWithOffset(
                    golem.getRestrictCenter(),
                    random.nextInt(randomXZRange) - range,
                    random.nextInt(randomYRange) - (range/2),
                    random.nextInt(randomXZRange) - range
            );
            if (golem.level().getBlockState(waterPos).getFluidState().is(FluidTags.WATER)){
                if (golem.level().getBlockState(waterPos.move(Direction.UP)).isAir()){
                    waterPos.move(Direction.DOWN);
                    return;
                }
            }
        }
        waterPos.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void tick() {
        super.tick();
        tickRemaining -= 1;
        for (var hookSupplier:golem.opentc4$getFishingHooks()){
            var hook = hookSupplier.get();
            if (hook != null){
                var accessor = ((FishingHookAccessor)hook);
                if (accessor.getNibble() > 0){
                    var usingStack = golem.getMainHandItem();
                    if (usingStack.isEmpty()){
                        usingStack = Items.FISHING_ROD.getDefaultInstance();
                    }
                    accessor.retrieve(usingStack);
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        tickRemaining = 0;
        waterPos.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void start() {
        super.start();
        tickRemaining = 300 + this.golem.getRandom().nextInt(200);
        golem.level().broadcastEntityEvent(golem,START_RIGHT_ARM_TIMER);
    }
}
