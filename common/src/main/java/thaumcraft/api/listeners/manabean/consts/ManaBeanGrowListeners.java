package thaumcraft.api.listeners.manabean.consts;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CompoundAspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.listeners.manabean.ManaBeanGrowContext;
import thaumcraft.api.listeners.manabean.listeners.ManaBeanGrowListener;
import thaumcraft.common.blocks.abstracts.IManaBeanAspectCombineProviderBlock;

import java.util.ArrayList;
import java.util.List;

import static thaumcraft.api.aspects.Aspects.PRIMAL_ASPECT_LIST;

public enum ManaBeanGrowListeners {
    COMBINE_WITH_NEAR(new ManaBeanGrowListener(500) {
        @Override
        public void onGrowStageChanged(ManaBeanGrowContext context) {
            if (context.growthStage <= 2){return;}
            var be = context.manaBeanBE;
            var level = be.getLevel();
            if (level == null){return;}
            var selfPos = be.getBlockPos();
            List<Aspect> aspectsCanCombine = new ArrayList<>(5);
            AspectList<Aspect> aspectsChangeInto = new LinkedHashAspectList<>(11,1);
            var selfCanCombine = be.getAspectOwning();
            if (!selfCanCombine.isEmpty()){
                aspectsChangeInto.addAll(selfCanCombine,1);
            }
            for (var direction: Direction.Plane.HORIZONTAL){
                var pickPos = selfPos.relative(direction);
                var pickState = level.getBlockState(pickPos);
                if (pickState.getBlock() instanceof IManaBeanAspectCombineProviderBlock aspectProvider){
                    var aspectCanProvide = aspectProvider.getAspectCanProvide(level, pickPos, pickState);
                    if (!aspectCanProvide.isEmpty()){
                        aspectsCanCombine.add(aspectCanProvide);
                        aspectsChangeInto.addAll(aspectCanProvide,1);
                    }
                }
            }
            for (int i = 0; i < aspectsCanCombine.size(); i++){
                for (int j = i + 1; j < aspectsCanCombine.size(); j++){
                    var aspect1 = aspectsCanCombine.get(i);
                    var aspect2 = aspectsCanCombine.get(j);
                    var combined = CompoundAspect.getCombinationResult(aspect1, aspect2);
                    if (!combined.isEmpty()){
                        aspectsChangeInto.addAll(combined,4);
                    }
                }
            }
            var aspect = aspectsChangeInto.randomWeightedAspect(level.random);
            if (aspect != null){
                be.setAspectOwning(aspect);
                be.markDirtyAndUpdateSelf();
            }
        }
    }),
    SET_ASPECT_IF_EMPTY(new ManaBeanGrowListener(1000) {
        @Override
        public void onGrowStageChanged(ManaBeanGrowContext context) {
            if (context.growthStage <= 2){return;}
            var be = context.manaBeanBE;
            var level = be.getLevel();
            RandomSource random = null;
            if (level != null) {
                random = level.random;
            }else {
                random = RandomSource.createNewThreadLocalInstance();
            }
            if (be.getAspectOwning().isEmpty()){
                if (random.nextInt(8) == 0){
                    be.setAspectOwning(Aspects.PLANT);
                }else {
                    be.setAspectOwning(PRIMAL_ASPECT_LIST.get(random.nextInt(PRIMAL_ASPECT_LIST.size())));
                }
                be.markDirtyAndUpdateSelf();
            }
        }
    })
    ;
    public final ManaBeanGrowListener listener;
    ManaBeanGrowListeners(ManaBeanGrowListener listener){
        this.listener = listener;
    }
}
