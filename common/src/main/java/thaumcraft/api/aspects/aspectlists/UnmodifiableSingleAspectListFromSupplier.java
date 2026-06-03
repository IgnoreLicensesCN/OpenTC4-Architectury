package thaumcraft.api.aspects.aspectlists;

import com.linearity.opentc4.utils.functionalinterface.ObjInt2BooleanFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.PrimalAspect;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;
import java.util.function.ObjIntConsumer;

//before using weakRef we have
//BE -(strong)-> List
//List -(strong)-> BE
//after
//BE -(strong)-> List
//List -(weak)-> BE
//now we can GC
public class UnmodifiableSingleAspectListFromSupplier<Asp extends Aspect>
        implements AspectListUnmodifiableDefault<Asp>
{
    public interface SingleAspectAndAmountSupplier<Asp extends Aspect> {
        Asp getAspectAsSupplier();
        int getAmountAsSupplier();
    }
    private final WeakReference<SingleAspectAndAmountSupplier<Asp>> supplierRef;
    public UnmodifiableSingleAspectListFromSupplier(SingleAspectAndAmountSupplier<Asp> supplier) {
        this.supplierRef = new WeakReference<>(supplier);
    }
    @Override
    public void addAspectDescriptionToList(@Nullable Player player, List<Component> aspectDescriptions) {
        //TODO:Remove method
    }

    private @Nullable Asp getOnlyAspect(){
        var aspectSupplier = supplierRef.get();
        if (aspectSupplier == null){
            return null;
        }
        return aspectSupplier.getAspectAsSupplier();

    }
    private int getOnlyAspectAmount(){
        return getOnlyAspectAmount(0);
    }
    private int getOnlyAspectAmount(int defaultValue){
        var amountSupplier = supplierRef.get();
        if (amountSupplier == null){
            return defaultValue;
        }
        return amountSupplier.getAmountAsSupplier();
    }

    @Override
    public int getOrDefault(Aspect aspect, int defaultValue) {
        if (getOnlyAspect() != aspect){
            return defaultValue;
        }
        return getOnlyAspectAmount(defaultValue);
    }

    @Override
    public AspectList<Asp> copy() {
        var aspect = getOnlyAspect();
        if (aspect == null){
            return UnmodifiableAspectList.of();
        }
        var amount = getOnlyAspectAmount();
        return UnmodifiableAspectList.of(aspect,amount);
    }

    @Override
    public @UnmodifiableView Set<Asp> keySet() {
        var aspect = getOnlyAspect();
        if (aspect == null){
            return Set.of();
        }
        return Set.of(aspect);
    }

    @Override
    public int get(Aspect aspect) {
        return getOrDefault(aspect,0);
    }

    @Override
    public int size() {
        return getOnlyAspect() == null?0:1;
    }

    @Override
    public int visSize() {
        return getOnlyAspectAmount();
    }

    @Override
    public AspectList<PrimalAspect> getPrimalAspects() {

        var aspect = getOnlyAspect();
        return aspect instanceof PrimalAspect primalAspect
                ?UnmodifiableAspectList.of(primalAspect,getOnlyAspectAmount())
                :UnmodifiableAspectList.EMPTY_PRIMAL;
    }

    @Override
    public List<Asp> getAspectsSorted() {
        var aspect = getOnlyAspect();
        if (aspect == null){
            return List.of();
        }
        return List.of(aspect);
    }

    @Override
    public List<Asp> getAspectsSortedAmount() {
        return getAspectsSorted();//it's singleton
    }
    @Override
    public void forEach(ObjIntConsumer<Asp> action) {

        var aspect = getOnlyAspect();
        if (aspect == null){
            return;
        }
        action.accept(aspect,getOnlyAspectAmount());
    }

    @Override
    public boolean forEachWithBreak(ObjInt2BooleanFunction<Asp> action) {

        var aspect = getOnlyAspect();
        if (aspect == null){
            return false;
        }
        return action.accept(aspect,getOnlyAspectAmount());
    }

    @Override
    public void acceptForIndex(int index, ObjIntConsumer<Asp> action) {
        var aspect = getOnlyAspect();
        if (aspect == null){
            return;
        }
        action.accept(aspect,getOnlyAspectAmount());
    }

    @Override
    public @Nullable("if empty") Asp randomAspect(RandomSource randomSource) {
        return getOnlyAspect();
    }

    @Override
    public @Nullable("if empty") Asp randomWeightedAspect(RandomSource randomSource) {
        return getOnlyAspect();
    }

    @Override
    public boolean isEmpty() {
        return getOnlyAspect() == null;
    }

    @Override
    public @NotNull("empty -> empty(aspect)") Asp getFirstAspect() {
        var aspect = getOnlyAspect();
        if (aspect == null){
            return (Asp) Aspects.EMPTY;
        }
        return aspect;
    }

    @Override
    public boolean containsKey(Aspect aspect) {
        return aspect == getOnlyAspect();
    }

}
