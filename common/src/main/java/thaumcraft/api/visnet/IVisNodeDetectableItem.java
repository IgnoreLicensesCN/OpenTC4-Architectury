package thaumcraft.api.visnet;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.tiles.crafted.VisNetRelayBlockEntity;

//implements to Item class 
// but you also need a VisNetNode that can scan items
// like VisNetNodeRelayBlockEntity
public interface IVisNodeDetectableItem {
    void onVisNodeNearby(VisNetNodeBlockEntity nearbyNode, ItemStack stack);
}
