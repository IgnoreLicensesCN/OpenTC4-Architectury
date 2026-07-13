package thaumcraft.api.aspects.aspect;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.annotations.ShouldNotModify;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.PrimalAspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;

import java.util.Map;

public interface IAspectReducibleToPrimal {
    static @Modifiable AspectList<PrimalAspect> reduceToPrimals(AspectList<Aspect> al) {
        return reduceToPrimals(al, false);
    }

    static @Modifiable AspectList<Aspect> reduceToPrimalsAndCast(AspectList<Aspect> al) {
        return (AspectList<Aspect>) (AspectList<?>) reduceToPrimals(al);
    }

    class ReducedCache {
        private static final Map<@Unmodifiable AspectList<Aspect>, AspectList<PrimalAspect>> reducedToPrimalMap = new MapMaker().weakKeys().makeMap();
        private static final Map<@Unmodifiable AspectList<Aspect>, AspectList<PrimalAspect>> reducedToPrimalMapMerged = new MapMaker().weakKeys().makeMap();
    }

    static @Modifiable AspectList<PrimalAspect> reduceToPrimals(@ShouldNotModify AspectList<Aspect> aspectsIn, boolean merge) {
        var usingMap = merge ? ReducedCache.reducedToPrimalMapMerged : ReducedCache.reducedToPrimalMap;
        return usingMap.computeIfAbsent(
                aspectsIn, toReduce -> {
                    AspectList<PrimalAspect> out = new LinkedHashAspectList<>();
                    toReduce.forEach((aspectToReduce, amountToReduce) -> {
                        if (aspectToReduce instanceof IAspectReducibleToPrimal reducibleToPrimal) {
                            var reduced = reducibleToPrimal.reduceToPrimal(merge);
                            reduced.forEach(((primalAspect, primalAmount) -> {
                                if (merge) {
                                    out.mergeWithHighest(primalAspect, primalAmount * amountToReduce);
                                } else {
                                    out.addAll(primalAspect, primalAmount * amountToReduce);
                                }
                            }));
                        }
                    });
                    return out;
                }
        );
    }

    @NotNull
    @Unmodifiable
    AspectList<PrimalAspect> reduceToPrimal(boolean merge);
    default AspectList<PrimalAspect> reduceToPrimal(){
        return reduceToPrimal(false);
    }
}
