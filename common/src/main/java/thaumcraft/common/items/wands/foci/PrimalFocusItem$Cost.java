package thaumcraft.common.items.wands.foci;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;

public class PrimalFocusItem$Cost {
    private static final int randomSize = 5;
    public static final CentiVisList<Aspect>[] RANDOM_COSTS = new CentiVisList[randomSize*randomSize*randomSize*randomSize*randomSize*randomSize];
    static {
        int counter = 0;
        for (int aer = 0;aer < randomSize;aer++) {
            for (int aqua = 0;aqua < randomSize;aqua++) {
                for (int ignis = 0;ignis < randomSize;ignis++) {
                    for (int terra = 0;terra < randomSize;terra++){
                        for (int ordo = 0;ordo < randomSize;ordo++){
                            for (int perditio = 0;perditio < randomSize;perditio++){
                                RANDOM_COSTS[counter]=
                                        UnmodifiableCentiVisList.of(
                                                Aspects.AIR,(aer+1)*50,
                                                Aspects.WATER,(aqua+1)*50,
                                                Aspects.FIRE,(ignis+1)*50,
                                                Aspects.EARTH,(terra+1)*50,
                                                Aspects.ORDER,(ordo+1)*50,
                                                Aspects.ENTROPY,(perditio+1)*50
                                        );
                                counter += 1;
                            }
                        }
                    }
                }
            }
        }
    }

}
