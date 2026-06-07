package thaumcraft.common.researches;

import com.linearity.opentc4.mixinaccessors.PlayerCompletedResearchInfoAccessor;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

//i have to say this one is misunderstanding.
//It just like "Oh i need some string to record something"
// not all research need this,I can just say "you've picked PrimePearl >= 1(MC statics or whatever)"(or i may lookup items scanned for resource location) so research is unlocked
// (or some advancement?like twilight forest)
//
public class PlayerCompletedResearchInfo {
    @ApiStatus.Internal
    public final Collection<ResearchItemResourceLocation> completedResearches = ConcurrentHashMap.newKeySet();

    public boolean hasResearchID(ResearchItemResourceLocation researchID){
        return completedResearches.contains(researchID);
    }
    public boolean addResearchID(ResearchItemResourceLocation researchID){
        return completedResearches.add(researchID);
    }

    public static PlayerCompletedResearchInfo getForPlayer(Player player){
        return ((PlayerCompletedResearchInfoAccessor)player).opentc4$getCompletedResearchesInfo();
    }

}
