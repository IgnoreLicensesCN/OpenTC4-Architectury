package thaumcraft.api.aspects;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.aspect.IAspectReducibleToPrimal;
import thaumcraft.api.aspects.aspect.IResearchConnectableToOtherAspect;
import thaumcraft.api.aspects.aspect.IScanDiscoverableAspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.researches.ResearchAndScannedInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static thaumcraft.api.aspects.Aspects.COMPOUND_ASPECTS;

public class CompoundAspect extends Aspect implements
        IAspectReducibleToPrimal,
        IResearchConnectableToOtherAspect,
        IScanDiscoverableAspect
{
    public static final Map<CompoundAspectComponent,CompoundAspect> COMPOUND_ASPECT_RECIPES = new ConcurrentHashMap<>();
    public static final CompoundAspect EMPTY = new CompoundAspect(
            AspectResourceLocation.of(Thaumcraft.MOD_ID,"empty_compound"),
            0x000000,
            1,
            true
    ) {
        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public @Unmodifiable @NotNull AspectList<PrimalAspect> reduceToPrimal(boolean merge) {
            return UnmodifiableAspectList.EMPTY_PRIMAL;
        }
    };
    //only for empty
    private CompoundAspect(@NotNull AspectResourceLocation aspectKey, @RGBColor int color, int blend, boolean noRegisterArg) {
        super(aspectKey,color,blend,noRegisterArg);
        this.components = null;
    }
    public final @NotNull("unless it's empty") CompoundAspectComponent components;

    public static @NotNull Aspect getCombinationResult(Aspect aspect1, Aspect aspect2) {
        if (aspect1.isEmpty() || aspect2.isEmpty()) {
            return Aspects.EMPTY;
        }
        var result = COMPOUND_ASPECT_RECIPES.get(CompoundAspectComponent.of(aspect1, aspect2));
        return result == null ?Aspects.EMPTY : result;
    }

    private void verifyDuplicate(CompoundAspect registeringAspect){
        var duplicatedIfNotNull = COMPOUND_ASPECT_RECIPES.getOrDefault(components,null);
        if (duplicatedIfNotNull != null) {
            //i have to say
            // if you have two aspects with same recipe
            // you wont be happy to handle this recipe collision.
            // (that would be weird in game.)
            throw new RuntimeException("Duplicate compound aspect recipe:" + components + "|" + duplicatedIfNotNull + " and " + registeringAspect);
        }
        COMPOUND_ASPECT_RECIPES.put(components,this);
    }
    public CompoundAspect(AspectResourceLocation tag, @RGBColor int color, @NotNull CompoundAspectComponent components, int blend) {
        super(tag,color,blend);
        this.components = components;
        COMPOUND_ASPECTS.put(tag,this);
        verifyDuplicate(this);
    }

    /**
     * Shortcut constructor I use for the default aspects - you shouldn't be using this.
     */
    public CompoundAspect(AspectResourceLocation tag, @RGBColor int color, CompoundAspectComponent components) {
        this(tag,color,components,1);
    }


    public boolean isCombinedFrom(Aspect a, Aspect b) {
        return components.isCombinedFrom(a,b);
    }

    @Override
    public String toString() {
        return "CompoundAspect{" +
                "aspectKey=" + aspectKey +
                ", components=" + components +
                ", color=" + color +
                ", blend=" + blend +
                '}';
    }
    private AspectList<PrimalAspect> reducedToPrimal = null;
    private AspectList<PrimalAspect> mergeReducedToPrimal = null;
    public @Unmodifiable @NotNull AspectList<PrimalAspect> reduceToPrimal(boolean merge){
        var cached = merge?mergeReducedToPrimal:reducedToPrimal;
        if (cached == null) {
            AspectList<PrimalAspect> reduced = new LinkedHashAspectList<>();
            if (this.components.aspectA() instanceof IAspectReducibleToPrimal reducibleToPrimal){
                reduced.mergeWithHighest(reducibleToPrimal.reduceToPrimal(merge));
            }
            if (this.components.aspectB() instanceof IAspectReducibleToPrimal reducibleToPrimal){
                reduced.mergeWithHighest(reducibleToPrimal.reduceToPrimal(merge));
            }
            cached = reduced;
            if (merge){
                mergeReducedToPrimal = cached;
            }else {
                reducedToPrimal = cached;
            }
        }
        return cached;
    }

    @Override
    public boolean canConnectTo(Aspect aspect) {
        return this.components.contains(aspect);
    }

    @Override
    public boolean canPlayerDiscover(Player player) {
        var info = ResearchAndScannedInfo.getFromPlayer(player);
        if (info != null) {
            return info.hasResearchAspect(components.aspectA()) && info.hasResearchAspect(components.aspectB());
        }
        return false;
    }

    @Override
    public @Nullable AspectResourceLocation getOneOfAspectRequiredToDiscover(Player player) {
        var info = ResearchAndScannedInfo.getFromPlayer(player);
        if (!info.hasResearchAspect(components.aspectA())){
            return components.aspectA().aspectKey;
        }
        if (!info.hasResearchAspect(components.aspectB())){
            return components.aspectB().aspectKey;
        }
        return null;
    }
}
