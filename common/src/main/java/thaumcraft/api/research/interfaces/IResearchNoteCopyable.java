package thaumcraft.api.research.interfaces;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;

public interface IResearchNoteCopyable {
    boolean canPlayerCopyResearch(Player player);
    @UnmodifiableView
    AspectList<Aspect> getCopyResearchBaseAspects();
}
