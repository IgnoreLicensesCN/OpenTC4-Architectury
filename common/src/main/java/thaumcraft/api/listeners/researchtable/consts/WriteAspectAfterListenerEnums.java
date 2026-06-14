package thaumcraft.api.listeners.researchtable.consts;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import thaumcraft.api.listeners.researchtable.WriteAspectContext;
import thaumcraft.api.listeners.researchtable.listeners.WriteAspectAfterListener;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.api.research.ThaumcraftResearches;
import thaumcraft.common.tiles.crafted.ResearchTableBlockEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public enum WriteAspectAfterListenerEnums {
    RESEARCH_MASTERY(new WriteAspectAfterListener(10) {
        @Override
        public void onEventTriggered(WriteAspectContext context) {

            if (context.doDrainAspect
                    && ThaumcraftResearches.RESEARCH_EXPERTISE.isPlayerCompletedResearch(context.player)) {
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
                var probablyTable = LevelBlockEntityAccessing.getExistingBlockEntity(context.atLevel, context.tablePos);
                if (probablyTable instanceof ResearchTableBlockEntity table){
                    var usingAspect = context.aspectToWrite;
                    if (usingAspect.isEmpty()){
                        return;
                    }
                    if (table.bonusAspects.getOrDefault(usingAspect, 0) > 0){
                        table.bonusAspects.remove(usingAspect);
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
                var info = ResearchAndScannedInfo.getFromPlayer(context.player);
                info.addResearchAspectAndSyncToPlayer(context.aspectToWrite,-1,context.player);
            }
        }
    }),
    IF_RESEARCH_FINISHED(new WriteAspectAfterListener(COST_ASPECT.listener.weight + 1) {
        @Override
        public void onEventTriggered(WriteAspectContext context) {
            if (context.noteData.completed){
                context.atLevel.playSound(context.player,context.tablePos, ThaumcraftSounds.LEARN,SoundSource.BLOCKS, 1.0F, 1.0F);
                if (LevelBlockEntityAccessing.getExistingBlockEntity(context.atLevel, context.tablePos) instanceof TileThaumcraft tile){
                    tile.markDirtyAndUpdateSelf();
                };
            }
        }
    })
    ;

    public final WriteAspectAfterListener listener;
    WriteAspectAfterListenerEnums(WriteAspectAfterListener listener) {
        this.listener = listener;
    }
}
