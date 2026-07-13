package com.linearity.opentc4.mixinaccessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.IResearchAndScannedInfoOwnerLivingEntity;
import thaumcraft.api.research.ResearchAndScannedInfo;

public interface PlayerResearchAndScannedInfoAccessor extends IResearchAndScannedInfoOwnerLivingEntity {
    ResearchAndScannedInfo opentc4$getResearchAndScannedInfo();
    void opentc4$setResearchAndScannedInfo(ResearchAndScannedInfo completedResearches);

    @Override
    default @Nullable ResearchAndScannedInfo getResearchAndScannedInfo(){
        return opentc4$getResearchAndScannedInfo();
    }

    @Override
    default void setResearchAndScannedInfo(@NotNull ResearchAndScannedInfo completedResearches){
        opentc4$setResearchAndScannedInfo(completedResearches);
    }
}
