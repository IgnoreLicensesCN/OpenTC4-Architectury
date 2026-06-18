package thaumcraft.api.research.interfaces;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.entity.LivingEntity;

//i should say research clue is not limited to string now
// maybe you can just "owned item" or "used something"/"ate something"
// but please keep a record in your own way.
@UtilityLikeAbstraction(reason = "all of you can make clue in many usages,including multi-clue-required research")
public interface IResearchClueOwner {
    boolean livingHasClue(LivingEntity living);
    void giveClueToLiving(LivingEntity living);
}
