package thaumcraft.common.items.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

//TODO:Render detailed on left top when handing
public class SanityCheckerItem extends Item {
    public SanityCheckerItem(Properties properties) {
        super(properties);
    }
    public SanityCheckerItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }
}
