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

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.linearity.opentc4.recipeclean.itemmatch.EmptyMatcher.EMPTY_MATCHER;
import static com.linearity.opentc4.utils.IndexPicker.pickByTime;

public class ShapedArcaneRecipe implements IArcaneRecipe {
    //Added in for future ease ofAspectVisList change, but hard coded for now.
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;

    //    public ItemStack output = null;
    public final RecipeItemMatcher[] input;
    private final ItemStack[] sampleArr;
    protected final Function<List<ItemStack>, ItemStack> resultGenerator;
    //    public final AspectList<Aspect>aspects;
    public final Function<List<ItemStack>, CentiVisList<Aspect>> aspectsGenerator;
    public final ResearchItem research;
    public final int width;
    public final int height;
    public final RecipeItemMatcher outMatcher;
    private boolean mirrored = true;

    private final boolean supportsAspectCalculation;
    private final @NotNull List<List<ItemStack>> inputForAspectCalculation;
    private final @NotNull ItemStack outputForAspectCalculation;
    private final @NotNull List<List<ItemStack>> remainingForAspectCalculation;
    private final @NotNull CentiVisList<Aspect> centiVisListForCalculation;
    //since the original is too messy,i should do some cleaning for this.
    public ShapedArcaneRecipe(
            ResearchItem research,
            Function<List<ItemStack>, ItemStack> resultGenerator,
            AspectList<Aspect> aspects,
            RecipeItemMatcher[] input,
            int width,
            int height,
            RecipeItemMatcher outMatcher
    ) {
        this(research, resultGenerator, arr -> CentiVisList.ofAspectVisList(aspects), input, width, height, outMatcher);
    }

    public ShapedArcaneRecipe(
            ResearchItem research,
            Function<List<ItemStack>, ItemStack> resultGenerator,
            CentiVisList<Aspect> aspects,
            RecipeItemMatcher[] input,
            int width,
            int height,
            RecipeItemMatcher outMatcher
    ) {
        this(
                research,
                resultGenerator,
                arr -> aspects,
                input,
                width,
                height,
                outMatcher
        );
    }

    public ShapedArcaneRecipe(
            ResearchItem research,
            Function<List<ItemStack>, ItemStack> resultGenerator,
            Function<List<ItemStack>, CentiVisList<Aspect>> aspectsGenerator,
            RecipeItemMatcher[] input,
            int width,
            int height,
            RecipeItemMatcher outMatcher
    ){
        this(research,resultGenerator,aspectsGenerator,input,width,height,outMatcher,null,null,null,null);
    }
    public ShapedArcaneRecipe(
            ResearchItem research,
            Function<List<ItemStack>, ItemStack> resultGenerator,
            Function<List<ItemStack>, CentiVisList<Aspect>> aspectsGenerator,
            RecipeItemMatcher[] input,
            int width,
            int height,
            RecipeItemMatcher outMatcher,
            ItemStack outputForAspectCalculation,
            List<List<ItemStack>> inputForAspectCalculation,
            List<List<ItemStack>> remainingForAspectCalculation,
            CentiVisList<Aspect> centiVisListForCalculation
    ) {
        if (input.length != width * height) {
            throw new IllegalArgumentException("Invalid recipe shape!");
        }//yeah that's quite easy
        for (RecipeItemMatcher recipe : input) {
            if (recipe == null) {
                throw new IllegalArgumentException("Invalid recipe content!null should be replace with EMPTY_MATCHER!");
            }
        }
        this.resultGenerator = resultGenerator;
        this.research = research;
        this.aspectsGenerator = aspectsGenerator;
//        this.aspects = aspects;
//        StringBuilder shape = new StringBuilder();
        this.input = input;
        this.sampleArr = new ItemStack[input.length];
        this.width = width;
        this.height = height;
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

//        int idx = 0;

//        if (recipe[idx] instanceof Boolean)
//        {
//            mirrored = (Boolean)recipe[idx];
//            if (recipe[idx+1] instanceof Object[])
//            {
//                recipe = (Object[])recipe[idx+1];
//            }
//            else
//            {
//                idx = 1;
//            }
//        }
//
//        if (recipe[idx] instanceof String[])
//        {
//            String[] parts = ((String[])recipe[idx++]);
//
//            for (String s : parts)
//            {
//                width = s.length();
//                shape.append(s);
//            }
//
//            height = parts.length;
//        }
//        else
//        {
//            while (recipe[idx] instanceof String)
//            {
//                String s = (String)recipe[idx++];
//                shape.append(s);
//                width = s.length();
//                height++;
//            }
//        }

//        if (width * height != shape.length())
//        {
//            StringBuilder ret = new StringBuilder("Invalid shaped ore recipe: ");
//            for (Object tmp :  recipe)
//            {
//                ret.append(tmp).append(", ");
//            }
//            ret.append(output);
//            throw new RuntimeException(ret.toString());
//        }
//
//        HashMap<Character, Object> itemMap = new HashMap<>();

//        for (; idx < recipe.length; idx += 2)
//        {
//            Character chr = (Character)recipe[idx];
//            Object in = recipe[idx + 1];
//
//            if (in instanceof ItemStack)
//            {
//                itemMap.put(chr, ((ItemStack)in).copy());
//            }
//            else if (in instanceof Item)
//            {
//                itemMap.put(chr, new ItemStack((Item)in));
//            }
//            else if (in instanceof Block)
//            {
//                itemMap.put(chr, new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE));
//            }
//            else if (in instanceof String)
//            {
//                itemMap.put(chr, OreDictionary.getOres((String)in));
//            }
//            else
//            {
//                StringBuilder ret = new StringBuilder("Invalid shaped ore recipe: ");
//                for (Object tmp :  recipe)
//                {
//                    ret.append(tmp).append(", ");
//                }
//                ret.append(output);
//                throw new RuntimeException(ret.toString());
//            }
//        }

//        input = new Object[width * height];
//        int x = 0;
//        for (char chr : shape.toString().toCharArray())
//        {
//            input[x++] = itemMap.get(chr);
//        }
    }

