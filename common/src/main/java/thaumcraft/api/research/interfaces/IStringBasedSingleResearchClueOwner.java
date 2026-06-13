
package thaumcraft.api.research.interfaces;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;

//i should say research clue is not limited to string now
// maybe you can just "owned item" or "used something"/"ate something"
// but please keep a record in your own way.
@UtilityLikeAbstraction(reason = "azanor used this but maybe we could have other ways?")
public interface IStringBasedSingleResearchClueOwner extends IResearchClueOwner {
    ClueResourceLocation getNeededClue();
    @Override
    default boolean playerHasClue(Player player){
        return ResearchAndScannedInfo.getFromPlayer(player).hasClue(getNeededClue());
    }

    @Override
    default void giveClueToPlayer(Player player){
        ResearchAndScannedInfo.getFromPlayer(player).addClue(getNeededClue());
    }
}
