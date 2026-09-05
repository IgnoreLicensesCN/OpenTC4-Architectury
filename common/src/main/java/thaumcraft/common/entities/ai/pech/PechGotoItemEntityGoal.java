package thaumcraft.common.entities.ai.pech;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.pech.AbstractPechEntity;
import thaumcraft.common.lib.utils.EntityUtils;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.ITEM_ENTITY_TEST;

public class PechGotoItemEntityGoal extends Goal {
    protected final AbstractPechEntity pech;
    protected ItemEntity targetEntity;
    float maxTargetDistance = 16.0F;
    private int count;
    private int failedPathFindingPenalty;
    public PechGotoItemEntityGoal(AbstractPechEntity par1AbstractPechEntity) {
        this.pech = par1AbstractPechEntity;
    }
    @Override
    public boolean canUse() {
        if (this.pech.tickCount % Config.golemDelay > 0) {
            return false;
        } else if (--this.count > 0) {
            return false;
        } else {
            var itemEntities = this.pech.level().getEntities(
                    ITEM_ENTITY_TEST,
                    this.pech.getBoundingBox().inflate(
                            this.maxTargetDistance, this.maxTargetDistance, this.maxTargetDistance),
                    itemEntity -> !(itemEntity.getOwner() instanceof AbstractPechEntity)
                            && itemEntity.distanceToSqr(pech) < (this.maxTargetDistance * this.maxTargetDistance)
                            && pech.canPickUpItemEntity(itemEntity)
            );

            itemEntities.stream().min(
                    (i1,i2) -> Double.compare(
                            i1.distanceToSqr(pech),i2.distanceToSqr(pech)
                    )
            ).ifPresentOrElse(
                    iEntity -> targetEntity = iEntity,() -> {}
            );

            return this.targetEntity != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetEntity != null
                && (this.targetEntity.isAlive()
                && this.pech.getNavigation().getPath() != null
                && this.targetEntity.distanceToSqr(this.pech) < (double) (this.maxTargetDistance * this.maxTargetDistance));
    }
    @Override
    public void stop() {
        this.targetEntity = null;
    }

    @Override
    public void start() {
        var pechNavigation = this.pech.getNavigation();
        pechNavigation.createPath(this.targetEntity,16);
        this.count = 0;
    }

    @Override
    public void tick() {
        var pechNavigation = this.pech.getNavigation();
        this.pech.lookAt(this.targetEntity, 30.0F, 30.0F);
        if (EntityUtils.canEntityBeSeen(targetEntity,pech) && --this.count <= 0) {
            this.count = this.failedPathFindingPenalty + 4 + this.pech.getRandom().nextInt(4);
            pechNavigation.moveTo(
                    this.targetEntity,
                    this.pech.getAttributeValue(Attributes.MOVEMENT_SPEED) * (double)1.5F);
            var path = pechNavigation.getPath();
            if (path != null) {
                var finalPathPoint = path.getEndNode();
                if (finalPathPoint != null
                        && this.targetEntity.distanceToSqr(finalPathPoint.asVec3()) < (double)1.0F) {
                    this.failedPathFindingPenalty = 0;
                } else {
                    this.failedPathFindingPenalty += 10;
                }
            } else {
                this.failedPathFindingPenalty += 10;
            }
        }

        double distanceSqr = this.pech.distanceToSqr(this.targetEntity);
        if (distanceSqr <= (double)1.5F) {
            this.pech.pickUpItemEntity(this.targetEntity);
            this.count = 0;
        }
    }
}
