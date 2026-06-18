package thaumcraft.api.listeners.researchtable.consts;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import thaumcraft.api.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.listeners.researchtable.listeners.RemoveAspectAfterListener;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.api.research.ThaumcraftResearches;

public enum RemoveAspectAfterListenerEnums {
    RESEARCH_EXPERTISE_AMD_MASTERY(
            new RemoveAspectAfterListener(20) {
                @Override
                public void onEventTriggered(RemoveAspectContext context) {
                    if (ThaumcraftResearches.RESEARCH_EXPERTISE.isLivingEntityCompletedResearch(context.player)) {
                        float returnAspectChance = 0.25F;

                        if (ThaumcraftResearches.RESEARCH_MASTERY.isLivingEntityCompletedResearch(context.player)) {
                            returnAspectChance = 0.5F;
                        }
                        if (!context.doReturnAspect && context.atLevel.random.nextFloat() < returnAspectChance){
                            context.doReturnAspect = true;
                        }
                    }
                }
            }
    ),
    RETURN_ASPECT(new RemoveAspectAfterListener(Integer.MAX_VALUE/2) {
        @Override
        public void onEventTriggered(RemoveAspectContext context) {
            if (context.doReturnAspect && !context.aspectToRemove.isEmpty()){

                context.atLevel.playSound(
                        context.player,
                        context.tablePos,
                        SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.BLOCKS,
                        .2F,.9F + context.atLevel.random.nextFloat()*0.2f
                );
                var info = ResearchAndScannedInfo.getFromLiving(context.player);
                info.addResearchAspectAndTrySyncToPlayer(context.aspectToRemove,1,context.player);
            }
        }
    });
    public final RemoveAspectAfterListener listener;
    RemoveAspectAfterListenerEnums(RemoveAspectAfterListener listener) {
        this.listener = listener;
    }
}
