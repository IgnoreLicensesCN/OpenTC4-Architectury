package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ClueResourceLocationTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResearchItemResourceLocationTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.collection.ModifiableConcurrentSetTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.researches.ResearchAndScannedInfo;

public class ResearchAndScannedInfoTagAccessor extends CompoundTagAccessor<ResearchAndScannedInfo> {
    private final CompoundTagAccessorImpl wrappedTagAccessor = new CompoundTagAccessorImpl(tagKey);
    private final ModifiableConcurrentSetTagAccessor<ResearchItemResourceLocation> researchIDAccessor
            = new ModifiableConcurrentSetTagAccessor<>(
                    tagKey + "_researches",new ResearchItemResourceLocationTagAccessor("research_id")
            );
    private final ModifiableConcurrentSetTagAccessor<ClueResourceLocation> clueIDAccessor
            = new ModifiableConcurrentSetTagAccessor<>(
            tagKey + "_clues",
            new ClueResourceLocationTagAccessor("clue_id")
    );

    public ResearchAndScannedInfoTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public ResearchAndScannedInfo readFromCompoundTag(CompoundTag tag) {
        var innerTag = wrappedTagAccessor.readFromCompoundTag(tag);
        ResearchAndScannedInfo info = new ResearchAndScannedInfo();
        info.completedResearches.addAll(researchIDAccessor.readFromCompoundTag(innerTag));
        info.completedClues.addAll(clueIDAccessor.readFromCompoundTag(innerTag));
        return info;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, ResearchAndScannedInfo value) {
        CompoundTag wrappedTag = new CompoundTag();
        researchIDAccessor.writeToCompoundTag(wrappedTag,value.completedResearches);
        clueIDAccessor.writeToCompoundTag(wrappedTag,value.completedClues);
        wrappedTagAccessor.writeToCompoundTag(tag,wrappedTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return wrappedTagAccessor.compoundTagHasKey(tag);
    }
}
