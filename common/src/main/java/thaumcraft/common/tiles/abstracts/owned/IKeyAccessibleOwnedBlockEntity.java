package thaumcraft.common.tiles.abstracts.owned;

import net.minecraft.world.entity.player.Player;

public interface IKeyAccessibleOwnedBlockEntity extends IOwnedBlockEntity {
    boolean playerCanUseThis(Player player);
    void addUser(Player player);
    String getKeyableName();
}
