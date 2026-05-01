package thaumcraft.api.research.interfaces;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Range;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

//remember it's just a way to do research
// and maybe you can create a "TimeAndItemResearchable" (moon in specific shape and inventory has a bed) to make that.
public interface IResearchNoteCreatableResearch extends IResearchableResearch {
    boolean canPlayerCreateResearchNote(Player player);
    @Range(from=1,to=3) int getComplexity();
    AspectList<Aspect> getResearchGivenAspects();
}
