package thaumcraft.api.wands;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;

import java.util.Map;

import static thaumcraft.api.expands.listeners.wandconsumption.ConsumptionModifierCalculator.getConsumptionModifier;

public interface ICentiVisContainer<Asp extends Aspect> {
    int CENTIVIS_MULTIPLIER = 100;
    /**
     *
     * @param stack which owns vis
     * @return a map showing owing vis but may not thread-safe
     */
    boolean tryCastAspectClass(Class<? extends Aspect> aspClass);
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
        var centiVisList = new CentiVisList<Asp>();
        centiVisList.put(aspect,vis);
        storeCentiVisOwning(itemStack,centiVisList);
    }
    //not multiplied by getVisCapacityMultiplier()
    @UnmodifiableView
    CentiVisList<Asp> getAllCentiVisCapacity(ItemStack stack);
    //not multiplied by getVisCapacityMultiplier()
    default int getVisCapacity(ItemStack stack,Asp aspect){
        return this.getAllCentiVisCapacity(stack).getOrDefault(aspect,0);
    }

    //amount 1 -> showing 0.01
    //return overflow amount

    default int addCentiVis(ItemStack stack, Asp aspect, int centiVisAmount /*alert:not multiplied by 100*/){
        return addCentiVis(stack, aspect, centiVisAmount, true);
    }
    /**
     * add vis to this stack
     * @param stack stack will be added vis inside
     * @param aspect to add
     * @param centiVisAmount to add (centiVisAmount 1 -> showing 0.01)
     * @return overflow centiVisAmount
     */
    default int addCentiVis(ItemStack stack, Asp aspect, int centiVisAmount /*alert:not multiplied by 100*/, boolean doIt) {
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
     * like {@link ICentiVisContainer#addCentiVis(ItemStack, Aspect, int)} but we do this with a map.Map in WILL be modified.
     * @param stack which will be set in vis.
     * @param addInto a map to add many aspects into.
     * @return remaining centiVis
     */
    default CentiVisList<Asp> addAllCentiVis(ItemStack stack, CentiVisList<Asp> addInto) {
        var capacity = getAllCentiVisCapacity(stack);
        var visOwning = getAllCentiVisOwning(stack);
        CentiVisList<Asp> remainingVis = new CentiVisList<>();
        for (var entry : addInto.entrySet()) {
            int currentCapacity = capacity.getOrDefault(entry.getKey(),0);
            if (currentCapacity == 0){
                continue;
            }
            int currentVis = visOwning.getOrDefault(entry.getKey(),0);
            currentVis += addInto.get(entry.getKey());
            int remaining = Math.max(0,currentVis - currentCapacity);
            currentVis = Math.min(currentVis,currentCapacity);
            remainingVis.put(entry.getKey(),remaining);
            visOwning.put(entry.getKey(),currentVis);
        }
        storeCentiVisOwning(stack, visOwning);
        return remainingVis;
    }
    default CentiVisList<Asp> getAspectsWithRoom(ItemStack wandstack) {
        var allVis = getAllCentiVisOwning(wandstack);
        var capacity = getAllCentiVisCapacity(wandstack);

        CentiVisList<Asp> res = new CentiVisList<>(allVis.size(),1.F);

        for (var entry : allVis.entrySet()) {
            var aspect = entry.getKey();
            var vis = entry.getValue();
            var remainingRoom = capacity.getOrDefault(aspect,0) - vis;
            if (remainingRoom > 0){
                res.put(aspect,remainingRoom);
            }
        }

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

    default boolean consumeAllCentiVisWithoutModifier(@NotNull ItemStack is, @NotNull CentiVisList<Asp> aspects, boolean doit) {
        if (!aspects.isEmpty()) {
            CentiVisList<Asp> nl = new CentiVisList<>();

            for (var entry : aspects.entrySet()) {
                var aspect = entry.getKey();
                int cost = entry.getValue();
                cost = (int) ((float) cost);
                nl.addAll(aspect, cost);
            }

            if (nl.entrySet().stream().anyMatch(
                    aspectIntegerEntry ->
                            getCentiVisOwning(
                                    is, aspectIntegerEntry.getKey())
                                    < aspectIntegerEntry.getValue()
            )){
                return false;
            }

            if (doit && Platform.getEnvironment() != Env.CLIENT) {
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

    default boolean consumeAllCentiVisCrafting(ItemStack is, @Nullable LivingEntity user, CentiVisList<Asp> aspects, boolean doit) {
        return this.consumeAllCentiVis(is, user, aspects, doit, true);
    }

    default boolean consumeAllCentiVis(@NotNull ItemStack is, @Nullable LivingEntity user, @NotNull CentiVisList<Asp> aspects, boolean doit, boolean crafting) {
        if (!aspects.isEmpty()) {
            CentiVisList<Asp> nl = new CentiVisList<>();

            for (var entry : aspects.entrySet()) {
                var aspect = entry.getKey();
                int cost = entry.getValue();
                cost = (int) ((float) cost * getConsumptionModifier(is.getItem(),is, user, aspect, crafting));
                nl.addAll(aspect, cost);
            }

            for (var entry : nl.entrySet()) {
                var aspect = entry.getKey();
                var amount = entry.getValue();
                if (getCentiVisOwning(is, aspect) < amount) {
                    return false;
                }
            }

            if (doit && Platform.getEnvironment() != Env.CLIENT) {

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