    @Override
    public ItemStack getCraftingResult(IArcaneWorkbenchContainer var1) {
        if (!matchesItems(var1)) {
            return ItemStack.EMPTY;
        }
        return resultGenerator.apply(var1.getInputItemStacks());
    }

    @Override
    public int getRecipeSize() {
        return input.length;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return resultGenerator.apply(List.of(getInputSample()));
    }

    @Override
    public boolean matches(IArcaneWorkbenchContainer inv, Level world, Player player) {
        if (research.isPlayerCompletedResearch(player)) {
            return false;
        }

        return matchesItems(inv);
    }

    private boolean matchesItems(IArcaneWorkbenchContainer inv) {
        for (int xOffset = 0; xOffset <= MAX_CRAFT_GRID_WIDTH - this.width; xOffset++) {
            for (int yOffset = 0; yOffset <= MAX_CRAFT_GRID_HEIGHT - this.height; ++yOffset) {
                if (checkMatch(inv, xOffset, yOffset, false)) {
                    return !resultGenerator.apply(inv.getInputItemStacks())
                            .isEmpty();
                }

                if (mirrored && checkMatch(inv, xOffset, yOffset, true)) {
                    return !resultGenerator.apply(inv.getInputItemStacks())
                            .isEmpty();
                }
            }
        }
        return false;
    }


    //so i've thrown item check job to RecipeItemMatcher.
    private boolean checkMatch(IArcaneWorkbenchContainer inv, int startX, int startY, boolean mirror) {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
                int subX = x - startX;
                int subY = y - startY;
                RecipeItemMatcher target = EMPTY_MATCHER;

                if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
                    if (mirror) {
                        target = input[this.width - subX - 1 + subY * this.width];
                    } else {
                        target = input[subX + subY * this.width];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x,y);

                if (!target.matches(slot)) {
                    return false;
                }
//                if (target instanceof ItemStack)
//                {
//                    if (!checkItemEquals((ItemStack)target, slot))
//                    {
//                        return false;
//                    }
//                }
//                else if (target instanceof ArrayList)
//                {
//                    boolean matched = false;
//
//                    for (ItemStack item : (ArrayList<ItemStack>)target)
//                    {
//                        matched = matched || checkItemEquals(item, slot);
//                    }
//
//                    if (!matched)
//                    {
//                        return false;
//                    }
//                }
//                else if (target == null && slot != null)
//                {
//                    return false;
//                }
            }
        }

        return true;
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

    public ShapedArcaneRecipe setMirrored(boolean mirror) {
        mirrored = mirror;
        return this;
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     *
     * @return The recipes input vales.
     */
    public RecipeItemMatcher[] getInput() {
        return this.input;
    }

    @Override
    public CentiVisList<Aspect> getAspects() {
        return aspectsGenerator.apply(null);
    }

    @Override
    public CentiVisList<Aspect> getAspects(IArcaneWorkbenchContainer inv) {
        return aspectsGenerator.apply(inv.getInputItemStacks());
    }

    @Override
    public ResearchItem getResearch() {
        return research;
    }

    @Override
    public ItemStack[] getInputSample() {
        for (int i = 0; i < input.length; i++) {
            sampleArr[i] = pickByTime(input[i].getAvailableItemStackSample());
        }
        return sampleArr;
    }

    private final ItemStack[] resultStore = new ItemStack[1];

    @Override
    public ItemStack[] getOutputSample(ItemStack[] inputSample) {
        resultStore[0] = resultGenerator.apply(Arrays.asList(inputSample));
        return resultStore;
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
