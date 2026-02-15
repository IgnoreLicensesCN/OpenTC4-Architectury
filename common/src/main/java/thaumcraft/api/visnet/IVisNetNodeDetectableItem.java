package thaumcraft.api.visnet;

import net.minecraft.world.item.ItemStack;

//implements to Item class 
// but you also need a VisNetNode that can scan items
// like VisNetNodeRelayBlockEntity
public interface IVisNetNodeDetectableItem {
    void onVisNodeNearby(VisNetNodeBlockEntity nearbyNode, ItemStack stack);
}
