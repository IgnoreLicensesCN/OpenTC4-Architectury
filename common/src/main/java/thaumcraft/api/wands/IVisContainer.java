package thaumcraft.api.wands;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.HashMap;
import java.util.Map;

import static thaumcraft.api.expands.wandconsumption.ConsumptionModifierCalculator.getConsumptionModifier;

public interface IVisContainer {
    public static final int CENTIVIS_MULTIPLIER = 100;
    /**
     *
     * @param stack which owns vis
     * @return a map showing owing vis but may not thread-safe
     */
    Map<Aspect,Integer> getAllVisOwning(ItemStack stack);
    default int getVisOwning(ItemStack stack,Aspect aspect) {
        return this.getAllVisOwning(stack).getOrDefault(aspect,0);
    }

    /**
     * warning:no capacity check
     * @param itemStack vis owner
     * @param aspects to store
     */
    void storeVisOwning(ItemStack itemStack,Map<Aspect,Integer> aspects);
    default void storeVisOwning(ItemStack itemStack,Aspect aspect,int vis){
        storeVisOwning(itemStack,Map.of(aspect,vis));
    };
    //not multiplied by getVisCapacityMultiplier()
    @UnmodifiableView
    Map<Aspect,Integer> getAllVisCapacity(ItemStack stack);
    //not multiplied by getVisCapacityMultiplier()
    default int getVisCapacity(ItemStack stack,Aspect aspect){
        return this.getAllVisCapacity(stack).getOrDefault(aspect,0);
    }

    //amount 1 -> showing 0.01
    //return overflow amount

    default int addCentiVis(ItemStack stack, Aspect aspect, int centiVisAmount /*alert:not multiplied by 100*/){
        return addCentiVis(stack, aspect, centiVisAmount, true);
    }
    /**
     * add vis to this stack
     * @param stack stack will be added vis inside
     * @param aspect to add
     * @param centiVisAmount to add (centiVisAmount 1 -> showing 0.01)
     * @return overflow centiVisAmount
     */
    default int addCentiVis(ItemStack stack, Aspect aspect, int centiVisAmount /*alert:not multiplied by 100*/, boolean doIt) {
        int capacity = getAllVisCapacity(stack).getOrDefault(aspect,0) * getVisCapacityMultiplier();
        if (capacity == 0){
            return centiVisAmount;
        }
        var visOwning = getAllVisOwning(stack);
        int currentVis = visOwning.getOrDefault(aspect,0);
        currentVis += centiVisAmount;
        int result = Math.max(currentVis - capacity, 0);
        currentVis = Math.min(capacity,currentVis);
        visOwning.put(aspect,currentVis);
        if (doIt){
            storeVisOwning(stack, visOwning);
        }
        return result;
    }

    /**
     * like {@link IVisContainer#addCentiVis(ItemStack, Aspect, int)} but we do this with a map.Map in WILL be modified.
     * @param stack which will be set in vis.
     * @param addInto a map to add many aspects into.
     * @return remaining centiVis
     */
    default Map<Aspect,Integer> addAllVis(ItemStack stack, Map<Aspect,Integer> addInto) {
        var capacity = getAllVisCapacity(stack);
        var visOwning = getAllVisOwning(stack);
        Map<Aspect,Integer> remainingVis = new HashMap<>(addInto.size(),1.F);
        for (var entry : addInto.entrySet()) {
            int currentCapacity = capacity.getOrDefault(entry.getKey(),0);
            if (currentCapacity == 0){
                continue;
            }
            int currentVis = visOwning.getOrDefault(entry.getKey(),0)  * getVisCapacityMultiplier();
            currentVis += addInto.get(entry.getKey());
            int remaining = Math.max(0,currentVis - currentCapacity);
            currentVis = Math.min(currentVis,currentCapacity);
            remainingVis.put(entry.getKey(),remaining);
            visOwning.put(entry.getKey(),currentVis);
        }
        storeVisOwning(stack, visOwning);
        return remainingVis;
    }

    /**
     * in fact when we set vis capacity "100" the actual vis is "10000",then you can see vis 100.00
     * yeah vis step is 0.01 it's still integer not float
     * @return capacity multiplier,100 is normal while sceptre will * 1.5(then it comes to 150)
     */
    default int getVisCapacityMultiplier(){
        return 100;
    };

    default Map<Aspect,Integer> getAspectsWithRoom(ItemStack wandstack) {
        var allVis = getAllVisOwning(wandstack);
        var capacity = getAllVisCapacity(wandstack);

        Map<Aspect,Integer> res = new HashMap<>(allVis.size(),1.F);

        for (var entry : allVis.entrySet()) {
            var aspect = entry.getKey();
            var vis = entry.getValue();
            var remainingRoom = capacity.getOrDefault(aspect,0) * getVisCapacityMultiplier() - vis;
            if (remainingRoom > 0){
                res.put(aspect,remainingRoom);
            }
        }

        return res;
    }

    default boolean consumeVis(@NotNull ItemStack is, @Nullable LivingEntity user,@NotNull Aspect aspect, int centiVisAmount, boolean crafting) {

        centiVisAmount = (int) ((float) centiVisAmount * getConsumptionModifier(is.getItem(),is, user, aspect, crafting));
        if (getVisOwning(is, aspect) >= centiVisAmount) {
            storeVisOwning(is, aspect, getVisOwning(is, aspect) - centiVisAmount);
            return true;
        } else {
            return false;
        }
    }

    default boolean consumeAllVisCrafting(ItemStack is,@Nullable LivingEntity user, AspectList aspects, boolean doit) {
        if (aspects != null && aspects.size() != 0) {
            AspectList nl = new AspectList();

            for (var entries : aspects.getAspects().entrySet()) {
                var aspect = entries.getKey();
                var amount = entries.getValue();
                int cost = amount * 100;
                nl.addAll(aspect, cost);
            }

            return this.consumeAllVis(is, user, nl, doit, true);
        } else {
            return false;
        }
    }

    default boolean consumeAllVis(@NotNull ItemStack is, @Nullable LivingEntity user,@NotNull AspectList aspects, boolean doit, boolean crafting) {
        if (aspects.size() != 0) {
            AspectList nl = new AspectList();

            for (var entry : aspects.getAspects().entrySet()) {
                var aspect = entry.getKey();
                int cost = entry.getValue();
                cost = (int) ((float) cost * getConsumptionModifier(is.getItem(),is, user, aspect, crafting));
                nl.addAll(aspect, cost);
            }

            for (var entry : nl.getAspects().entrySet()) {
                var aspect = entry.getKey();
                var amount = entry.getValue();
                if (getVisOwning(is, aspect) < amount) {
                    return false;
                }
            }

            if (doit && Platform.getEnvironment() != Env.CLIENT) {

                for (var entry : nl.getAspects().entrySet()) {
                    var aspect = entry.getKey();
                    var amount = entry.getValue();
                    storeVisOwning(is, aspect, getVisOwning(is, aspect) - amount);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
