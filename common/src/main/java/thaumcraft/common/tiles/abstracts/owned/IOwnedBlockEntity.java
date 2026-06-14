package thaumcraft.common.tiles.abstracts.owned;

import net.minecraft.world.entity.player.Player;

public interface IOwnedBlockEntity {
    boolean playerOwnThis(Player player);

    void addOwner(Player player);
}
