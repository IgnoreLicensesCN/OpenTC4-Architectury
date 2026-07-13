package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ClueResourceLocationTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResearchItemResourceLocationTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResourceLocationTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ScannedTypeResourceLocationTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect.HashAspectListAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.collection.ModifiableConcurrentSetTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.collection.ModifiableMapAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;

import java.util.Set;

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
    private final HashAspectListAccessor researchAspectsAccessor = new HashAspectListAccessor(tagKey + "_research_aspects");
    private final ModifiableMapAccessor<ScannedTypeResourceLocation, Set<ResourceLocation>> scannedThingsAccessor
            = new ModifiableMapAccessor<>(
            tagKey + "_scanned_things",
            new ScannedTypeResourceLocationTagAccessor("scanned_type"),
            new ModifiableConcurrentSetTagAccessor<>("scanned_things",new ResourceLocationTagAccessor("t"))
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
        info.owningResearchAspect.addAll(researchAspectsAccessor.readFromCompoundTag(innerTag));
        info.scannedThings.putAll(scannedThingsAccessor.readFromCompoundTag(innerTag));
        return info;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, ResearchAndScannedInfo value) {
        CompoundTag wrappedTag = new CompoundTag();
        {
            researchIDAccessor.writeToCompoundTag(wrappedTag, value.completedResearches);
            clueIDAccessor.writeToCompoundTag(wrappedTag, value.completedClues);
            researchAspectsAccessor.writeToCompoundTag(wrappedTag, value.owningResearchAspect);
            scannedThingsAccessor.writeToCompoundTag(wrappedTag, value.scannedThings);
        }
        wrappedTagAccessor.writeToCompoundTag(tag,wrappedTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return wrappedTagAccessor.compoundTagHasKey(tag);
    }
}
