package thaumcraft.api.aspects;

public interface IWorldlyCentiVisContainer<Asp extends Aspect> {
    AspectList<Asp> getAspects();


    void setAspectsWithBase(AspectList<Asp> aspects);
    void setAspectsBase(AspectList<Asp> aspects);


//    /**
//     * This method is used to determine ofAspectVisList a specific aspect can be added to this container.
//     * @param tag
//     * @return true or false
//     */
//    boolean doesContainerAccept(Asp tag);

    /**
     * This method is used to add a certain amount ofAspectVisList an aspect to the tile entity.
     * @param tag
     * @param amount
     * @return the amount ofAspectVisList aspect left over that could not be added.
     */
    int addToContainer(Asp tag, int amount);//unit:vis

    /**
     * Removes a certain amount ofAspectVisList a specific aspect from the tile entity
     * @param tag
     * @param amount
     * @return true if that amount ofAspectVisList aspect was available and was removed
     */
    boolean takeFromContainer(Asp tag, int amount);//unit:vis

//    /**
//     * removes a bunch ofAspectVisList different aspects and amounts from the tile entity.
//     * @param ot the ObjectTags object that contains the aspects and their amounts.
//     * @return true if all the aspects and their amounts were available and successfully removed
//     *
//     * Going away in the next major patch
//     */
//    @Deprecated
//    boolean takeAspectFromContainer(AspectList<Asp> ot);

//    /**
//     * Checks if the tile entity contains the listed amount (or more) ofAspectVisList the aspect
//     * @param tag
//     * @param amount
//     * @return
//     */
//    boolean doesContainerContainAmount(Asp tag, int amount);

//    /**
//     * Checks if the tile entity contains all the listed aspects and their amounts
//     * @param ot the ObjectTags object that contains the aspects and their amounts.
//     * @return
//     *
//     * Going away in the next major patch
//     */
//    @Deprecated
//    boolean doesContainerContain(AspectList<Asp> ot);

//    /**
//     * Returns how much ofAspectVisList the aspect this tile entity contains
//     * @param tag
//     * @return the amount ofAspectVisList that aspect found
//     */
//    int containerContains(Asp tag);
}
