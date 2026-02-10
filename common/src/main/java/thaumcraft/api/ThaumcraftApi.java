package thaumcraft.api;

import com.linearity.opentc4.simpleutils.SimplePair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.*;
import thaumcraft.api.internal.WeightedRandomLootCollection;
import thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration;
import thaumcraft.api.research.*;
import thaumcraft.api.research.interfaces.IResearchWarpOwner;
import thaumcraft.api.research.scan.IScanEventHandler;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.OpenTC4.platformUtils;


/**<p>
 * @author Azanor
 * </p>
  <p>
<s>
IMPORTANT: If you are adding your own aspects to items it is a good idea to do it AFTER Thaumcraft adds its aspects, otherwise odd things may happen.
</s>
</p>
 * <p><u>from IgnoreLicensesCN:it's doesnt matter now,
 * just remember to initialize Aspects.class,
 * but when you create an aspect you will surly attach to its <a><</a>clinit<a>></a>
 * because calling new Aspect() will put it into a static map in Aspects.class</u></p>
 */
public class ThaumcraftApi {
    public static final Tier TOOL_THAUMIUM = new Tier() {
        @Override
        public int getUses() {
            return 400;
        }

        @Override
        public float getSpeed() {
            return 7F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2F;
        }

        @Override
        public int getLevel() {
            return 3;
        }

        @Override
        public int getEnchantmentValue() {
            return 22;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(ThaumcraftItems.THAUMIUM_INGOT);
        }
    };
    public static final Tier TOOL_VOID = new Tier() {
        @Override
        public int getUses() {
            return 150;
        }

        @Override
        public float getSpeed() {
            return 8F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3F;
        }

        @Override
        public int getLevel() {
            return 4;
        }

        @Override
        public int getEnchantmentValue() {
            return 10;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(ThaumcraftItems.VOID_INGOT);
        }
    };
    public static final Tier TOOL_THAUMIUM_ELEMENTAL = new Tier() {
        @Override
        public int getUses() {
            return 1500;
        }

        @Override
        public float getSpeed() {
            return 10F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3F;
        }

        @Override
        public int getLevel() {
            return 3;
        }

        @Override
        public int getEnchantmentValue() {
            return 18;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(ThaumcraftItems.THAUMIUM_INGOT);
        }
    };
    public static final ArmorMaterial THAUMIUM = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 13 * 25;
                case CHESTPLATE -> 15 * 25;
                case LEGGINGS -> 16 * 25;
                case BOOTS -> 11 * 25;
            };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 2;
                case CHESTPLATE -> 6;
                case LEGGINGS -> 5;
                case BOOTS -> 2;
            };
        }

        @Override
        public int getEnchantmentValue() {
            return 25;
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(ThaumcraftItems.THAUMIUM_INGOT);
        }

        @Override
        public @NotNull String getName() {
            return "thaumium";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    };
    public static final ArmorMaterial SPECIAL = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 13 * 25;
                case CHESTPLATE -> 15 * 25;
                case LEGGINGS -> 16 * 25;
                case BOOTS -> 11 * 25;
            };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 1;
                case CHESTPLATE -> 3;
                case LEGGINGS -> 2;
                case BOOTS -> 1;
            };
        }

        @Override
        public int getEnchantmentValue() {
            return 25;
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }

        @Override
        public @NotNull String getName() {
            return "special";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    };
    public static final ArmorMaterial THAUMIUM_FORTRESS = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 13 * 40;
                case CHESTPLATE -> 15 * 40;
                case LEGGINGS -> 16 * 40;
                case BOOTS -> 11 * 40;
            };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 3;
                case CHESTPLATE -> 7;
                case LEGGINGS -> 6;
                case BOOTS -> 3;
            };
        }

        @Override
        public int getEnchantmentValue() {
            return 25;
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(ThaumcraftItems.THAUMIUM_INGOT);
        }

        @Override
        public @NotNull String getName() {
            return "fortress";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    };
    public static final ArmorMaterial VOID = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 13 * 10;
                case CHESTPLATE -> 15 * 10;
                case LEGGINGS -> 16 * 10;
                case BOOTS -> 11 * 10;
            };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 3;
                case CHESTPLATE -> 7;
                case LEGGINGS -> 6;
                case BOOTS -> 3;
            };
        }

        @Override
        public int getEnchantmentValue() {
            return 10;
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(ThaumcraftItems.VOID_INGOT);
        }

        @Override
        public @NotNull String getName() {
            return "void";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    };
    public static final ArmorMaterial VOID_FORTRESS = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 13 * 18;
                case LEGGINGS -> 15 * 18;
                case CHESTPLATE -> 16 * 18;
                case BOOTS -> 11 * 18;
            };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 4;
                case CHESTPLATE -> 8;
                case LEGGINGS -> 7;
                case BOOTS -> 4;
            };
        }

        @Override
        public int getEnchantmentValue() {
            return 10;
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(ThaumcraftItems.VOID_INGOT);
        }

        @Override
        public @NotNull String getName() {
            return "voidfortress";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    };

    //Enchantment references
    public static int enchantFrugal;
    public static int enchantPotency;
    public static int enchantWandFortune;
    public static int enchantHaste;
    public static int enchantRepair;

    //Miscellaneous
    /**
     * Portable Hole Block-id Blacklist.
     * Simply add the block-id's ofAspectVisList blocks you don't want the portable hole to go through.
     */
    public static ArrayList<Block> portableHoleBlackList = new ArrayList<>();

    //Internal (Do not alter this unless you like pretty explosions)
    //Calling methods from this will only work properly once Thaumcraft is past the FMLPreInitializationEvent phase.
