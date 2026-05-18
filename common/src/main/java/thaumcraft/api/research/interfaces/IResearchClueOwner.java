package thaumcraft.api.research.interfaces;

import net.minecraft.world.entity.player.Player;

//i should say research clue is not limited to string now
// maybe you can just "owned item" or "used something"/"ate something"
// but please keep a record in your own way.
public interface IResearchClueOwner {
    boolean playerHasClue(Player player);
    void giveClueToPlayer(Player player);
}
