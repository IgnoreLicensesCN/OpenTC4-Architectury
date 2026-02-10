package thaumcraft.api.crafting;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.*;
import thaumcraft.api.crafting.interfaces.IArcaneRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.tiles.abstracts.IArcaneWorkbenchContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.linearity.opentc4.utils.IndexPicker.pickByTime;

public class ShapelessArcaneRecipe implements IArcaneRecipe
{
    private final Function<ItemStack[],ItemStack> resultGenerator;
    private final RecipeItemMatcher[] input;
    private final ItemStack[] inputSampleArr;
    
    public final CentiVisList<Aspect>aspects;
    public final ResearchItem research;
    private final RecipeItemMatcher outMatcher;

    private final boolean supportsAspectCalculation;
    private final @NotNull List<List<ItemStack>> inputForAspectCalculation;
    private final @NotNull ItemStack outputForAspectCalculation;
    private final @NotNull List<List<ItemStack>> remainingForAspectCalculation;
    private final @NotNull CentiVisList<Aspect> centiVisListForCalculation;

    //do not set ItemStack.EMPTY matcher here.
    public ShapelessArcaneRecipe(
            ResearchItem research,
            Function<ItemStack[],ItemStack> resultGenerator,
            CentiVisList<Aspect>aspects,
            RecipeItemMatcher[] recipe,
            RecipeItemMatcher outMatcher,
            ItemStack outputForAspectCalculation,
            List<List<ItemStack>> inputForAspectCalculation,
            List<List<ItemStack>> remainingForAspectCalculation,
            CentiVisList<Aspect> centiVisListForCalculation
    )
    {
        this.resultGenerator = resultGenerator;
        this.research = research;
        this.aspects = new UnmodifiableCentiVisList<>(aspects.aspectView);
        this.input = recipe;
        this.inputSampleArr = new ItemStack[input.length];
        this.outMatcher = outMatcher;

        this.supportsAspectCalculation =
                inputForAspectCalculation != null
                        && outputForAspectCalculation != null
                        && remainingForAspectCalculation != null;
        if (!this.supportsAspectCalculation
                && !(inputForAspectCalculation == null
                        && outputForAspectCalculation == null
                        && remainingForAspectCalculation == null
                )
        ){
            OpenTC4.LOGGER.warn(
                    """
                            not all aspect calculation elements are null or notnull,
                            this might be a bug or misunderstanding.
                            using researchItem:{}
                            """,research,new Exception());
        }
        this.inputForAspectCalculation = inputForAspectCalculation == null?List.of():inputForAspectCalculation;
        this.outputForAspectCalculation = outputForAspectCalculation == null?ItemStack.EMPTY:outputForAspectCalculation;
        this.remainingForAspectCalculation = remainingForAspectCalculation == null?List.of():remainingForAspectCalculation;
        this.centiVisListForCalculation = centiVisListForCalculation == null? UnmodifiableCentiVisList.EMPTY:centiVisListForCalculation;
    }

    public ShapelessArcaneRecipe(
            ResearchItem research,
            Function<ItemStack[],ItemStack> resultGenerator,
            CentiVisList<Aspect>aspects,
            RecipeItemMatcher[] recipe,
            RecipeItemMatcher outMatcher
    )
    {
        this(research,resultGenerator,aspects,recipe,outMatcher,null,null,null,null);
    }

    @Override
    public int getRecipeSize(){ return input.length; }

    @Override
    public ItemStack getRecipeOutput(){ return getOutputSample(getInputSample())[0]; }

    @Override
    public ItemStack getCraftingResult(IArcaneWorkbenchContainer var1){
        List<ItemStack> inStacks = new ArrayList<>(9);
        int inCounter = 0;
        for (int x = 0; x < 9; x++)
        {
            ItemStack slot = var1.getItem(x);
            if (!slot.isEmpty()){
                inStacks.add(slot);
                inCounter += 1;
            }
        }
        if (input.length != inCounter){
            return ItemStack.EMPTY;
        }
        boolean[] used = new boolean[inCounter];
        if (matchPermutation(0, inStacks, used)) {
            return resultGenerator.apply(inStacks.toArray(new ItemStack[0]));
        }

        return ItemStack.EMPTY;


//        return output.copy();
    }

