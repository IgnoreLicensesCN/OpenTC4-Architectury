package thaumcraft.api.aspects.aspect;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

public interface IScanDiscoverableAspect {
    boolean canPlayerDiscover(Player player);
    @Nullable AspectResourceLocation getOneOfAspectRequiredToDiscover(Player player);
}
