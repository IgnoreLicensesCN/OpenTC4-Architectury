package thaumcraft.common.items.misc;

import net.minecraft.world.item.Rarity;

public class IronKeyItem extends AbstractKeyItem {
    public IronKeyItem(Properties properties) {
        super(properties);
    }
    public IronKeyItem() {
        this(new Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
    }
}
