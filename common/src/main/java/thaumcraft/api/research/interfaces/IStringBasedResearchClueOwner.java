
package thaumcraft.api.research.interfaces;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

//i should say research clue is not limited to string now
// maybe you can just "owned item" or "used something"/"ate something"
// but please keep a record in your own way.
@UtilityLikeAbstraction(reason = "azanor used this but maybe we could have other ways?")
public interface IStringBasedResearchClueOwner extends IResearchClueOwner {
    ResearchItemResourceLocation getKey();
    @Override
    default boolean playerHasClue(Player player){
        return ResearchManager.getClueForPlayer(player).contains(getKey().convertToClueResLoc());
    }

    @Override
    default void giveClueToPlayer(Player player){
        Thaumcraft.researchManager.completeClue(player,getKey().convertToClueResLoc());
    }
}
