package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction;

import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.entities.golems.BaseGolemEntity.BaseGolemEntityClientEventSignals.START_ACTION_TIMER;

public class GolemDropItemStackHomeIfNoContainerGoal extends AbstractGolemGoal {
    public GolemDropItemStackHomeIfNoContainerGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public boolean canUse() {
        var stackInHand = golem.getMainHandItem();
        if (stackInHand.isEmpty()) {
            return false;
        }
        if (golem.getRestrictCenter().distToCenterSqr(golem.position()) < maxToHomeDistanceSquaredToExecute() 
                && golem.level().dimension().location().equals(golem.homeLevelResourceLocation)){
            var bePos = golem.getHomeAttachedBlockPos();
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

        var pos = golem.getHomeAttachedBlockPos().getCenter();
        var stackToDrop = golem.getMainHandItem();
        var item = new ItemEntity(golem.level(),pos.x,pos.y,pos.z, ExtendedItemStack.splitAll(stackToDrop));
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
        this.golem.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
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
