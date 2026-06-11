package thaumcraft.api.crafting.infusion;

import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.StrictItemStackMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableCentiVisList;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils.SameValueList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.resourcelocations.InfusionRecipeResourceLocation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

//the usual form of infusion recipe
//items in and the only possible itemStack out
public class SimpleInfusionRecipe extends InfusionRecipe{
    protected final @Unmodifiable List<RecipeItemMatcher> components;
    protected final RecipeItemMatcher centralInput;
    protected final ItemStack output;
    protected final AspectList<Aspect> aspectsRequiring;
    public final ResearchItem research;
    protected final RecipeItemMatcher recipeOutputMatcher;

    protected final int instability;

    public SimpleInfusionRecipe(
            InfusionRecipeResourceLocation id,
            @Unmodifiable List<RecipeItemMatcher> components,
            RecipeItemMatcher centralInput,
            ItemStack output,
            AspectList<Aspect> aspectsRequiring,
            ResearchItem research,
            int instability
    ) {
        super(id);
        this.components = components;
        this.centralInput = centralInput;
        this.output = output;
        this.recipeOutputMatcher = new StrictItemStackMatcher(output);
        this.aspectsRequiring = aspectsRequiring;
        this.research = research;
        this.instability = instability;
        List<List<ItemStack>> inputsForAspectCalc = new ArrayList<>(components.size() + 1);
        List<List<Function<ItemStack, ItemStack>>> remainingForAspectCalculation = new ArrayList<>(components.size() + 1);
        for (var component : components) {
            var inSample = component.getAvailableItemStackSample();
            inputsForAspectCalc.add(inSample);
            remainingForAspectCalculation.add(new SameValueList<>(inSample.size(),component::getRemainingStack));
        }
        var centralSample = centralInput.getAvailableItemStackSample();
        inputsForAspectCalc.add(centralSample);
        remainingForAspectCalculation.add(new SameValueList<>(centralSample.size(),centralInput::getRemainingStack));
        this.inputForAspectCalculation = inputsForAspectCalc;
        this.remainingForAspectCalculation = remainingForAspectCalculation;
    }


    @Override
    public List<ItemStack> getExampleRecipeInput() {
        return centralInput.getAvailableItemStackSample();
    }

    public ItemStack getRecipeOutput(ItemStack input) {
        return output.copy();
    }
    @Override
    public boolean matches(List<ItemStack> input, ItemStack central, Level world, Player player, BlockPos probablyInfusionMatrixPos) {
        //infusionEnchantment check XP here
        return simpleMatch(input, central, player, research, components, centralInput);
    }

    @Override
    public ResearchItem getResearch() {
        return research;
    }

    @Override
    public AspectList<Aspect> getAspects(ItemStack input) {
        return aspectsRequiring;
    }

    @Override
    public int getInstability(ItemStack input) {
        return instability;
    }

    @Override
    public boolean matchViaOutput(ItemStack res) {
        return recipeOutputMatcher.matches(res);
    }



    protected final @NotNull List<List<ItemStack>> inputForAspectCalculation;
    protected final @NotNull List<List<Function<ItemStack,ItemStack>>> remainingForAspectCalculation;

    @Override
    public boolean supportsAspectCalculation() {
        return true;
    }

    @Override
    public @NotNull List<List<ItemStack>> getAspectCalculationInputs() {
        return inputForAspectCalculation;
    }

    @Override
    public @NotNull ItemStack getAspectCalculationOutput() {
        return output;
    }

    @Override
    public @NotNull List<List<Function<ItemStack,ItemStack>>> getAspectCalculationRemaining() {
        return remainingForAspectCalculation;
    }

    @Override
    public @NotNull AspectList<Aspect> getAspectCalculationAspectsList() {
        return aspectsRequiring;
    }

    @Override
    public @NotNull CentiVisList<Aspect> getAspectCalculationCentiVisList() {
        return UnmodifiableCentiVisList.EMPTY;
    }

    @Override
    public List<ItemStack> getRemainingStacks(List<ItemStack> inputsNotCenter){
        List<ItemStack> outStacks = new LinkedList<>();
        int counter = 0;
        for (var in: inputsNotCenter){
            var matcher = components.get(counter);
            if (!matcher.matches(in)){
                throw new IllegalArgumentException("should match recipe before getRemainingStacks!");
            }
            outStacks.add(matcher.getRemainingStack(in));
            counter += 1;
        }
        return outStacks;
    }
}
