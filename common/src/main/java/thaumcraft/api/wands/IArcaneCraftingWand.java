package thaumcraft.api.wands;

//which can be used for arcane crafting
public interface IArcaneCraftingWand {

    //yeah you can just call it
    default boolean canInsertIntoArcaneCraftingTable(){
        return true;
    };
}
