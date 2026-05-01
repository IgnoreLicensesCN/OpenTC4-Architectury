package thaumcraft.common.items.dispensebehavior;

import net.minecraft.world.level.block.DispenserBlock;
import thaumcraft.common.items.ThaumcraftItems;

public class ThaumcraftDispenseBehaviors {

    public static void init(){
        DispenserBlock.registerBehavior(
                ThaumcraftItems.ALUMENTUM,
                new BehaviorDispenseAlumentum());

    }
}
