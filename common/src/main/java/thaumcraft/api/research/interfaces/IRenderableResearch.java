package thaumcraft.api.research.interfaces;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;

import java.util.List;

public interface IRenderableResearch {
    ShownInfoInResearchCategory getShownInfo(@NotNull ResearchCategory category);
    List<ResearchPage> getPages(@NotNull ResearchCategory category, @Nullable Player player);
}
