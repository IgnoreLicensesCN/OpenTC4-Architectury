package thaumcraft.common.items.equipment.armor.voidarmor;

import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import thaumcraft.api.IGoggles;
import thaumcraft.common.items.ThaumcraftItems;

public class VoidRobeHelmetItem extends VoidRobeArmorItem implements IGoggles {
    public VoidRobeHelmetItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }

    public VoidRobeHelmetItem() {
        this(ThaumcraftItems.ToolAndArmorMaterial.ARMOR_VOID,Type.HELMET,new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }
}
