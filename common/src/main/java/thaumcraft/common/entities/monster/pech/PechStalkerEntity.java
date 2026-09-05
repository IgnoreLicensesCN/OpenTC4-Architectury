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
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.items.ThaumcraftItemInstances;

import java.util.ArrayList;
import java.util.function.Supplier;

import static thaumcraft.common.items.ThaumcraftItemsRegistry.RANDOM_MANA_BEAN_SUPPLIER;

public class PechStalkerEntity extends AbstractPechEntity{
    public PechStalkerEntity(Level world) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.PECH_STALKER(),world);
    }
    public PechStalkerEntity(EntityType<PechStalkerEntity> entityType, Level level) {
        super(entityType, level);
    }


    public static final PechTradeManager.PechTradeOptionProviderExample STALKER_TRADES = new PechTradeManager.PechTradeOptionProviderExample();

    static {
        initTrades();
    }

    //mixin point
    private static void initTrades() {
        var optionsByValue = STALKER_TRADES.TRADE_OPTIONS_BY_VALUE;
        {
            var forValue1 = new ArrayList<Supplier<ItemStack>>();
            forValue1.add(ThaumcraftItemInstances.BLACK_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.WHITE_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.CYAN_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.PURPLE_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.PINK_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.RED_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.BLUE_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.GREEN_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.ORANGE_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.GRAY_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.LIME_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.BROWN_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.MAGENTA_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.YELLOW_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.LIGHT_GRAY_TALLOW_CANDLE()::getDefaultInstance);
            forValue1.add(ThaumcraftItemInstances.LIGHT_GRAY_TALLOW_CANDLE()::getDefaultInstance);

            forValue1.add(RANDOM_MANA_BEAN_SUPPLIER);
            optionsByValue.add(new ObjectIntPair<>(forValue1, 1));
        }
        {
            var forValue2 = new ArrayList<Supplier<ItemStack>>();
            forValue2.add(Items.GHAST_TEAR::getDefaultInstance);
            forValue2.add(ThaumcraftItemInstances.GREATWOOD_SAPLING()::getDefaultInstance);
            forValue2.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH));
            forValue2.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS));

            var book = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(
                    book,
                    new EnchantmentInstance(Enchantments.POWER_ARROWS, 1)
            );
            forValue2.add(book::copy);
            optionsByValue.add(new ObjectIntPair<>(forValue2, 2));
        }
        {

            var forValue3 = new ArrayList<Supplier<ItemStack>>();
            forValue3.add(Items.EXPERIENCE_BOTTLE::getDefaultInstance);
            forValue3.add(ThaumcraftItemInstances.KNOWLEDGE_FRAGMENT()::getDefaultInstance);
            forValue3.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_INVISIBILITY));
            forValue3.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION));
            forValue3.add(Items.GOLDEN_APPLE::getDefaultInstance);
            optionsByValue.add(new ObjectIntPair<>(forValue3, 3));
        }
        {

            var forValue4 = new ArrayList<Supplier<ItemStack>>();
            forValue4.add(ThaumcraftItemInstances.THAUMIUM_BOOTS()::getDefaultInstance);
            optionsByValue.add(new ObjectIntPair<>(forValue4, 4));
        }
        {

            var forValue5 = new ArrayList<Supplier<ItemStack>>();
            forValue5.add(Items.ENCHANTED_GOLDEN_APPLE::getDefaultInstance);
            forValue5.add(ThaumcraftItemInstances.PROTECTION_RING()::getDefaultInstance);

            var book = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(
                    book,
                    new EnchantmentInstance(Enchantments.FLAMING_ARROWS, 1)
            );
            forValue5.add(book::copy);

            book = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(
                    book,
                    new EnchantmentInstance(Enchantments.INFINITY_ARROWS, 1)
            );
            forValue5.add(book::copy);
            optionsByValue.add(new ObjectIntPair<>(forValue5, 5));
        }
    }
    @Override
    public PechTradeManager.PechTradeOptionProvider getTradeOptions() {
        return STALKER_TRADES;
    }

    @Override
    protected void populatePechSpecificEquip(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }
}
