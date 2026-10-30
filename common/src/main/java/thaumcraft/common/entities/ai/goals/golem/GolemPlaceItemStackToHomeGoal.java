package thaumcraft.common.entities.ai.goals.golem;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.EnumSet;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.config.Config.CONFIG_INSTANCE;

public class GolemPlaceItemStackToHomeGoal extends Goal {
    public GolemPlaceItemStackToHomeGoal(BaseGolemEntity golem) {
        this.golem = golem;
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    protected final BaseGolemEntity golem;

    @Override
    public boolean canUse() {
        var stackInHand = golem.getItemInHand(InteractionHand.MAIN_HAND);
        if (stackInHand.isEmpty()) {
            return false;
        }
        if (this.golem.tickCount % CONFIG_INSTANCE.getGolemDelay() != 0){
            return false;
        }
        if (golem.getRestrictCenter().distToCenterSqr(golem.position()) < maxToHomeDistanceSquaredToExecute()
                && golem.level().dimension().location().equals(golem.homeLevelResourceLocation)){
            return checkForExecution(stackInHand);
        }
        return false;
    }

    protected boolean checkForExecution(ItemStack stackInHand) {
        return true;
    }

    public double maxToHomeDistanceSquaredToExecute(){
        return 5;
    }

    @Override
    public void stop() {
        super.stop();

        golem.playSound(SoundEvents.CHEST_CLOSE, 1.0F, 1.0F);
        var homePos = this.golem.getRestrictCenter();
        var facing = golem.homeDirection;
        var bePos = homePos.relative(facing.getOpposite());
        golem.level().gameEvent(golem, GameEvent.CONTAINER_CLOSE,bePos);
    }

    @Override
    public void start() {
        super.start();
        var homePos = this.golem.getRestrictCenter();
        var facing = golem.homeDirection;
        var bePos = homePos.relative(facing.getOpposite());
        var be = getExistingBlockEntity(golem.level(),bePos);
        if (be instanceof Container container) {
            ItemStack result = InventoryUtils.placeItemStackIntoInventory(
                    this.golem.getItemInHand(InteractionHand.MAIN_HAND),
                    container,
                    facing,
                    true
            );
            if (!ItemStack.isSameItem(result, this.golem.getItemInHand(InteractionHand.MAIN_HAND))) {
                this.golem.setItemInHand(InteractionHand.MAIN_HAND,result);


//                if (Config.golemChestInteract) {
//                    container.startOpen(null);//TODO:[maybe wont finished]open inventory anim and close it(maybe require fake player)
                golem.level().gameEvent(golem, GameEvent.CONTAINER_OPEN,bePos);
                golem.playSound(SoundEvents.CHEST_OPEN, 1.0F, 1.0F);
//                }

                this.cooldown = 5;
            }
        }
    }



    @Override
    public void tick() {
        super.tick();
        cooldown -= 1;
    }

    public int cooldown = 0;
    @Override
    public boolean canContinueToUse() {
        return this.canUse() && this.cooldown <= 0;
    }
}
