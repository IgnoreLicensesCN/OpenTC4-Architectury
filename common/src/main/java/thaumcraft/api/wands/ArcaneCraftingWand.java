package thaumcraft.api.wands;

//which can be used for arcane crafting
public interface ArcaneCraftingWand {

    //yeah you can just call it
    default boolean canInsertIntoArcaneCraftingTable(){
        return true;
    };
}
