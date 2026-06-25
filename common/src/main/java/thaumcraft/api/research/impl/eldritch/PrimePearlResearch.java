package thaumcraft.api.research.impl.eldritch;

import net.minecraft.world.entity.LivingEntity;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.scan.ThaumcraftScannedTypes;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import static thaumcraft.common.items.ThaumcraftItemInstances.PRIME_PEARL;

public class PrimePearlResearch extends ResearchItem {
    public static final ResearchItemResourceLocation ID = ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "prime_pearl");
    public PrimePearlResearch() {
        super(ID);
    }

    //oops you may not have to become crazy (and flux) to get one(from a loot bag or your friend)
    @Override
    public boolean isLivingEntityCompletedResearch(LivingEntity living) {
        var researchInfo = ResearchAndScannedInfo.getFromLiving(living);
        if (researchInfo == null) {
            return false;
        }
        return researchInfo.hasScannedForType(ThaumcraftScannedTypes.ITEM, PRIME_PEARL().arch$registryName());
    }

    @Override
    public void completeResearchFor(LivingEntity living) {
        var researchInfo = ResearchAndScannedInfo.getFromLiving(living);
        if (researchInfo == null) {
            return;
        }
        researchInfo.addScannedForType(ThaumcraftScannedTypes.ITEM, PRIME_PEARL().arch$registryName());
    }
}
