package thaumcraft.api.research.interfaces;

import net.minecraft.world.entity.player.Player;

public interface IResearchable {
    boolean canPlayerResearch(Player player);
}
