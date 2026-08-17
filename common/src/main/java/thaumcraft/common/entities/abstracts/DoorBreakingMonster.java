package thaumcraft.common.entities.abstracts;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.ai.goals.ZombieLikeAttackGoal;

import java.util.function.Predicate;

import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

@UtilityLikeAbstraction
public abstract class DoorBreakingMonster extends Monster {
    protected boolean canBreakDoors = true;
    protected BreakDoorGoal breakDoorGoal = new BreakDoorGoal(this, DIFFICULTY_PREDICATE);
    protected static final Predicate<Difficulty> DIFFICULTY_PREDICATE = d -> d == Difficulty.HARD;
    public DoorBreakingMonster(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public boolean canBreakDoors() {
        return this.canBreakDoors;
    }
    public void setCanBreakDoors(boolean bl) {
        if (this.supportsBreakDoorGoal() && GoalUtils.hasGroundPathNavigation(this)) {
            if (this.canBreakDoors != bl) {
                this.canBreakDoors = bl;
                ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(bl);
                if (bl) {
                    this.goalSelector.addGoal(1, this.breakDoorGoal);
                } else {
                    this.goalSelector.removeGoal(this.breakDoorGoal);
                }
            }
        } else if (this.canBreakDoors) {
            this.goalSelector.removeGoal(this.breakDoorGoal);
            this.canBreakDoors = false;
        }
    }
    protected boolean supportsBreakDoorGoal() {
        return true;
    }
}
