package thaumcraft.common.tiles.abstracts;

import net.minecraft.server.level.ServerPlayer;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.utils.HexCoord;

public interface IResearchAspectPlaceableBlockEntity {
    void placeAspect(HexCoord hexCoord, Aspect aspect, ServerPlayer player);
}
