package thaumcraft.common.entities.monster.pech;

import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.items.ThaumcraftItemInstances;

import java.util.ArrayList;
import java.util.function.Supplier;

import static thaumcraft.common.items.ThaumcraftItemInstances.MANA_BEAN;
import static thaumcraft.common.items.ThaumcraftItemsRegistry.CLUSTER_ITEMS;
import static thaumcraft.common.items.ThaumcraftItemsRegistry.RANDOM_MANA_BEAN_SUPPLIER;

public class PechForagerEntity extends AbstractPechEntity {
    public PechForagerEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.PECH_FORAGER(), level);
    }

    public PechForagerEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static final PechTradeManager.PechTradeOptionProviderExample FORAGER_TRADES = new PechTradeManager.PechTradeOptionProviderExample();

    static {
        initTrades();
    }

    //mixin point
    private static void initTrades() {
        var optionsByValue = FORAGER_TRADES.TRADE_OPTIONS_BY_VALUE;
        {
            var forValue1 = new ArrayList<Supplier<ItemStack>>();
            for (var i : CLUSTER_ITEMS.values()) {
                forValue1.add(() -> i.get()
                        .getDefaultInstance());
            }

            forValue1.add(RANDOM_MANA_BEAN_SUPPLIER);
            optionsByValue.add(new ObjectIntPair<>(forValue1, 1));
        }
        {
            var forValue2 = new ArrayList<Supplier<ItemStack>>();
            forValue2.add(Items.BLAZE_ROD::getDefaultInstance);
            forValue2.add(ThaumcraftItemInstances.GREATWOOD_SAPLING()::getDefaultInstance);
            forValue2.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH));
            forValue2.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS));

            optionsByValue.add(new ObjectIntPair<>(forValue2, 2));
        }
        {

            var forValue3 = new ArrayList<Supplier<ItemStack>>();
            forValue3.add(Items.EXPERIENCE_BOTTLE::getDefaultInstance);
            forValue3.add(ThaumcraftItemInstances.KNOWLEDGE_FRAGMENT()::getDefaultInstance);
            forValue3.add(Items.GOLDEN_APPLE::getDefaultInstance);
            forValue3.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_STRENGTH));
            forValue3.add(() -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_NIGHT_VISION));
            optionsByValue.add(new ObjectIntPair<>(forValue3, 3));
        }
        {

            var forValue4 = new ArrayList<Supplier<ItemStack>>();
            forValue4.add(ThaumcraftItemInstances.THAUMIUM_PICKAXE()::getDefaultInstance);
            optionsByValue.add(new ObjectIntPair<>(forValue4, 4));
        }
        {

            var forValue5 = new ArrayList<Supplier<ItemStack>>();
            forValue5.add(Items.ENCHANTED_GOLDEN_APPLE::getDefaultInstance);
            forValue5.add(ThaumcraftItemInstances.SILVERWOOD_SAPLING()::getDefaultInstance);
            forValue5.add(ThaumcraftItemInstances.SILVERWOOD_SAPLING()::getDefaultInstance);
            optionsByValue.add(new ObjectIntPair<>(forValue5, 5));
        }
    }

    @Override
    public PechTradeManager.PechTradeOptionProvider getTradeOptions() {
        return FORAGER_TRADES;
    }

    @Override
    protected void populatePechSpecificEquip(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }
}
