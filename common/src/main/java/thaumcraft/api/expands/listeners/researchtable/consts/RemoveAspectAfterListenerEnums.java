package thaumcraft.api.expands.listeners.researchtable.consts;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import thaumcraft.api.expands.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.expands.listeners.researchtable.listeners.RemoveAspectAfterListener;
import thaumcraft.api.researchtable.IResearchTableEditAspectListener;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.playerdata.PacketAspectPoolS2C;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.researches.ThaumcraftResearches;

public enum RemoveAspectAfterListenerEnums {
    RESEARCH_EXPERTISE(
            new RemoveAspectAfterListener(20) {
                @Override
                public void onEventTriggered(RemoveAspectContext context) {
                    if (ThaumcraftResearches.RESEARCH_EXPERTISE.isPlayerCompletedResearch(context.player.getGameProfile().getName())) {
                        if (!context.doReturnAspect && context.atLevel.random.nextFloat() < 0.25F){
                            context.doReturnAspect = true;
                        }
                    }
                }
            }
    ),
    RESEARCH_MASTERY(
            new RemoveAspectAfterListener(20) {
                @Override
                public void onEventTriggered(RemoveAspectContext context) {
                    if (ThaumcraftResearches.RESEARCH_MASTERY.isPlayerCompletedResearch(context.player.getGameProfile().getName())) {
                        if (!context.doReturnAspect && context.atLevel.random.nextFloat() < 0.5F){
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
                var playerName = context.player.getGameProfile().getName();
                Thaumcraft.playerKnowledge.addAspectPool(
                        playerName,
                        context.aspectToRemove,
                        (short) 1);
                ResearchManager.scheduleSave(playerName);
                new PacketAspectPoolS2C(
                        context.aspectToRemove.aspectKey,
                        0,
                        Thaumcraft.playerKnowledge.getAspectPoolFor(playerName,context.aspectToRemove)).sendTo(context.player);
            }
        }
    });
    public final RemoveAspectAfterListener listener;
    RemoveAspectAfterListenerEnums(RemoveAspectAfterListener listener) {
        this.listener = listener;
    }
}
