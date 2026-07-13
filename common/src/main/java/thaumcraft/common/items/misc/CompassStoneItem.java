package thaumcraft.common.items.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

//renderer only
public class CompassStoneItem extends Item {
    public CompassStoneItem(Properties properties) {
        super(properties);
    }
    public CompassStoneItem() {
        this(new Properties().rarity(Rarity.RARE));
    }
}
