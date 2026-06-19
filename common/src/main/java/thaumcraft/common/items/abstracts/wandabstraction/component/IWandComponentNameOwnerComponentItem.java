package thaumcraft.common.items.abstracts.wandabstraction.component;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;


public interface IWandComponentNameOwnerComponentItem {

    default Component getComponentName() {
        if (this instanceof Item item){
            return item.getName(item.getDefaultInstance());
        }
        return Component.empty();
    }
}
