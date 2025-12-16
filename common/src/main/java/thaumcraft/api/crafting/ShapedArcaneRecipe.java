package thaumcraft.api.crafting;

import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.TileMagicWorkbench;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.linearity.opentc4.recipeclean.itemmatch.EmptyMatcher.EMPTY_MATCHER;
import static com.linearity.opentc4.utils.IndexPicker.pickByTime;

public class ShapedArcaneRecipe implements IArcaneRecipe
{
    //Added in for future ease of change, but hard coded for now.
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;

//    public ItemStack output = null;
    public final RecipeItemMatcher[] input;
    protected final Function<ItemStack[],ItemStack> resultGenerator;
//    public final AspectList aspects;
    public final Function<ItemStack[],AspectList> aspectsGenerator;
    public final String research;
    public final int width;
    public final int height;
    public final RecipeItemMatcher outMatcher;
    private boolean mirrored = true;

    //since the original is too messy,i should do some cleaning for this.
    public ShapedArcaneRecipe(String research, Function<ItemStack[],ItemStack> resultGenerator, AspectList aspects, RecipeItemMatcher[] input, int width, int height,RecipeItemMatcher outMatcher) {
        this(research,resultGenerator,arr -> aspects,input,width,height,outMatcher);
    }
    public ShapedArcaneRecipe(String research, Function<ItemStack[],ItemStack> resultGenerator, Function<ItemStack[],AspectList> aspectsGenerator, RecipeItemMatcher[] input, int width, int height,RecipeItemMatcher outMatcher) {
        if (input.length != width*height){
            throw new IllegalArgumentException("Invalid recipe shape!");
        }//yeah that's quite easy
        for (RecipeItemMatcher recipe : input){
            if (recipe == null){
                throw new IllegalArgumentException("Invalid recipe content!null should be replace with EMPTY_MATCHER!");
            }
        }
        this.resultGenerator = resultGenerator;
        this.research = research;
        this.aspectsGenerator = aspectsGenerator;
//        this.aspects = aspects;
//        StringBuilder shape = new StringBuilder();
        this.input = input;
        this.width = width;
        this.height = height;
        this.outMatcher = outMatcher;
        this.allSampled = new ItemStack[input.length][];
        for (int i=0;i<allSampled.length;i++){
            allSampled[i] = input[i].getAvailableItemStackSample().toArray(new ItemStack[0]);
        }
        
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
    public ItemStack getCraftingResult(Container var1){
        if (!matchesItems(var1)){
            return ItemStack.EMPTY;
        }
        return resultGenerator.apply(getInputItemStacks(var1));
    }

    @Override
    public int getRecipeSize(){ return input.length; }

    @Override
    public ItemStack getRecipeOutput(){
        return resultGenerator.apply(getInputSample());
    }

    @Override
    public boolean matches(Container inv, Level world, Player player)
    {
    	if (!research.isEmpty() && !ThaumcraftApiHelper.isResearchComplete(player.getName().getString(), research)) {
    		return false;
    	}

        return matchesItems(inv);
    }

    private boolean matchesItems(Container inv){
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
            {
                if (checkMatch(inv, x, y, false))
                {
                    return !resultGenerator.apply(getInputItemStacks(inv)).isEmpty();
                }

                if (mirrored && checkMatch(inv, x, y, true))
                {
                    return !resultGenerator.apply(getInputItemStacks(inv)).isEmpty();
                }
            }
        }
        return false;
    }

    public static ItemStack[] getInputItemStacks(Container inv){
        ItemStack[] inputItemStacks = new ItemStack[9];
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++){
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++){
                inputItemStacks[x + (y*2)] = ThaumcraftApiHelper.getStackInRowAndColumn((TileMagicWorkbench) inv, x, y);
            }
        }
        return inputItemStacks;
    }

    //so i've thrown item check job to RecipeItemMatcher.
    private boolean checkMatch(Container inv, int startX, int startY, boolean mirror)
    {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                RecipeItemMatcher target = EMPTY_MATCHER;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = input[width - subX - 1 + subY * width];
                    }
                    else
                    {
                        target = input[subX + subY * width];
                    }
                }

                ItemStack slot = ThaumcraftApiHelper.getStackInRowAndColumn((TileMagicWorkbench) inv, x, y);

                if (!target.matches(slot)){
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

    public ShapedArcaneRecipe setMirrored(boolean mirror)
    {
        mirrored = mirror;
        return this;
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     * @return The recipes input vales.
     */
    public RecipeItemMatcher[] getInput()
    {
        return this.input;
    }
    
    @Override		
	public AspectList getAspects() {
		return aspectsGenerator.apply(null);
	}
    
    @Override		
	public AspectList getAspects(Container inv) {
		return aspectsGenerator.apply(getInputItemStacks(inv));
	}
	
	@Override
	public String getResearch() {
		return research;
	}

    @Override
    public ItemStack[] getInputSample() {
        ItemStack[] sampled = new ItemStack[input.length];
        for (int i = 0; i < input.length; i++){
            sampled[i] = pickByTime(input[i].getAvailableItemStackSample());
        }
        return sampled;
    }

    private final ItemStack[][] allSampled;
    @Override
    public ItemStack[][] getAllInputSample(){
        return allSampled;
    }

    private final ItemStack[] resultStore = new ItemStack[1];
    @Override
    public ItemStack[] getOutputSample(ItemStack[] inputSample) {
        resultStore[0] = resultGenerator.apply(inputSample);
        return resultStore;
    }

    @Override
    public boolean matchViaOutput(ItemStack res) {
        return outMatcher.matches(res);
    }
}
