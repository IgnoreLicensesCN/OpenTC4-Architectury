package thaumcraft.api.research.interfaces;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.client.ResearchCategory;
import thaumcraft.api.research.client.ResearchPage;
import thaumcraft.api.research.client.render.ShownInfoInResearchCategory;

import java.util.List;

public interface IRenderableResearch {
    @Nullable
    ShownInfoInResearchCategory getShownInfo(@NotNull ResearchCategory category);
    @Deprecated(forRemoval = true,since = "preparing for XML Rendering")
    List<ResearchPage> getPages(@NotNull ResearchCategory category, @Nullable Player player);
}
