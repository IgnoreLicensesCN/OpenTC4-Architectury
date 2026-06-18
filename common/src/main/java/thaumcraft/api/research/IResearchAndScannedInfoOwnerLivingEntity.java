package thaumcraft.api.research;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IResearchAndScannedInfoOwnerLivingEntity {
    @Nullable ResearchAndScannedInfo getResearchAndScannedInfo();
    void setResearchAndScannedInfo(@NotNull ResearchAndScannedInfo completedResearches);
}
