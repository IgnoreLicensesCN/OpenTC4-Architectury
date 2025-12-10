package thaumcraft.api.wands;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


public interface WandComponentNameOwner {

    default Component getComponentName() {
        if (this instanceof Item item){
            return item.getName(item.getDefaultInstance());
        }
        return Component.empty();
    }
}
