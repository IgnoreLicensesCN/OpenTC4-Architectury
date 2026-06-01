package thaumcraft.api.research.interfaces;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedTreeAspectList;

public interface IResearchNoteCopyable {
    boolean canPlayerCopyResearch(Player player);
    @UnmodifiableView
    AspectList<Aspect> getCopyResearchBaseAspects();
}