//    public static IInternalMethodHandler internalMethods = new DummyInternalMethodHandler();

    //RESEARCH/////////////////////////////////////////
    public static ArrayList<IScanEventHandler> scanEventhandlers = new ArrayList<>();
    public static ArrayList<EntityTags> scanEntities = new ArrayList<>();

    public static class EntityTagsNBT {
        public EntityTagsNBT(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String name;
        public Object value;
    }

    public static class EntityTags {
        public EntityTags(String entityName, AspectList<Aspect>aspects, EntityTagsNBT... nbts) {
            this.entityName = entityName;
            this.nbts = nbts;
            this.aspects = aspects;
        }

        public String entityName;
        public EntityTagsNBT[] nbts;
        public AspectList<Aspect>aspects;
    }

    /**
     * not really working atm, so ignore it for now
     *
     * @param scanEventHandler
     */
    public static void registerScanEventhandler(IScanEventHandler scanEventHandler) {
        scanEventhandlers.add(scanEventHandler);
    }

    /**
     * This is used to add aspects to entities which you can then scan using a thaumometer.
     * Also used to calculate vis drops from mobs.
     *
     * @param entityName
     * @param aspects
     * @param nbt        you can specify certain nbt keys and their values
     *                   to differentiate between mobs. <br>For example the normal and wither skeleton:
     *                   <br>ThaumcraftApi.registerEntityTag("Skeleton", (new AspectList<>()).add(Aspect.DEATH, 5));
     *                   <br>ThaumcraftApi.registerEntityTag("Skeleton", (new AspectList<>()).add(Aspect.DEATH, 8), new NBTTagByte("SkeletonType",(byte) 1));
     */
    public static void registerEntityTag(String entityName, AspectList<Aspect>aspects, EntityTagsNBT... nbt) {
        scanEntities.add(new EntityTags(entityName, aspects, nbt));
    }

    //	@Deprecated(forRemoval = true)
//	private static HashMap<Object,ItemStack> smeltingBonus = new HashMap<>();


    //	@UnmodifiableView
//	public static List<RecipeInAndOutSampler> getCraftingRecipes() {
//		return Collections.unmodifiableList(craftingRecipes);
//	}


    /**
     * Used by the thaumonomicon drilldown feature.
     *
     * @param stack the item
     * @return the thaumcraft recipe key that produces that item.
     */
    private static final HashMap<SimplePair<Item, Integer>, ResearchKeyAndPage> keyCache = new HashMap<>();

    public record ResearchKeyAndPage(String research, int page) {
        @Override
        public boolean equals(Object o) {

            if (!(o instanceof ResearchKeyAndPage that)) return false;
            return page == that.page && Objects.equals(research, that.research);
        }

        @Override
        public int hashCode() {
            return Objects.hash(research, page);
        }
    }

    public static ResearchKeyAndPage getCraftingRecipeKey(Player player, ItemStack stack) {
        SimplePair<Item, Integer> key = new SimplePair<>(stack.getItem(), stack.getDamageValue());
        if (keyCache.containsKey(key)) {
            if (keyCache.get(key) == null) return null;
            if (ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), (keyCache.get(key)).research))
                return keyCache.get(key);
            else
                return null;
        }
        for (ResearchCategory rcl : ResearchCategory.researchCategories.values()) {
            for (ResearchItem ri : rcl.researches.values()) {
                if (ri.getPages() == null) continue;
                for (int a = 0; a < ri.getPages().length; a++) {
                    ResearchPage page = ri.getPages()[a];
                    if (page.recipe instanceof CrucibleRecipe[] crs) {
                        for (CrucibleRecipe cr : crs) {
                            if (cr.matchViaOutput(stack)) {
                                keyCache.put(key, new ResearchKeyAndPage(ri.key, a));
                                if (ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), ri.key))
                                    return new ResearchKeyAndPage(ri.key, a);
                            }
                        }
                    } else if (page.recipeOutput != null
                            && page.recipeOutput.getDamageValue() == stack.getDamageValue()
                            && Objects.equals(page.recipeOutput.getItem(), stack.getItem())
                    ) {
                        keyCache.put(key, new ResearchKeyAndPage(ri.key, a));
                        if (ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), ri.key))
                            return new ResearchKeyAndPage(ri.key, a);
                        else
                            return null;
                    }
                }
            }
        }
        keyCache.put(key, null);
        return null;
    }

    //ASPECTS////////////////////////////////////////

    public static ConcurrentHashMap<Item, AspectList<Aspect>> objectTags = new ConcurrentHashMap<>();

    /**
     * Checks to see if the passed item/block already has aspects associated with it.
     *
     * @param item
     * @return existence
     */
    @Deprecated(forRemoval = true,since = "prepare for new api")
    public static boolean exists(Item item) {
        return ThaumcraftApi.objectTags.contains(item);
    }

    /**
     * Used to assign apsects to the given ore dictionary item.
     *
     * @param tagString the ore dictionary name
     * @param aspects   A ObjectTags object ofAspectVisList the associated aspects
     */
    @Deprecated(forRemoval = true,since = "prepare for new api")
    public static void registerObjectTag(String tagString, AspectList<Aspect>aspects) {
        if (aspects == null) aspects = new AspectList<>();
        List<ItemStack> ores = platformUtils.getItemsFromTag(tagString).stream().map(ItemStack::new).toList();
        if (!ores.isEmpty()) {
            for (ItemStack ore : ores) {
                try {
                    objectTags.put(ore.getItem(), aspects);
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Used to assign aspects to the given item/block.
     * Attempts to automatically generate aspect tags by checking registered recipes.
     * Here is an example ofAspectVisList the declaration for pistons:<p>
     * <i>ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.cobblestone), (new AspectList<>()).add(Aspect.MECHANISM, 2).add(Aspect.MOTION, 4));</i>
     * IMPORTANT - this should only be used if you are not happy with the default aspects the object would be assigned.
     *
     * @param item,   pass OreDictionary.WILDCARD_VALUE to meta if all damage values ofAspectVisList this item/block should have the same aspects
     * @param aspects A ObjectTags object ofAspectVisList the associated aspects
     */
    @Deprecated(forRemoval = true,since = "prepare for new api")
    public static void registerComplexObjectTag(ItemStack item, AspectList<Aspect> aspects) {
        if (!exists(item.getItem())) {
            AspectList<Aspect> tmp = ThaumcraftApiHelper.generateBaseAspects(item.getItem());
            if (tmp != null && !tmp.isEmpty()) {
                for (Aspect tag : tmp.getAspectTypes()) {
                    aspects.addAll(tag, tmp.getAmount(tag));
                }
            }
            ItemBasicAspectRegistration.registerItemBasicAspects(item, aspects);
        } else {
            AspectList<Aspect> tmp = ThaumcraftApiHelper.getObjectAspects(item);
            for (Aspect tag : aspects.getAspectTypes()) {
                tmp.mergeWithHighest(tag, tmp.getAmount(tag));
            }
            ItemBasicAspectRegistration.registerItemBasicAspects(item, tmp);
        }
    }

    //WARP ///////////////////////////////////////////////////////////////////////////////////////
    private static final Map<Item, Integer> itemWarpMap = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, Integer> clueWarpMap = new ConcurrentHashMap<>();

    /**
     * This method is used to determine how much warp is gained if the item is crafted. The warp
     * added is "sticky" warp
     *
     * @param craftresult The item crafted
     * @param amount      how much warp is gained
     */
    public static void addWarpToItem(ItemStack craftresult, int amount) {
        itemWarpMap.put(craftresult.getItem(), amount);
    }

    public static void addWarpToClue(ResourceLocation research, int amount) {
        clueWarpMap.put(research, amount);
    }


    /**
     * Returns how much warp is gained from the item or research passed in
     *
     * @param in itemstack or string
     * @return how much warp it will give
     */
    public static int getResearchWarp(Item in) {
        return itemWarpMap.getOrDefault(in,0);
    }
    @Deprecated(forRemoval = true)
    public static int getResearchWarp(ResearchItemResourceLocation in) {
        var research = ResearchItem.getResearch(in);
        if (!(research instanceof IResearchWarpOwner warpOwner)){
            return 0;
        }
        return warpOwner.getWarp();
    }
    public static int getClueWarp(ResourceLocation in) {
        return clueWarpMap.getOrDefault(in,0);
    }

    //LOOT BAGS //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Used to add possible loot to treasure bags. As a reference, the weight ofAspectVisList gold coins are 2000
     * and a diamond is 50.
     * The weights are the same for all loot bag types - the only difference is how many items the bag
     * contains.
     *
     * @param item
     * @param weight
     * @param bagTypes array ofAspectVisList which type ofAspectVisList bag to add this loot to. Multiple types can be specified
     *                 0 = common, 1 = uncommon, 2 = rare
     */
    //TODO:More LootBag
    public static void addLootBagItem(ItemStack item, int weight, int... bagTypes) {
        if (bagTypes == null || bagTypes.length == 0)
            WeightedRandomLootCollection.lootBagCommon.add(new WeightedRandomLootCollection.WeightedRandomLoot(item, weight));
        else {
            for (int rarity : bagTypes) {
                switch (rarity) {
                    case 0:
                        WeightedRandomLootCollection.lootBagCommon.add(new WeightedRandomLootCollection.WeightedRandomLoot(item, weight));
                        break;
                    case 1:
                        WeightedRandomLootCollection.lootBagUncommon.add(new WeightedRandomLootCollection.WeightedRandomLoot(item, weight));
                        break;
                    case 2:
                        WeightedRandomLootCollection.lootBagRare.add(new WeightedRandomLootCollection.WeightedRandomLoot(item, weight));
                        break;
                }
            }
        }
    }

    //CROPS //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * To define mod crops you need to use FMLInterModComms in your @Mod.Init method.
     * There are two 'types' ofAspectVisList crops you can add. Standard crops and clickable crops.
     *
     * Standard crops work like normal vanilla crops - they grow until a certain metadata
     * value is reached and you harvest them by destroying the block and collecting the blocks.
     * You need to create and ItemStack that tells the golem what block id and metadata represents
     * the crop when fully grown. Sending a metadata ofAspectVisList [OreDictionary.WILDCARD_VALUE] will mean the metadata won't get
     * checked.
     * Example for vanilla wheat:
     * FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", new ItemStack(Block.crops,1,7));
     *
     * Clickable crops are crops that you right click to gather their bounty instead ofAspectVisList destroying them.
     * As for standard crops, you need to create and ItemStack that tells the golem what block id
     * and metadata represents the crop when fully grown. The golem will trigger the blocks onBlockActivated method.
     * Sending a metadata ofAspectVisList [OreDictionary.WILDCARD_VALUE] will mean the metadata won't get checked.
     * Example (this will technically do nothing since clicking wheat does nothing, but you get the idea):
     * FMLInterModComms.sendMessage("Thaumcraft", "harvestClickableCrop", new ItemStack(Block.crops,1,7));
     *
     * Stacked crops (like reeds) are crops that you wish the bottom block should remain after harvesting.
     * As for standard crops, you need to create and ItemStack that tells the golem what block id
     * and metadata represents the crop when fully grown. Sending a metadata ofAspectVisList [OreDictionary.WILDCARD_VALUE] will mean the actualy md won't get
     * checked. If it has the order upgrade it will only harvest if the crop is more than one block high.
     * Example:
     * FMLInterModComms.sendMessage("Thaumcraft", "harvestStackedCrop", new ItemStack(Block.reed,1,7));
     */

    //NATIVE CLUSTERS //////////////////////////////////////////////////////////////////////////////////

    /**
     * You can define certain ores that will have a chance to produce native clusters via FMLInterModComms
     * in your @Mod.Init method using the "nativeCluster" string message.
     * The format should be:
     * "[ore item/block id],[ore item/block metadata],[cluster item/block id],[cluster item/block metadata],[chance modifier float]"
     *
     * NOTE: The chance modifier is a multiplier applied to the default chance for that cluster to be produced (default 27.5% for a pickaxe ofAspectVisList the core)
     *
     * Example for vanilla iron ore to produce one ofAspectVisList my own native iron clusters (assuming default id's) at double the default chance:
     * FMLInterModComms.sendMessage("Thaumcraft", "nativeCluster","15,0,25016,16,2.0");
     */

    //LAMP OF GROWTH BLACKLIST ///////////////////////////////////////////////////////////////////////////
    /**
     * You can blacklist crops that should not be effected by the Lamp ofAspectVisList Growth via FMLInterModComms
     * in your @Mod.Init method using the "lampBlacklist" itemstack message.
     * Sending a metadata ofAspectVisList [OreDictionary.WILDCARD_VALUE] will mean the metadata won't get checked.
     * Example for vanilla wheat:
     * FMLInterModComms.sendMessage("Thaumcraft", "lampBlacklist", new ItemStack(Block.crops,1,OreDictionary.WILDCARD_VALUE));
     */

    //DIMENSION BLACKLIST ///////////////////////////////////////////////////////////////////////////
    /**
     * You can blacklist a dimension to not spawn certain thaumcraft features
     * in your @Mod.Init method using the "dimensionBlacklist" string message in the format "[dimension]:[level]"
     * The level values are as follows:
     * [0] stop all tc spawning and generation
     * [1] allow ore and node generation (and node special features)
     * [2] allow mob spawning
     * [3] allow ore and node gen + mob spawning (and node special features)
     * Example:
     * FMLInterModComms.sendMessage("Thaumcraft", "dimensionBlacklist", "15:1");
     */

    //BIOME BLACKLIST ///////////////////////////////////////////////////////////////////////////
    /**
     * You can blacklist a biome to not spawn certain thaumcraft features
     * in your @Mod.Init method using the "biomeBlacklist" string message in the format "[biome id]:[level]"
     * The level values are as follows:
     * [0] stop all tc spawning and generation
     * [1] allow ore and node generation (and node special features)
     * [2] allow mob spawning
     * [3] allow ore and node gen + mob spawning (and node special features)
     * Example:
     * FMLInterModComms.sendMessage("Thaumcraft", "biomeBlacklist", "180:2");
     */

    //CHAMPION MOB WHITELIST ///////////////////////////////////////////////////////////////////////////
    /**
     * You can whitelist an entity class so it can rarely spawn champion versions in your @Mod.Init method using
     * the "championWhiteList" string message in the format "[Entity]:[level]"
     * The entity must extend EntityMob.
     * [Entity] is in a similar format to what is used for mob spawners and such (see EntityList.class for vanilla examples).
     * The [level] value indicate how rare the champion version will be - the higher the number the more common.
     * The number roughly equals the [n] in 100 chance ofAspectVisList a mob being a champion version.
     * You can give 0 or negative numbers to allow champions to spawn with a very low chance only in particularly dangerous places.
     * However anything less than about -2 will probably result in no spawns at all.
     * Example:
     * FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Thaumcraft.Wisp:1");
     */
}
