package thaumcraft.api.expands.listeners.researchtable.consts;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import thaumcraft.api.expands.listeners.researchtable.WriteAspectContext;
import thaumcraft.api.expands.listeners.researchtable.listeners.WriteAspectAfterListener;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.playerdata.PacketAspectPoolS2C;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.researches.ThaumcraftResearches;
import thaumcraft.common.tiles.crafted.ResearchTableBlockEntity;

public enum WriteAspectAfterListenerEnums {
    RESEARCH_MASTERY(new WriteAspectAfterListener(10) {
        @Override
        public void onEventTriggered(WriteAspectContext context) {

            if (context.doDrainAspect
                    && ThaumcraftResearches.RESEARCH_EXPERTISE.isPlayerCompletedResearch(context.player.getGameProfile().getName())) {
                if (context.atLevel.random.nextDouble() < 0.1) {
                    context.atLevel.playSound(
                            context.player,
                            context.tablePos,
                            SoundEvents.EXPERIENCE_ORB_PICKUP,
                            SoundSource.BLOCKS,
                            .2F,.9F + context.atLevel.random.nextFloat()*0.2f
                    );
                    context.doDrainAspect = false;
                }
            }
        }
    }),
    RESEARCH_TABLE_BONUS_ASPECT_DISCOUNT(new WriteAspectAfterListener(30) {
        @Override
        public void onEventTriggered(WriteAspectContext context) {
            if (context.doDrainAspect){
                var probablyTable = context.atLevel.getBlockEntity(context.tablePos);
                if (probablyTable instanceof ResearchTableBlockEntity table){
                    var usingAspect = context.aspectToWrite;
                    if (usingAspect.isEmpty()){
                        return;
                    }
                    if (table.bonusAspect.getAspects().getOrDefault(usingAspect, 0) > 0){
                        table.bonusAspect.reduceAndRemoveIfNotPositive(usingAspect);
                        context.doDrainAspect= false;;
                        table.markDirtyAndUpdateSelf();
                    }
                }
            }
        }
    }),
    COST_ASPECT(new WriteAspectAfterListener(Integer.MAX_VALUE/2) {
        @Override
        public void onEventTriggered(WriteAspectContext context) {
            if (context.doDrainAspect && !context.aspectToWrite.isEmpty()){
                var playerName = context.player.getGameProfile().getName();
                Thaumcraft.playerKnowledge.addAspectPool(
                        playerName,
                        context.aspectToWrite,
                        (short) -1);
                ResearchManager.scheduleSave(playerName);
                new PacketAspectPoolS2C(
                        context.aspectToWrite.aspectKey,
                        0,
                        Thaumcraft.playerKnowledge.getAspectPoolFor(playerName,context.aspectToWrite)).sendTo(context.player);
            }
        }
    }),
    ;

    public final WriteAspectAfterListener listener;
    WriteAspectAfterListenerEnums(WriteAspectAfterListener listener) {
        this.listener = listener;
    }
}
