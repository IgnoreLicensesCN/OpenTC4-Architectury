package thaumcraft.common.entities.monster.cultists;

import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class CultistKnightEntity extends CultistEntity{


    public CultistKnightEntity(EntityType<? extends CultistKnightEntity> entityType, Level level) {
        super(entityType, level);
    }

    public CultistKnightEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.CULTIST_KNIGHT(),level);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return CultistEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 36);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_PLATE_CHESTPLATE()));
        this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_PLATE_LEGGINGS()));
        this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_BOOTS()));
        boolean noPlateHelmet = false;
        if (randomSource.nextFloat() < (difficultyInstance.getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
            int i = randomSource.nextInt(5);
            if (i == 0) {
                this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.VOID_SWORD()));
                this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_ROBE_HELMET()));
                noPlateHelmet = true;
            } else {
                this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.THAUMIUM_SWORD()));
                if (randomSource.nextBoolean()) {
                    noPlateHelmet = true;
                }
            }
        } else {
            this.equipItemIfPossible(new ItemStack(Items.IRON_SWORD));
        }
        if (!noPlateHelmet) {
            this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_PLATE_HELMET()));
        }
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        float f = difficultyInstance.getEffectiveDifficulty();
        var helmet = getItemBySlot(EquipmentSlot.HEAD);
        if (!helmet.isEmpty() && randomSource.nextFloat() < 0.25F * f) {
            EnchantmentHelper.enchantItem(randomSource,helmet,(int)(5.0F + f * randomSource.nextInt(18)),false);
        }
    }
}
