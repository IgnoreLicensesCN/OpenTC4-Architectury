package thaumcraft.common.entities.ai.goals.golem;

import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import java.util.EnumSet;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.START_ACTION_TIMER;

public class GolemDropItemStackHomeIfNoContainerGoal extends Goal {
    public GolemDropItemStackHomeIfNoContainerGoal(BaseGolemEntity golem) {
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
        if (golem.getRestrictCenter().distToCenterSqr(golem.position()) < maxToHomeDistanceSquaredToExecute() 
                && golem.level().dimension().location().equals(golem.homeLevelResourceLocation)){
            var homePos = this.golem.getRestrictCenter();
            var facing = golem.homeDirection;
            var bePos = homePos.relative(facing.getOpposite());
            var be = getExistingBlockEntity(golem.level(),bePos);
            return !(be instanceof Container);
        }
        return false;
    }
    public double maxToHomeDistanceSquaredToExecute(){
        return 5;
    }

    @Override
    public void start() {
        super.start();
        this.cooldown = 200;
//        ChunkCoordinates home = this.golem.getHomePosition();
//        Direction facing = Direction.getOrientation(this.golem.homeFacing);
//        int cX = home.posX - facing.offsetX;
//        int cY = home.posY - facing.offsetY;
//        int cZ = home.posZ - facing.offsetZ;

        var homePos = this.golem.getRestrictCenter();
        var facing = golem.homeDirection;
        var pos = homePos.relative(facing.getOpposite()).getCenter();
        var stackToDrop = golem.getItemInHand(InteractionHand.MAIN_HAND);
        var item = new ItemEntity(golem.level(),pos.x,pos.y,pos.z,stackToDrop.split(stackToDrop.getCount()));
        var golemPos = golem.position();
        double distance = golemPos.distanceTo(pos);

        var itemMovementX = pos.x;
        var itemMovementY = pos.y;
        var itemMovementZ = pos.z;
        itemMovementX -= golemPos.x;
        itemMovementY -= golemPos.y;
        itemMovementZ -= golemPos.z;
        var scaleSize = distance/3.F;
        itemMovementX *= scaleSize;
        itemMovementY *= scaleSize;
        itemMovementZ *= scaleSize;

        item.setDeltaMovement(itemMovementX,itemMovementY,itemMovementZ);
        item.setPickUpDelay(10);
        this.golem.level().addFreshEntity(item);
        this.golem.level().broadcastEntityEvent(golem, START_ACTION_TIMER);
        this.golem.updateItemInHand();
    }



    @Override
    public void tick() {
        super.tick();
        cooldown -= 1;
    }

    public int cooldown = 0;
    @Override
    public boolean canContinueToUse() {
        return this.canUse() && this.cooldown > 0;
    }
}
