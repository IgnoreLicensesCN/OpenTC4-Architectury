package thaumcraft.api.wands;

import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.annotations.forvalue.CentiVisAmount;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.LinkedHashCentiVisList;

import static thaumcraft.api.listeners.wandconsumption.ConsumptionModifierCalculator.getConsumptionModifier;

//Oh I forgot that (Thaumic Horizon)crystal disposable wand.
//In that case you may want to make capacity returns centivis owning.
public interface ICentiVisContainerItem<Asp extends Aspect> {
    int CENTIVIS_MULTIPLIER = 100;
    boolean tryCastAspectClass(Class<? extends Aspect> aspClass);
    /**
     *
     * @param stack which owns vis
     * @return a map showing owing vis but may not thread-safe
     */
    CentiVisList<Asp> getAllCentiVisOwning(ItemStack stack);
    default int getCentiVisOwning(ItemStack stack, Asp aspect) {
        return this.getAllCentiVisOwning(stack).getOrDefault(aspect,0);
    }

    /**
     * warning:no capacity check
     * @param itemStack vis owner
     * @param aspects to store
     */
    void storeCentiVisOwning(ItemStack itemStack, CentiVisList<Asp> aspects);
    default void storeCentiVisOwning(ItemStack itemStack, Asp aspect, int vis){
        var centiVisList = new LinkedHashCentiVisList<Asp>();
        centiVisList.put(aspect,vis);
        storeCentiVisOwning(itemStack,centiVisList);
    }
    @UnmodifiableView
    CentiVisList<Asp> getAllCentiVisCapacity(ItemStack stack);
    default int getCentiVisCapacity(ItemStack stack, Asp aspect){
        return this.getAllCentiVisCapacity(stack).getOrDefault(aspect,0);
    }

    //amount 1 -> showing 0.01
    //return overflow amount

    default int addCentiVis(ItemStack stack, Asp aspect, int centiVisAmount /*alert:not multiplied by CENTIVIS_MULTIPLIER*/){
        return addCentiVis(stack, aspect, centiVisAmount, true);
    }
    /**
     * add vis to this stack
     * @param stack stack will be added vis inside
     * @param aspect to add
     * @param centiVisAmount to add (centiVisAmount 1 -> showing 0.01)
     * @return overflow centiVis Amount
     */
    default @CentiVisAmount int addCentiVis(ItemStack stack, Asp aspect, int centiVisAmount /*alert:not multiplied by CENTIVIS_MULTIPLIER*/, boolean doIt) {
        int capacity = getAllCentiVisCapacity(stack).getOrDefault(aspect,0);
        if (capacity == 0){
            return centiVisAmount;
        }
        var visOwning = getAllCentiVisOwning(stack);
        int currentVis = visOwning.getOrDefault(aspect,0);
        currentVis += centiVisAmount;
        int result = Math.max(currentVis - capacity, 0);
        currentVis = Math.min(capacity,currentVis);
        visOwning.put(aspect,currentVis);
        if (doIt){
            storeCentiVisOwning(stack, visOwning);
        }
        return result;
    }

    default CentiVisList<Asp> addAllVis(ItemStack stack, CentiVisList<Asp> centiVisList){
        return addAllCentiVis(stack,centiVisList);
    }

