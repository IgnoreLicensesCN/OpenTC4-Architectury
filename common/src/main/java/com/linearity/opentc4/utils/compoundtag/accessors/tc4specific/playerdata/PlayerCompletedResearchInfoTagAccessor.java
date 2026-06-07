package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResearchItemResourceLocationTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.collection.ModifiableConcurrentSetTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.researches.PlayerCompletedResearchInfo;

public class PlayerCompletedResearchInfoTagAccessor extends CompoundTagAccessor<PlayerCompletedResearchInfo> {
    private final CompoundTagAccessorImpl wrappedTagAccessor = new CompoundTagAccessorImpl(tagKey);
    private final ModifiableConcurrentSetTagAccessor<ResearchItemResourceLocation> researchIDAccessor
            = new ModifiableConcurrentSetTagAccessor<>(
                    tagKey + "_researches",new ResearchItemResourceLocationTagAccessor("research_id")
            );

    public PlayerCompletedResearchInfoTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public PlayerCompletedResearchInfo readFromCompoundTag(CompoundTag tag) {
        var innerTag = wrappedTagAccessor.readFromCompoundTag(tag);
        PlayerCompletedResearchInfo info = new PlayerCompletedResearchInfo();
        info.completedResearches.clear();
        info.completedResearches.addAll(researchIDAccessor.readFromCompoundTag(innerTag));
        return info;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, PlayerCompletedResearchInfo value) {
        CompoundTag wrappedTag = new CompoundTag();
        researchIDAccessor.writeToCompoundTag(wrappedTag,value.completedResearches);
        wrappedTagAccessor.writeToCompoundTag(tag,wrappedTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return wrappedTagAccessor.compoundTagHasKey(tag);
    }
}
