package thaumcraft.common.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.item.Items.GOLD_INGOT;
import static thaumcraft.common.items.ThaumcraftItems.ItemTags.THAUMIUM_INGOT_TAG;
import static thaumcraft.common.items.ThaumcraftItems.ItemTags.VOID_INGOT_TAG;

public class ThaumcraftToolAndArmorMaterial {

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

        public static final Ingredient ingredient = Ingredient.of(THAUMIUM_INGOT_TAG);

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
        }
    };
    public static final Tier TOOL_VOID = new Tier() {
        //"VOID", 4, 150, 8F, 3, 10
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

        public static final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.PRIMAL_CHARM());

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
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

        public static final Ingredient ingredient = Ingredient.of(THAUMIUM_INGOT_TAG);

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
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

        public static final Ingredient ingredient = Ingredient.of(THAUMIUM_INGOT_TAG);

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
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
    public static final ArmorMaterial ROBE = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 11 * 25;
                case CHESTPLATE -> 16 * 25;
                case LEGGINGS -> 15 * 25;
                case BOOTS -> 13 * 25;
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

        private final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.ENCHANTED_FABRIC());

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
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
    public static final ArmorMaterial SPECIAL = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 11 * 25;
                case CHESTPLATE -> 16 * 25;
                case LEGGINGS -> 15 * 25;
                case BOOTS -> 13 * 25;
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

        private final Ingredient ingredient = Ingredient.of(GOLD_INGOT);

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
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

        public static final Ingredient ingredient = Ingredient.of(THAUMIUM_INGOT_TAG);

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
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
    public static final ArmorMaterial ARMOR_VOID = new ArmorMaterial() {
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

        public static final Ingredient ingredient = Ingredient.of(VOID_INGOT_TAG);

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
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
    //azanor didn't use this one not me,but keep it in case someone wants.
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

        public static final Ingredient ingredient = Ingredient.of(VOID_INGOT_TAG);

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
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
    public static final Tier PRIMAL_VOID = new Tier() {

        @Override
        public int getUses() {
            return 500;
        }

        @Override
        public float getSpeed() {
            return 8.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 4.0F;
        }

        @Override
        public int getLevel() {
            return 5;
        }

        @Override
        public int getEnchantmentValue() {
            return 20;
        }

        private final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.PRIMAL_CHARM());

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
        }
    };
    public static final Tier CRIMSON_VOID = new Tier() {

        @Override
        public int getUses() {
            return 200;
        }

        @Override
        public float getSpeed() {
            return 8.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3.5F;
        }

        @Override
        public int getLevel() {
            return 5;
        }

        @Override
        public int getEnchantmentValue() {
            return 20;
        }

        private final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.PRIMAL_CHARM());

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return ingredient;
        }
    };
}