    private boolean matchPermutation(int index, List<ItemStack> inStacks, boolean[] used) {
        if (index == input.length) {
            return true; // 全部匹配成功
        }

        for (int i = 0; i < inStacks.size(); i++) {
            if (!used[i] && input[index].matches(inStacks.get(i))) {
                used[i] = true;
                if (matchPermutation(index + 1, inStacks, used)) {
                    return true;
                }
                used[i] = false;
            }
        }

        return false;
    }

    @Override
    public boolean matches(IArcaneWorkbenchContainer var1, Level world, Player player)
    {
    	if (!research.isPlayerCompletedResearch(player)) {
    		return false;
    	}

        return getCraftingResult(var1).isEmpty();
    	
//        ArrayList required = new ArrayList(input);
//
//        for (int x = 0; x < 9; x++)
//        {
//            ItemStack slot = var1.getItem(x);
//
//            if (!slot.isEmpty())
//            {
//                boolean inRecipe = false;
//
//                for (Object o : required) {
//                    boolean match = false;
//
//                    Object next = o;
//
//                    if (next instanceof ItemStack itemStack) {
//                        match = checkItemEquals(itemStack, slot);
//                    } else if (next instanceof List<?> list) {
//                        for (ItemStack item : (List<ItemStack>) list) {
//                            match = match || checkItemEquals(item, slot);
//                        }
//                    }
//
//                    if (match) {
//                        inRecipe = true;
//                        required.remove(next);
//                        break;
//                    }
//                }
//
//                if (!inRecipe)
//                {
//                    return false;
//                }
//            }
//        }
        
//        return required.isEmpty();
    }

//    private boolean checkItemEquals(ItemStack target, ItemStack input)
//    {
//        if (input == null && target != null || input != null && target == null)
//        {
//            return false;
//        }
//        return (target.getItem() == input.getItem() &&
//        		(!target.hasTagCompound() || ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(input,target)) &&
//        		(ignoresDamage(target)|| target.getDamageValue() == input.getDamageValue()));
//    }

//    /**
//     * Returns the input for this recipe, any mod accessing this value should never
//     * manipulate the values in this array as it will effect the recipe itself.
//     * @return The recipes input vales.
//     */
//    public ArrayList getInput()
//    {
//        return this.input;
//    }
    
    @Override		
	public CentiVisList<Aspect> getAspects() {
		return aspects;
	}
    
    @Override		
	public CentiVisList<Aspect> getAspects(IArcaneWorkbenchContainer inv) {
		return aspects;
	}
	
	@Override
	public ResearchItem getResearch() {
		return research;
	}

    @Override
    public ItemStack[] getInputSample() {
        for (int i=0;i<input.length;i++){
            inputSampleArr[i] = pickByTime(input[i].getAvailableItemStackSample());
        }
        return inputSampleArr;
    }


    private final ItemStack[] outputSampleArr = new ItemStack[1];
    @Override
    public ItemStack[] getOutputSample(ItemStack[] inputSample) {
        outputSampleArr[0] = resultGenerator.apply(inputSample);
        return outputSampleArr;
    }
    @Override
    public boolean matchViaOutput(ItemStack res) {
        return outMatcher.matches(res);
    }

    @Override
    public boolean supportsAspectCalculation() {
        return supportsAspectCalculation;
    }

    @Override
    public @NotNull List<List<ItemStack>> getAspectCalculationInputs() {
        if (!supportsAspectCalculation){
            throw new RuntimeException("check supportsAspectCalculation() first!");
        }
        return inputForAspectCalculation;
    }

    @Override
    public @NotNull ItemStack getAspectCalculationOutput() {
        if (!supportsAspectCalculation){
            throw new RuntimeException("check supportsAspectCalculation() first!");
        }
        return outputForAspectCalculation;
    }

    @Override
    public @NotNull List<List<ItemStack>> getAspectCalculationRemaining() {
        if (!supportsAspectCalculation){
            throw new RuntimeException("check supportsAspectCalculation() first!");
        }
        return remainingForAspectCalculation;
    }

    @Override
    public @NotNull AspectList<Aspect> getAspectCalculationAspectsList() {
        if (!supportsAspectCalculation){
            throw new RuntimeException("check supportsAspectCalculation() first!");
        }
        return UnmodifiableAspectList.EMPTY;
    }

    @Override
    public @NotNull CentiVisList<Aspect> getAspectCalculationCentiVisList() {
        if (!supportsAspectCalculation){
            throw new RuntimeException("check supportsAspectCalculation() first!");
        }
        return centiVisListForCalculation;
    }
}