    /**
     * like {@link ICentiVisContainerItem#addCentiVis(ItemStack, Aspect, int)} but we do this with a map.Map in WILL be modified.
     * @param stack which will be set in vis.
     * @param addInto a map to add many aspects into.
     * @return remaining centiVis
     */
    default CentiVisList<Asp> addAllCentiVis(ItemStack stack, CentiVisList<Asp> addInto) {
        var capacity = getAllCentiVisCapacity(stack);
        var visOwning = getAllCentiVisOwning(stack);
        CentiVisList<Asp> remainingVis = new LinkedHashCentiVisList<>();
        addInto.forEach((aspect,amount) -> {
            int currentCapacity = capacity.getOrDefault(aspect,0);
            if (currentCapacity == 0){
                return;
            }
            int currentVis = visOwning.getOrDefault(aspect,0);
            currentVis += addInto.get(aspect);
            int remaining = Math.max(0,currentVis - currentCapacity);
            currentVis = Math.min(currentVis,currentCapacity);
            remainingVis.put(aspect,remaining);
            visOwning.put(aspect,currentVis);
        });
        storeCentiVisOwning(stack, visOwning);
        return remainingVis;
    }
    @Modifiable
    @NotNull
    default CentiVisList<Asp> getAspectsWithRoomRemaining(ItemStack wandstack) {
        var allVis = getAllCentiVisOwning(wandstack);
        var capacity = getAllCentiVisCapacity(wandstack);

        CentiVisList<Asp> res = new LinkedHashCentiVisList<>(allVis.size(),1.F);

        allVis.forEach((aspect,vis)->{
            var remainingRoom = capacity.getOrDefault(aspect,0) - vis;
            if (remainingRoom > 0){
                res.put(aspect,remainingRoom);
            }
        });

        return res;
    }

    default boolean consumeCentiVis(@NotNull ItemStack is, @Nullable LivingEntity user, @NotNull Asp aspect, int centiVisAmount, boolean crafting) {
        centiVisAmount = (int) ((float) centiVisAmount * getConsumptionModifier(is.getItem(),is, user, aspect, crafting));
        if (getCentiVisOwning(is, aspect) >= centiVisAmount) {
            storeCentiVisOwning(is, aspect, getCentiVisOwning(is, aspect) - centiVisAmount);
            return true;
        } else {
            return false;
        }
    }
    default boolean consumeCentiVisWithoutModifier(@NotNull ItemStack is, @Nullable LivingEntity user, @NotNull Asp aspect, int centiVisAmount, boolean crafting) {
        if (getCentiVisOwning(is, aspect) >= centiVisAmount) {
            storeCentiVisOwning(is, aspect, getCentiVisOwning(is, aspect) - centiVisAmount);
            return true;
        } else {
            return false;
        }
    }

    default boolean consumeAllCentiVisWithoutModifier(@NotNull ItemStack is, @NotNull CentiVisList<Asp> aspectsToConsume, boolean doit,boolean serverLevel) {
        if (!aspectsToConsume.isEmpty()) {
            if (aspectsToConsume.forEachWithBreak(
                    (aspect,amountRequired) ->
                    getCentiVisOwning(is, aspect) < amountRequired)
            ){
                return false;
            }

            if (doit && serverLevel) {
                aspectsToConsume.forEach((aspect, amount) ->
                                storeCentiVisOwning(
                                        is,
                                        aspect,
                                        getCentiVisOwning(is, aspect) - amount
                        ));
            }
            return true;
        } else {
            return false;
        }
    }

    default boolean consumeAllCentiVisCrafting(ItemStack is, @Nullable LivingEntity user, CentiVisList<Asp> aspects, boolean doit,boolean serverSide) {
        return this.consumeAllCentiVis(is, user, aspects, doit, true,serverSide);
    }

    default boolean consumeAllCentiVis(
            @NotNull ItemStack is,
            @Nullable LivingEntity user,
            @NotNull CentiVisList<Asp> aspects,
            boolean doit, boolean crafting,boolean serverSide) {
        if (!aspects.isEmpty()) {
            CentiVisList<Asp> nl = new LinkedHashCentiVisList<>();

            aspects.forEach((aspect,cost) -> {

                cost = (int) ((float) cost * getConsumptionModifier(is.getItem(),is, user, aspect, crafting));
                nl.addAll(aspect, cost);
            });
            if (nl.forEachWithBreak((aspect,amountRequired) -> getCentiVisOwning(is, aspect) < amountRequired)){
                return false;
            }
            if (doit && serverSide) {

                nl
                        .forEach((aspect, amount) ->
                                storeCentiVisOwning(
                                        is,
                                        aspect,
                                        getCentiVisOwning(is, aspect) - amount
                                ));
            }
            return true;
        } else {
            return false;
        }
    }
}
