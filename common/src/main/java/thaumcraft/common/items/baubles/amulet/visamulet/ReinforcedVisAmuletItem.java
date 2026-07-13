package thaumcraft.common.items.baubles.amulet.visamulet;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;

public class ReinforcedVisAmuletItem extends VisAmuletItem {
    public ReinforcedVisAmuletItem(Properties properties) {
        super(properties);
    }
    public ReinforcedVisAmuletItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }


    private static final @Unmodifiable CentiVisList<Aspect> CAPACITY = UnmodifiableCentiVisList.of(
            Aspects.AIR,25000,
            Aspects.WATER,25000,
            Aspects.FIRE,25000,
            Aspects.EARTH,25000,
            Aspects.ENTROPY,25000,
            Aspects.ORDER,25000
    );
    @Override
    public @UnmodifiableView CentiVisList<Aspect> getAllCentiVisCapacity(ItemStack stack) {
        return CAPACITY;
    }
}
