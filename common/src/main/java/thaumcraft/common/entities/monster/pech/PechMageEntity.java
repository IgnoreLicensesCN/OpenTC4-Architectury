package thaumcraft.common.entities.monster.pech;

import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.items.ThaumcraftItemInstances;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;

import java.util.ArrayList;
import java.util.function.Supplier;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.common.items.ThaumcraftItemInstances.*;
import static thaumcraft.common.items.ThaumcraftItems.ItemTags.CRYSTAL_CLUSTERS;
import static thaumcraft.common.items.ThaumcraftItems.ItemTags.PRIMAL_SHARDS;
import static thaumcraft.common.items.ThaumcraftItemsRegistry.RANDOM_MANA_BEAN_SUPPLIER;

public class PechMageEntity extends AbstractPechEntity {
    public PechMageEntity(Level world) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.PECH_MAGE(), world);
    }

    public PechMageEntity(EntityType<PechMageEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populatePechSpecificEquip(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        var wandItem = WAND_CASTING();
        ItemStack wand = WAND_CASTING().getDefaultInstance();
        ItemStack focus = PECH_FOCUS().getDefaultInstance();
        wandItem.changeFocusItemStack(wand, focus);
        wandItem.addCentiVis(wand, Aspects.EARTH, 2 + random.nextInt(6), true);
        wandItem.addCentiVis(wand, Aspects.ENTROPY, 2 + random.nextInt(6), true);
        wandItem.addCentiVis(wand, Aspects.WATER, 2 + random.nextInt(6), true);
        wandItem.addCentiVis(wand, Aspects.AIR, random.nextInt(4), true);
        wandItem.addCentiVis(wand, Aspects.FIRE, random.nextInt(4), true);
        wandItem.addCentiVis(wand, Aspects.ORDER, random.nextInt(4), true);
        setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, wand);
        setDropChance(EquipmentSlot.MAINHAND,0.1F);
    }

    public static final PechTradeManager.PechTradeOptionProviderExample MAGE_TRADES = new PechTradeManager.PechTradeOptionProviderExample();

    static {
        initTrades();
    }

    //mixin point
    private static void initTrades() {
        var optionsByValue = MAGE_TRADES.TRADE_OPTIONS_BY_VALUE;

        {
            var forValue1 = new ArrayList<Supplier<ItemStack>>();
            forValue1.add(RANDOM_MANA_BEAN_SUPPLIER);
            for (var i: platformUtils.getItemsFromTag(PRIMAL_SHARDS)){
                forValue1.add(i::getDefaultInstance);
            }
            forValue1.add(ThaumcraftItemInstances.KNOWLEDGE_FRAGMENT()::getDefaultInstance);
            optionsByValue.add(new ObjectIntPair<>(forValue1, 1));
        }
        {
            var forValue2 = new ArrayList<Supplier<ItemStack>>();
            forValue2.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.REGENERATION));
            forValue2.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING));
            forValue2.add(ThaumcraftItemInstances.KNOWLEDGE_FRAGMENT()::getDefaultInstance);
            optionsByValue.add(new ObjectIntPair<>(forValue2, 2));
        }
        {
            var forValue3 = new ArrayList<Supplier<ItemStack>>();

            var book = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(
                    book,
                    new EnchantmentInstance(ThaumcraftEnchantments.ThaumcraftEnchantmentInstances.HASTE(), 1)
            );
            forValue3.add(book::copy);
            forValue3.add(Items.GOLDEN_APPLE::getDefaultInstance);
            forValue3.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION));
            forValue3.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING));

            optionsByValue.add(new ObjectIntPair<>(forValue3,3));
        }
        {
            var forValue4 = new ArrayList<Supplier<ItemStack>>();
            platformUtils.getItemsFromTag(CRYSTAL_CLUSTERS).forEach(item -> forValue4.add(item::getDefaultInstance));
            optionsByValue.add(new ObjectIntPair<>(forValue4, 4));
        }
        {
            var forValue5 = new ArrayList<Supplier<ItemStack>>();
            forValue5.add(Items.GOLDEN_APPLE::getDefaultInstance);
            var book = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(
                    book,
                    new EnchantmentInstance(ThaumcraftEnchantments.ThaumcraftEnchantmentInstances.REPAIR(), 1)
            );
            forValue5.add(book::copy);
            forValue5.add(ThaumcraftItemInstances.FOCUS_POUCH()::getDefaultInstance);
            forValue5.add(ThaumcraftItemInstances.PECH_FOCUS()::getDefaultInstance);
            forValue5.add(ThaumcraftItemInstances.VIS_AMULET()::getDefaultInstance);

            optionsByValue.add(new ObjectIntPair<>(forValue5, 5));
        }
    }

    @Override
    public PechTradeManager.PechTradeOptionProvider getTradeOptions() {
        return MAGE_TRADES;
    }
}
