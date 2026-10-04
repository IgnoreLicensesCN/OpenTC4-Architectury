package thaumcraft.common.entities.ai.goals;

import com.linearity.opentc4.annotations.StoleFrom;
import net.minecraft.sounds.SoundEvents;
import thaumcraft.common.entities.abstracts.StayableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;
import java.util.Set;

@StoleFrom("FollowOwnerGoal")
public class CrossDimensionFollowingOwnerGoal extends Goal {
    public static final int TELEPORT_WHEN_DISTANCE_IS = 12;
    private static final int MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 2;
    private static final int MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 3;
    private static final int MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 1;
    protected final OwnableEntity ownable;
    protected final Mob ownableMob;
    private LivingEntity owner;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;
    private final boolean canFly;

    public CrossDimensionFollowingOwnerGoal(OwnableEntity ownable,Mob mob, double d, float f, float g, boolean bl) {
        if (ownable != mob){
            throw new IllegalArgumentException("OwnableEntity and mob must be same");
        }
        this.ownable = ownable;
        this.ownableMob = mob;
        this.speedModifier = d;
        this.navigation = mob.getNavigation();
        this.startDistance = f;
        this.stopDistance = g;
        this.canFly = bl;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(mob.getNavigation() instanceof GroundPathNavigation) && !(mob.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.ownable.getOwner();
        if (livingEntity == null) {
            return false;
        } else if (livingEntity.isSpectator()) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else if (this.ownableMob.distanceToSqr(livingEntity) < this.startDistance * this.startDistance) {
            return false;
        } else {
            this.owner = livingEntity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else {
            return !this.unableToMove() && !(this.ownableMob.distanceToSqr(this.owner) <= this.stopDistance * this.stopDistance);
        }
    }

    private boolean unableToMove() {
        return (this.ownable instanceof StayableEntity stayable
                && stayable.thaumcraft$getStay())
                || this.ownableMob.isPassenger()
                || this.ownableMob.isLeashed();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.ownableMob.getPathfindingMalus(BlockPathTypes.WATER);
        this.ownableMob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.ownableMob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.ownableMob.getLookControl().setLookAt(this.owner, 10.0F, this.ownableMob.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (this.ownableMob.distanceToSqr(this.owner) >= TELEPORT_WHEN_DISTANCE_IS*TELEPORT_WHEN_DISTANCE_IS || this.ownableMob.level() != this.owner.level()) {
                this.teleportToOwner();
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
            }
        }
    }

    private void teleportToOwner() {
        BlockPos blockPos = this.owner.blockPosition();

        for (int i = 0; i < 10; i++) {
            int j = this.randomIntInclusive(-MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING, MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING);
            int k = this.randomIntInclusive(-MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING, MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING);
            int l = this.randomIntInclusive(-MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING, MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING);
            boolean bl = this.maybeTeleportTo(this.owner.level(),blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
            if (bl) {
                return;
            }
        }
    }

    protected boolean maybeTeleportTo(Level level, int i, int j, int k) {
        if (Math.abs(i - this.owner.getX()) < MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING && Math.abs(k - this.owner.getZ()) < MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING) {
            return false;
        } else if (!this.canTeleportTo(this.owner.level(),new BlockPos(i, j, k))) {
            return false;
        } else {
            if (level instanceof ServerLevel serverLevel && serverLevel != this.ownableMob.level()) {
                this.ownableMob.playSound(SoundEvents.ENDERMAN_TELEPORT, 0.5F, 1.0F);
                this.ownableMob.teleportTo(serverLevel,i + 0.5, j, k + 0.5, Set.of(), this.ownableMob.getYRot(), this.ownableMob.getXRot());
                this.ownableMob.setTarget(null);
            }else {
                this.ownableMob.moveTo(i + 0.5, j, k + 0.5, this.ownableMob.getYRot(), this.ownableMob.getXRot());
            }
            this.navigation.stop();
            return true;
        }
    }

    protected boolean canTeleportTo(Level level,BlockPos blockPos) {
        if (level != this.ownableMob.level()) {
            return true;//cross world
        }
        BlockPathTypes blockPathTypes = WalkNodeEvaluator.getBlockPathTypeStatic(level, blockPos.mutable());
        if (blockPathTypes != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockState = level.getBlockState(blockPos.below());
            if (!this.canFly && blockState.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockPos2 = blockPos.subtract(this.ownableMob.blockPosition());
                return level.noCollision(this.ownableMob, this.ownableMob.getBoundingBox().move(blockPos2));
            }
        }
    }

    protected int randomIntInclusive(int i, int j) {
        return this.ownableMob.getRandom().nextInt(j - i + 1) + i;
    }
}
